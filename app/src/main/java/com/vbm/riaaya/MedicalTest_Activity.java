package com.vbm.riaaya;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.vbm.riaaya.adapter.AllMedicalTestAdapter;
import com.vbm.riaaya.adapter.MedicalTestAdapter;
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

public class MedicalTest_Activity extends Activity {
    LinearLayout lin_login;
    Dialog myDialog,dialog;
    private RecyclerView rec_medicaltest;
    private List<MedicalTest_Data> medical_data;
    private AllMedicalTestAdapter medicaltest_adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
        changeStatusBarColor();
        setContentView(R.layout.activity_medicaltest);

        rec_medicaltest=(RecyclerView)findViewById(R.id.rec_medicaltest);
        rec_medicaltest.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true);
        layoutManager.setReverseLayout(false);
        rec_medicaltest.setLayoutManager(new GridLayoutManager(this, 2));
        medical_data=new ArrayList<>();
        medicaltest_adapter=new AllMedicalTestAdapter(medical_data,this);
        int resId1 = R.anim.test;
        LayoutAnimationController animation1 = AnimationUtils.loadLayoutAnimation(this, resId1);
        rec_medicaltest.setLayoutAnimation(animation1);



    /*    TextView txt_viewmore = (TextView)findViewById(R.id.txt_viewmore);
        txt_viewmore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MedicalTest_Activity.this,BookTestList_Activity.class);
                startActivity(intent);
            }
        });
*/
        TextView txt_login;
        txt_login = (TextView)findViewById(R.id.txt_login);

        lin_login = (LinearLayout)findViewById(R.id.lin_login);

        ImageView user_profile = (ImageView)findViewById(R.id.user_profile);
        try {

            Glide.with(MedicalTest_Activity.this)
                    .load(SPUtils.imageurl+AppController.getSpUserInfo().getString(SPUtils.USER_ProfilePicUrl,"").replace(" ","%20"))
                    .skipMemoryCache(false)
                    .error(R.drawable.ic_user)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(user_profile);

        } catch (Exception e) {
            e.printStackTrace();
        }

        if (AppController.getSpIsLogin().getBoolean(SPUtils.IS_LOGIN, false)) {
            txt_login.setText(R.string.logout);
            lin_login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    myDialog = new Dialog(MedicalTest_Activity.this);
                    myDialog.setContentView(R.layout.custompopup);
                    Button Yes = (Button)myDialog.findViewById(R.id.Yes);
                    Button No = (Button)myDialog.findViewById(R.id.No);

                    Yes.setTransformationMethod(null);
                    Yes.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            AppController.getSpUserInfo().edit().clear().commit();
                            AppController.getSpIsLogin().edit().clear().commit();
                            Intent intent = new Intent(MedicalTest_Activity.this, SplashScreen.class);
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
                    Intent intent = new Intent(MedicalTest_Activity.this,LoginActivity.class);
                    startActivity(intent);
                }
            });
        }

        FloatingActionButton fab_home = (FloatingActionButton)findViewById(R.id.fab_home);
        fab_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(MedicalTest_Activity.this, MainActivity.class);
                intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent1);
                finish();
            }
        });

        LinearLayout lin_profile = (LinearLayout)findViewById(R.id.lin_profile);
        lin_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (AppController.getSpIsLogin().getBoolean(SPUtils.IS_LOGIN, false)) {
                    Intent intent = new Intent(MedicalTest_Activity.this,UserProfileActivity.class);
                    startActivity(intent);
                }
                else
                {
                    Intent intent = new Intent(MedicalTest_Activity.this,LoginActivity.class);
                    startActivity(intent);
                }
            }
        });
        LinearLayout lin_booktest = (LinearLayout)findViewById(R.id.lin_booktest);
        lin_booktest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MedicalTest_Activity.this,MedicalTest_Activity.class);
                startActivity(intent);
            }
        });

        EditText edtxt_search = (EditText)findViewById(R.id.edtxt_search);

        edtxt_search.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(medicaltest_adapter==null)
                {
                    //dataAdapter.getFilter().filter(s.toString());
                    Log.e("Call","null");
                }
                else {
                    medicaltest_adapter.getFilter().filter(s.toString());
                    Log.e("Call","Not null"+medicaltest_adapter.toString());
                }

            }
        });


        if(AppUtils.isNetworkAvailable(MedicalTest_Activity.this))
        {
            displayLoader();
            MedicalTestList();

        }
        else
        {
            Toasty.warning(MedicalTest_Activity.this,getResources().getString(R.string.nointernet), Toast.LENGTH_LONG).show();
        }

        LinearLayout lin_back= (LinearLayout)findViewById(R.id.lin_back);
        lin_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
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
                        response = AppUtils.callWebServiceWithMultiParam(MedicalTest_Activity.this,postParameters, "testList.php", "TAG");


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
                            Toasty.warning(MedicalTest_Activity.this,getResources().getString(R.string.nodata), Toast.LENGTH_LONG).show();
                            /*lin_1.setVisibility(View.GONE);
                            lin_nointernet.setVisibility(View.GONE);
                            lin_nodata.setVisibility(View.VISIBLE);
                            lin_servererror.setVisibility(View.GONE);*/
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toasty.warning(MedicalTest_Activity.this,e.getMessage(), Toast.LENGTH_LONG).show();
                       /* lin_1.setVisibility(View.GONE);
                        lin_nointernet.setVisibility(View.GONE);
                        lin_nodata.setVisibility(View.GONE);
                        lin_servererror.setVisibility(View.VISIBLE);*/
                    }
                }
            }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    private void displayLoader() {
        dialog = new Dialog(MedicalTest_Activity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.loading_alert);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.show();

    }
}
