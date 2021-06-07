package com.vbm.riaaya;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.vbm.riaaya.data.MedicalTest_Data;
import com.vbm.riaaya.session.AppController;
import com.vbm.riaaya.session.AppUtils;
import com.vbm.riaaya.session.SPUtils;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import es.dmoral.toasty.Toasty;

public class BookMyTestActivity extends Activity {
    Dialog myDialog;
    LinearLayout lin_login;
    RelativeLayout rel_1;
    String collection_mode;
    String p_time;
    String p_gender;
    String price;
    String testid;
    String offeramount;
    TextView txt_booktest,et_patientdob,test_price,offer_price,total_price;
    EditText et_patientmame,et_patienage,et_patienemail,book_address,et_offer;
    Dialog dialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
        changeStatusBarColor();
        setContentView(R.layout.book_your_test);

        Intent intent = getIntent();
        price = intent.getStringExtra("price");
        testid = intent.getStringExtra("testid");

        TextView txt_login;
        txt_login = (TextView)findViewById(R.id.txt_login);
        lin_login = (LinearLayout)findViewById(R.id.lin_login);
        rel_1 = (RelativeLayout)findViewById(R.id.rel_1);

        et_patientmame = (EditText)findViewById(R.id.et_patientmame);
        et_patienage = (EditText)findViewById(R.id.et_patienage);
        et_patienemail = (EditText)findViewById(R.id.et_patienemail);
        et_patientdob = (TextView) findViewById(R.id.et_patientdob);
        book_address = (EditText)findViewById(R.id.book_address);
        et_offer = (EditText)findViewById(R.id.et_offer);

        txt_booktest = (TextView)findViewById(R.id.txt_booktest);

        test_price = (TextView)findViewById(R.id.test_price);
        offer_price = (TextView)findViewById(R.id.offer_price);
        total_price = (TextView)findViewById(R.id.total_price);
        test_price.setText(price);
        offer_price.setText("0");
        total_price.setText(price);


        ImageView user_profile = (ImageView)findViewById(R.id.user_profile);
        try {

            Glide.with(BookMyTestActivity.this)
                    .load(SPUtils.imageurl+AppController.getSpUserInfo().getString(SPUtils.USER_ProfilePicUrl,"").replace(" ","%20"))
                    .skipMemoryCache(false)
                    .error(R.drawable.ic_user)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(user_profile);

        } catch (Exception e) {
            e.printStackTrace();
        }

        if (AppController.getSpIsLogin().getBoolean(SPUtils.IS_LOGIN, false)) {

            et_patientmame.setText(AppController.getSpUserInfo().getString(SPUtils.USER_NAME,""));
            et_patienemail.setText(AppController.getSpUserInfo().getString(SPUtils.USER_EmailId,""));
            et_patientdob.setText(AppController.getSpUserInfo().getString(SPUtils.USER_DOB,""));
        }
        else
        {
            et_patientmame.setText("");
            et_patienemail.setText("");
            et_patientdob.setText("");
        }

        List<String> genderlist = new ArrayList<String>();
        genderlist.add(getResources().getString(R.string.male));
        genderlist.add(getResources().getString(R.string.female));
        Spinner spin_male = (Spinner)findViewById(R.id.spin_male);
        ArrayAdapter<String> maritalStatusadapter = new ArrayAdapter<String>(BookMyTestActivity.this, android.R.layout.simple_spinner_dropdown_item, genderlist);
        spin_male.setAdapter(maritalStatusadapter);

        spin_male.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                p_gender = parent.getItemAtPosition(position).toString();
                ((TextView) view).setTextSize(14);
                ((TextView) view).setTextColor(getResources().getColor(R.color.darkgray));
                Typeface typeface = ResourcesCompat.getFont(BookMyTestActivity.this, R.font.opensans_regular);
                ((TextView) view).setTypeface(Typeface.create(typeface,Typeface.BOLD));
                ((TextView) view).setSingleLine(true);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        List<String> collectionplacelist = new ArrayList<String>();
        collectionplacelist.add(getResources().getString(R.string.cfh));
        collectionplacelist.add(getResources().getString(R.string.vh));
        Spinner spin_collplace = (Spinner)findViewById(R.id.spin_collplace);
        ArrayAdapter<String> maritalcollectionplaceadapter = new ArrayAdapter<String>(BookMyTestActivity.this, android.R.layout.simple_spinner_dropdown_item, collectionplacelist);
        spin_collplace.setAdapter(maritalcollectionplaceadapter);

        spin_collplace.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                collection_mode = parent.getItemAtPosition(position).toString().replace(" ","%20");
                ((TextView) view).setTextSize(14);
                ((TextView) view).setTextColor(getResources().getColor(R.color.darkgray));
                Typeface typeface = ResourcesCompat.getFont(BookMyTestActivity.this, R.font.opensans_regular);
                ((TextView) view).setTypeface(Typeface.create(typeface,Typeface.BOLD));
                ((TextView) view).setSingleLine(true);

