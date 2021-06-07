package com.vbm.riaaya;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;
import com.vbm.riaaya.session.AppController;
import com.vbm.riaaya.session.AppUtils;
import com.vbm.riaaya.session.SPUtils;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;

public class LoginActivity extends Activity {

    EditText et_email,et_password;
    TextView txt_login,txt_forgotpass,txt_createaccount;
    Dialog dialog;
    String resvalue;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
        changeStatusBarColor();
        setContentView(R.layout.activity_login);

        et_email = (EditText)findViewById(R.id.et_email);
        et_password = (EditText)findViewById(R.id.et_password);
        txt_login = (TextView)findViewById(R.id.txt_login);

        txt_forgotpass = (TextView)findViewById(R.id.txt_forgotpass);
        txt_createaccount = (TextView)findViewById(R.id.txt_createaccount);


        txt_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (AppUtils.isNetworkAvailable(LoginActivity.this))
                {
                    if(et_email.getText().toString().matches("")){

                        Toasty.error(LoginActivity.this,getResources().getString(R.string.emailmsg), Toast.LENGTH_LONG).show();
                    }
                    else if(!isValidEmail(et_email.getText().toString()))
                    {
                        Toasty.error(LoginActivity.this,getResources().getString(R.string.emailmsg1), Toast.LENGTH_LONG).show();
                    }

                    else if(et_password.getText().toString().matches("")){

                        Toasty.error(LoginActivity.this,getResources().getString(R.string.passwordmsg),Toast.LENGTH_LONG).show();
                    }
                    else
                    {


                       /* Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                        startActivity(intent);*/
                        displayLoader();
                        //new Login().execute();
                        Login(et_email.getText().toString(),et_password.getText().toString());
                    }

                }
                else
                {
                    Toasty.warning(LoginActivity.this,getResources().getString(R.string.nointernet),Toast.LENGTH_LONG).show();

                }
            }
        });


        txt_createaccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);
            }
        });

    }

    private void displayLoader() {
        dialog = new Dialog(LoginActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.loading_alert);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.show();

    }

    private void Login(final String email,final String password) {
        String URL = "https://demo.innodasolutions.com/riaaya/API/riaaya_user/user.php?email="+email+"&password="+password;
        Log.e("URL",URL);
        StringRequest stringRequest=new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dialog.dismiss();
                try {
                    JSONObject jsonObject=new JSONObject(response);
                    Log.e("response",response.toString());

                    if (jsonObject.getString("status").equalsIgnoreCase("S")) {
                        Log.e("Data",jsonObject.getString("token"));
                        JSONObject jobj = jsonObject.getJSONObject("userdata");

                        AppController.getSpUserInfo().edit().putString(SPUtils.USER_ID, jobj.getString("id")).commit();
                        AppController.getSpUserInfo().edit().putString(SPUtils.USER_NAME,jobj.getString("name")).commit();
                        AppController.getSpUserInfo().edit().putString(SPUtils.USER_EmailId, jobj.getString("email")).commit();
                        AppController.getSpUserInfo().edit().putString(SPUtils.USER_Gender, jobj.getString("gender")).commit();
                        AppController.getSpUserInfo().edit().putString(SPUtils.USER_DOB, jobj.getString("dob")).commit();
                        AppController.getSpUserInfo().edit().putString(SPUtils.USER_MobileNo,jobj.getString("phone")).commit();
                        AppController.getSpUserInfo().edit().putString(SPUtils.USER_Address, jobj.getString("address")).commit();
                        AppController.getSpUserInfo().edit().putString(SPUtils.USER_TOKEN, jsonObject.getString("token")).commit();
                        AppController.getSpUserInfo().edit().putString(SPUtils.USER_ProfilePicUrl, jobj.getString("file")).commit();
                        AppController.getSpIsLogin().edit().putBoolean(SPUtils.IS_LOGIN, true).commit();

                        Intent intent1 = new Intent(LoginActivity.this, MainActivity.class);
                        intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent1);
                        finish();

                       /* AppController.getSpIsLogin().edit().putBoolean(SPUtils.IS_LOGIN, true).commit();
                        Intent intent1 = new Intent(LoginActivity.this, MainActivity.class);
                        intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent1);
                        finish();*/
                    }


                    else {
                        Toasty.warning(LoginActivity.this, jsonObject.getString("message") , Toast.LENGTH_SHORT, true).show();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                    dialog.dismiss();
                     Toasty.warning(LoginActivity.this,e.getMessage(),Toast.LENGTH_LONG).show();
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


    private void changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
    }

    private static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }


}
