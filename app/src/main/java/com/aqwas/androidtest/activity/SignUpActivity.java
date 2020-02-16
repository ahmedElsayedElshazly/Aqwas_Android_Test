package com.aqwas.androidtest.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import com.aqwas.androidtest.R;
import com.aqwas.androidtest.api.RetroService;
import com.aqwas.androidtest.api.RetrofitClient;
import com.aqwas.androidtest.databinding.ActivitySignUpBinding;
import com.aqwas.androidtest.model.UserModel;
import com.aqwas.androidtest.utilities.BaseActivity;
import com.aqwas.androidtest.utilities.ResourceUtil;
import com.aqwas.androidtest.utilities.SharedPrefManager;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class SignUpActivity extends BaseActivity<ActivitySignUpBinding> {
    RetroService retroService;
    CompositeDisposable compositeDisposable;

    @Override
    public int getLayoutId() {
        return R.layout.activity_sign_up;
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
            getViewDataBinding().arrow.setImageResource(R.drawable.ic_arrow);
            getViewDataBinding().userNameText.setGravity(Gravity.RIGHT);
            getViewDataBinding().emailText.setGravity(Gravity.RIGHT);
            getViewDataBinding().passwordText.setGravity(Gravity.RIGHT);
        } else {
            getViewDataBinding().arrow.setImageResource(R.drawable.ic_arrow_en);
            getViewDataBinding().userNameText.setGravity(Gravity.LEFT);
            getViewDataBinding().emailText.setGravity(Gravity.LEFT);
            getViewDataBinding().passwordText.setGravity(Gravity.LEFT);
        }

        getViewDataBinding().close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        getViewDataBinding().signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkText()) {
                    register();
                }
            }
        });

    }


    private boolean checkText() {


        if (getViewDataBinding().userNameText.getText().toString().isEmpty()) {
            Toast.makeText(this, getString(R.string.r_user_name), Toast.LENGTH_SHORT).show();
            return false;
        }

        if (getViewDataBinding().emailText.getText().toString().isEmpty()) {
            Toast.makeText(this, getString(R.string.r_email), Toast.LENGTH_SHORT).show();
            return false;

        }
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(getViewDataBinding().emailText.getText().toString()).matches()) {
            Toast.makeText(this, getString(R.string.r_email_confirm), Toast.LENGTH_SHORT).show();
            return false;
        }

        if (getViewDataBinding().passwordText.getText().toString().isEmpty()) {
            Toast.makeText(this, getString(R.string.r_password), Toast.LENGTH_SHORT).show();
            return false;
        }
        if (getViewDataBinding().passwordText.getText().toString().length()<6) {
            Toast.makeText(this, getString(R.string.r_password_lenght), Toast.LENGTH_SHORT).show();
            return false;
        }
//        if (!getViewDataBinding().confirmPassword.getText().toString().equals(getViewDataBinding().password.getText().toString())) {
//            Toast.makeText(this, getString(R.string.r_confirmPassword), Toast.LENGTH_SHORT).show();
//            return false;
//        }
//

        return true;
    }

    private void register() {
        if (ResourceUtil.isNetworkAvailable(this)) {
            showProgressDialog();

            compositeDisposable.add(
                    retroService.signUp(getViewDataBinding().userNameText.getText().toString(),
                            getViewDataBinding().emailText.getText().toString(),
                            getViewDataBinding().passwordText.getText().toString())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribeOn(Schedulers.io()).subscribeWith
                            (new DisposableSingleObserver<UserModel>() {
                                @Override
                                public void onSuccess(UserModel model) {
                                    dismissProgressDialog();
                                    if (model.getUser() != null) {
                                        SharedPrefManager.getInstance(SignUpActivity.this).userLogin(model.getUser());
                                        ResourceUtil.saveToken(model.getJwt(), SignUpActivity.this);
                                        ResourceUtil.saveIsLogin(true, SignUpActivity.this);
                                        startActivity(new Intent(SignUpActivity.this, MainActivity.class));
                                        finishAffinity();
                                    } else {
                                        showSnakeBar(model.getMessage().get(0).getMessages().get(0).getMessage());
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