                if(collection_mode==(getResources().getString(R.string.vh)))
                {
                    rel_1.setVisibility(View.GONE);
                }
                else
                {
                    rel_1.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        List<String> prefeeredtimelist = new ArrayList<String>();
        prefeeredtimelist.add(getResources().getString(R.string.sypt));
        prefeeredtimelist.add("06:00 AM to 08:00 AM");
        prefeeredtimelist.add("08:00 AM to 10:00 AM");
        prefeeredtimelist.add("10:00 AM to 12:00 PM");
        prefeeredtimelist.add("12:00 PM to 02:00 PM");
        prefeeredtimelist.add("02:00 PM to 04:00 PM");
        prefeeredtimelist.add("04:00 PM to 06:00 PM");


        Spinner spin_preferedtime = (Spinner)findViewById(R.id.spin_preferedtime);
        ArrayAdapter<String> preferedtimelistadapter = new ArrayAdapter<String>(BookMyTestActivity.this, android.R.layout.simple_spinner_dropdown_item, prefeeredtimelist);
        spin_preferedtime.setAdapter(preferedtimelistadapter);

        spin_preferedtime.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                p_time = parent.getItemAtPosition(position).toString().replace(" ","%20");;
                ((TextView) view).setTextSize(14);
                ((TextView) view).setTextColor(getResources().getColor(R.color.darkgray));
                Typeface typeface = ResourcesCompat.getFont(BookMyTestActivity.this, R.font.opensans_regular);
                ((TextView) view).setTypeface(Typeface.create(typeface,Typeface.BOLD));
                ((TextView) view).setSingleLine(true);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        if (AppController.getSpIsLogin().getBoolean(SPUtils.IS_LOGIN, false)) {
            txt_login.setText(R.string.logout);
            lin_login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    myDialog = new Dialog(BookMyTestActivity.this);
                    myDialog.setContentView(R.layout.custompopup);
                    Button Yes = (Button)myDialog.findViewById(R.id.Yes);
                    Button No = (Button)myDialog.findViewById(R.id.No);

                    Yes.setTransformationMethod(null);
                    Yes.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            AppController.getSpUserInfo().edit().clear().commit();
                            AppController.getSpIsLogin().edit().clear().commit();
                            Intent intent = new Intent(BookMyTestActivity.this, SplashScreen.class);
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
                    Intent intent = new Intent(BookMyTestActivity.this,LoginActivity.class);
                    startActivity(intent);
                }
            });
        }

        et_patientdob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et_patientdob.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final Calendar c = Calendar.getInstance();
                        int mYear = c.get(Calendar.YEAR);
                        int mMonth = c.get(Calendar.MONTH);
                        int mDay = c.get(Calendar.DAY_OF_MONTH);
                        DatePickerDialog dateDialog = new DatePickerDialog(BookMyTestActivity.this, datePickerListener, mYear, mMonth, mDay);
                        /*dateDialog.getDatePicker().setMinDate(new Date().getTime());
                        dateDialog.getDatePicker().setMaxDate((c.getTimeInMillis())+(1000*60*60*24*6));*/
                        dateDialog.show();
                    }
                });

            }
        });

        FloatingActionButton fab_home = (FloatingActionButton)findViewById(R.id.fab_home);
        fab_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(BookMyTestActivity.this, MainActivity.class);
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
                    Intent intent = new Intent(BookMyTestActivity.this,UserProfileActivity.class);
                    startActivity(intent);
                }
                else
                {
                    Intent intent = new Intent(BookMyTestActivity.this,LoginActivity.class);
                    startActivity(intent);
                }
            }
        });

        LinearLayout lin_booktest = (LinearLayout)findViewById(R.id.lin_booktest);
        lin_booktest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BookMyTestActivity.this,MedicalTest_Activity.class);
                startActivity(intent);
            }
        });
        et_offer.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String id = et_offer.getText().toString().trim();

                if (id.length() > 4) {
                    displayLoader();
                    executetoCheckSponsorName(et_offer.getText().toString());
                }
            }
        });

        txt_booktest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(et_patientmame.getText().toString().matches(""))
                {
                    Toasty.error(BookMyTestActivity.this,getResources().getString(R.string.patientnamemsg), Toast.LENGTH_LONG).show();
                }
                else if(et_patientmame.getText().toString().matches(" "))
                {
                    Toasty.error(BookMyTestActivity.this,getResources().getString(R.string.patientmsg1), Toast.LENGTH_LONG).show();
                }
                else if(et_patienage.getText().toString().matches(" "))
                {
                    Toasty.error(BookMyTestActivity.this,getResources().getString(R.string.agemsg), Toast.LENGTH_LONG).show();
                }
                else if(et_patienage.getText().toString().matches(" "))
                {
                    Toasty.error(BookMyTestActivity.this,getResources().getString(R.string.agemsg1), Toast.LENGTH_LONG).show();
                }
                else if(et_patienemail.getText().toString().matches("")){

                    Toasty.error(BookMyTestActivity.this,getResources().getString(R.string.emailmsg), Toast.LENGTH_LONG).show();
                }
                else if(!isValidEmail(et_patienemail.getText().toString()))
                {
                    Toasty.error(BookMyTestActivity.this,getResources().getString(R.string.emailmsg1), Toast.LENGTH_LONG).show();
                }

                else if(et_patientdob.getText().toString().matches("")){

                    Toasty.error(BookMyTestActivity.this,getResources().getString(R.string.emailmsg), Toast.LENGTH_LONG).show();
                }

                else if(p_time.matches(getResources().getString(R.string.sypt)))
                {
                    Toasty.error(BookMyTestActivity.this,getResources().getString(R.string.sypt), Toast.LENGTH_LONG).show();
                }
                else if(book_address.getText().toString().matches("")){

                    Toasty.error(BookMyTestActivity.this,getResources().getString(R.string.addressmsg), Toast.LENGTH_LONG).show();
                }
                else if(book_address.getText().toString().matches(" "))
                {
                    Toasty.error(BookMyTestActivity.this,getResources().getString(R.string.addressmsg1), Toast.LENGTH_LONG).show();
                }
                else
                {
                    if(AppUtils.isNetworkAvailable(BookMyTestActivity.this))
                    {
                        displayLoader();
                        Booktest();

                    }
                    else
                    {
                        Toasty.warning(BookMyTestActivity.this,getResources().getString(R.string.nointernet), Toast.LENGTH_LONG).show();
                    }

                }

            }
        });

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

    private static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private void displayLoader() {
        dialog = new Dialog(BookMyTestActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.loading_alert);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.show();

    }

    // offer
    //https://demo.innodasolutions.com/riaaya/API/riaaya_user/book_test.php?code=FAT21&price=30&token=904b0e-e8b9d6-d2abdd-17e565-a50852-81
    private void Booktest() {
        String URL = "https://demo.innodasolutions.com/riaaya/API/riaaya_user/test-book.php?test_id="+testid+"&book_date="+et_patientdob.getText().toString()+"&token="+AppController.getSpUserInfo().getString(SPUtils.USER_TOKEN,"")+"&phone="+AppController.getSpUserInfo().getString(SPUtils.USER_MobileNo,"")+
                "&email="+et_patienemail.getText().toString()+"&address="+book_address.getText().toString()+"&collection_mode="+collection_mode+"&offer="+et_offer.getText().toString()+"&total="+total_price.getText().toString()+"&p_time="+p_time+"&p_name="+et_patientmame.getText().toString()+
                "&p_age="+et_patienage.getText().toString()+"&p_gender="+p_gender;
        Log.e("URL",URL);
        StringRequest stringRequest=new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dialog.dismiss();
                try {
                    JSONObject jsonObject=new JSONObject(response);
                    Log.e("response",response.toString());

                    if (jsonObject.getString("statusCode").equalsIgnoreCase("S")) {
                        Intent intent = new Intent(BookMyTestActivity.this, BookingSuccessActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                    }
                    else {
                        Toasty.warning(BookMyTestActivity.this,jsonObject.getString("message"),Toast.LENGTH_SHORT, true).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    dialog.dismiss();
                    Toasty.warning(BookMyTestActivity.this,e.getMessage(),Toast.LENGTH_LONG).show();
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


    private void executetoCheckSponsorName(final String offercode) {
        String URL = "https://demo.innodasolutions.com/riaaya/API/riaaya_user/book_test.php?code="+offercode+"&price="+price+"&token="+AppController.getSpUserInfo().getString(SPUtils.USER_TOKEN,"");
        Log.e("URL",URL);
        StringRequest stringRequest=new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dialog.dismiss();
                try {
                    JSONObject jsonObject=new JSONObject(response);
                    Log.e("response",response.toString());

                    if (jsonObject.getString("statusCode").equalsIgnoreCase("S")) {
                        offeramount = jsonObject.getString("price");
                        offer_price.setText(jsonObject.getString("price"));

                        // calculation
                        int a = Integer.parseInt(price);
                        int b = Integer.parseInt(offeramount);
                        int c = a-b;
                        offer_price.setText(""+c);
                        total_price.setText(""+jsonObject.getString("price"));
                    }
                    else {
                        offer_price.setText("0");
                        offeramount = "0";
                        Toasty.warning(BookMyTestActivity.this, jsonObject.getString("message") , Toast.LENGTH_SHORT, true).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    dialog.dismiss();
                    Toasty.warning(BookMyTestActivity.this,e.getMessage(),Toast.LENGTH_LONG).show();
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

    private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int year, int month, int day) {
            Calendar c = Calendar.getInstance();
            c.set(Calendar.YEAR, year);
            c.set(Calendar.MONTH, month);
            c.set(Calendar.DAY_OF_MONTH, day);
            String format = new SimpleDateFormat("dd MMM yyyy").format(c.getTime());
            // formatpass = new SimpleDateFormat("yyyy-MM-dd").format(c.getTime());
            et_patientdob.setText(format);
        }
    };

}
