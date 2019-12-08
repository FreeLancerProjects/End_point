package com.creativeshare.end_point.adapter;

import android.content.Context;
import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;


import com.creativeshare.end_point.R;
import com.creativeshare.end_point.databinding.LoadMoreBinding;
import com.creativeshare.end_point.databinding.TimeRowBinding;
import com.creativeshare.end_point.models.Times_Model;

import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;

public class Times_Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final int ITEM_DATA = 1;
    private final int LOAD = 2;
    private List<Times_Model.Data> orderlist;
    private Context context;
    private LayoutInflater inflater;
    private String lang;

    public Times_Adapter(List<Times_Model.Data> orderlist, Context context) {
        this.orderlist = orderlist;
        this.context = context;
        inflater = LayoutInflater.from(context);
        Paper.init(context);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (viewType==ITEM_DATA)
        {
            TimeRowBinding binding  = DataBindingUtil.inflate(inflater, R.layout.time_row,parent,false);
            return new EventHolder(binding);

        }else
            {
                LoadMoreBinding binding = DataBindingUtil.inflate(inflater, R.layout.load_more,parent,false);
                return new LoadHolder(binding);
            }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Times_Model.Data order_data = orderlist.get(position);
        if (holder instanceof EventHolder)
        {
            EventHolder eventHolder = (EventHolder) holder;
            eventHolder.binding.setLang(lang);
            eventHolder.binding.setTimesModel(order_data);






        }else
            {
                LoadHolder loadHolder = (LoadHolder) holder;
                loadHolder.binding.progBar.setIndeterminate(true);
            }
    }

    @Override
    public int getItemCount() {
        return orderlist.size();
    }

    public class EventHolder extends RecyclerView.ViewHolder {
        public TimeRowBinding binding;
        public EventHolder(@NonNull TimeRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;


        }
    }

    public class LoadHolder extends RecyclerView.ViewHolder {
        private LoadMoreBinding binding;
        public LoadHolder(@NonNull LoadMoreBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            binding.progBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(context,R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
        }

    }

    @Override
    public int getItemViewType(int position) {
        Times_Model.Data order_Model = orderlist.get(position);
        if (order_Model!=null)
        {
            return ITEM_DATA;
        }else
            {
                return LOAD;
            }

    }


}
