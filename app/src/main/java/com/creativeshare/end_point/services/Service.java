package com.creativeshare.end_point.services;



import com.creativeshare.end_point.models.ScanResultModel;
import com.creativeshare.end_point.models.UserModel;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface Service {

    @FormUrlEncoded
    @POST("api/login")
    Call<UserModel> login(
            @Field("email") String email,
            @Field("password") String password
    );
    @FormUrlEncoded
    @POST("api/attendance")
    Call<ScanResultModel> scan(
            @Field("user_id") String user_id,
            @Field("qrcode_key") String qrcode_key,
            @Field("attendance_time") String attendance_time
    );
}