package com.vbm.riaaya;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.vbm.riaaya.data.TestHistoryData;
import com.vbm.riaaya.session.AppController;
import com.vbm.riaaya.session.AppUtils;
import com.vbm.riaaya.session.SPUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import es.dmoral.toasty.Toasty;

public class TestHistoryDetails extends Activity {
    Dialog dialog;
    TextView txt_name,txt_gender,txt_age,txt_address,txt_hos_name,txt_rgd,txt_rgt,txt_testname,txt_result,txt_units,txt_bri;
    String getid;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
        changeStatusBarColor();
        setContentView(R.layout.testhistorydetails);
        txt_name = (TextView)findViewById(R.id.txt_name);
        txt_gender = (TextView)findViewById(R.id.txt_gender);
        txt_age = (TextView)findViewById(R.id.txt_age);
        txt_address = (TextView)findViewById(R.id.txt_address);
        txt_hos_name = (TextView)findViewById(R.id.txt_hos_name);
        txt_rgd = (TextView)findViewById(R.id.txt_rgd);
        txt_rgt = (TextView)findViewById(R.id.txt_rgt);
        txt_testname = (TextView)findViewById(R.id.txt_testname);
        txt_result = (TextView)findViewById(R.id.txt_result);
        txt_units = (TextView)findViewById(R.id.txt_units);
        txt_bri = (TextView)findViewById(R.id.txt_bri);

        Intent intent = getIntent();
        getid = intent.getStringExtra("getid");

        ImageView user_profile = (ImageView)findViewById(R.id.user_profile);
        try {

            Glide.with(TestHistoryDetails.this)
                    .load(SPUtils.imageurl+AppController.getSpUserInfo().getString(SPUtils.USER_ProfilePicUrl,"").replace(" ","%20"))
                    .skipMemoryCache(false)
                    .error(R.drawable.ic_user)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(user_profile);

        } catch (Exception e) {
            e.printStackTrace();
        }

        FloatingActionButton fab_home = (FloatingActionButton)findViewById(R.id.fab_home);
        fab_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(TestHistoryDetails.this, MainActivity.class);
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
                    Intent intent = new Intent(TestHistoryDetails.this,UserProfileActivity.class);
                    startActivity(intent);
                }
                else
                {
                    Intent intent = new Intent(TestHistoryDetails.this,LoginActivity.class);
                    startActivity(intent);
                }
            }
        });

        LinearLayout lin_booktest = (LinearLayout)findViewById(R.id.lin_booktest);
        lin_booktest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TestHistoryDetails.this,MedicalTest_Activity.class);
                startActivity(intent);
            }
        });

        LinearLayout lin_back = (LinearLayout)findViewById(R.id.lin_back);
        lin_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        if(AppUtils.isNetworkAvailable(TestHistoryDetails.this))
        {
            displayLoader();
            TestDetailsList();

        }
        else
        {
            Toasty.warning(TestHistoryDetails.this,getResources().getString(R.string.nointernet), Toast.LENGTH_LONG).show();

        }


    }

    private void TestDetailsList() {
        String URL = "https://demo.innodasolutions.com/riaaya/API/riaaya_user/report.php?token="+AppController.getSpUserInfo().getString(SPUtils.USER_TOKEN,"")+"&bid="+getid;
        Log.e("URL",URL);
        StringRequest stringRequest=new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dialog.dismiss();
                try {
                    JSONObject jsonObject=new JSONObject(response);
                    Log.e("response",response.toString());

                    if (jsonObject.getString("statusCode").equalsIgnoreCase("S")) {
                        JSONArray jarr = jsonObject.getJSONArray("result");
                        JSONObject ob = jarr.getJSONObject(0);
                        txt_name.setText(ob.getString("p_name"));
                        txt_gender.setText(ob.getString("p_gender"));
                        txt_age.setText(ob.getString("p_age"));
                        txt_address.setText(ob.getString("address"));
                        txt_hos_name.setText(ob.getString("hospital_name"));
                        //hospital_arb
                        txt_rgd.setText(ob.getString("report_genarate_date"));
                        txt_rgt.setText(ob.getString("report_genarate_time"));
                        txt_testname.setText(ob.getString("test_name"));
                        // test_arb
                        txt_result.setText(ob.getString("result"));
                        txt_units.setText(ob.getString("unit"));
                        txt_bri.setText(ob.getString("ref_range"));
                    }
                    else
                    {

                        Toasty.warning(TestHistoryDetails.this,getResources().getString(R.string.nodata), Toast.LENGTH_LONG).show();

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    dialog.dismiss();
                    Toasty.warning(TestHistoryDetails.this,e.getMessage(), Toast.LENGTH_LONG).show();

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();
            }
        });
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
        dialog.dismiss();
    }


    private void displayLoader() {
        dialog = new Dialog(TestHistoryDetails.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.loading_alert);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.show();

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
}
