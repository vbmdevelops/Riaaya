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

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.vbm.riaaya.adapter.AllHosptialListAdapter;
import com.vbm.riaaya.adapter.BookTestAdapter;
import com.vbm.riaaya.data.Hosptial_Data;
import com.vbm.riaaya.session.AppController;
import com.vbm.riaaya.session.AppUtils;
import com.vbm.riaaya.session.SPUtils;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;

public class BookTestList_Activity extends Activity {
    LinearLayout lin_login;
    Dialog myDialog,dialog;
    String testid,testname;

    private RecyclerView rec_hosptiallist;
    private List<Hosptial_Data> hosptial_Data;
    private BookTestAdapter hosptialAdapter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
        changeStatusBarColor();
        setContentView(R.layout.activity_booktest);

        Intent intent = getIntent();
        testid = intent.getStringExtra("testid");
        testname = intent.getStringExtra("testname");

        TextView txt_login;
        txt_login = (TextView)findViewById(R.id.txt_login);

        lin_login = (LinearLayout)findViewById(R.id.lin_login);

        rec_hosptiallist=(RecyclerView)findViewById(R.id.rec_hosptiallist);
        rec_hosptiallist.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true);
        layoutManager.setReverseLayout(false);
        rec_hosptiallist.setLayoutManager(new GridLayoutManager(this, 2));
        hosptial_Data=new ArrayList<>();
        hosptialAdapter=new BookTestAdapter(hosptial_Data,this);
        int resId1 = R.anim.test;
        LayoutAnimationController animation1 = AnimationUtils.loadLayoutAnimation(this, resId1);
        rec_hosptiallist.setLayoutAnimation(animation1);

        ImageView user_profile = (ImageView)findViewById(R.id.user_profile);
        try {

            Glide.with(BookTestList_Activity.this)
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
                    myDialog = new Dialog(BookTestList_Activity.this);
                    myDialog.setContentView(R.layout.custompopup);
                    Button Yes = (Button)myDialog.findViewById(R.id.Yes);
                    Button No = (Button)myDialog.findViewById(R.id.No);

                    Yes.setTransformationMethod(null);
                    Yes.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            AppController.getSpUserInfo().edit().clear().commit();
                            AppController.getSpIsLogin().edit().clear().commit();
                            Intent intent = new Intent(BookTestList_Activity.this, SplashScreen.class);
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
                    Intent intent = new Intent(BookTestList_Activity.this,LoginActivity.class);
                    startActivity(intent);
                }
            });

            FloatingActionButton fab_home = (FloatingActionButton)findViewById(R.id.fab_home);
            fab_home.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent1 = new Intent(BookTestList_Activity.this, MainActivity.class);
                    intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent1);
                    finish();
                }
            });
        }

        LinearLayout lin_profile = (LinearLayout)findViewById(R.id.lin_profile);
        lin_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (AppController.getSpIsLogin().getBoolean(SPUtils.IS_LOGIN, false)) {
                    Intent intent = new Intent(BookTestList_Activity.this,UserProfileActivity.class);
                    startActivity(intent);
                }
                else
                {
                    Intent intent = new Intent(BookTestList_Activity.this,LoginActivity.class);
                    startActivity(intent);
                }
            }
        });

        LinearLayout lin_booktest = (LinearLayout)findViewById(R.id.lin_booktest);
        lin_booktest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BookTestList_Activity.this,MedicalTest_Activity.class);
                startActivity(intent);
            }
        });

        if(AppUtils.isNetworkAvailable(BookTestList_Activity.this))
        {
            displayLoader();
            HosTestList();

        }
        else
        {
            Toasty.warning(BookTestList_Activity.this,getResources().getString(R.string.nointernet), Toast.LENGTH_LONG).show();
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


    private void displayLoader() {
        dialog = new Dialog(BookTestList_Activity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.loading_alert);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.show();

    }

    //  test

    private void HosTestList() {
        String URL = "https://demo.innodasolutions.com/riaaya/API/riaaya_user/testHospital.php?test_name_eng="+testname;
        Log.e("URL",URL);
        StringRequest stringRequest=new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dialog.dismiss();
                try {
                    JSONObject jsonObject=new JSONObject(response);
                    Log.e("response",response.toString());
                    hosptial_Data.clear();
                    if (jsonObject.getString("statusCode").equalsIgnoreCase("S")) {
                        JSONArray jarr = jsonObject.getJSONArray("result");
                        for (int i = 0; i < jarr.length(); i++) {
                            JSONObject ob = jarr.getJSONObject(i);

                            if(AppController.getSpUserInfo().getString(SPUtils.USER_LANG, "").matches("ar"))
                            {
                                Hosptial_Data ld = new Hosptial_Data(ob.getString("hname_arb"),ob.getString("hospital_img"),ob.getString("hospital_id"));
                                hosptial_Data.add(ld);
                            }
                            else
                            {
                                Hosptial_Data ld = new Hosptial_Data(ob.getString("hname_eng"),ob.getString("hospital_img"),ob.getString("hospital_id"));
                                hosptial_Data.add(ld);
                            }

                        }
                        rec_hosptiallist.setAdapter(hosptialAdapter);
                    }
                    else
                    {
                        Toasty.warning(BookTestList_Activity.this,getResources().getString(R.string.nodata), Toast.LENGTH_LONG).show();

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    dialog.dismiss();
                    Toasty.warning(BookTestList_Activity.this,e.getMessage(), Toast.LENGTH_LONG).show();
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


}
