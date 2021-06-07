package com.vbm.riaaya;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
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
import com.mikhaellopez.circularimageview.CircularImageView;
import com.vbm.riaaya.data.MedicalTest_Data;
import com.vbm.riaaya.session.AppController;
import com.vbm.riaaya.session.AppUtils;
import com.vbm.riaaya.session.SPUtils;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import es.dmoral.toasty.Toasty;

public class UserProfileActivity extends Activity {
    Dialog myDialog;
    LinearLayout lin_login;
    TextView username,usermobile,useremail,edit_profile,et_patientdob,txt_booktest;
    ImageView user_profile;
    Dialog dialog;
    LinearLayout lin_1,lin_2;
    String count ="0",p_gender,resvalue,imageStoragePath;

    EditText et_patientmame,et_patienemail,et_reg_cno,book_address;

    // Image
    String videopath = "";
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    String userChoosenTask = "" , imagepath;
    Bitmap main_bitmap;
    Activity act = UserProfileActivity.this;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
        changeStatusBarColor();
        setContentView(R.layout.user_profile);

        TextView txt_login;
        txt_login = (TextView)findViewById(R.id.txt_login);
        lin_login = (LinearLayout)findViewById(R.id.lin_login);
        username = (TextView)findViewById(R.id.username);
        usermobile = (TextView)findViewById(R.id.usermobile);
        useremail = (TextView)findViewById(R.id.useremail);
        lin_1 = (LinearLayout)findViewById(R.id.lin_1);
        lin_2 = (LinearLayout)findViewById(R.id.lin_2);
        edit_profile = (TextView)findViewById(R.id.edit_profile);

        et_patientmame = (EditText)findViewById(R.id.et_patientmame);
        et_patienemail = (EditText)findViewById(R.id.et_patienemail);
        et_reg_cno = (EditText)findViewById(R.id.et_reg_cno);
        book_address = (EditText)findViewById(R.id.book_address);
        et_patientdob = (TextView)findViewById(R.id.et_patientdob);
        txt_booktest = (TextView)findViewById(R.id.txt_booktest);

        user_profile = (ImageView)findViewById(R.id.user_profile);


