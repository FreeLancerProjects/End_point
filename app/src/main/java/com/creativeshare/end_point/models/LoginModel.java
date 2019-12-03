package com.creativeshare.end_point.models;

import android.content.Context;
import android.text.TextUtils;
import android.util.Patterns;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.ObservableField;

import com.creativeshare.end_point.BR;
import com.creativeshare.end_point.R;

public class LoginModel extends BaseObservable {

    private String email;
    private String password;
    public ObservableField<String> error_email= new ObservableField<>();
    public ObservableField<String> error_password = new ObservableField<>();


    public LoginModel() {
        this.email = "";
        this.password="";
    }

    public LoginModel(String email, String password) {
        this.email = email;
        notifyPropertyChanged(BR.email);
        this.password = password;
        notifyPropertyChanged(BR.password);


    }
    @Bindable
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Bindable
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
        notifyPropertyChanged(BR.password);

    }

    public boolean isDataValid(Context context)
    {
        if (!TextUtils.isEmpty(email)&&
                Patterns.EMAIL_ADDRESS.matcher(email).matches()&&
                !TextUtils.isEmpty(password)&&
                password.length()>=6
        )
        {
            error_email.set(null);
            error_password.set(null);

            return true;
        }else
            {
                if (email.isEmpty())
                {
                    error_email.set(context.getString(R.string.field_req));
                }
                else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches())
                {
                    error_email.set(context.getString(R.string.inv_email));}

                else
                    {
                        error_email.set(null);
                    }


                if (password.isEmpty())
                {
                    error_password.set(context.getString(R.string.field_req));
                }else if (password.length()<6)
                {
                    error_password.set(context.getString(R.string.pass_short));
                }else
                    {
                        error_password.set(null);

                    }
                return false;
            }
    }


}
