package com.vbm.riaaya;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.vbm.riaaya.adapter.HosptialAdapter;
import com.vbm.riaaya.adapter.MedicalTestAdapter;
import com.vbm.riaaya.data.Hosptial_Data;
import com.vbm.riaaya.data.MedicalTest_Data;
import com.vbm.riaaya.session.AppController;
import com.vbm.riaaya.session.AppUtils;
import com.vbm.riaaya.session.SPUtils;
import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;

public class MainActivity extends Activity {
    TextView txt_login;
    LinearLayout lin_login;
    Dialog myDialog;

    private RecyclerView rec_medicaltest;
    private List<MedicalTest_Data> medical_data;
    private MedicalTestAdapter medicaltest_adapter;
    Dialog dialog;

    private RecyclerView rec_hosptiallist;
    private List<Hosptial_Data> hosptial_Data;
    private HosptialAdapter hosptialAdapter;

    ImageView user_profile,user_icon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
        changeStatusBarColor();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txt_login = (TextView)findViewById(R.id.txt_login);
        lin_login = (LinearLayout)findViewById(R.id.lin_login);
        user_profile = (ImageView)findViewById(R.id.user_profile);
      /*  user_icon = (ImageView)findViewById(R.id.user_icon);*/

        rec_medicaltest=(RecyclerView)findViewById(R.id.rec_medicaltest);
        rec_medicaltest.setHasFixedSize(true);
        LinearLayoutManager layoutManager1 = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, true);
        layoutManager1.setReverseLayout(false);
        rec_medicaltest.setLayoutManager(layoutManager1);
        medical_data=new ArrayList<>();
        medicaltest_adapter=new MedicalTestAdapter(medical_data,this);
        int resId1 = R.anim.test;
        LayoutAnimationController animation1 = AnimationUtils.loadLayoutAnimation(this, resId1);
        rec_medicaltest.setLayoutAnimation(animation1);


        rec_hosptiallist=(RecyclerView)findViewById(R.id.rec_hosptiallist);
        rec_hosptiallist.setHasFixedSize(true);
        LinearLayoutManager layoutManager11 = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, true);
        layoutManager11.setReverseLayout(false);
        rec_hosptiallist.setLayoutManager(layoutManager11);
        hosptial_Data=new ArrayList<>();
        hosptialAdapter=new HosptialAdapter(hosptial_Data,this);
        int resId11 = R.anim.test;
        LayoutAnimationController animation11 = AnimationUtils.loadLayoutAnimation(this, resId1);
        rec_hosptiallist.setLayoutAnimation(animation11);


        try {

            Glide.with(MainActivity.this)
                    .load(SPUtils.imageurl+AppController.getSpUserInfo().getString(SPUtils.USER_ProfilePicUrl,"").replace(" ","%20"))
                    .skipMemoryCache(false)
                    .error(R.drawable.ic_user)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(user_profile);

        } catch (Exception e) {
            e.printStackTrace();
        }

