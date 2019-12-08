package com.creativeshare.end_point.activities_fragments.activity_fragments.activity_home;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;

import com.creativeshare.end_point.R;
import com.creativeshare.end_point.activities_fragments.activity_fragments.activities_fragments_signin.fragments.activities.SignInActivity;
import com.creativeshare.end_point.activities_fragments.activity_fragments.activity_Qr.ScanActivity;
import com.creativeshare.end_point.activities_fragments.activity_fragments.activity_my_times.MyTimesActivity;
import com.creativeshare.end_point.databinding.ActivityHomeBinding;
import com.creativeshare.end_point.databinding.DialogLanguageBinding;
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
    private String [] language_array;
    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(Language.updateResources(newBase, Paper.book().read("lang", Locale.getDefault().getLanguage())));

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
        language_array = new String[]{"English","العربية"};
        if(userModel!=null){
            binding.tvName.setText(userModel.getName());
        }
        binding.llLogout.setOnClickListener(new View.OnClickListener() {
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
binding.imagetimes.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        Intent intent = new Intent(HomeActivity.this, MyTimesActivity.class);
        startActivity(intent);
    }
});
binding.llLang.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
CreateLanguageDialog();
    }
});
binding.imagemenue.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
       binding.drawerLayout.openDrawer(GravityCompat.START);
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
    private void refreshActivity(String lang) {
        preferences.create_update_language(this, lang);
        Paper.book().write("lang", lang);
        Language.setNewLocale(this, lang);
        Intent intent = getIntent();
        finish();
        startActivity(intent);}
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==11&&resultCode==RESULT_OK&&data!=null)
        {
            if (data.hasExtra("lang"))
            {
                final String lang = data.getStringExtra("lang");
                new Handler()
                        .postDelayed(new Runnable() {
                            @Override
                            public void run() {
                            refreshActivity(lang);
                            }
                        },1000);

            }
        }
    }

    private void CreateLanguageDialog()
    {
        final AlertDialog dialog = new AlertDialog.Builder(this)
                .setCancelable(true)
                .create();

        DialogLanguageBinding binding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.dialog_language, null, false);



        if (current_language.equals("ar"))
        {
            binding.rbAr.setSelected(true);

        }else if (current_language.equals("en"))
        {
            binding.rbEn.setSelected(true);

        }
        binding.rbAr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
               refreshActivity("ar");


            }
        });

        binding.rbEn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                refreshActivity("en");


            }
        });


        binding.btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        //  dialog.getWindow().getAttributes().windowAnimations = R.style.dialog_congratulation_animation;
        dialog.setView(binding.getRoot());
        dialog.show();
    }

    @Override
    public void onBackPressed() {
        Back();
    }

    private void Back() {
        if(binding.drawerLayout.isDrawerOpen(GravityCompat.START)){
            binding.drawerLayout.closeDrawer(GravityCompat.START);
        }
        else {
            finish();
        }

    }

}
