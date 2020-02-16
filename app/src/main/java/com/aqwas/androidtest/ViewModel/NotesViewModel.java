package com.aqwas.androidtest.ViewModel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;


import com.aqwas.androidtest.model.NotesModel;
import com.aqwas.androidtest.repositry.NotesRepositry;
import com.aqwas.androidtest.utilities.BaseViewModel;

import java.util.ArrayList;

import io.reactivex.observers.DisposableSingleObserver;

public class NotesViewModel extends BaseViewModel {

    NotesRepositry notesRepositry;

    MutableLiveData<ArrayList<NotesModel>> itemMutableLiveData = new MutableLiveData<>();


    public NotesViewModel() {
        this.notesRepositry = new NotesRepositry();
    }


    public LiveData<ArrayList<NotesModel>> getNote() {

        setIsLoading(true);
        getmCompositeDisposable().add(notesRepositry.getNotes()
                .subscribeWith(new DisposableSingleObserver<ArrayList<NotesModel>>() {
                    @Override
                    public void onSuccess(ArrayList<NotesModel> response) {
                        setIsLoading(false);
                        itemMutableLiveData.setValue(response);
                    }

                    @Override
                    public void onError(Throwable e) {
                        setErrorMessage(e.getMessage());
                        setIsLoading(false);
                    }
                }));

        return itemMutableLiveData;
    }
    public LiveData<ArrayList<NotesModel>> searchNote(String text) {

        setIsLoading(true);
        getmCompositeDisposable().add(notesRepositry.searchNotes(text)
                .subscribeWith(new DisposableSingleObserver<ArrayList<NotesModel>>() {
                    @Override
                    public void onSuccess(ArrayList<NotesModel> response) {
                        setIsLoading(false);
                        itemMutableLiveData.setValue(response);
                    }

                    @Override
                    public void onError(Throwable e) {
                        setErrorMessage(e.getMessage());
                        setIsLoading(false);
                    }
                }));

        return itemMutableLiveData;
    }

}
