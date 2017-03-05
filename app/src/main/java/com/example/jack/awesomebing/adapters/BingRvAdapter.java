package com.example.jack.awesomebing.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.jack.awesomebing.R;
import com.example.jack.awesomebing.beanForGson.DailyInfo;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Jack on 2017/3/5.
 */
public class BingRvAdapter extends RecyclerView.Adapter<BingRvAdapter.BingItemHolder> {
    private Context context;
    private ArrayList<DailyInfo> infos;

    public BingRvAdapter(Context context,ArrayList<DailyInfo> infos) {
        this.context = context;
        this.infos = infos;
    }

    @Override
    public BingItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        BingItemHolder holder = new BingItemHolder(LayoutInflater.from(context).inflate(R.layout.item_layout,parent,false));
        return holder;
    }

    @Override
    public void onBindViewHolder(BingItemHolder holder, int position) {
        DailyInfo info = infos.get(position);
        holder.tv_date.setText(info.enddate);
        Picasso.with(context).load(info.bmiddle_pic).into(holder.image);
    }

    @Override
    public int getItemCount() {
        return infos.size();
    }



    public class BingItemHolder extends RecyclerView.ViewHolder {
        private ImageView image;
        private TextView tv_date;

        public BingItemHolder(View itemView) {
            super(itemView);
            this.image = (ImageView) itemView.findViewById(R.id.image);
            this.tv_date = (TextView) itemView.findViewById(R.id.tv_date);
        }
    }
}
