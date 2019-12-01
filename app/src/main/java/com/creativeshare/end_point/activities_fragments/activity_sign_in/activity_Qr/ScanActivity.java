package com.creativeshare.end_point.activities_fragments.activity_sign_in.activity_Qr;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import com.creativeshare.end_point.R;
import com.creativeshare.end_point.databinding.ActivityHomeBinding;
import com.creativeshare.end_point.databinding.ActivityScanBinding;
import com.creativeshare.end_point.language.Language;
import com.google.zxing.Result;

import io.paperdb.Paper;
import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class ScanActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler{
    private ActivityScanBinding binding;
    private final int CAMERA_REQ = 1022;
    private final String camera_perm = Manifest.permission.CAMERA;

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
    }


    @Override
    public void handleResult(Result result) {

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
}
