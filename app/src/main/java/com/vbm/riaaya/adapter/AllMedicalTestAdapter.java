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
import com.squareup.picasso.Picasso;
import com.vbm.riaaya.BookTestList_Activity;
import com.vbm.riaaya.LabTestDetailsActivity;
import com.vbm.riaaya.R;
import com.vbm.riaaya.data.Hosptial_Data;
import com.vbm.riaaya.data.MedicalTest_Data;

import java.util.ArrayList;
import java.util.List;

public class AllMedicalTestAdapter extends RecyclerView.Adapter<AllMedicalTestAdapter.ViewHolder> {
    public List<MedicalTest_Data> medicalTest_Data;
    private Context context;
    CustomFilter filter;

    public AllMedicalTestAdapter(List<MedicalTest_Data> medicalTest_Data, Context context) {
        this.medicalTest_Data = medicalTest_Data;
        this.context = context;
    }

    @Override
    public AllMedicalTestAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.all_medical_testlist,parent,false);
        return new AllMedicalTestAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final AllMedicalTestAdapter.ViewHolder holder, final int position) {
        final MedicalTest_Data listData= medicalTest_Data.get(position);

        holder.txt_title.setText(listData.gettest_name_eng());
     /*   Picasso.with(context).load(listData.getfile()).into(holder.img_test);*/

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
                Intent intent = new Intent(context, LabTestDetailsActivity.class);
                intent.putExtra("hosid",listData.getid());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return medicalTest_Data.size();
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
            filter=new CustomFilter((ArrayList<MedicalTest_Data>) medicalTest_Data,this);
        }

        return filter;
    }


}