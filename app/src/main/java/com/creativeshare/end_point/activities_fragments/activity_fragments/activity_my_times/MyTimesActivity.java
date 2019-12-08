package com.creativeshare.end_point.activities_fragments.activity_fragments.activity_my_times;

import android.content.Context;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.creativeshare.end_point.R;
import com.creativeshare.end_point.adapter.Times_Adapter;
import com.creativeshare.end_point.databinding.ActivityMyTimesBinding;
import com.creativeshare.end_point.interfaces.Listeners;
import com.creativeshare.end_point.language.Language;
import com.creativeshare.end_point.models.Times_Model;
import com.creativeshare.end_point.models.UserModel;
import com.creativeshare.end_point.preferences.Preferences;
import com.creativeshare.end_point.remote.Api;
import com.creativeshare.end_point.tags.Tags;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyTimesActivity extends AppCompatActivity implements Listeners.BackListener {
    private ActivityMyTimesBinding binding;
    private String lang;
    private Times_Adapter times_adapter;
    private List<Times_Model.Data> advesriment_data_list;
    private LinearLayoutManager manager;
    private boolean isLoading = false;
    private int current_page2 = 1;
    private Preferences preferences;
    private UserModel userModel;
    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(Language.updateResources(newBase, Paper.book().read("lang", Locale.getDefault().getLanguage())));

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_my_times);
        initView();
        if(userModel!=null){
        getAds();}

    }

    private void initView() {
        advesriment_data_list=new ArrayList<>();
        preferences= Preferences.getInstance();
        userModel=preferences.getUserData(this);
        Paper.init(this);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        binding.setLang(lang);
        binding.setBackListener(this);
        manager = new LinearLayoutManager(this);

        binding.progBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(this, R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
        binding.recView.setLayoutManager(manager);
        times_adapter = new Times_Adapter(advesriment_data_list,this);
        binding.recView.setItemViewCacheSize(25);
        binding.recView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        binding.recView.setDrawingCacheEnabled(true);
        binding.progBar.setVisibility(View.GONE);
        binding.llNoTimes.setVisibility(View.GONE);

        binding.recView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if(dy>0){
                    int totalItems = times_adapter.getItemCount();
                    int lastVisiblePos = manager.findLastCompletelyVisibleItemPosition();
                    if (totalItems > 5 && (totalItems - lastVisiblePos) == 1 && !isLoading) {
                        isLoading = true;
                        advesriment_data_list.add(null);
                        times_adapter.notifyItemInserted(advesriment_data_list.size() - 1);
                        int page= current_page2 +1;
                        loadMore(page);




                    }
                }
            }
        });
        binding.recView.setAdapter(times_adapter);

    }
    public void getAds() {
        //   Common.CloseKeyBoard(homeActivity, edt_name);
        advesriment_data_list.clear();
        times_adapter.notifyDataSetChanged();
        binding.progBar.setVisibility(View.VISIBLE);

        // rec_sent.setVisibility(View.GONE);
        try {


            Api.getService( Tags.base_url)
                    .getTimes(1,userModel.getId()+"")
                    .enqueue(new Callback<Times_Model>() {
                        @Override
                        public void onResponse(Call<Times_Model> call, Response<Times_Model> response) {

                            binding.progBar.setVisibility(View.GONE);
                            if (response.isSuccessful() && response.body() != null && response.body().getData() != null) {
                                advesriment_data_list.clear();
                                advesriment_data_list.addAll(response.body().getData());
                                if (response.body().getData().size() > 0) {
                                    // rec_sent.setVisibility(View.VISIBLE);
                                    //  Log.e("data",response.body().getData().get(0).getAr_title());

                                    binding.llNoTimes.setVisibility(View.GONE);
                                    times_adapter.notifyDataSetChanged();
                                    //   total_page = response.body().getMeta().getLast_page();

                                } else {
                                    times_adapter.notifyDataSetChanged();

                                    binding.llNoTimes.setVisibility(View.VISIBLE);

                                }
                            } else {
                                times_adapter.notifyDataSetChanged();

                                binding.llNoTimes.setVisibility(View.VISIBLE);

                                //Toast.makeText(activity, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                                try {
                                    Log.e("Error_code", response.code() + "_" + response.errorBody().string());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<Times_Model> call, Throwable t) {
                            try {

                                binding.progBar.setVisibility(View.GONE);
                                binding.llNoTimes.setVisibility(View.VISIBLE);


                                Toast.makeText(MyTimesActivity.this, getString(R.string.something), Toast.LENGTH_SHORT).show();
                                Log.e("error", t.getMessage());
                            } catch (Exception e) {
                            }
                        }
                    });
        }catch (Exception e){
            binding.progBar.setVisibility(View.GONE);
            binding.llNoTimes.setVisibility(View.VISIBLE);

        }
    }

    private void loadMore(int page) {
        try {


            Api.getService(Tags.base_url)
                    .getTimes(page, userModel.getId()+"")
                    .enqueue(new Callback<Times_Model>() {
                        @Override
                        public void onResponse(Call<Times_Model> call, Response<Times_Model> response) {
                            advesriment_data_list.remove(advesriment_data_list.size() - 1);
                            times_adapter.notifyItemRemoved(advesriment_data_list.size() - 1);
                            isLoading = false;
                            if (response.isSuccessful() && response.body() != null && response.body().getData() != null) {

                                advesriment_data_list.addAll(response.body().getData());
                                // categories.addAll(response.body().getCategories());
                                current_page2 = response.body().getCurrent_page();
                                times_adapter.notifyDataSetChanged();

                            } else {
                                //Toast.makeText(activity, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                                try {
                                    Log.e("Error_code", response.code() + "_" + response.errorBody().string());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<Times_Model> call, Throwable t) {
                            try {
                                advesriment_data_list.remove(advesriment_data_list.size() - 1);
                                times_adapter.notifyItemRemoved(advesriment_data_list.size() - 1);
                                isLoading = false;
                                // Toast.makeText(activity, getString(R.string.something), Toast.LENGTH_SHORT).show();
                                Log.e("error", t.getMessage());
                            } catch (Exception e) {
                            }
                        }
                    });}
        catch (Exception e){
            advesriment_data_list.remove(advesriment_data_list.size() - 1);
            times_adapter.notifyItemRemoved(advesriment_data_list.size() - 1);
            isLoading = false;
        }
    }

    @Override
    public void back() {
        finish();
    }
}
