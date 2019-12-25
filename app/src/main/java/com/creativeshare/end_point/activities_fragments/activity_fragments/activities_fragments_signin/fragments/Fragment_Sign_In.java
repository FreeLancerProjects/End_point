package com.creativeshare.end_point.activities_fragments.activity_fragments.activities_fragments_signin.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import android.app.ProgressDialog;


import com.creativeshare.end_point.R;
import com.creativeshare.end_point.activities_fragments.activity_fragments.activities_fragments_signin.fragments.activities.SignInActivity;
import com.creativeshare.end_point.activities_fragments.activity_fragments.activity_home.HomeActivity;
import com.creativeshare.end_point.databinding.FragmentSignInBinding;
import com.creativeshare.end_point.interfaces.Listeners;
import com.creativeshare.end_point.models.LoginModel;
import com.creativeshare.end_point.models.UserModel;
import com.creativeshare.end_point.preferences.Preferences;
import com.creativeshare.end_point.remote.Api;
import com.creativeshare.end_point.share.Common;
import com.creativeshare.end_point.tags.Tags;

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
login(loginModel);          // Toast.makeText(activity,user_name,Toast.LENGTH_LONG).show();
        }
    }

    private void login(LoginModel loginModel)
    {
          final ProgressDialog dialog = Common.createProgressDialog(activity,getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();
        try {

            Api.getService(Tags.base_url)
                    .login(loginModel.getEmail()+"@gmail.com",loginModel.getPassword())
                    .enqueue(new Callback<UserModel>() {
                        @Override
                        public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                            dialog.dismiss();
                            if (response.isSuccessful()&&response.body()!=null)
                            {
                                preferences.create_update_userdata(activity,response.body());
                                preferences.create_update_session(activity, Tags.session_login);
                                Intent intent = new Intent(activity, HomeActivity.class);
                                startActivity(intent);
                                activity.finish();

                            }else
                            {
                                if (response.code() == 422) {
                                    Toast.makeText(activity, getString(R.string.inv_em_ps), Toast.LENGTH_SHORT).show();
                                } else if (response.code() == 500) {
                                    Toast.makeText(activity, "Server Error", Toast.LENGTH_SHORT).show();


                                }else if (response.code()==401||response.code()==404)
                                {
                                    Toast.makeText(activity, R.string.inv_em_ps, Toast.LENGTH_SHORT).show();

                                }else
                                {
                                    Toast.makeText(activity, getString(R.string.failed), Toast.LENGTH_SHORT).show();

                                    try {

                                        Log.e("error",response.code()+"_"+response.errorBody().string());
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<UserModel> call, Throwable t) {
                            try {
                                dialog.dismiss();
                                if (t.getMessage()!=null)
                                {
                                    Log.e("error",t.getMessage());
                                    if (t.getMessage().toLowerCase().contains("failed to connect")||t.getMessage().toLowerCase().contains("unable to resolve host"))
                                    {
                                        Toast.makeText(activity,R.string.something, Toast.LENGTH_SHORT).show();
                                    }else
                                    {
                                        Toast.makeText(activity,t.getMessage(), Toast.LENGTH_SHORT).show();
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
