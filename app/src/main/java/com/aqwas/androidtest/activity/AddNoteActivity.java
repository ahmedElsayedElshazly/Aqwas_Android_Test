package com.aqwas.androidtest.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import com.aqwas.androidtest.R;
import com.aqwas.androidtest.api.RetroService;
import com.aqwas.androidtest.api.RetrofitClient;
import com.aqwas.androidtest.databinding.ActivityAddNoteBinding;
import com.aqwas.androidtest.model.NotesModel;
import com.aqwas.androidtest.utilities.BaseActivity;
import com.aqwas.androidtest.utilities.ResourceUtil;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class AddNoteActivity extends BaseActivity<ActivityAddNoteBinding> {

    RetroService retroService;
    CompositeDisposable compositeDisposable;
    NotesModel notesModel;
    boolean isFromEdit;
    int position;

    @Override
    public int getLayoutId() {
        return R.layout.activity_add_note;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    private void init() {
        retroService = RetrofitClient.getClient().create(RetroService.class);
        compositeDisposable = new CompositeDisposable();


        if (ResourceUtil.getCurrentLanguage(this).equals("ar")) {
            getViewDataBinding().title.setGravity(Gravity.RIGHT);
            getViewDataBinding().description.setGravity(Gravity.RIGHT);
        } else {
            getViewDataBinding().title.setGravity(Gravity.LEFT);
            getViewDataBinding().description.setGravity(Gravity.LEFT);
        }

        getViewDataBinding().close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        getViewDataBinding().addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkText()) {
                    if (isFromEdit) {
                        editNote();
                    } else {
                        addNote();
                    }
                }
            }
        });

        isFromEdit = getIntent().getBooleanExtra("isFromEdit", false);
        if (isFromEdit) {
            notesModel = (NotesModel) getIntent().getSerializableExtra("model");
            position = getIntent().getIntExtra("position", -1);
            if (notesModel != null) {
                getViewDataBinding().title.setText(notesModel.getTitle());
                getViewDataBinding().description.setText(notesModel.getBody());
            }
            getViewDataBinding().addText.setText(getResources().getString(R.string.edit));
        }


    }


    private boolean checkText() {

        if (getViewDataBinding().title.getText().toString().isEmpty()) {
            Toast.makeText(this, getString(R.string.r_title), Toast.LENGTH_SHORT).show();
            return false;

        }

        if (getViewDataBinding().description.getText().toString().isEmpty()) {
            Toast.makeText(this, getString(R.string.r_description), Toast.LENGTH_SHORT).show();
            return false;
        }


        return true;

    }

    private void addNote() {
        if (ResourceUtil.isNetworkAvailable(this)) {
            showProgressDialog();

            compositeDisposable.add(
                    retroService.addNote(getViewDataBinding().title.getText().toString(),
                            getViewDataBinding().description.getText().toString())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribeOn(Schedulers.io()).subscribeWith
                            (new DisposableSingleObserver<NotesModel>() {
                                @Override
                                public void onSuccess(NotesModel model) {
                                    dismissProgressDialog();
                                    if (model != null) {
                                        notesModel = model;
                                        Intent intent = new Intent();
                                        intent.putExtra("noteModel", notesModel);
                                        setResult(Activity.RESULT_OK, intent);
                                        finish();
                                    }
                                }

                                @Override
                                public void onError(Throwable e) {
                                    dismissProgressDialog();
                                    showSnakeBar(e.getMessage());
                                }
                            }));
        } else {
            showSnakeBar(ResourceUtil.getCurrentLanguage(this).equals("en") ? "Please check your network connection" : "تحقق من اتصالك بالإنترنت ");
        }
    }

    private void editNote() {
        if (ResourceUtil.isNetworkAvailable(this)) {
            showProgressDialog();

            compositeDisposable.add(
                    retroService.editNote(notesModel.get_id(),
                            getViewDataBinding().title.getText().toString(),
                            getViewDataBinding().description.getText().toString())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribeOn(Schedulers.io()).subscribeWith
                            (new DisposableSingleObserver<NotesModel>() {
                                @Override
                                public void onSuccess(NotesModel model) {
                                    dismissProgressDialog();
                                    if (model != null) {
                                        notesModel = model;
                                        Intent intent = new Intent();
                                        intent.putExtra("noteModel", notesModel);
                                        intent.putExtra("position", position);
                                        setResult(Activity.RESULT_OK, intent);
                                        finish();
                                    }
                                }

                                @Override
                                public void onError(Throwable e) {
                                    dismissProgressDialog();
                                    showSnakeBar(e.getMessage());
                                }
                            }));
        } else {
            showSnakeBar(ResourceUtil.getCurrentLanguage(this).equals("en") ? "Please check your network connection" : "تحقق من اتصالك بالإنترنت ");
        }
    }


}
