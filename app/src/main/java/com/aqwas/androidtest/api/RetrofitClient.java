package com.aqwas.androidtest.api;


import android.support.annotation.NonNull;

import com.aqwas.androidtest.utilities.MyApp;
import com.aqwas.androidtest.utilities.ResourceUtil;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static final String BASE_URL = "http://hr_test_api.aqwas.dev:82/";
    private static Retrofit retrofit = null;
    private static OkHttpClient okHttpClient = new OkHttpClient()
            .newBuilder()
            .readTimeout(60, TimeUnit.SECONDS)
            .connectTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .addInterceptor(new Interceptor() {
                @Override
                public okhttp3.Response intercept(@NonNull Chain chain) throws IOException {

                    Request originalRequest = chain.request();
                    Request.Builder builder = originalRequest.newBuilder();
                    builder.addHeader("Content-Type", "application/json");
                     if (ResourceUtil.isLogin(MyApp.getContext())) {
                        builder.addHeader("Authorization", "Bearer "+ ResourceUtil.getToken(MyApp.getContext()));
                    }
                    Request newRequest = builder.build();
                    return chain.proceed(newRequest);
                }
            })
            .addInterceptor(loggingInterceptor())
            .build();

    public static Retrofit getClient() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .client(okHttpClient)
                    .build();
        }
        return retrofit;
    }


    public static Retrofit getClient(String BASE_URL) {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .client(okHttpClient)
                    .build();
        }
        return retrofit;
    }

    public static HttpLoggingInterceptor loggingInterceptor() {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY).setLevel(HttpLoggingInterceptor.Level.HEADERS);
        return loggingInterceptor;
    }
}


