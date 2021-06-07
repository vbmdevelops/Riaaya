package com.vbm.riaaya.adapter;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.vbm.riaaya.R;
import com.vbm.riaaya.TestHistoryDetails;
import com.vbm.riaaya.data.TestHistoryData;
import java.util.List;

public class TestHistoryAdapter extends RecyclerView.Adapter<TestHistoryAdapter.ViewHolder> {
    private List<TestHistoryData> testHistoryData;
    private Context context;
    public TestHistoryAdapter(List<TestHistoryData> testHistoryData, Context context) {
        this.testHistoryData = testHistoryData;
        this.context = context;
    }

    @Override
    public TestHistoryAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.booktest_list,parent,false);
        return new TestHistoryAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final TestHistoryAdapter.ViewHolder holder, final int position) {
        final TestHistoryData listData= testHistoryData.get(position);
        holder.txt_bookingno.setText(listData.getbooking_no());
        holder.patient_name.setText(listData.getp_name());
        holder.test_name.setText(listData.gettest_name());
        holder.txt_date.setText(listData.getbook_date());
        // holder.txt_status.setText(listData.getstatus());
        if(listData.getstatus().matches("P"))
        {
            holder.txt_st.setTextColor(context.getResources().getColor(R.color.radlight));
            holder.txt_status.setTextColor(context.getResources().getColor(R.color.radlight));
            holder.txt_status.setText(R.string.afr);
        }
        else
        {
            holder.txt_st.setTextColor(context.getResources().getColor(R.color.lightgreen));
            holder.txt_status.setTextColor(context.getResources().getColor(R.color.lightgreen));
            holder.txt_status.setText(R.string.rg);
        }

        holder.txt_viewmore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, TestHistoryDetails.class);
                intent.putExtra("getid",listData.getid());
                context.startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return testHistoryData.size();
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