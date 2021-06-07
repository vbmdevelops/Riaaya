package com.vbm.riaaya.adapter;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.squareup.picasso.Picasso;
import com.vbm.riaaya.AvailableTestActivity;
import com.vbm.riaaya.BookedTestDetailActivity;
import com.vbm.riaaya.R;
import com.vbm.riaaya.data.BookedTestData;
import com.vbm.riaaya.data.Hosptial_Data;

import java.util.List;

public class BookedTestAdapter extends RecyclerView.Adapter<BookedTestAdapter.ViewHolder> {
    private List<BookedTestData> bookedTestData;
    private Context context;
    public BookedTestAdapter(List<BookedTestData> bookedTestData, Context context) {
        this.bookedTestData = bookedTestData;
        this.context = context;
    }

    @Override
    public BookedTestAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.booktest_list,parent,false);
        return new BookedTestAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final BookedTestAdapter.ViewHolder holder, final int position) {
        final BookedTestData listData= bookedTestData.get(position);
        holder.txt_bookingno.setText(listData.getbooking_no());
        holder.patient_name.setText(listData.getp_name());
        holder.test_name.setText(listData.gettest_name());
        holder.txt_date.setText(listData.getbook_date());
       // holder.txt_status.setText(listData.getstatus());
        if(listData.getstatus().matches("P"))
        {
            holder.txt_st.setTextColor(context.getResources().getColor(R.color.orange));
            holder.txt_status.setTextColor(context.getResources().getColor(R.color.orange));
            holder.txt_status.setText(R.string.penmsg);
        }
        else
        {
            holder.txt_st.setTextColor(context.getResources().getColor(R.color.lightgreen));
            holder.txt_status.setTextColor(context.getResources().getColor(R.color.lightgreen));
            holder.txt_status.setText(R.string.susmsg);
        }

        holder.txt_viewmore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, BookedTestDetailActivity.class);
                intent.putExtra("booking",listData.getbooking_no());
                intent.putExtra("status",listData.getstatus());
                intent.putExtra("testimg",listData.getTest_img());
                intent.putExtra("testname",listData.gettest_name());
                intent.putExtra("hospital_name",listData.getHospital_name());
                intent.putExtra("datetime",listData.getDatetime());
                intent.putExtra("hospital_address_eng",listData.getHospital_address_eng());
                intent.putExtra("name",listData.getp_name());
                intent.putExtra("p_age",listData.getP_age());
                intent.putExtra("p_gender",listData.getP_gender());
                intent.putExtra("phone",listData.getPhone());
                intent.putExtra("email",listData.getEmail());
                intent.putExtra("address",listData.getAddress());
                context.startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return bookedTestData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private ImageView class_imageView;
        TextView txt_viewmore;
        TextView patient_name;
        TextView txt_bookingno;
        TextView test_name;
        TextView txt_date;
        TextView txt_status;
        TextView txt_st;
        public ViewHolder(View itemView) {
            super(itemView);
            class_imageView=(ImageView)itemView.findViewById(R.id.class_imageView);
            txt_bookingno = (TextView)itemView.findViewById(R.id.txt_bookingno);
            txt_viewmore = (TextView) itemView.findViewById(R.id.txt_viewmore);
            patient_name = (TextView)itemView.findViewById(R.id.patient_name);
            test_name = (TextView)itemView.findViewById(R.id.test_name);
            txt_date = (TextView)itemView.findViewById(R.id.txt_date);
            txt_status = (TextView)itemView.findViewById(R.id.txt_status);
            txt_st = (TextView)itemView.findViewById(R.id.txt_st);
        }
    }
}