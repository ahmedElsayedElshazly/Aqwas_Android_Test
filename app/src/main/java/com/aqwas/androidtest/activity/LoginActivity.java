package com.aqwas.androidtest.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.aqwas.androidtest.R;
import com.aqwas.androidtest.api.RetroService;
import com.aqwas.androidtest.api.RetrofitClient;
import com.aqwas.androidtest.databinding.ActivityLoginBinding;
import com.aqwas.androidtest.model.UserModel;
import com.aqwas.androidtest.utilities.BaseActivity;
import com.aqwas.androidtest.utilities.ResourceUtil;
import com.aqwas.androidtest.utilities.SharedPrefManager;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

public class LoginActivity extends BaseActivity<ActivityLoginBinding> {
    RetroService retroService;
    CompositeDisposable compositeDisposable;

    @Override
    public int getLayoutId() {
        return R.layout.activity_login;
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
        } else {
            getViewDataBinding().arrow.setImageResource(R.drawable.ic_arrow_en);

        }

        getViewDataBinding().signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkText())
                    signIn();
            }
        });

        getViewDataBinding().signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
            }
        });
    }

    private boolean checkText() {


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
        if (getViewDataBinding().passwordText.getText().toString().length() < 6) {
            Toast.makeText(this, getString(R.string.r_password_lenght), Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;

    }


    private void signIn() {
        if (ResourceUtil.isNetworkAvailable(this)) {
            showProgressDialog();

            compositeDisposable.add(
                    retroService.signIn(getViewDataBinding().emailText.getText().toString(),
                            getViewDataBinding().passwordText.getText().toString())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribeOn(Schedulers.io()).subscribeWith
                            (new DisposableSingleObserver<UserModel>() {
                                @Override
                                public void onSuccess(UserModel model) {
                                    dismissProgressDialog();
                                    if (model != null) {
                                        SharedPrefManager.getInstance(LoginActivity.this).userLogin(model.getUser());
                                        ResourceUtil.saveToken(model.getJwt(), LoginActivity.this);
                                        ResourceUtil.saveIsLogin(true, LoginActivity.this);
                                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                    }
                                }

                                @Override
                                public void onError(Throwable e) {
                                    dismissProgressDialog();
                                    showSnakeBar(getResources().getString(R.string.r_login));
                                }
                            }));
        } else {
            showSnakeBar(ResourceUtil.getCurrentLanguage(this).equals("en") ? "Please check your network connection" : "تحقق من اتصالك بالإنترنت ");
        }
    }
}
