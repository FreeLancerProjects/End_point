package com.creativeshare.end_point.activities_fragments.activity_fragments.activity_home;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.creativeshare.end_point.R;
import com.creativeshare.end_point.activities_fragments.activity_fragments.activities_fragments_signin.fragments.activities.SignInActivity;
import com.creativeshare.end_point.activities_fragments.activity_fragments.activity_Qr.ScanActivity;
import com.creativeshare.end_point.databinding.ActivityHomeBinding;
import com.creativeshare.end_point.language.Language;
import com.creativeshare.end_point.models.UserModel;
import com.creativeshare.end_point.preferences.Preferences;
import com.creativeshare.end_point.tags.Tags;

import java.util.Locale;

import io.paperdb.Paper;

public class HomeActivity extends AppCompatActivity {
    private ActivityHomeBinding binding;
    private Preferences preferences;
private String current_language;
private UserModel userModel;
    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(Language.updateResources(newBase, Paper.book().read("lang", "ar")));

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home);
        initView();



    }

    @SuppressLint("RestrictedApi")
    private void initView() {
       preferences = Preferences.getInstance();
        Paper.init(this);
        current_language = Paper.book().read("lang", Locale.getDefault().getLanguage());
        binding.setLang(current_language);
        userModel=preferences.getUserData(this);
        if(userModel!=null){
            binding.tvName.setText(userModel.getName());
        }
        binding.imagelogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Logout();
            }
        });
binding.btnscan.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        Intent intent = new Intent(HomeActivity.this, ScanActivity.class);
        startActivity(intent);
    }
});
    }

    private void Logout() {
        preferences.create_update_userdata(this,null);
        preferences.create_update_session(this, Tags.session_logout);
        Intent intent = new Intent(HomeActivity.this, SignInActivity.class);
        startActivity(intent);
        finish();
    }


}
