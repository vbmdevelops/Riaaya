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

public class RegisterActivity extends Activity {
    EditText et_reg_name,et_reg_email,et_reg_cno,et_reg_dob,et_reg_password,et_reg_conpassword;
    TextView txt_signup,txt_login;
    String confirmpass,resvalue;
    Dialog dialog;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
        changeStatusBarColor();
        setContentView(R.layout.activity_register);

        et_reg_name = (EditText)findViewById(R.id.et_reg_name);
        et_reg_email = (EditText)findViewById(R.id.et_reg_email);
        et_reg_cno = (EditText)findViewById(R.id.et_reg_cno);
        et_reg_dob = (EditText)findViewById(R.id.et_reg_dob);
        et_reg_password = (EditText)findViewById(R.id.et_reg_password);
        et_reg_conpassword = (EditText)findViewById(R.id.et_reg_conpassword);

        txt_signup = (TextView)findViewById(R.id.txt_signup);
        txt_login = (TextView)findViewById(R.id.txt_login);
        txt_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this,LoginActivity.class);
                startActivity(intent);
            }
        });

        txt_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                confirmpass = et_reg_conpassword.getText().toString();

                if(et_reg_name.getText().toString().matches(""))
                {
                    Toasty.error(RegisterActivity.this,getResources().getString(R.string.namemsg), Toast.LENGTH_LONG).show();
                }
                else if(et_reg_name.getText().toString().matches(" "))
                {
                    Toasty.error(RegisterActivity.this,getResources().getString(R.string.namemsg1), Toast.LENGTH_LONG).show();
                }
                else if(et_reg_email.getText().toString().matches("")){

                    Toasty.error(RegisterActivity.this,getResources().getString(R.string.emailmsg), Toast.LENGTH_LONG).show();
                }
                else if(!isValidEmail(et_reg_email.getText().toString()))
                {
                    Toasty.error(RegisterActivity.this,getResources().getString(R.string.emailmsg1), Toast.LENGTH_LONG).show();
                }
                else if(et_reg_cno.getText().toString().matches(""))
                {
                    Toasty.error(RegisterActivity.this,getResources().getString(R.string.cnomsg), Toast.LENGTH_LONG).show();
                }
                else if(et_reg_cno.getText().toString().matches(" "))
                {
                    Toasty.error(RegisterActivity.this,getResources().getString(R.string.cnomsg1), Toast.LENGTH_LONG).show();
                }
                else if(et_reg_dob.getText().toString().matches(""))
                {
                    Toasty.error(RegisterActivity.this,getResources().getString(R.string.dobmsg), Toast.LENGTH_LONG).show();
                }
                else if(et_reg_dob.getText().toString().matches(" "))
                {
                    Toasty.error(RegisterActivity.this,getResources().getString(R.string.dobmsg1), Toast.LENGTH_LONG).show();
                }

                else if(et_reg_password.getText().toString().matches(""))
                {
                    Toasty.error(RegisterActivity.this,getResources().getString(R.string.password), Toast.LENGTH_LONG).show();
                }
                else if(et_reg_conpassword.getText().toString().matches(" "))
                {
                    Toasty.error(RegisterActivity.this,getResources().getString(R.string.cpassmsg), Toast.LENGTH_LONG).show();
                }

                else if(!confirmpass.matches(et_reg_password.getText().toString()))
                {
                    Toasty.error(RegisterActivity.this,getResources().getString(R.string.cpassmsg1), Toast.LENGTH_LONG).show();
                }
                else
                {

                    if(AppUtils.isNetworkAvailable(RegisterActivity.this))
                    {
                        /*Intent intent = new Intent(RegisterActivity.this,MainActivity.class);
                        startActivity(intent);*/
                        displayLoader();
                        //new Register().execute();
                        Register();

                    }
                    else
                    {
                        Toasty.warning(RegisterActivity.this,getResources().getString(R.string.nointernet),Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

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

    private void displayLoader() {
        dialog = new Dialog(RegisterActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.loading_alert);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.show();

    }


    private void Register() {
        String URL = "https://demo.innodasolutions.com/riaaya/API/riaaya_user/testregister.php?case=register&name="+et_reg_name.getText().toString()+"&email="+et_reg_email.getText().toString()
                +"&password="+et_reg_conpassword.getText().toString()+"&phone="+et_reg_cno.getText().toString()+"&dob="+et_reg_dob.getText().toString();
        Log.e("URL",URL);
        StringRequest stringRequest=new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dialog.dismiss();
                try {
                    JSONObject jsonObject=new JSONObject(response);
                    Log.e("response",response.toString());

                    if (jsonObject.getString("status").equalsIgnoreCase("S")) {

                        Intent intent1 = new Intent(RegisterActivity.this, LoginActivity.class);
                        intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent1);
                        finish();
                    }

                    else {
                        Toasty.warning(RegisterActivity.this, jsonObject.getString("message") , Toast.LENGTH_SHORT, true).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    dialog.dismiss();
                    Toasty.warning(RegisterActivity.this,e.getMessage(),Toast.LENGTH_LONG).show();
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

   /* public class Register extends AsyncTask<String, Void, Object>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Object result) {
            dialog.dismiss();
            if (!TextUtils.isEmpty(resvalue))
            {
                Log.d("result ",resvalue);
                try{

                    JSONObject jsonObject = new JSONObject(resvalue);
                    if (jsonObject.getString("status").equalsIgnoreCase("S")) {

                        Intent intent1 = new Intent(RegisterActivity.this, LoginActivity.class);
                        intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent1);
                        finish();
                    }

                    else {
                        Toasty.warning(RegisterActivity.this, jsonObject.getString("message") , Toast.LENGTH_SHORT, true).show();
                    }
                }catch (Exception ex)
                {
                    ex.printStackTrace();
                }
            }



            super.onPostExecute(result);
        }

        @Override
        protected Object doInBackground(String... params) {
            uploadFile();
            return null;
        }
    }

    public void uploadFile() {
        String charset = "UTF-8";
        String requestURL = null;
        try {

            requestURL = "https://demo.innodasolutions.com/riaaya/API/riaaya_user/user.php?case=register&name="+et_reg_name.getText().toString()+"&email="+et_reg_email.getText().toString()
                    +"&password="+et_reg_conpassword.getText().toString()+"&phone="+et_reg_cno.getText().toString()+"&dob="+et_reg_dob.getText().toString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            FileUploader multipart = new FileUploader(requestURL, charset);
            List<String> response = multipart.finish();
            for (String line : response) {
                System.out.println(line);
                resvalue = line;
            }
            System.out.println("deewa line" + resvalue);

        } catch (IOException ex) {
            System.err.println(ex);
        }
    }*/

   /* private void Register() {
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
                        postParameters.add(new BasicNameValuePair("case","register"));
                        postParameters.add(new BasicNameValuePair("name",et_reg_name.getText().toString()));
                        postParameters.add(new BasicNameValuePair("email",et_reg_email.getText().toString()));
                        postParameters.add(new BasicNameValuePair("password",et_reg_conpassword.getText().toString()));
                        postParameters.add(new BasicNameValuePair("phone",et_reg_cno.getText().toString()));
                        postParameters.add(new BasicNameValuePair("dob",et_reg_dob.getText().toString()));


                        response = AppUtils.callWebServiceWithMultiParam(RegisterActivity.this, postParameters, "user.php", "TAG");

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
                        Log.e("resultData",resultData);
                        if (jsonObject.getString("status").equalsIgnoreCase("S")) {
                            JSONArray jarr = jsonObject.getJSONArray("data");
                            JSONObject jobj = jarr.getJSONObject(0);
*//*
                            AppController.getSpUserInfo().edit().putString(SPUtils.USER_ID, jobj.getString("id")).commit();
                            AppController.getSpUserInfo().edit().putString(SPUtils.USER_NAME,jobj.getString("name")).commit();
                            AppController.getSpUserInfo().edit().putString(SPUtils.USER_EmailId, jobj.getString("email")).commit();
                            AppController.getSpUserInfo().edit().putString(SPUtils.USER_Gender, jobj.getString("gender")).commit();
                            AppController.getSpUserInfo().edit().putString(SPUtils.USER_DOB, jobj.getString("DOB")).commit();
                            AppController.getSpUserInfo().edit().putString(SPUtils.USER_MobileNo,jobj.getString("phone")).commit();
                            AppController.getSpUserInfo().edit().putString(SPUtils.USER_Address, jobj.getString("address")).commit();
                            AppController.getSpUserInfo().edit().putString(SPUtils.USER_TOKEN, jobj.getString("token")).commit();
                            AppController.getSpIsLogin().edit().putBoolean(SPUtils.IS_LOGIN, true).commit();*//*
                            Intent intent1 = new Intent(RegisterActivity.this, LoginActivity.class);
                            intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent1.putExtra("lcome","M");
                            startActivity(intent1);
                            finish();
                        }

                        else {
                            Toasty.warning(RegisterActivity.this, jsonObject.getString("message") , Toast.LENGTH_SHORT, true).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        dialog.dismiss();

                    }
                }
            }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

        } catch (Exception e) {
            e.printStackTrace();
            dialog.dismiss();

        }
    }*/
}
