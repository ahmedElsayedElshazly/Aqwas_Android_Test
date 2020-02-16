package com.aqwas.androidtest.utilities;

import android.app.AlertDialog;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.View;
import android.widget.TextView;

import com.aqwas.androidtest.R;

import java.io.ByteArrayOutputStream;


public abstract class BaseActivity<T extends ViewDataBinding> extends AppCompatActivity {
    private static final String TAG = "ActivityHelper";

    AlertDialog dialog;
    private T mViewDataBinding;


    public abstract
    @LayoutRes
    int getLayoutId();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        performDataBinding();
        makeDialog();
    }

    protected void showProgressDialog() {
        if (dialog != null && !dialog.isShowing()) {
            dialog.show();
            dialog.getWindow().setLayout(200, 200);
        }
    }

    protected void dismissProgressDialog() {
        dialog.dismiss();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


    public void makeDialog() {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(this);
        View mView = getLayoutInflater().inflate(R.layout.loading_ui, null);
        mBuilder.setView(mView);
        dialog = mBuilder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setDimAmount(0.0f);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }

    protected void showSnakeBar(String message) {
        Snackbar snackbar = Snackbar.make(mViewDataBinding.getRoot(), message, Snackbar.LENGTH_LONG);
        snackbar.setActionTextColor(Color.BLACK);
        View sbView = snackbar.getView();
        sbView.setBackgroundColor(getResources().getColor(R.color.gray));
        TextView textView = sbView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(Color.BLACK);
        snackbar.show();
    }


    public T getViewDataBinding() {
        return mViewDataBinding;
    }

    private void performDataBinding() {
        mViewDataBinding = DataBindingUtil.setContentView(this, getLayoutId());
        mViewDataBinding.executePendingBindings();
    }


//    public String getCountryName(double latitude, double longitude) {
//        Geocoder geocoder = new Geocoder(this, new Locale(ResourceUtil.getCurrentLanguage(this)));
//        List<Address> addresses = null;
//        try {
//            addresses = geocoder.getFromLocation(latitude, longitude, 1);
//            Address result;
//
//            if (addresses != null && !addresses.isEmpty()) {
//                if (addresses.get(0).getLocality() == null)
//                    return addresses.get(0).getSubAdminArea();
//                else {
//                    Address address = addresses.get(0);
//                    String addressName = "";
//                    // Thoroughfare seems to be the street name without numbers
//                    if (address.getThoroughfare() != null) {
//                        addressName = address.getThoroughfare();
//                        if (addresses.get(0).getLocality() != null) {
//                            addressName = addressName + " , " + addresses.get(0).getLocality();
//                            if (addresses.get(0).getCountryName() != null) {
//                                addressName = addressName + " , " + addresses.get(0).getCountryName();
//                            }
//                        }
//                    } else if (addresses.get(0).getSubAdminArea() != null) {
//                        addressName = addresses.get(0).getSubAdminArea();
//                        if (addresses.get(0).getCountryName() != null) {
//                            addressName = addressName + " , " + addresses.get(0).getCountryName();
//                        }
//                    } else if (addresses.get(0).getCountryName() != null) {
//                        addressName = addresses.get(0).getCountryName();
//                    }
//                    String street = address.getThoroughfare();
//                    String name = street + " , " + addresses.get(0).getSubAdminArea() + " ," + addresses.get(0).getCountryName();
//                    return addressName;
//
//
//                }
//            }
//            return null;
//        } catch (Exception ignored) {
//            ignored.printStackTrace();
//            return null;
//        }
//
//    }


//    public void showgallary(final int i) {
//        AlertDialog.Builder mBuilder = new AlertDialog.Builder(this);
//        LayoutInflater li = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        View mView = li.inflate(R.layout.custom_item_gallarey, null);
//        final TextView Text_camera = mView.findViewById(R.id.camera);
//        final TextView Text_gallary = mView.findViewById(R.id.gallary);
//        final TextView Text_cancel = mView.findViewById(R.id.cancel);
//
//        Text_camera.setGravity(Gravity.CENTER);
//        Text_gallary.setGravity(Gravity.CENTER);
//        Text_cancel.setGravity(Gravity.CENTER);
//        mBuilder.setView(mView);
//        final AlertDialog dialog = mBuilder.create();
//        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//
//        Window window = dialog.getWindow();
//        WindowManager.LayoutParams wlp = window.getAttributes();
//
//        wlp.gravity = Gravity.BOTTOM;
//        wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
//        window.setAttributes(wlp);
//        dialog.show();
//
//
//        Text_camera.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                if (i == 1)
//                    startActivityForResult(cameraIntent, 222);
//                else if (i == 2)
//                    startActivityForResult(cameraIntent, 333);
//                dialog.dismiss();
//            }
//        });
//
//        Text_gallary.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (i == 1)
//                    openGallaryMultiple(666);
//                else if (i == 2)
//                    openGallary(777);
//                dialog.dismiss();
//            }
//        });
//
//        Text_cancel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                dialog.dismiss();
//            }
//        });
//
//    }

    public String encodeImage(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] b = baos.toByteArray();
        String encImage = Base64.encodeToString(b, Base64.DEFAULT);
        return encImage;
    }

    private void openGallary(int request_code) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), request_code);
    }

    private void openGallaryMultiple(int request_code) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), request_code);
    }


}
