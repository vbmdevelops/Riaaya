package com.vbm.riaaya.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.vbm.riaaya.AvailableTestActivity;
import com.vbm.riaaya.R;
import com.vbm.riaaya.data.Hosptial_Data;

import java.util.ArrayList;
import java.util.List;

public class AllHosptialListAdapter extends RecyclerView.Adapter<AllHosptialListAdapter.ViewHolder> {
    public List<Hosptial_Data> hosptial_Data;
    private Context context;
    HosptialFilter1 filter;
    public AllHosptialListAdapter(List<Hosptial_Data> hosptial_Data, Context context) {
        this.hosptial_Data = hosptial_Data;
        this.context = context;
    }

    @Override
    public AllHosptialListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.all_medical_testlist,parent,false);
        return new AllHosptialListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final AllHosptialListAdapter.ViewHolder holder, final int position) {
        final Hosptial_Data listData= hosptial_Data.get(position);

        holder.txt_title.setText(listData.gethos_name());
       /* Picasso.with(context).load(listData.getfile()).into(holder.img_test);*/

        try {

            Glide.with(context)
                    .load(listData.getfile().replace(" ","%20"))
                    .skipMemoryCache(false)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(holder.img_test);

        } catch (Exception e) {
            e.printStackTrace();
        }


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
        return hosptial_Data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private ImageView img_test;
        TextView txt_viewmore;
        TextView txt_title;
        public ViewHolder(View itemView) {
            super(itemView);
            img_test=(ImageView)itemView.findViewById(R.id.img_test);
            txt_title = (TextView)itemView.findViewById(R.id.txt_title);
            txt_viewmore = (TextView) itemView.findViewById(R.id.txt_viewmore);
        }
    }

    public Filter getFilter() {
        if(filter==null)
        {
            filter=new HosptialFilter1((ArrayList<Hosptial_Data>) hosptial_Data,this);
        }

        return filter;
    }
}