        ImageView user_profile = (ImageView)findViewById(R.id.user_profile);
        try {

            Glide.with(UserProfileActivity.this)
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
            et_reg_cno.setText(AppController.getSpUserInfo().getString(SPUtils.USER_MobileNo,""));
            book_address.setText(AppController.getSpUserInfo().getString(SPUtils.USER_Address,""));
            et_patientdob.setText(AppController.getSpUserInfo().getString(SPUtils.USER_DOB,""));
        }
        else
        {
            et_patientmame.setText("");
            et_patienemail.setText("");
            et_patientdob.setText("");
            et_reg_cno.setText("");
            book_address.setText("");
            et_patientdob.setText("");
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
                        DatePickerDialog dateDialog = new DatePickerDialog(UserProfileActivity.this, datePickerListener, mYear, mMonth, mDay);
                        dateDialog.show();
                    }
                });

            }
        });

        lin_1.setVisibility(View.GONE);
        lin_2.setVisibility(View.VISIBLE);

        username.setText(AppController.getSpUserInfo().getString(SPUtils.USER_NAME,""));
        usermobile.setText(AppController.getSpUserInfo().getString(SPUtils.USER_MobileNo,""));
        useremail.setText(AppController.getSpUserInfo().getString(SPUtils.USER_EmailId,""));

        List<String> genderlist = new ArrayList<String>();
        genderlist.add(getResources().getString(R.string.male));
        genderlist.add(getResources().getString(R.string.female));
        Spinner spin_male = (Spinner)findViewById(R.id.spin_male);
        ArrayAdapter<String> maritalStatusadapter = new ArrayAdapter<String>(UserProfileActivity.this, android.R.layout.simple_spinner_dropdown_item, genderlist);
        spin_male.setAdapter(maritalStatusadapter);

        spin_male.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                p_gender = parent.getItemAtPosition(position).toString();
                ((TextView) view).setTextSize(14);
                ((TextView) view).setTextColor(getResources().getColor(R.color.darkgray));
                Typeface typeface = ResourcesCompat.getFont(UserProfileActivity.this, R.font.opensans_regular);
                ((TextView) view).setTypeface(Typeface.create(typeface,Typeface.BOLD));
                ((TextView) view).setSingleLine(true);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        edit_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(count.matches("0"))
                {
                    lin_1.setVisibility(View.VISIBLE);
                    lin_2.setVisibility(View.GONE);
                    count="1";
                    user_profile.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            boolean result1 = Utility.checkPermissionForWriteStoage(act);
                            boolean result2 = Utility.checkPermissionForStoage(act);
                            if (result1) {
                                if (result2) {
                                    selectImage();
                                }
                            }
                        }
                    });
                }
                else
                {
                    lin_1.setVisibility(View.GONE);
                    lin_2.setVisibility(View.VISIBLE);
                    count="0";

                }

            }
        });

        if (AppController.getSpIsLogin().getBoolean(SPUtils.IS_LOGIN, false)) {
            txt_login.setText(R.string.logout);
            lin_login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    myDialog = new Dialog(UserProfileActivity.this);
                    myDialog.setContentView(R.layout.custompopup);
                    Button Yes = (Button)myDialog.findViewById(R.id.Yes);
                    Button No = (Button)myDialog.findViewById(R.id.No);

                    Yes.setTransformationMethod(null);
                    Yes.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            AppController.getSpUserInfo().edit().clear().commit();
                            AppController.getSpIsLogin().edit().clear().commit();
                            Intent intent = new Intent(UserProfileActivity.this, SplashScreen.class);
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
                    Intent intent = new Intent(UserProfileActivity.this,LoginActivity.class);
                    startActivity(intent);
                }
            });
        }

        FloatingActionButton fab_home = (FloatingActionButton)findViewById(R.id.fab_home);
        fab_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(UserProfileActivity.this, MainActivity.class);
                intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent1);
                finish();
            }
        });

        RelativeLayout rel_bookedtest = (RelativeLayout)findViewById(R.id.rel_bookedtest);
        rel_bookedtest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserProfileActivity.this,BookedTestActivity.class);
                startActivity(intent);
            }
        });

        RelativeLayout rel_testhistory = (RelativeLayout)findViewById(R.id.rel_testhistory);
        rel_testhistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserProfileActivity.this,TestHistoryActivity.class);
                startActivity(intent);
            }
        });


        LinearLayout lin_booktest = (LinearLayout)findViewById(R.id.lin_booktest);
        lin_booktest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserProfileActivity.this,MedicalTest_Activity.class);
                startActivity(intent);
            }
        });

        txt_booktest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(et_patientmame.getText().toString().matches(""))
                {
                    Toasty.error(UserProfileActivity.this,getResources().getString(R.string.patientnamemsg), Toast.LENGTH_LONG).show();
                }
                else if(et_patientmame.getText().toString().matches(" "))
                {
                    Toasty.error(UserProfileActivity.this,getResources().getString(R.string.patientmsg1), Toast.LENGTH_LONG).show();
                }
                else if(et_patienemail.getText().toString().matches("")){

                    Toasty.error(UserProfileActivity.this,getResources().getString(R.string.emailmsg), Toast.LENGTH_LONG).show();
                }
                else if(!isValidEmail(et_patienemail.getText().toString()))
                {
                    Toasty.error(UserProfileActivity.this,getResources().getString(R.string.emailmsg1), Toast.LENGTH_LONG).show();
                }

                else if(et_reg_cno.getText().toString().matches(""))
                {
                    Toasty.error(UserProfileActivity.this,getResources().getString(R.string.cnomsg), Toast.LENGTH_LONG).show();
                }
                else if(et_reg_cno.getText().toString().matches(" "))
                {
                    Toasty.error(UserProfileActivity.this,getResources().getString(R.string.cnomsg1), Toast.LENGTH_LONG).show();
                }
                else if(book_address.getText().toString().matches("")){

                    Toasty.error(UserProfileActivity.this,getResources().getString(R.string.addressmsg), Toast.LENGTH_LONG).show();
                }
                else if(book_address.getText().toString().matches(" "))
                {
                    Toasty.error(UserProfileActivity.this,getResources().getString(R.string.addressmsg1), Toast.LENGTH_LONG).show();
                }

                else if(et_patientdob.getText().toString().matches(""))
                {
                    Toasty.error(UserProfileActivity.this,getResources().getString(R.string.dobmsg), Toast.LENGTH_LONG).show();
                }
                else if(et_patientdob.getText().toString().matches(" "))
                {
                    Toasty.error(UserProfileActivity.this,getResources().getString(R.string.dobmsg1), Toast.LENGTH_LONG).show();
                }
                else
                {
                    if(AppUtils.isNetworkAvailable(UserProfileActivity.this))
                    {
                        displayLoader();
                        new UploaderAsyn().execute();
                       // updateprofile();

                    }
                    else
                    {
                        Toasty.warning(UserProfileActivity.this,getResources().getString(R.string.nointernet), Toast.LENGTH_LONG).show();
                    }
                }

            }
        });

    }

    private void displayLoader() {
        dialog = new Dialog(UserProfileActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.loading_alert);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.show();

    }

    private void changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
    }


   /* public class UploaderAsyn extends AsyncTask<String, Void, Object> {
        @Override
        protected Object doInBackground(String... parms) {
            uploadFile();
            return null;
        }

        @Override
        protected void onPreExecute() {
            AppUtils.showProgressDialog(UserProfileActivity.this);
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Object result) {
            AppUtils.dismissProgressDialog();
            if (!TextUtils.isEmpty(resvalue)) {
                Log.e("resvalue1", resvalue);
                try {
                    JSONObject jobj = new JSONObject(resvalue);
                    String status = jobj.getString("status");
                    String message = jobj.getString("message");

                    if (status.equals("S")) {
                        Toast.makeText(UserProfileActivity.this, message, Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(UserProfileActivity.this, "Your Status is...." , Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            super.onPostExecute(result);
        }
    }

    public void uploadFile() {
        String charset = "UTF-8";
        String requestURL = "https://demo.innodasolutions.com/riaaya/API/FileUpload/fileUpload.php";
        try {
            FileUploader multipart = new FileUploader(requestURL, charset);
            multipart.addFilePart("image", new File(imageStoragePath));
            multipart.addFormField("website", "www.google.com");
            multipart.addFormField("view", "dummy@gmail.com");
            List<String> response = multipart.finish();
            for (String line : response) {
                System.out.println(line);
                resvalue = line;
            }
            System.out.println("deewa line" + resvalue);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/

   /* public class UploaderAsyn extends AsyncTask<String, Void, Object> {
        @Override
        protected Object doInBackground(String... parms) {
            uploadFile();
            return null;
        }

        @Override
        protected void onPreExecute() {
            AppUtils.showProgressDialog(UserProfileActivity.this);
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Object result) {
            AppUtils.dismissProgressDialog();
            if (!TextUtils.isEmpty(resvalue)) {
                Log.e("resvalue1", resvalue);
                try {
                    JSONObject jsonObject = new JSONObject(resvalue);
                    if (jsonObject.getString("statusCode").equalsIgnoreCase("S")) {
                        Toasty.success(UserProfileActivity.this, jsonObject.getString("message") , Toast.LENGTH_SHORT, true).show();
                        JSONObject jobj = jsonObject.getJSONObject("userdata");
                        AppController.getSpUserInfo().edit().putString(SPUtils.USER_ID, jobj.getString("id")).commit();
                        AppController.getSpUserInfo().edit().putString(SPUtils.USER_NAME,jobj.getString("name")).commit();
                        AppController.getSpUserInfo().edit().putString(SPUtils.USER_EmailId, jobj.getString("email")).commit();
                        AppController.getSpUserInfo().edit().putString(SPUtils.USER_Gender, jobj.getString("gender")).commit();
                        AppController.getSpUserInfo().edit().putString(SPUtils.USER_DOB, jobj.getString("dob")).commit();
                        AppController.getSpUserInfo().edit().putString(SPUtils.USER_MobileNo,jobj.getString("phone")).commit();
                        AppController.getSpUserInfo().edit().putString(SPUtils.USER_Address, jobj.getString("address")).commit();
                        AppController.getSpUserInfo().edit().putString(SPUtils.USER_ProfilePicUrl, jobj.getString("file")).commit();
                        AppController.getSpIsLogin().edit().putBoolean(SPUtils.IS_LOGIN, true).commit();
                        Intent intent1 = new Intent(UserProfileActivity.this, MainActivity.class);
                        intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent1);
                        finish();
                    }
                    else {
                        Toast.makeText(UserProfileActivity.this,jsonObject.getString("message"), Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            super.onPostExecute(result);
        }
    }

    public void uploadFile() {
        String charset = "UTF-8";
        String requestURL = "https://demo.innodasolutions.com/riaaya/API/riaaya_user/proUpdate.php";
        try {
            FileUploader multipart = new FileUploader(requestURL, charset);
            multipart.addFormField("email",AppController.getSpUserInfo().getString(SPUtils.USER_EmailId,""));
            multipart.addFormField("phone",AppController.getSpUserInfo().getString(SPUtils.USER_MobileNo,""));
            multipart.addFormField("token",AppController.getSpUserInfo().getString(SPUtils.USER_TOKEN,""));
            multipart.addFormField("dob",et_patientdob.getText().toString());
            multipart.addFormField("address",book_address.getText().toString());
            multipart.addFormField("gender",p_gender);
            multipart.addFormField("name",et_patientmame.getText().toString());
            multipart.addFilePart("file", new File(imageStoragePath));

            List<String> response = multipart.finish();
            for (String line : response) {
                System.out.println(line);
                resvalue = line;
            }
            System.out.println("deewa line" + resvalue);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/



    /*private void updateprofile() {

        String URL = "https://demo.innodasolutions.com/riaaya/API/riaaya_user/proUpdate.php?email="+AppController.getSpUserInfo().getString(SPUtils.USER_EmailId,"")+"&phone="+AppController.getSpUserInfo().getString(SPUtils.USER_MobileNo,"")
                +"&token="+AppController.getSpUserInfo().getString(SPUtils.USER_TOKEN,"")+"&dob="+et_patientdob.getText().toString()+
                "&address="+book_address.getText().toString().replace(" ","%20")+"&gender="+p_gender+"&name="+et_patientmame.getText().toString()+"&file="+imageStoragePath;
        Log.e("URL",URL);
        StringRequest stringRequest=new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dialog.dismiss();
                try {
                    JSONObject jsonObject=new JSONObject(response);
                    Log.e("response",response.toString());

                    if (jsonObject.getString("statusCode").equalsIgnoreCase("S")) {
                        Toasty.success(UserProfileActivity.this, jsonObject.getString("message") , Toast.LENGTH_SHORT, true).show();
                        JSONObject jobj = jsonObject.getJSONObject("userdata");
                        Log.e("file",jobj.getString("file").replace("/",""));
                        AppController.getSpUserInfo().edit().putString(SPUtils.USER_ID, jobj.getString("id")).commit();
                        AppController.getSpUserInfo().edit().putString(SPUtils.USER_NAME,jobj.getString("name")).commit();
                        AppController.getSpUserInfo().edit().putString(SPUtils.USER_EmailId, jobj.getString("email")).commit();
                        AppController.getSpUserInfo().edit().putString(SPUtils.USER_Gender, jobj.getString("gender")).commit();
                        AppController.getSpUserInfo().edit().putString(SPUtils.USER_DOB, jobj.getString("dob")).commit();
                        AppController.getSpUserInfo().edit().putString(SPUtils.USER_MobileNo,jobj.getString("phone")).commit();
                        AppController.getSpUserInfo().edit().putString(SPUtils.USER_Address, jobj.getString("address")).commit();
                        AppController.getSpUserInfo().edit().putString(SPUtils.USER_ProfilePicUrl,jobj.getString("file")).commit();

                        Intent intent1 = new Intent(UserProfileActivity.this, MainActivity.class);
                        intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent1);
                        finish();
                    }


                    else {
                        Toasty.warning(UserProfileActivity.this, jsonObject.getString("message") , Toast.LENGTH_SHORT, true).show();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                    dialog.dismiss();
                    Toasty.warning(UserProfileActivity.this,e.getMessage(),Toast.LENGTH_LONG).show();
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
    }*/

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

    private static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }


    /// test vipin
    private void selectImage() {
        // final CharSequence[] items = {"Take Photo", "Choose from Library", "Cancel"};
        final CharSequence[] items = {"Take Photo", "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(act);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                //   boolean result = Utility.checkPermission(act);

                if (items[item].equals("Take Photo")) {
                    userChoosenTask = "Take Photo";
                    // if (result)
                    cameraIntent();

                } else if (items[item].equals("Choose from Library")) {
                    userChoosenTask = "Choose from Library";
                    //   if (result)
                    galleryIntent();

                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void galleryIntent() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE);
    }

    private void cameraIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE)
                onSelectFromGalleryResult(data);
            else if (requestCode == REQUEST_CAMERA)
                onCaptureImageResult(data);
        }
    }

    private void onCaptureImageResult(Intent data) {
        Bitmap bitmap = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes);

       /* File destination = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis() + ".jpg");*/

        File path = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        File destination = new File(path, System.currentTimeMillis() + ".jpg");

        String imageStoragePath = destination.getAbsolutePath();
        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        videopath = imageStoragePath;
        // executePostImageUploadRequest(bitmap);
        main_bitmap = bitmap;
        // profileImage.setImageBitmap(bitmap);
        user_profile.setImageBitmap(bitmap);
        Log.e("from camera data", imageStoragePath);
    }

    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {

        Bitmap bm = null;
        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        // executePostImageUploadRequest(bm);
        main_bitmap = bm;
        // profileImage.setImageBitmap(bm);
        user_profile.setImageBitmap(bm);
        videopath = bm.toString();
        Log.e("from gallery data", imagepath);
    }

    public class UploaderAsyn extends AsyncTask<String, Void, Object> {
        @Override
        protected Object doInBackground(String... parms) {
            uploadFile();
            return null;
        }

        @Override
        protected void onPreExecute() {
            // AppUtils.showProgressDialog(act);
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Object result) {
            //AppUtils.dismissProgressDialog();
            if (!TextUtils.isEmpty(resvalue)) {
                Log.e("resvalue1", resvalue);
                try {
                    JSONObject jobj1 = null;
                    try {
                        jobj1 = new JSONObject(resvalue);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    String status = jobj1.getString("statusCode");
                    String message = jobj1.getString("message");
                    if (status.equals("S")) {
                        Toast.makeText(act, message, Toast.LENGTH_LONG).show();
                        JSONObject jobj = jobj1.getJSONObject("userdata");
                        AppController.getSpUserInfo().edit().putString(SPUtils.USER_ID, jobj.getString("id")).commit();
                        AppController.getSpUserInfo().edit().putString(SPUtils.USER_NAME,jobj.getString("name")).commit();
                        AppController.getSpUserInfo().edit().putString(SPUtils.USER_EmailId, jobj.getString("email")).commit();
                        AppController.getSpUserInfo().edit().putString(SPUtils.USER_Gender, jobj.getString("gender")).commit();
                        AppController.getSpUserInfo().edit().putString(SPUtils.USER_DOB, jobj.getString("dob")).commit();
                        AppController.getSpUserInfo().edit().putString(SPUtils.USER_MobileNo,jobj.getString("phone")).commit();
                        AppController.getSpUserInfo().edit().putString(SPUtils.USER_Address, jobj.getString("address")).commit();
                        AppController.getSpUserInfo().edit().putString(SPUtils.USER_ProfilePicUrl,jobj.getString("file")).commit();

                        Intent intent1 = new Intent(UserProfileActivity.this, MainActivity.class);
                        intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent1);
                        finish();

                        //1622804769910.jpg
                    } else {
                        Toast.makeText(act, "Your Status is...." + message, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            super.onPostExecute(result);
        }
    }

    public void uploadFile() {
        String charset = "UTF-8";
        String requestURL = "https://demo.innodasolutions.com/riaaya/API/riaaya_user/proUpdate.php";
        try {
            FileUploader multipart = new FileUploader(requestURL, charset);
            multipart.addFormField("email",AppController.getSpUserInfo().getString(SPUtils.USER_EmailId,""));
            multipart.addFormField("phone",AppController.getSpUserInfo().getString(SPUtils.USER_MobileNo,""));
            multipart.addFormField("token",AppController.getSpUserInfo().getString(SPUtils.USER_TOKEN,""));
            multipart.addFormField("dob",et_patientdob.getText().toString());
            multipart.addFormField("address",book_address.getText().toString());
            multipart.addFormField("gender",p_gender);
            multipart.addFormField("name",et_patientmame.getText().toString());
            multipart.addFilePart("image", new File(videopath));

            List<String> response = multipart.finish();
            for (String line : response) {
                System.out.println(line);
                resvalue = line;
            }
            System.out.println("deewa line" + resvalue);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
