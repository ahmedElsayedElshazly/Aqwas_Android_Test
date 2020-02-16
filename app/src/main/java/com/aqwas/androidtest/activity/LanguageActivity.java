package com.aqwas.androidtest.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.aqwas.androidtest.R;
import com.aqwas.androidtest.databinding.ActivityLanguageBinding;
import com.aqwas.androidtest.utilities.BaseActivity;
import com.aqwas.androidtest.utilities.ResourceUtil;


public class LanguageActivity extends BaseActivity<ActivityLanguageBinding> {

    @Override
    public int getLayoutId() {
        return R.layout.activity_language;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    private void init() {

        getViewDataBinding().arabic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ResourceUtil.changeLang("ar", LanguageActivity.this);
                startIntet();
            }
        });
        getViewDataBinding().english.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ResourceUtil.changeLang("en", LanguageActivity.this);
                startIntet();
            }
        });
    }

    private void startIntet() {
        if (ResourceUtil.isLogin(LanguageActivity.this)) {
            Intent i = new Intent(LanguageActivity.this, MainActivity.class);
            startActivity(i);
            finish();
        } else {
            Intent i = new Intent(LanguageActivity.this, LoginActivity.class);
            startActivity(i);
            finish();
        }
    }
}
