package com.aqwas.androidtest.activity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.Toast;

import com.aqwas.androidtest.R;
import com.aqwas.androidtest.api.RetroService;
import com.aqwas.androidtest.api.RetrofitClient;
import com.aqwas.androidtest.databinding.ActivityAddTaskBinding;
import com.aqwas.androidtest.model.AddTaskModel;
import com.aqwas.androidtest.model.NotesModel;
import com.aqwas.androidtest.model.TasksModel;
import com.aqwas.androidtest.utilities.BaseActivity;
import com.aqwas.androidtest.utilities.ResourceUtil;

import java.util.ArrayList;
import java.util.Calendar;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class AddTaskActivity extends BaseActivity<ActivityAddTaskBinding> {
    RetroService retroService;
    CompositeDisposable compositeDisposable;
    TasksModel tasksModel;
    ArrayList<String> priorityList = new ArrayList<>();
    String priority = "None";
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    boolean isFromEdit;
    int position;

    @Override
    public int getLayoutId() {
        return R.layout.activity_add_task;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    private void init() {
        retroService = RetrofitClient.getClient().create(RetroService.class);
        compositeDisposable = new CompositeDisposable();

        priorityList.add(getString(R.string.none));
        priorityList.add(getString(R.string.low));
        priorityList.add(getString(R.string.medium));
        priorityList.add(getString(R.string.high));
        ResourceUtil.setSpinnerCustomAdubter(getViewDataBinding().spinnerPriority,
                priorityList, R.layout.custom_spinner_item, this);
        getViewDataBinding().spinnerPriority.setSelection(0);

        // Choose Priority
        getViewDataBinding().spinnerPriority.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long l) {
                if (pos == 0) {
                    priority = "None";
                } else if (pos == 1) {
                    priority = "Low";
                } else if (pos == 2) {
                    priority = "Medium";
                } else if (pos == 3) {
                    priority = "High";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        if (ResourceUtil.getCurrentLanguage(this).equals("ar")) {
            getViewDataBinding().title.setGravity(Gravity.RIGHT);
            getViewDataBinding().description.setGravity(Gravity.RIGHT | Gravity.TOP);
        } else {
            getViewDataBinding().title.setGravity(Gravity.LEFT);
            getViewDataBinding().description.setGravity(Gravity.LEFT | Gravity.TOP);
        }
        // Back button
        getViewDataBinding().close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        // Add Button
        getViewDataBinding().addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkText()) {
                    if (isFromEdit) {
                        editTask();
                    } else {
                        addTask();
                    }
                }
            }
        });

        // Date Picker
        getViewDataBinding().date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int year,day,month;
                if(isFromEdit){
                    String [] dateParts = tasksModel.getDueDate().split("-");
                    year = Integer.valueOf(dateParts[0]);
                     month = Integer.valueOf(dateParts[1])-1;
                     day = Integer.valueOf(dateParts[2]);


                }else {
                    Calendar cal = Calendar.getInstance();
                     year = cal.get(Calendar.YEAR);
                     month = cal.get(Calendar.MONTH);
                     day = cal.get(Calendar.DAY_OF_MONTH);
                }
                DatePickerDialog dialog = new DatePickerDialog(
                        AddTaskActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDateSetListener,
                        year, month, day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }

        });

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                String Month = String.valueOf(month);
                if (month < 10) {
                    Month = "0" + month;
                }
                String Day = String.valueOf(day);
                if (day < 10) {
                    Day = "0" + day;
                }
                String date = year + "-" + Month + "-" + Day;
                getViewDataBinding().date.setText(date);
            }
        };

        //Edit Task
        isFromEdit = getIntent().getBooleanExtra("isFromEdit", false);
        if (isFromEdit) {
            tasksModel = (TasksModel) getIntent().getSerializableExtra("model");
            position = getIntent().getIntExtra("position", -1);
            if (tasksModel != null) {
                getViewDataBinding().title.setText(tasksModel.getTitle());
                getViewDataBinding().description.setText(tasksModel.getDescription());
                getViewDataBinding().date.setText(tasksModel.getDueDate());
                if (tasksModel.getPriority().equals("None")) {
                    getViewDataBinding().spinnerPriority.setSelection(0);
                    priority = "None";
                } else if (tasksModel.getPriority().equals("Low")) {
                    getViewDataBinding().spinnerPriority.setSelection(1);
                    priority = "Low";
                } else if (tasksModel.getPriority().equals("Medium")) {
                    getViewDataBinding().spinnerPriority.setSelection(2);
                    priority = "Medium";
                } else if (tasksModel.getPriority().equals("High")) {
                    getViewDataBinding().spinnerPriority.setSelection(3);
                    priority = "High";
                }
            }
            getViewDataBinding().addText.setText(getResources().getString(R.string.edit));
        }
    }


    private boolean checkText() {

        if (getViewDataBinding().title.getText().toString().isEmpty()) {
            Toast.makeText(this, getString(R.string.r_title), Toast.LENGTH_SHORT).show();
            return false;

        }
        if (getViewDataBinding().date.getText().toString().isEmpty()) {
            Toast.makeText(this, getString(R.string.r_date), Toast.LENGTH_SHORT).show();
            return false;

        }

//        if (getViewDataBinding().description.getText().toString().isEmpty()) {
//            Toast.makeText(this, getString(R.string.r_description), Toast.LENGTH_SHORT).show();
//            return false;
//        }


        return true;

    }

    private void addTask() {
        AddTaskModel addTaskModel;

        if (getViewDataBinding().description.getText().toString().isEmpty() ||
                getViewDataBinding().description.getText().toString().equals("")) {
            addTaskModel = new AddTaskModel(
                    getViewDataBinding().title.getText().toString(),
                    getViewDataBinding().date.getText().toString(),
                    priority);
        } else {
            addTaskModel = new AddTaskModel(
                    getViewDataBinding().title.getText().toString(),
                    getViewDataBinding().date.getText().toString(),
                    priority,
                    getViewDataBinding().description.getText().toString());
        }

        if (ResourceUtil.isNetworkAvailable(this)) {
            showProgressDialog();
            compositeDisposable.add(
                    retroService.addTask(addTaskModel)
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribeOn(Schedulers.io()).subscribeWith
                            (new DisposableSingleObserver<TasksModel>() {
                                @Override
                                public void onSuccess(TasksModel model) {
                                    dismissProgressDialog();
                                    if (model != null) {
                                        tasksModel = model;
                                        Intent intent = new Intent();
                                        intent.putExtra("tasksModel", tasksModel);
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

    private void editTask() {
        AddTaskModel addTaskModel;


            addTaskModel = new AddTaskModel(
                    getViewDataBinding().title.getText().toString(),
                    getViewDataBinding().date.getText().toString(),
                    priority,
                    getViewDataBinding().description.getText().toString());


        if (ResourceUtil.isNetworkAvailable(this)) {
            showProgressDialog();
            compositeDisposable.add(
                    retroService.editTask(tasksModel.getId(),
                            addTaskModel)
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribeOn(Schedulers.io()).subscribeWith
                            (new DisposableSingleObserver<TasksModel>() {
                                @Override
                                public void onSuccess(TasksModel model) {
                                    dismissProgressDialog();
                                    if (model != null) {
                                        tasksModel = model;
                                        Intent intent = new Intent();
                                        intent.putExtra("tasksModel", tasksModel);
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