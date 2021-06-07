package com.vbm.riaaya.session;
import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.Base64;
import android.util.Log;
import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SPUtils extends Application {
    public static String USER_INFO = "sp_userinfo";
    public static String IS_LOGIN = "sp_isLogin";
    public static String api = "http://demo.innodasolutions.com/riaaya/API/riaaya_user/";
    public static String imageurl = "https://demo.innodasolutions.com/riaaya/API/FileUpload/uploads/";
    public static String USER_ID = "sp_user_id";
    public static String USER_NAME = "Name";
    public static String USER_TOKEN = "token";
    public static String USER_ProfilePicUrl = "ProfilePicUrl";
    public static String USER_Password = "Password";
    public static String USER_MobileNo = "MobileNO";
    public static String USER_EmailId = "emailid";
    public static String USER_DOB = "dob";
    public static String USER_Gender = "gender";
    public static String USER_Address = "address";
    public static String USER_LANG = "lang";
    static Context context;
    private static SPUtils instance = new SPUtils();
    public SPUtils getInstance(Context con) {
        context = con;
        return instance;
    }

    public static String getDateFromAPIDate(String date) {
        try {
            String oldFormat= "mm-dd-yyyy hh:mm:ss Aa";
            String newFormat= "dd-MMM-yyyy";
            SimpleDateFormat dateFormat = new SimpleDateFormat(oldFormat);
            Date myDate = null;
            try {
                myDate = dateFormat.parse(date);
            } catch (java.text.ParseException e) {
                e.printStackTrace();
            }
            SimpleDateFormat timeFormat = new SimpleDateFormat(newFormat);
            date = timeFormat.format(myDate);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return date;
    }

    public static String getDOB(String date) {
        try {
            String oldFormat= "dd/mm/yyyy";
            String newFormat= "yyyy/MM/dd";
            SimpleDateFormat dateFormat = new SimpleDateFormat(oldFormat);
            Date myDate = null;
            try {
                myDate = dateFormat.parse(date);
            } catch (java.text.ParseException e) {
                e.printStackTrace();
            }

            SimpleDateFormat timeFormat = new SimpleDateFormat(newFormat);
            date = timeFormat.format(myDate);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return date;
    }

    public static String getBase64StringFromBitmap(Bitmap bitmap) {
        String imageString = "";
        try {
            if (bitmap != null) {
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 20, stream);
                byte[] image = stream.toByteArray();
                if (AppUtils.showLogs)
                    Log.e("AppUtills", "Image Size after comress : " + image.length);
                imageString = Base64.encodeToString(image, Base64.DEFAULT);
            } else {
                imageString = "";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return imageString;
    }
}