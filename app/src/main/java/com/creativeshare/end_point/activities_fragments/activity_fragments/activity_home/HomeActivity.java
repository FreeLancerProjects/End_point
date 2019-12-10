package com.creativeshare.end_point.activities_fragments.activity_fragments.activity_home;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
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

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import io.paperdb.Paper;

public class HomeActivity extends AppCompatActivity {
    private ActivityHomeBinding binding;
    private Preferences preferences;
private String current_language;
private UserModel userModel;
    private CountDownTimer countDownTimer;
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
gettime();


    }

    private void gettime() {
        Calendar cal = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        String end_date="12:00";

        Date currentLocalTime = cal.getTime();
        DateFormat date = new SimpleDateFormat("HH:mm", Locale.ENGLISH);
        Date end = null,cureenttime = null;
        String localTime = date.format(currentLocalTime);
        try {
            end=date.parse(end_date);
            cureenttime=date.parse(localTime);
        } catch (ParseException e) {
            Log.e("err",e.getMessage()+localTime);        }
        cal.setTime(cureenttime);
        cal2.setTime(end);

        int nowHour = cal.get(Calendar.HOUR_OF_DAY);
        int nowMin = cal.get(Calendar.MINUTE);
        int timeNow = nowHour*60 + nowMin;
        int stopSHhour = cal2.get(Calendar.HOUR_OF_DAY);
        int stopSHmin = cal2.get(Calendar.MINUTE);
        int timeStop = stopSHhour*60 + stopSHmin;
        Log.e("time",(timeStop+" "+timeNow)+""+preferences.getTime(this));
        if(timeStop>timeNow){
            Log.e("time",(timeStop-timeNow)+""+preferences.getTime(this));

            if(preferences.getTime(this).equals("1")){

startCounter(timeStop-timeNow);
            }
        }
        else {
            preferences.create_update_time(this,"0");
        }
    }
    private void startCounter(long time)
    {

        time=(time*60)*1000;
        Log.e("times",time+"");
        countDownTimer = new CountDownTimer(time, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {
                binding.tvTime.setVisibility(View.VISIBLE);
                binding.btnscan.setVisibility(View.GONE);
                int AllSeconds = (int) (millisUntilFinished / 1000);
                int allminute=AllSeconds/60;
                int hour=allminute/60;
                int minute=allminute%60;
                int seconds= AllSeconds%60;
                binding.tvTime.setText(hour+":"+minute+":"+seconds);
            }

            @Override
            public void onFinish() {

                binding.btnscan.setVisibility(View.VISIBLE);
                binding.tvTime.setVisibility(View.GONE);
                preferences.create_update_time(HomeActivity.this,"0");

            }
        }.start();
    }
    @SuppressLint("RestrictedApi")
    private void initView() {
       preferences = Preferences.getInstance();
        String visitTime = preferences.getLastVisit(this);
        Calendar calendar = Calendar.getInstance();
        long timeNow = calendar.getTimeInMillis();

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        String date = dateFormat.format(new Date(timeNow));

        if (!date.equals(visitTime)) {
            addVisit(date);
        }
        Paper.init(this);
        current_language = Paper.book().read("lang", Locale.getDefault().getLanguage());
        binding.setLang(current_language);
        userModel=preferences.getUserData(this);
        binding.tvTime.setVisibility(View.GONE);
        binding.btnscan.setVisibility(View.VISIBLE);
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
        startActivityForResult(intent,3);
    }
});
binding.llTimes.setOnClickListener(new View.OnClickListener() {
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

    private void addVisit(String date) {
        preferences.create_update_time(this,"0");
        preferences.setLastVisit(this,date);
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
        gettime();
    }

    private void CreateLanguageDialog()
    {
        final AlertDialog dialog = new AlertDialog.Builder(this)
                .setCancelable(true)
                .create();

        DialogLanguageBinding binding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.dialog_language, null, false);



        if (current_language.equals("ar"))
        {
            binding.rbAr.setChecked(true);

        }else if (current_language.equals("en"))
        {
            binding.rbEn.setChecked(true);

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
