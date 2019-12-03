package com.creativeshare.end_point.activities_fragments.activity_sign_in.activity_Qr;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import com.creativeshare.end_point.R;
import com.creativeshare.end_point.activity_home.HomeActivity;
import com.creativeshare.end_point.databinding.ActivityHomeBinding;
import com.creativeshare.end_point.databinding.ActivityScanBinding;
import com.creativeshare.end_point.language.Language;
import com.creativeshare.end_point.models.LoginModel;
import com.creativeshare.end_point.models.ScanResultModel;
import com.creativeshare.end_point.models.UserModel;
import com.creativeshare.end_point.preferences.Preferences;
import com.creativeshare.end_point.remote.Api;
import com.creativeshare.end_point.share.Common;
import com.creativeshare.end_point.tags.Tags;
import com.google.zxing.Result;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import io.paperdb.Paper;
import me.dm7.barcodescanner.zxing.ZXingScannerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ScanActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler{
    private ActivityScanBinding binding;
    private final int CAMERA_REQ = 1022;
    private final String camera_perm = Manifest.permission.CAMERA;
private Preferences preferences;
private UserModel userModel;
    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(Language.updateResources(newBase, Paper.book().read("lang", "ar")));

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_scan);
        initView();
checkCameraPermission();

    }
    private void checkCameraPermission()
    {
        if (ContextCompat.checkSelfPermission(this, camera_perm) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{camera_perm}, CAMERA_REQ);

        } else {
            binding.scannerView.startCamera();


        }
    }
    @SuppressLint("RestrictedApi")
    private void initView() {
        binding.scannerView.setFormats(ZXingScannerView.ALL_FORMATS);
        binding.scannerView.setResultHandler(this);
        binding.scannerView.setAutoFocus(false);
preferences=Preferences.getInstance();
userModel=preferences.getUserData(this);
         }


    @Override
    public void handleResult(Result result) {
Toast.makeText(this,result.toString(),Toast.LENGTH_LONG).show();
        Toast.makeText(this,result.getBarcodeFormat().toString(),Toast.LENGTH_LONG).show();
        Toast.makeText(this,result.getText(),Toast.LENGTH_LONG).show();
scan(result.getText());

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_REQ) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (binding.scannerView != null) {
                    binding.scannerView.startCamera();
                }
            } else {
                Toast.makeText(this, R.string.cam_pem_denied, Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        checkCameraPermission();
    }
    @Override
    protected void onStop()
    {
        super.onStop();
        if (binding.scannerView != null) {
            binding.scannerView.stopCamera();
        }
    }
    private void scan(String code)
    {
        final ProgressDialog dialog = Common.createProgressDialog(this,getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();


        Calendar cal = Calendar.getInstance();
        Date currentLocalTime = cal.getTime();
        DateFormat date = new SimpleDateFormat("HH:mm a");
// you can get seconds by adding  "...:ss" to it

        String localTime = date.format(currentLocalTime);


        try {

            Api.getService(Tags.base_url)
                    .scan(userModel.getId()+"",code,localTime)
                    .enqueue(new Callback<ScanResultModel>() {
                        @Override
                        public void onResponse(Call<ScanResultModel> call, Response<ScanResultModel> response) {
                            dialog.dismiss();
                            if (response.isSuccessful()&&response.body()!=null)
                            {
                                Toast.makeText(ScanActivity.this,response.body().getAttendance_time(),Toast.LENGTH_LONG).show();
                             finish();

                            }else
                            {
                                if (response.code() == 500) {
                                    Toast.makeText(ScanActivity.this, "Server Error", Toast.LENGTH_SHORT).show();


                                }else
                                {
                                    Toast.makeText(ScanActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();

                                    try {

                                        Log.e("error",response.code()+"_"+response.errorBody().string());
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<ScanResultModel> call, Throwable t) {
                            try {
                                dialog.dismiss();
                                if (t.getMessage()!=null)
                                {
                                    Log.e("error",t.getMessage());
                                    if (t.getMessage().toLowerCase().contains("failed to connect")||t.getMessage().toLowerCase().contains("unable to resolve host"))
                                    {
                                        Toast.makeText(ScanActivity.this,R.string.something, Toast.LENGTH_SHORT).show();
                                    }else
                                    {
                                        Toast.makeText(ScanActivity.this,t.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }

                            }catch (Exception e){}
                        }
                    });
        }catch (Exception e){
            dialog.dismiss();

        }
    }
}
