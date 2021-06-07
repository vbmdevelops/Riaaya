package com.vbm.riaaya.adapter;

import android.widget.Filter;

import com.vbm.riaaya.data.Hosptial_Data;

import java.util.ArrayList;

public class HosptialFilter1 extends Filter {
    AllHosptialListAdapter TestHosptialAdapter;
    ArrayList<Hosptial_Data> jobData;


    public HosptialFilter1(ArrayList<Hosptial_Data> filterList, AllHosptialListAdapter adapter)
    {
        this.TestHosptialAdapter=adapter;
        this.jobData=filterList;

    }

    //FILTERING OCURS
    @Override
    protected FilterResults performFiltering(CharSequence constraint) {
        FilterResults results=new FilterResults();

        //CHECK CONSTRAINT VALIDITY
        if(constraint != null && constraint.length() > 0)
        {
            //CHANGE TO UPPER
            constraint=constraint.toString().toUpperCase();
            //STORE OUR FILTERED PLAYERS
            ArrayList<Hosptial_Data> filteredPlayers=new ArrayList<>();

            for (int i=0;i<jobData.size();i++)
            {
                //CHECK
                if(jobData.get(i).gethos_name().toUpperCase().contains(constraint))
                {
                    filteredPlayers.add(jobData.get(i));
                }



            }

            results.count=filteredPlayers.size();
            results.values=filteredPlayers;
        }else
        {
            results.count=jobData.size();
            results.values=jobData;

        }


        return results;
    }

    @Override
    protected void publishResults(CharSequence constraint, FilterResults results) {

        TestHosptialAdapter.hosptial_Data= (ArrayList<Hosptial_Data>) results.values;

        //REFRESH
        TestHosptialAdapter.notifyDataSetChanged();
    }
}
