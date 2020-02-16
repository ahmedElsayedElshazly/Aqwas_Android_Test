package com.aqwas.androidtest.activity;

import android.annotation.SuppressLint;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.aqwas.androidtest.R;
import com.aqwas.androidtest.ViewModel.NotesViewModel;
import com.aqwas.androidtest.adapter.NotesAdapter;
import com.aqwas.androidtest.adapter.TasksAdapter;
import com.aqwas.androidtest.api.RetroService;
import com.aqwas.androidtest.api.RetrofitClient;
import com.aqwas.androidtest.databinding.ActivityMainBinding;
import com.aqwas.androidtest.model.NotesModel;
import com.aqwas.androidtest.model.TasksModel;
import com.aqwas.androidtest.utilities.BaseActivity;
import com.aqwas.androidtest.utilities.ResourceUtil;

import java.util.ArrayList;
import java.util.Locale;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends BaseActivity<ActivityMainBinding> {

    RetroService retroService;
    CompositeDisposable compositeDisposable;

    // notes using  MVVM
    NotesViewModel notesViewModel;
    ArrayList<NotesModel> notesArrayList = new ArrayList<>();
    ArrayList<NotesModel> searchNotesArrayList = new ArrayList<>();
    NotesAdapter notesAdapter;
    boolean isChecked = false, isNote = false;

    //tasks using MVC
    ArrayList<TasksModel> tasksArrayList = new ArrayList<>();
    ArrayList<TasksModel> tasksArrayList2 = new ArrayList<>();
    ArrayList<TasksModel> searchTasksArrayList = new ArrayList<>();
    TasksAdapter tasksAdapter;


    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.clear();
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_profile) {

            Toast.makeText(MainActivity.this, getString(R.string.r_support), Toast.LENGTH_SHORT).show();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private void init() {
        retroService = RetrofitClient.getClient().create(RetroService.class);
        compositeDisposable = new CompositeDisposable();
        notesViewModel = ViewModelProviders.of(this).get(NotesViewModel.class);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getResources().getString(R.string.app_name));
        toolbar.setTitleTextColor(getResources().getColor(R.color.black));

        if (ResourceUtil.getCurrentLanguage(this).equals("ar")) {
            getViewDataBinding().searchText.setGravity(Gravity.RIGHT);
            getViewDataBinding().radioNotes.setBackgroundResource(R.drawable.radio_selector_left);
            getViewDataBinding().radioTasks.setBackgroundResource(R.drawable.radio_selector_right);
        } else {
            getViewDataBinding().searchText.setGravity(Gravity.LEFT);
            getViewDataBinding().radioNotes.setBackgroundResource(R.drawable.radio_selector_right);
            getViewDataBinding().radioTasks.setBackgroundResource(R.drawable.radio_selector_left);
        }

        notesAdapter = new NotesAdapter(notesArrayList, MainActivity.this);
        tasksAdapter = new TasksAdapter(tasksArrayList, MainActivity.this);

        // Add New task Or Note
        getViewDataBinding().fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAdd();
            }
        });

        //search with voice
        getViewDataBinding().mic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                i.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                i.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
                i.putExtra(RecognizerIntent.EXTRA_PROMPT, "");
                try {
                    startActivityForResult(i, 300);

                } catch (ActivityNotFoundException a) {
                    showSnakeBar("please try again");
                }
            }
        });


        // Tasks button
        getViewDataBinding().radioTasks.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onClick(View v) {
                if (isChecked && isNote) {
                    isNote = false;
                    if (tasksArrayList.size() == 0) {
                        getViewDataBinding().noData.setVisibility(View.VISIBLE);
                        getViewDataBinding().fab.setVisibility(View.GONE);
                        getViewDataBinding().noDataText.setText(getResources().getString(R.string.no_tasks));
                        getViewDataBinding().buttonText.setText(getResources().getString(R.string.add_new_task));
                    } else {
                        getViewDataBinding().noData.setVisibility(View.GONE);
                        getViewDataBinding().fab.setVisibility(View.VISIBLE);
                        getViewDataBinding().recyclerView.setAdapter(tasksAdapter);
                    }
                }

            }
        });


        //Note button
        getViewDataBinding().radioNotes.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onClick(View v) {
                if (isNote == false) {
                    isNote = true;

                    if (isChecked) {
                        if (notesArrayList.size() == 0) {
                            getViewDataBinding().noData.setVisibility(View.VISIBLE);
                            getViewDataBinding().fab.setVisibility(View.GONE);
                            getViewDataBinding().noDataText.setText(getResources().getString(R.string.no_notes));
                            getViewDataBinding().buttonText.setText(getResources().getString(R.string.add_new_note));
                        } else {
                            getViewDataBinding().noData.setVisibility(View.GONE);
                            getViewDataBinding().fab.setVisibility(View.VISIBLE);
                            getViewDataBinding().recyclerView.setAdapter(notesAdapter);
                        }
                    } else {
                        getNotes();
                    }
                    isChecked = true;
                }
            }
        });

        //Swipe for  Refreshing List
        getViewDataBinding().swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (isNote)
                    getNotes();
                else
                    getTasks();

            }
        });

        getViewDataBinding().addNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isNote) {
                    startActivityForResult(new Intent(MainActivity.this, AddNoteActivity.class), 100);
                } else {
                    startActivityForResult(new Intent(MainActivity.this, AddTaskActivity.class), 200);
                }
            }
        });
        // Search
        getViewDataBinding().searchText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @SuppressLint("RestrictedApi")
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Toast.makeText(MainActivity.this, getString(R.string.r_support), Toast.LENGTH_SHORT).show();

