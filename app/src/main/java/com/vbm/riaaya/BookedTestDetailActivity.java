package com.vbm.riaaya;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;
import com.vbm.riaaya.session.AppController;
import com.vbm.riaaya.session.SPUtils;

public class BookedTestDetailActivity extends Activity {
    String booking,status,testimg,hospital_name,datetime,hospital_address_eng,name,p_age,p_gender,phone,email,address,testname;
    ImageView test_image,img_green,img_orange;
    TextView txt_hosname,txt_testname,txt_bookingno,date_time,txt_hosaddress,txt_patname,txt_age_gender,txt_phone,txt_emailaddress,txt_address,txt_msg;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.booktest_details);
        Intent intent = getIntent();
        booking = intent.getStringExtra("booking");
        status = intent.getStringExtra("status");
        testimg = intent.getStringExtra("testimg");
        hospital_name = intent.getStringExtra("hospital_name");
        datetime = intent.getStringExtra("datetime");
        hospital_address_eng = intent.getStringExtra("hospital_address_eng");
        name = intent.getStringExtra("name");
        p_age = intent.getStringExtra("p_age");
        p_gender = intent.getStringExtra("p_gender");
        phone = intent.getStringExtra("phone");
        email = intent.getStringExtra("email");
        address = intent.getStringExtra("address");
        testname = intent.getStringExtra("testname");

        test_image = (ImageView)findViewById(R.id.test_image);
        txt_hosname = (TextView)findViewById(R.id.txt_hosname);
        txt_testname = (TextView)findViewById(R.id.txt_testname);
        txt_bookingno = (TextView)findViewById(R.id.txt_bookingno);
        date_time = (TextView)findViewById(R.id.date_time);
        txt_hosaddress = (TextView)findViewById(R.id.txt_hosaddress);
        txt_patname = (TextView)findViewById(R.id.txt_patname);
        txt_age_gender = (TextView)findViewById(R.id.txt_age_gender);
        txt_phone = (TextView)findViewById(R.id.txt_phone);
        txt_emailaddress = (TextView)findViewById(R.id.txt_emailaddress);
        txt_address = (TextView)findViewById(R.id.txt_address);

        img_green = (ImageView)findViewById(R.id.img_green);
        img_orange = (ImageView)findViewById(R.id.img_orange);
        txt_msg = (TextView)findViewById(R.id.txt_msg);

        /*Picasso.with(BookedTestDetailActivity.this).load(testimg).into(test_image);*/
        txt_hosname.setText(hospital_name);
        txt_testname.setText(testname);
        txt_bookingno.setText(booking);
        date_time.setText(datetime);
        txt_hosaddress.setText(hospital_address_eng);
        txt_patname.setText(name);
        txt_age_gender.setText(p_age+" , "+p_gender);
        txt_phone.setText(phone);
        txt_emailaddress.setText(email);
        txt_address.setText(address);

         ImageView user_profile = (ImageView)findViewById(R.id.user_profile);

          try {

                Glide.with(BookedTestDetailActivity.this)
                        .load(SPUtils.imageurl+AppController.getSpUserInfo().getString(SPUtils.USER_ProfilePicUrl,"").replace(" ","%20"))
                        .skipMemoryCache(false)
                        .error(R.drawable.ic_user)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(user_profile);

            } catch (Exception e) {
                e.printStackTrace();
            }

        if(status.matches("P"))
        {
            img_orange.setVisibility(View.VISIBLE);
            img_green.setVisibility(View.GONE);
            txt_msg.setText(getResources().getString(R.string.successmsg));
            txt_msg.setTextColor(getResources().getColor(R.color.orange));
        }
        else
        {
            img_orange.setVisibility(View.GONE);
            img_green.setVisibility(View.VISIBLE);
            txt_msg.setTextColor(getResources().getColor(R.color.lightgreen));
        }

        FloatingActionButton fab_home = (FloatingActionButton)findViewById(R.id.fab_home);
        fab_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(BookedTestDetailActivity.this, MainActivity.class);
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
                    Intent intent = new Intent(BookedTestDetailActivity.this,UserProfileActivity.class);
                    startActivity(intent);
                }
                else
                {
                    Intent intent = new Intent(BookedTestDetailActivity.this,LoginActivity.class);
                    startActivity(intent);
                }
            }
        });

        LinearLayout lin_booktest = (LinearLayout)findViewById(R.id.lin_booktest);
        lin_booktest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BookedTestDetailActivity.this,MedicalTest_Activity.class);
                startActivity(intent);
            }
        });

    }
}
