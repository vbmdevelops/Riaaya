package com.vbm.riaaya;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
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
import com.squareup.picasso.Picasso;
import com.vbm.riaaya.data.Hosptial_Data;
import com.vbm.riaaya.session.AppController;
import com.vbm.riaaya.session.AppUtils;
import com.vbm.riaaya.session.SPUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import es.dmoral.toasty.Toasty;

public class LabTestDetailsActivity extends Activity {
    LinearLayout lin_login;
    Dialog myDialog,dialog;
    String hosid,price;
    ImageView test_image;
    TextView test_name,test_decs,txt_legel_notice,test_price;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
        changeStatusBarColor();
        setContentView(R.layout.activity_labtestdetails);

        Intent intent = getIntent();
        hosid = intent.getStringExtra("hosid");
        TextView txt_book_now = (TextView)findViewById(R.id.txt_book_now);
        txt_book_now.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(price.matches(""))
                {
                    Toasty.warning(LabTestDetailsActivity.this,"No data Available", Toast.LENGTH_LONG).show();
                }
                else
                {

                    if (AppController.getSpIsLogin().getBoolean(SPUtils.IS_LOGIN, false)) {
                        Intent intent = new Intent(LabTestDetailsActivity.this, BookMyTestActivity.class);
                        intent.putExtra("price",price);
                        intent.putExtra("testid",hosid);
                        startActivity(intent);
                    }
                    else
                    {
                        Intent intent = new Intent(LabTestDetailsActivity.this,LoginActivity.class);
                        startActivity(intent);
                    }
                }

            }
        });

        TextView txt_login;
        txt_login = (TextView)findViewById(R.id.txt_login);

        lin_login = (LinearLayout)findViewById(R.id.lin_login);
        test_image = (ImageView)findViewById(R.id.test_image);
        test_name = (TextView)findViewById(R.id.test_name);
        test_decs = (TextView)findViewById(R.id.test_decs);
        txt_legel_notice = (TextView)findViewById(R.id.txt_legel_notice);
        test_price = (TextView)findViewById(R.id.test_price);


        ImageView user_profile = (ImageView)findViewById(R.id.user_profile);
        try {

            Glide.with(LabTestDetailsActivity.this)
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
                    myDialog = new Dialog(LabTestDetailsActivity.this);
                    myDialog.setContentView(R.layout.custompopup);
                    Button Yes = (Button)myDialog.findViewById(R.id.Yes);
                    Button No = (Button)myDialog.findViewById(R.id.No);

                    Yes.setTransformationMethod(null);
                    Yes.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            AppController.getSpUserInfo().edit().clear().commit();
                            AppController.getSpIsLogin().edit().clear().commit();
                            Intent intent = new Intent(LabTestDetailsActivity.this, SplashScreen.class);
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
                    Intent intent = new Intent(LabTestDetailsActivity.this,LoginActivity.class);
                    startActivity(intent);
                }
            });
        }

        FloatingActionButton fab_home = (FloatingActionButton)findViewById(R.id.fab_home);
        fab_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(LabTestDetailsActivity.this, MainActivity.class);
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
                    Intent intent = new Intent(LabTestDetailsActivity.this,UserProfileActivity.class);
                    startActivity(intent);
                }
                else
                {
                    Intent intent = new Intent(LabTestDetailsActivity.this,LoginActivity.class);
                    startActivity(intent);
                }
            }
        });

        LinearLayout lin_booktest = (LinearLayout)findViewById(R.id.lin_booktest);
        lin_booktest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LabTestDetailsActivity.this,MedicalTest_Activity.class);
                startActivity(intent);
            }
        });

        if(AppUtils.isNetworkAvailable(LabTestDetailsActivity.this))
        {
            displayLoader();
            LabTestDetails();
        }
        else
        {
            Toasty.warning(LabTestDetailsActivity.this,getResources().getString(R.string.nointernet), Toast.LENGTH_LONG).show();
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
        dialog = new Dialog(LabTestDetailsActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.loading_alert);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.show();

    }

    //  test

    private void LabTestDetails() {
        String URL = "https://demo.innodasolutions.com/riaaya/API/riaaya_user/userLabDetails.php?id="+hosid;
        Log.e("URL",URL);
        StringRequest stringRequest=new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dialog.dismiss();
                try {
                    JSONObject jsonObject=new JSONObject(response);
                    Log.e("response",response.toString());

                    if (jsonObject.getString("statusCode").equalsIgnoreCase("S")) {


                        String test_name_eng = jsonObject.getString("test_name_eng");
                        String test_name_arb = jsonObject.getString("test_name_arb");
                        String desc_eng = jsonObject.getString("desc_eng");
                        String desc_arb = jsonObject.getString("desc_arb");
                        String legal_notice_eng = jsonObject.getString("legal_notice_eng");
                        String legal_notice_arb = jsonObject.getString("legal_notice_arb");

                        price = jsonObject.getString("price");
                        String hospital_id = jsonObject.getString("hospital_id");
                        String file = jsonObject.getString("file");
                        String hname_eng = jsonObject.getString("hname_eng");
                        String hname_arb = jsonObject.getString("hname_arb");

                      /*  Picasso.with(LabTestDetailsActivity.this).load(file).into(test_image);*/
                        try {

                            Glide.with(LabTestDetailsActivity.this)
                                    .load(file.replace(" ","%20"))
                                    .skipMemoryCache(false)
                                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                                    .into(test_image);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        if(AppController.getSpUserInfo().getString(SPUtils.USER_LANG, "").matches("ar"))
                        {
                            test_name.setText(test_name_arb);
                            test_decs.setText(desc_arb);
                            txt_legel_notice.setText(legal_notice_arb);
                            test_price.setText(getResources().getString(R.string.txtprice)+""+price);
                        }
                        else
                        {
                            test_name.setText(test_name_eng);
                            test_decs.setText(desc_eng);
                            txt_legel_notice.setText(legal_notice_eng);
                            test_price.setText(getResources().getString(R.string.txtprice)+" "+price);
                        }

                    }
                    else
                    {
                        Toasty.warning(LabTestDetailsActivity.this,getResources().getString(R.string.nodata), Toast.LENGTH_LONG).show();

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    dialog.dismiss();
                    Toasty.warning(LabTestDetailsActivity.this,e.getMessage(), Toast.LENGTH_LONG).show();
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