/*
        if(!AppController.getSpUserInfo().getString(SPUtils.USER_ProfilePicUrl,"").matches(""))
        {
            try {

                Log.e("profile",AppController.getSpUserInfo().getString(SPUtils.USER_ProfilePicUrl,""));
                Glide.with(MainActivity.this)
                        .load("https://demo.innodasolutions.com/riaaya/user_image/"+AppController.getSpUserInfo().getString(SPUtils.USER_ProfilePicUrl,"").replace(" ","%20"))
                        .skipMemoryCache(false)

                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(user_profile);

            *//*  user_icon.setVisibility(View.GONE);*//*
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else {

         *//*   user_icon.setVisibility(View.VISIBLE);*//*
            user_profile.setVisibility(View.GONE);
        }*/

        Log.e("profile",AppController.getSpUserInfo().getString(SPUtils.USER_ProfilePicUrl,""));
        if (AppController.getSpIsLogin().getBoolean(SPUtils.IS_LOGIN, false)) {

            txt_login.setText(R.string.logout);
            lin_login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    myDialog = new Dialog(MainActivity.this);
                    myDialog.setContentView(R.layout.custompopup);
                    Button Yes = (Button)myDialog.findViewById(R.id.Yes);
                    Button No = (Button)myDialog.findViewById(R.id.No);

                    Yes.setTransformationMethod(null);
                    Yes.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            AppController.getSpUserInfo().edit().clear().commit();
                            AppController.getSpIsLogin().edit().clear().commit();
                            Intent intent = new Intent(MainActivity.this, SplashScreen.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            finish();

                        }
                    });

                    No.setTransformationMethod(null);
                    No.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            myDialog.dismiss();

                        }
                    });
                    myDialog.setCancelable(false);
                    myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    myDialog.show();
                }
            });

        }
        else
        {
            txt_login.setText(R.string.login);
            lin_login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MainActivity.this,LoginActivity.class);
                    /* intent.putExtra("lcome","M");*/
                    startActivity(intent);
                }
            });
        }

        TextView txt_medicaltest = (TextView)findViewById(R.id.txt_medicaltest);
        txt_medicaltest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,MedicalTest_Activity.class);
                startActivity(intent);
            }
        });

        TextView txt_allhosptial = (TextView)findViewById(R.id.txt_allhosptial);
        txt_allhosptial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AllHosptialListActivity.class);
                startActivity(intent);
            }
        });

        FloatingActionButton fab_home = (FloatingActionButton)findViewById(R.id.fab_home);
        fab_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = getIntent();
                finish();
                startActivity(intent);
            }
        });

        LinearLayout lin_profile = (LinearLayout)findViewById(R.id.lin_profile);
        lin_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (AppController.getSpIsLogin().getBoolean(SPUtils.IS_LOGIN, false)) {
                    Intent intent = new Intent(MainActivity.this,UserProfileActivity.class);
                    startActivity(intent);
                }
                else
                {
                    Intent intent = new Intent(MainActivity.this,LoginActivity.class);
                    startActivity(intent);
                }
            }
        });

        LinearLayout lin_booktest = (LinearLayout)findViewById(R.id.lin_booktest);
        lin_booktest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,MedicalTest_Activity.class);
                startActivity(intent);
            }
        });

        if(AppUtils.isNetworkAvailable(MainActivity.this))
        {
            displayLoader();

            HosptialList();
            MedicalTestList();

        }
        else
        {
            Toasty.warning(MainActivity.this,getResources().getString(R.string.nointernet), Toast.LENGTH_LONG).show();
        }
    }

    private void changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
    }

    private void MedicalTestList() {
        try {
            new AsyncTask<Void, Void, String>() {
                protected void onPreExecute() {
                    dialog.show();
                }
                @Override
                protected String doInBackground(Void... params) {
                    String response = "";
                    try {
                        List<NameValuePair> postParameters = new ArrayList<>();
                       /* postParameters.add(new BasicNameValuePair("SearchByDoctorName_Speciality",""));
                        postParameters.add(new BasicNameValuePair("SearchByHospitalCode",hosid));*/
                        response = AppUtils.callWebServiceWithMultiParam(MainActivity.this,postParameters, "testList.php", "TAG");


                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return response;
                }

                @Override
                protected void onPostExecute(String resultData) {
                    try {
                        dialog.dismiss();
                        JSONObject jsonObject = new JSONObject(resultData);
                        Log.e("resultData", resultData);
                        medical_data.clear();
                        if (jsonObject.getString("statusCode").equalsIgnoreCase("S")) {
                            JSONArray jarr = jsonObject.getJSONArray("result");
                            for (int i = 0; i < jarr.length(); i++) {
                                JSONObject ob = jarr.getJSONObject(i);

                                if(AppController.getSpUserInfo().getString(SPUtils.USER_LANG, "").matches("ar"))
                                {
                                    MedicalTest_Data ld = new MedicalTest_Data(ob.getString("test_name_arb"),ob.getString("file"),ob.getString("id"));
                                    medical_data.add(ld);

                                }
                                else
                                {
                                    MedicalTest_Data ld = new MedicalTest_Data(ob.getString("test_name_eng"),ob.getString("file"),ob.getString("id"));
                                    medical_data.add(ld);
                                }

                            }
                            rec_medicaltest.setAdapter(medicaltest_adapter);
                        }
                        else {
                            Toasty.warning(MainActivity.this,jsonObject.getString("statusCode"), Toast.LENGTH_LONG).show();
                            /*lin_1.setVisibility(View.GONE);
                            lin_nointernet.setVisibility(View.GONE);
                            lin_nodata.setVisibility(View.VISIBLE);
                            lin_servererror.setVisibility(View.GONE);*/
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                       /* lin_1.setVisibility(View.GONE);
                        lin_nointernet.setVisibility(View.GONE);
                        lin_nodata.setVisibility(View.GONE);
                        lin_servererror.setVisibility(View.VISIBLE);*/
                        Toasty.warning(MainActivity.this,e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    // hosptial list
    private void HosptialList() {
        try {
            new AsyncTask<Void, Void, String>() {
                protected void onPreExecute() {
                    dialog.show();
                }
                @Override
                protected String doInBackground(Void... params) {
                    String response = "";
                    try {
                        List<NameValuePair> postParameters = new ArrayList<>();
                        response = AppUtils.callWebServiceWithMultiParam(MainActivity.this,postParameters, "userhospital.php", "TAG");


                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return response;
                }

                @Override
                protected void onPostExecute(String resultData) {
                    try {
                        dialog.dismiss();
                        JSONObject jsonObject = new JSONObject(resultData);
                        Log.e("resultData", resultData);
                        hosptial_Data.clear();
                        if (jsonObject.getString("statusCode").equalsIgnoreCase("S")) {
                            JSONArray jarr = jsonObject.getJSONArray("result");
                            for (int i = 0; i < jarr.length(); i++) {
                                JSONObject ob = jarr.getJSONObject(i);

                                if(AppController.getSpUserInfo().getString(SPUtils.USER_LANG, "").matches("ar"))
                                {
                                    Hosptial_Data ld = new Hosptial_Data(ob.getString("hname_arb"),ob.getString("file"),ob.getString("id"));
                                    hosptial_Data.add(ld);

                                }
                                else
                                {
                                    Hosptial_Data ld = new Hosptial_Data(ob.getString("hname_eng"),ob.getString("file"),ob.getString("id"));
                                    hosptial_Data.add(ld);
                                }

                            }
                            rec_hosptiallist.setAdapter(hosptialAdapter);
                        }
                        else {
                            /*lin_1.setVisibility(View.GONE);
                            lin_nointernet.setVisibility(View.GONE);
                            lin_nodata.setVisibility(View.VISIBLE);
                            lin_servererror.setVisibility(View.GONE);*/
                            Toasty.warning(MainActivity.this,jsonObject.getString("statusCode"), Toast.LENGTH_LONG).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                       /* lin_1.setVisibility(View.GONE);
                        lin_nointernet.setVisibility(View.GONE);
                        lin_nodata.setVisibility(View.GONE);
                        lin_servererror.setVisibility(View.VISIBLE);*/
                        Toasty.warning(MainActivity.this,e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

        } catch (Exception e) {
            e.printStackTrace();

        }
    }


    private void displayLoader() {
        dialog = new Dialog(MainActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.loading_alert);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.show();

    }
}