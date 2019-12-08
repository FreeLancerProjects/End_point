package com.creativeshare.end_point.general_ui_method;


import android.widget.TextView;

import androidx.databinding.BindingAdapter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class GeneralMethod {



    @BindingAdapter({"Time"})
    public static void displayTime(TextView textView, String time) {
       double times=Double.parseDouble(time);
       int hour= (int) (times/60);
       int minutes= (int) (times%60);
       textView.setText(hour+":"+minutes);
    }


}
