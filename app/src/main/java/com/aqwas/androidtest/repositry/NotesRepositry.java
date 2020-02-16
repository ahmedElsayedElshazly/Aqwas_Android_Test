package com.aqwas.androidtest.repositry;


import com.aqwas.androidtest.api.RetroService;
import com.aqwas.androidtest.api.RetrofitClient;
import com.aqwas.androidtest.model.NotesModel;

import java.util.ArrayList;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class NotesRepositry {

    RetroService retroService;

    public NotesRepositry() {
        retroService = RetrofitClient.getClient().create(RetroService.class);
    }

    public Single<ArrayList<NotesModel>> getNotes() {
        return retroService.getNotes()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Single<ArrayList<NotesModel>> searchNotes(String text) {
        return retroService.searchNotes(text)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
