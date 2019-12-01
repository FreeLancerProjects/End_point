package com.creativeshare.end_point.activities_fragments.activity_sign_in.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;


import com.creativeshare.end_point.R;
import com.creativeshare.end_point.activities_fragments.activity_sign_in.activities.SignInActivity;
import com.creativeshare.end_point.databinding.FragmentSignInBinding;
import com.creativeshare.end_point.interfaces.Listeners;
import com.creativeshare.end_point.models.LoginModel;
import com.creativeshare.end_point.preferences.Preferences;
import com.creativeshare.end_point.share.Common;

import java.io.IOException;
import java.util.Locale;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment_Sign_In extends Fragment implements Listeners.LoginListener {
    private FragmentSignInBinding binding;
    private SignInActivity activity;
    private String current_language;
    private Preferences preferences;
    private LoginModel loginModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_sign_in, container, false);
        View view = binding.getRoot();
        initView();
        return view;
    }

    private void initView() {
        loginModel = new LoginModel();
        activity = (SignInActivity) getActivity();
        preferences = Preferences.getInstance();
        Paper.init(activity);
        current_language = Paper.book().read("lang", Locale.getDefault().getLanguage());
        binding.setLoginModel(loginModel);
        binding.setLang(current_language);
        binding.setLoginListener(this);





    }



    public static Fragment_Sign_In newInstance() {
        return new Fragment_Sign_In();
    }


    @Override
    public void checkDataLogin(String user_name, String password) {

        loginModel = new LoginModel(user_name,password);
        binding.setLoginModel(loginModel);

        if (loginModel.isDataValid(activity))
        {
            activity.NavigateToHomeActivity();
           // Toast.makeText(activity,user_name,Toast.LENGTH_LONG).show();
        }
    }





}
