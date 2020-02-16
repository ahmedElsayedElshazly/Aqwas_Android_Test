package com.aqwas.androidtest.utilities;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.provider.Settings;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.text.Spannable;
import android.text.SpannableString;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.aqwas.androidtest.R;
import com.aqwas.androidtest.activity.LoginActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import static android.content.ContentValues.TAG;


public class ResourceUtil {
     private static Locale myLocale;


    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            Log.d(TAG, "isNetworkAvailable: True");
            return true;
        }
        Log.d(TAG, "isNetworkAvailable: False");
        return false;
    }




    public static void hideKeyboard(Activity activity) {
        View view = activity.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }


    public static void changeLang(String lang, Context context) {
        if (lang.equalsIgnoreCase(""))
            return;
        myLocale = new Locale(lang);
        Locale.setDefault(myLocale);
        android.content.res.Configuration config = new android.content.res.Configuration();
        config.locale = myLocale;
        context.getResources().updateConfiguration(config, context.getResources().getDisplayMetrics());
        saveLocale(lang, context);

    }


    public static void saveLocale(String lang, Context context) {
        String langPref = context.getPackageName() + "App_Language";
        SharedPreferences prefs = context.getSharedPreferences("CommonPrefs", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(langPref, lang);
        editor.commit();
    }


    public static String getCurrentLanguage(Context context) {
        String langPref = context.getPackageName() + "App_Language";
        SharedPreferences prefs = context.getSharedPreferences("CommonPrefs", Activity.MODE_PRIVATE);
        String old = prefs.getString(langPref, "ar");
        return old;
    }





    public static void setSpinnerCustomAdubter(Spinner spinner, ArrayList<String> list, int layId, Context context) {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                context.getApplicationContext(), layId, list);
        adapter.setDropDownViewResource(layId);
        spinner.setAdapter(adapter);
    }






    public static boolean isFirst(Context c) {
        String isFirst = "isFirst";
        SharedPreferences prefs = c.getSharedPreferences("CommonPrefs", Activity.MODE_PRIVATE);
        boolean isFirstBoolean = prefs.getBoolean(isFirst, false);
        return isFirstBoolean;
    }

    public static void saveIsFirst(boolean val, Context c) {
        String isFirst = "isFirst";
        SharedPreferences prefs = c.getSharedPreferences("CommonPrefs", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(isFirst, val);
        editor.commit();
    }


    public static String getDate(long timeStamp) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
            Date netDate = (new Date(timeStamp));
            return sdf.format(netDate);
        } catch (Exception ex) {
            return "xx";
        }
    }

    public static String getTime(long timeStamp) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a", Locale.ENGLISH);
            Date netDate = (new Date(timeStamp));
            return sdf.format(netDate);
        } catch (Exception ex) {
            return "xx";
        }
    }

    public static String getCompleteDate(long timeStamp) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.ENGLISH);
            Date netDate = (new Date(timeStamp));
            return sdf.format(netDate);
        } catch (Exception ex) {
            return "xx";
        }
    }


    public static void saveToken(String token, Context context) {
        String langPref = context.getPackageName() + "Token";
        SharedPreferences prefs = context.getSharedPreferences("CommonPrefs", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(langPref, token);
        editor.commit();
    }


    public static String getToken(Context context) {
        String langPref = context.getPackageName() + "Token";
        SharedPreferences prefs = context.getSharedPreferences("CommonPrefs", Activity.MODE_PRIVATE);
        String old = prefs.getString(langPref, "");
        return old;
    }


    public static void showAlertLogin( Context context) {
        try {
            final AlertDialog.Builder dlgAlert = new AlertDialog.Builder(context);
            dlgAlert.setMessage(context.getString(R.string.showlogin));
            dlgAlert.setTitle(context.getString(R.string.M_title));
            dlgAlert.setCancelable(false);
            dlgAlert.setPositiveButton(context.getString(R.string.M_go), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(context, LoginActivity.class).putExtra("fromElse",true);
                    context.startActivity(intent);
                }
            });
            dlgAlert.setNegativeButton(context.getString(R.string.M_no), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });

            dlgAlert.create().show();
        } catch (Exception e) {
            Log.v("ERROR", e.getMessage());
        }


    }


    public static void saveIsLogin(boolean login, Context context) {
        String langPref = "login";
        SharedPreferences prefs = context.getSharedPreferences("CommonPrefs", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(langPref, login);
        editor.commit();
    }

    public static boolean isLogin(Context context) {
        String langPref = "login";
        SharedPreferences prefs = context.getSharedPreferences("CommonPrefs", Activity.MODE_PRIVATE);
        boolean login = prefs.getBoolean(langPref, false);
        return login;
    }






}