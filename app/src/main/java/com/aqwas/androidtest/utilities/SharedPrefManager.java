
package com.aqwas.androidtest.utilities;

import android.content.Context;
import android.content.SharedPreferences;

import com.aqwas.androidtest.model.UserModel;

/**
 * Created by shazly on 10/2/2020.
 */


public class SharedPrefManager {

    //the constants
    public static final String SHARED_PREF_NAME = "simplifiedcodingsharedpref";
    private static final String KEY_ID = "keyid";
    private static final String KEY_NAME = "keyName";
    private static final String KEY_MOBILE = "keymobile";
    private static final String KEY_EMAIL = "keyemail";
    private static final String KEY_Token = "keytoken";



    private static SharedPrefManager mInstance;
    private static Context mCtx;

    private SharedPrefManager(Context context) {
        mCtx = context;
    }

    public static synchronized SharedPrefManager getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new SharedPrefManager(context);
        }
        return mInstance;
    }


    public void userLogin(UserModel.UserBean user) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_ID, user.getId());
        editor.putString(KEY_NAME, user.getUsername());
        editor.putString(KEY_EMAIL, user.getEmail());
    }



    //    this method will give the logged in user
    public UserModel.UserBean getUser() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return new UserModel.UserBean(
                sharedPreferences.getString(KEY_ID, null),
                sharedPreferences.getString(KEY_NAME, null),
                sharedPreferences.getString(KEY_EMAIL, null));

    }



    //this method will logout the user
    public void logout() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();

    }


}