//                if (!getViewDataBinding().searchText.getText().toString().isEmpty()) {
//                    if (isNote) {
//                        searchNotes();
//                    } else {
//                        searchTasks();
//                    }
//                } else {
//                    if (isNote) {
//                        searchNotesArrayList.clear();
//                        notesAdapter = new NotesAdapter(notesArrayList, MainActivity.this);
//                        getViewDataBinding().recyclerView.setAdapter(notesAdapter);
//                        getViewDataBinding().fab.setVisibility(View.VISIBLE);
//                        notesAdapter.notifyDataSetChanged();
//                    } else {
//                        searchTasksArrayList.clear();
//                        tasksAdapter = new TasksAdapter(tasksArrayList, MainActivity.this);
//                        getViewDataBinding().recyclerView.setAdapter(tasksAdapter);
//                        getViewDataBinding().fab.setVisibility(View.VISIBLE);
//                        tasksAdapter.notifyDataSetChanged();
//                    }
//                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        getTasks();
    }

    @SuppressLint("RestrictedApi")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 100 && resultCode == RESULT_OK) {
            NotesModel model = (NotesModel) data.getSerializableExtra("noteModel");
            if (model != null) {
                getViewDataBinding().noData.setVisibility(View.GONE);
                getViewDataBinding().fab.setVisibility(View.VISIBLE);
                getViewDataBinding().recyclerView.setAdapter(notesAdapter);
                notesArrayList.add(model);
                notesAdapter.notifyDataSetChanged();
            }
        } else if (requestCode == 200 && resultCode == RESULT_OK) {
            TasksModel model = (TasksModel) data.getSerializableExtra("tasksModel");
            if (model != null) {
                getViewDataBinding().noData.setVisibility(View.GONE);
                getViewDataBinding().fab.setVisibility(View.VISIBLE);
                getViewDataBinding().recyclerView.setAdapter(tasksAdapter);
                tasksArrayList2.add(model);
                sortTasks();
                tasksAdapter.notifyDataSetChanged();
            }
        } else if (requestCode == 300 && resultCode == RESULT_OK) {
            ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            getViewDataBinding().searchText.setText(result.get(0));
        } else if (requestCode == 400 && resultCode == RESULT_OK) {
            NotesModel model = (NotesModel) data.getSerializableExtra("noteModel");
            int pos = data.getIntExtra("position", -1);
            if (model != null) {
                notesArrayList.get(pos).setTitle(model.getTitle());
                notesArrayList.get(pos).setBody(model.getBody());
                notesAdapter.notifyDataSetChanged();
            }
        } else if (requestCode == 500 && resultCode == RESULT_OK) {
            TasksModel model = (TasksModel) data.getSerializableExtra("tasksModel");
            int pos = data.getIntExtra("position", -1);
            if (model != null) {
                tasksArrayList.get(pos).setTitle(model.getTitle());
                tasksArrayList.get(pos).setDescription(model.getDescription());
                tasksArrayList.get(pos).setDueDate(model.getDueDate());
                tasksArrayList.get(pos).setPriority(model.getPriority());
                sortTasks();
                tasksAdapter.notifyDataSetChanged();
            }
        }
    }


    public void showAdd() {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(this);
        LayoutInflater li = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View mView = li.inflate(R.layout.alert_add, null);
        final TextView Text_add_note = mView.findViewById(R.id.add_note);
        final TextView Text_add_task = mView.findViewById(R.id.add_task);
        final TextView Text_cancel = mView.findViewById(R.id.cancel);

        Text_add_note.setGravity(Gravity.CENTER);
        Text_add_task.setGravity(Gravity.CENTER);
        Text_cancel.setGravity(Gravity.CENTER);
        mBuilder.setView(mView);
        final AlertDialog dialog = mBuilder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        Window window = dialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();

        wlp.gravity = Gravity.BOTTOM;
        wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        window.setAttributes(wlp);
        dialog.show();


        Text_add_note.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivityForResult(new Intent(MainActivity.this, AddNoteActivity.class), 100);
                dialog.dismiss();
            }
        });

        Text_add_task.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(MainActivity.this, AddTaskActivity.class), 200);
                dialog.dismiss();
            }
        });

        Text_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

    }

    private void sortTasks() {
        tasksArrayList.clear();
        for (int i = 0; i < tasksArrayList2.size(); i++) {
            if (tasksArrayList2.get(i).getPriority().equals("High")) {
                tasksArrayList.add(tasksArrayList2.get(i));
            }
        }
        for (int i = 0; i < tasksArrayList2.size(); i++) {
            if (tasksArrayList2.get(i).getPriority().equals("Medium")) {
                tasksArrayList.add(tasksArrayList2.get(i));
            }
        }
        for (int i = 0; i < tasksArrayList2.size(); i++) {
            if (tasksArrayList2.get(i).getPriority().equals("Low")) {
                tasksArrayList.add(tasksArrayList2.get(i));
            }
        }
        for (int i = 0; i < tasksArrayList2.size(); i++) {
            if (tasksArrayList2.get(i).getPriority().equals("None")) {
                tasksArrayList.add(tasksArrayList2.get(i));
            }
        }
        checkTaskList();
    }

    private void getTasks() {
        if (ResourceUtil.isNetworkAvailable(this)) {
            showProgressDialog();

            compositeDisposable.add(
                    retroService.getTasks()
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribeOn(Schedulers.io()).subscribeWith
                            (new DisposableSingleObserver<ArrayList<TasksModel>>() {
                                @Override
                                public void onSuccess(ArrayList<TasksModel> tasks) {
                                    dismissProgressDialog();
                                    getViewDataBinding().swipeRefresh.setRefreshing(false);
                                    isNote = false;
                                    tasksArrayList2.clear();
                                    tasksArrayList2.addAll(tasks);
                                    sortTasks();

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

    @SuppressLint("RestrictedApi")
    private void checkTaskList() {
        if (tasksArrayList.size() == 0) {
            getViewDataBinding().noData.setVisibility(View.VISIBLE);
            getViewDataBinding().fab.setVisibility(View.GONE);
            getViewDataBinding().noDataText.setText(getResources().getString(R.string.no_tasks));
            getViewDataBinding().buttonText.setText(getResources().getString(R.string.add_new_task));

        } else {
            getViewDataBinding().noData.setVisibility(View.GONE);
            getViewDataBinding().fab.setVisibility(View.VISIBLE);
            getViewDataBinding().recyclerView.setAdapter(tasksAdapter);
            tasksAdapter.notifyDataSetChanged();
        }
    }

    public void checkOrUncheckTask(String id, int position) {
        if (ResourceUtil.isNetworkAvailable(this)) {
            showProgressDialog();

            compositeDisposable.add(
                    retroService.checkOrUncheckTasks(id,
                            !tasksArrayList.get(position).isIsDone())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribeOn(Schedulers.io()).subscribeWith
                            (new DisposableSingleObserver<TasksModel>() {
                                @SuppressLint("RestrictedApi")
                                @Override
                                public void onSuccess(TasksModel tasks) {
                                    dismissProgressDialog();
                                    tasksArrayList.get(position).setIsDone(!tasksArrayList.get(position).isIsDone());
                                    tasksAdapter.notifyDataSetChanged();
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

    private void searchTasks() {
        if (ResourceUtil.isNetworkAvailable(this)) {
            showProgressDialog();

            compositeDisposable.add(
                    retroService.searchTasks(getViewDataBinding().searchText.getText().toString())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribeOn(Schedulers.io()).subscribeWith
                            (new DisposableSingleObserver<ArrayList<TasksModel>>() {
                                @SuppressLint("RestrictedApi")
                                @Override
                                public void onSuccess(ArrayList<TasksModel> tasks) {
                                    dismissProgressDialog();
                                    isNote = false;
                                    searchTasksArrayList.addAll(tasks);
                                    if (searchTasksArrayList.size() == 0) {
                                        getViewDataBinding().noData.setVisibility(View.VISIBLE);
                                        getViewDataBinding().fab.setVisibility(View.GONE);
                                        getViewDataBinding().noDataText.setText(getResources().getString(R.string.no_notes));
                                        getViewDataBinding().buttonText.setVisibility(View.GONE);
                                    } else {
                                        getViewDataBinding().noData.setVisibility(View.GONE);
                                        tasksAdapter = new TasksAdapter(searchTasksArrayList, MainActivity.this);
                                        getViewDataBinding().recyclerView.setAdapter(tasksAdapter);
                                        getViewDataBinding().fab.setVisibility(View.VISIBLE);
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

    public void deleteTasks(String id, int position) {
        if (ResourceUtil.isNetworkAvailable(this)) {
            showProgressDialog();

            compositeDisposable.add(
                    retroService.deleteTasks(id)
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribeOn(Schedulers.io()).subscribeWith
                            (new DisposableSingleObserver<TasksModel>() {
                                @Override
                                public void onSuccess(TasksModel tasks) {
                                    dismissProgressDialog();
                                    tasksArrayList.remove(position);
                                    checkTaskList();
                                    tasksAdapter.notifyDataSetChanged();
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

    private void getNotes() {
        notesViewModel.getNote().observe(this, new Observer<ArrayList<NotesModel>>() {
            @Override
            public void onChanged(@Nullable ArrayList<NotesModel> notes) {
                isNote = true;
                isChecked = true;
                notesArrayList.clear();
                notesArrayList.addAll(notes);
                getViewDataBinding().swipeRefresh.setRefreshing(false);
                checkNotesList();
                notesAdapter.notifyDataSetChanged();
            }
        });
    }

    @SuppressLint("RestrictedApi")
    private void checkNotesList() {
        if (notesArrayList.size() == 0) {
            getViewDataBinding().noData.setVisibility(View.VISIBLE);
            getViewDataBinding().fab.setVisibility(View.GONE);
            getViewDataBinding().noDataText.setText(getResources().getString(R.string.no_notes));
            getViewDataBinding().buttonText.setText(getResources().getString(R.string.add_new_note));
        } else {
            getViewDataBinding().recyclerView.setAdapter(notesAdapter);
            getViewDataBinding().fab.setVisibility(View.VISIBLE);
        }
    }

    private void searchNotes() {

        notesViewModel.searchNote(getViewDataBinding().searchText.getText().toString()).observe(this, new Observer<ArrayList<NotesModel>>() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onChanged(@Nullable ArrayList<NotesModel> notes) {
                isNote = true;
                isChecked = true;
                searchNotesArrayList.addAll(notes);
                if (searchNotesArrayList.size() == 0) {
                    getViewDataBinding().noData.setVisibility(View.VISIBLE);
                    getViewDataBinding().fab.setVisibility(View.GONE);
                    getViewDataBinding().noDataText.setText(getResources().getString(R.string.no_notes));
                    getViewDataBinding().buttonText.setVisibility(View.GONE);

                } else {
                    getViewDataBinding().noData.setVisibility(View.GONE);
                    notesAdapter = new NotesAdapter(searchNotesArrayList, MainActivity.this);
                    getViewDataBinding().recyclerView.setAdapter(notesAdapter);
                    getViewDataBinding().fab.setVisibility(View.VISIBLE);
                }
                notesAdapter.notifyDataSetChanged();
            }
        });
    }

    public void deleteNotes(String id, int position) {
        if (ResourceUtil.isNetworkAvailable(this)) {
            showProgressDialog();

            compositeDisposable.add(
                    retroService.deleteNotes(id)
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribeOn(Schedulers.io()).subscribeWith
                            (new DisposableSingleObserver<NotesModel>() {
                                @Override
                                public void onSuccess(NotesModel tasks) {
                                    dismissProgressDialog();
                                    notesArrayList.remove(position);
                                    notesAdapter.notifyDataSetChanged();
                                    checkNotesList();
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
