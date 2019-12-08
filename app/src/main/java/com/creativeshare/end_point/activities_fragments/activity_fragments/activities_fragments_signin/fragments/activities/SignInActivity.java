package com.creativeshare.end_point.activities_fragments.activity_fragments.activities_fragments_signin.fragments.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;


import com.creativeshare.end_point.R;
import com.creativeshare.end_point.activities_fragments.activity_fragments.activities_fragments_signin.fragments.Fragment_Sign_In;
import com.creativeshare.end_point.activities_fragments.activity_fragments.activity_home.HomeActivity;
import com.creativeshare.end_point.language.Language;
import com.creativeshare.end_point.preferences.Preferences;

import java.util.Locale;

import io.paperdb.Paper;

public class SignInActivity extends AppCompatActivity {

    private FragmentManager fragmentManager;

    private int fragment_count = 0;
    private Fragment_Sign_In fragment_sign_in;

    private String cuurent_language;
    private Preferences preferences;
    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(Language.updateResources(newBase, Paper.book().read("lang", Locale.getDefault().getLanguage())));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        Paper.init(this);


        initView();
        if (savedInstanceState == null) {
           /* if (!preferences.isLanguageSelected(this))
            {
                DisplayFragmentLanguage();
            }else
            {
                DisplayFragmentSignIn();

            }*/
            DisplayFragmentSignIn();

        }

    }

    private void initView() {
        Paper.init(this);
        preferences = Preferences.getInstance();
        cuurent_language = Paper.book().read("lang", Locale.getDefault().getLanguage());
        fragmentManager = this.getSupportFragmentManager();

    }

    public void DisplayFragmentSignIn() {
        fragment_count += 1;
        fragment_sign_in = Fragment_Sign_In.newInstance();
        if (fragment_sign_in.isAdded()) {
            fragmentManager.beginTransaction().show(fragment_sign_in).commit();
        } else {
            fragmentManager.beginTransaction().add(R.id.fragment_sign_in_container, fragment_sign_in, "fragment_sign_in").addToBackStack("fragment_sign_in").commit();
        }
    }




    public void RefreshActivity(String selected_language) {
        Paper.book().write("lang", selected_language);
        Language.setNewLocale(this, selected_language);
        preferences.setIsLanguageSelected(this);


        Intent intent = getIntent();
        finish();
        startActivity(intent);


    }
    @Override
    public void onBackPressed() {
        Back();
    }

    public void Back() {
        if (fragment_sign_in!=null&&fragment_sign_in.isAdded()&&fragment_sign_in.isVisible())
        {
            finish();

        }else
        {
            if (fragment_count >1) {
                fragment_count -= 1;
                super.onBackPressed();


            } else  {

finish();
            }
        }


    }

    public void NavigateToHomeActivity()
    {
        Intent intent = new Intent(SignInActivity.this, HomeActivity.class);
        startActivity(intent);
        finish();
    }
}
