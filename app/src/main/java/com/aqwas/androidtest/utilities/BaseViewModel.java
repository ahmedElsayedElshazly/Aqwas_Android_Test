package com.aqwas.androidtest.utilities;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import io.reactivex.disposables.CompositeDisposable;

public class BaseViewModel extends ViewModel {

    private final MutableLiveData<Boolean> mIsLoading = new MutableLiveData<>();
    private final MutableLiveData<String> mErrorMessage = new MutableLiveData<>();
    private CompositeDisposable mCompositeDisposable;


    public BaseViewModel() {
        this.mCompositeDisposable = new CompositeDisposable();
        this.mIsLoading.setValue(false);
        this.mErrorMessage.setValue("");
    }
    @Override
    protected void onCleared() {
        mCompositeDisposable.dispose();
        super.onCleared();
    }

    public LiveData<Boolean> getmIsLoading() {
        return mIsLoading;
    }

    public LiveData<String> getmErrorMessage() {
        return mErrorMessage;
    }

    public CompositeDisposable getmCompositeDisposable() {
        return mCompositeDisposable;
    }

    public void setIsLoading(boolean isLoading) {
        mIsLoading.setValue(isLoading);
    }

    public LiveData<String> getErrorMessage() {
        return mErrorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        mErrorMessage.setValue(errorMessage);
    }
}
