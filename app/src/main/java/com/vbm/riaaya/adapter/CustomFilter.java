package com.vbm.riaaya.adapter;
import android.widget.Filter;
import com.vbm.riaaya.data.MedicalTest_Data;
import java.util.ArrayList;

public class CustomFilter extends Filter {
    AllMedicalTestAdapter AllMedicalTestAdapter;
    ArrayList<MedicalTest_Data> medicalTest_Data;


    public CustomFilter(ArrayList<MedicalTest_Data> filterList, AllMedicalTestAdapter adapter)
    {
        this.AllMedicalTestAdapter=adapter;
        this.medicalTest_Data=filterList;

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
            ArrayList<MedicalTest_Data> filteredPlayers=new ArrayList<>();

            for (int i=0;i<medicalTest_Data.size();i++)
            {
                //CHECK
                if(medicalTest_Data.get(i).gettest_name_eng().toUpperCase().contains(constraint))
                {
                    filteredPlayers.add(medicalTest_Data.get(i));
                }


            }

            results.count=filteredPlayers.size();
            results.values=filteredPlayers;
        }else
        {
            results.count=medicalTest_Data.size();
            results.values=medicalTest_Data;

        }


        return results;
    }

    @Override
    protected void publishResults(CharSequence constraint, FilterResults results) {

        AllMedicalTestAdapter.medicalTest_Data= (ArrayList<MedicalTest_Data>) results.values;

        //REFRESH
        AllMedicalTestAdapter.notifyDataSetChanged();
    }
}
