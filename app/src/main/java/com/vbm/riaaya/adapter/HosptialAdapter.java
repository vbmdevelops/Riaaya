package com.vbm.riaaya.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.vbm.riaaya.AvailableTestActivity;
import com.vbm.riaaya.R;
import com.vbm.riaaya.data.Hosptial_Data;

import java.util.List;

public class HosptialAdapter extends RecyclerView.Adapter<HosptialAdapter.ViewHolder> {
    private List<Hosptial_Data> categoty_data;
    private Context context;
    public HosptialAdapter(List<Hosptial_Data> categoty_data, Context context) {
        this.categoty_data = categoty_data;
        this.context = context;
    }

    @Override
    public HosptialAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.hoaptial_list,parent,false);
        return new HosptialAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final HosptialAdapter.ViewHolder holder, final int position) {
        final Hosptial_Data listData= categoty_data.get(position);
       // Picasso.with(context).load(listData.getfile().replace(" ","%20")).into(holder.class_imageView);

        try {

            Glide.with(context)
                    .load(listData.getfile().replace(" ","%20"))
                    .skipMemoryCache(false)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(holder.class_imageView);

        } catch (Exception e) {
            e.printStackTrace();
        }

        Log.e("hosptial",listData.getfile().replace(" ","%20"));
        holder.hos_name.setText(listData.gethos_name());
        holder.txt_viewmore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, AvailableTestActivity.class);
                intent.putExtra("hosid", listData.getid());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return categoty_data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView hos_name;
        TextView txt_viewmore;
        ImageView class_imageView;
        public ViewHolder(View itemView) {
            super(itemView);
            class_imageView = (ImageView)itemView.findViewById(R.id.class_imageView);
            hos_name = (TextView)itemView.findViewById(R.id.hos_name);
            txt_viewmore = (TextView) itemView.findViewById(R.id.txt_viewmore);
        }
    }
}