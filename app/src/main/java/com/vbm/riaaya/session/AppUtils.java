package com.vbm.riaaya.session;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.media.ExifInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.format.DateFormat;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.vbm.riaaya.R;

import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.KeyStore;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class AppUtils {

    public static final String IMAGE_DIRECTORY_NAME = String.valueOf(R.string.app_name);
    public static final int MEDIA_TYPE_IMAGE = 1;
    public static ProgressDialog progressDialog;
    public static boolean showLogs = true;
    public static String lastCompressedImageFileName = "";
    public static String mPANPattern = "[A-Z]{5}[0-9]{4}[A-Z]{1}";
    public static String mPINCodePattern = "^[1-9][0-9]{5}$";

    public static void hideKeyboardOnClick(Context con, View view) {
        try {
            InputMethodManager inputManager = (InputMethodManager) con.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        } catch (Exception e) {
            e.printStackTrace();
            AppUtils.showExceptionDialog(con);
        }
    }


    public static String isNetworkWifiMobileData(Context context) {
        String isType = "";
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

            if (networkInfo.getTypeName().equalsIgnoreCase("WIFI")) {
                isType = "W";
            } else if (networkInfo.getTypeName().equalsIgnoreCase("MOBILE")) {
                isType = "M";
            } else {
                isType = "MW";
            }
        } catch (Exception e) {
            e.printStackTrace();
            AppUtils.showExceptionDialog(context);
        }
        return isType;
    }

    public static String getAppVersionName(Context context) {
        PackageManager manager = context.getPackageManager();
        String versionName = "";
        try {
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            versionName = info.versionName;
        } catch (Exception e) {
            e.printStackTrace();
            AppUtils.showExceptionDialog(context);
        }
        return versionName;
    }

    public static void showProgressDialog(Context conn) {
        try {
            if (progressDialog != null) {
                if (!progressDialog.isShowing()) {
                    if (!((Activity) conn).isFinishing()) {
                        progressDialog.show();
                    }
                }
            } else {

                progressDialog = new ProgressDialog(conn);
                progressDialog.setMessage("Please Wait...");
                progressDialog.setTitle("Loading...");
                progressDialog.setIndeterminate(true);
                progressDialog.setCancelable(false);
                progressDialog.setInverseBackgroundForced(false);
                progressDialog.show();

                if (!progressDialog.isShowing()) {
                    //   progressDialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
                    progressDialog.show();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void showExceptionDialog(Context con) {
        try {
            AppUtils.dismissProgressDialog();
            //   AppUtils.alertDialog(con, "Sorry, There seems to be some problem. Try again later");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void dismissProgressDialog() {
        try {
            if (progressDialog != null) {
                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                    progressDialog = null;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static Bitmap getBitmapFromString(String imageString) {
        Bitmap bitmap = null;
        try {
            byte[] byteArray = Base64.decode(imageString, Base64.DEFAULT);
            bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    public static String getPath(Uri uri, Context context) {
        if (uri == null)
            return null;

        if (AppUtils.showLogs) Log.d("URI", uri + "");
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = context.getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null) {
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            String temp = cursor.getString(column_index);
            cursor.close();
            if (AppUtils.showLogs) Log.v("temp", "" + temp);
            return temp;
        } else
            return null;
    }

    public static Bitmap compressImage(String filePath) {
        Bitmap scaledBitmap = null;

        BitmapFactory.Options options = new BitmapFactory.Options();

//      by setting this field as true, the actual bitmap pixels are not loaded in the memory. Just the bounds are loaded. If
//      you try the use the bitmap here, you will get null.
        options.inJustDecodeBounds = true;
        Bitmap bmp = BitmapFactory.decodeFile(filePath, options);

        int actualHeight = options.outHeight;
        int actualWidth = options.outWidth;

//      max Height and width values of the compressed image is taken as 816x612

        float maxHeight = 500.0f;
        float maxWidth = 500.0f;
        float imgRatio = actualWidth / actualHeight;
        float maxRatio = maxWidth / maxHeight;

//      width and height values are set maintaining the aspect ratio of the image

        if (actualHeight > maxHeight || actualWidth > maxWidth) {
            if (imgRatio < maxRatio) {
                imgRatio = maxHeight / actualHeight;
                actualWidth = (int) (imgRatio * actualWidth);
                actualHeight = (int) maxHeight;
            } else if (imgRatio > maxRatio) {
                imgRatio = maxWidth / actualWidth;
                actualHeight = (int) (imgRatio * actualHeight);
                actualWidth = (int) maxWidth;
            } else {
                actualHeight = (int) maxHeight;
                actualWidth = (int) maxWidth;

            }
        }

//      setting inSampleSize value allows to load a scaled down version of the original image

        options.inSampleSize = calculateInSampleSize(options, actualWidth, actualHeight);

//      inJustDecodeBounds set to false to load the actual bitmap
        options.inJustDecodeBounds = false;

//      this options allow android to claim the bitmap memory if it runs low on memory
        options.inPurgeable = true;
        options.inInputShareable = true;
        options.inTempStorage = new byte[16 * 1024];

        try {
//          load the bitmap from its path
            bmp = BitmapFactory.decodeFile(filePath, options);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();

        }
        try {
            scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight, Bitmap.Config.ARGB_8888);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();
        }

        float ratioX = actualWidth / (float) options.outWidth;
        float ratioY = actualHeight / (float) options.outHeight;
        float middleX = actualWidth / 2.0f;
        float middleY = actualHeight / 2.0f;

        Matrix scaleMatrix = new Matrix();
        scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);

        Canvas canvas = new Canvas(scaledBitmap);
        canvas.setMatrix(scaleMatrix);
        canvas.drawBitmap(bmp, middleX - bmp.getWidth() / 2, middleY - bmp.getHeight() / 2, new Paint(Paint.FILTER_BITMAP_FLAG));

//      check the rotation of the image and display it properly
        ExifInterface exif;
        try {
            exif = new ExifInterface(filePath);

            int orientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION, 0);
            if (AppUtils.showLogs) Log.d("EXIF", "Exif: " + orientation);
            Matrix matrix = new Matrix();
            if (orientation == 6) {
                matrix.postRotate(90);
                if (AppUtils.showLogs) Log.d("EXIF", "Exif: " + orientation);
            } else if (orientation == 3) {
                matrix.postRotate(180);
                if (AppUtils.showLogs) Log.d("EXIF", "Exif: " + orientation);
            } else if (orientation == 8) {
                matrix.postRotate(270);
                if (AppUtils.showLogs) Log.d("EXIF", "Exif: " + orientation);
            }
            scaledBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0,
                    scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix,
                    true);
        } catch (IOException e) {
            e.printStackTrace();
        }

        FileOutputStream out = null;
        lastCompressedImageFileName = getFilename();
        try {
            out = new FileOutputStream(lastCompressedImageFileName);

//          write the compressed bitmap at the destination specified by filename.
            scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 80, out);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return scaledBitmap;
    }

    public static String getFilename() {
        File file = new File(Environment.getExternalStorageDirectory().getPath(), "Molt");
        if (!file.exists()) {
            file.mkdirs();
        }
        return (file.getAbsolutePath() + "/" + System.currentTimeMillis() + ".jpg");
    }

    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        final float totalPixels = width * height;
        final float totalReqPixelsCap = reqWidth * reqHeight * 2;
        while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
            inSampleSize++;
        }

        return inSampleSize;
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


    public static boolean isNetworkAvailable(Context context) {
        boolean connected = false;
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            connected = networkInfo != null && networkInfo.isAvailable() && networkInfo.isConnected();
            return connected;
        } catch (Exception e) {
            e.printStackTrace();
            AppUtils.showExceptionDialog(context);
        }
        return connected;
    }
    public static HttpClient createHttpClient() {
        try {
            KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
            trustStore.load(null, null);

            MySSLSocketFactory sf = new MySSLSocketFactory(trustStore);
            sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

            HttpParams params = new BasicHttpParams();
            HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
            HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);

            SchemeRegistry registry = new SchemeRegistry();
            registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
            registry.register(new Scheme("https", sf, 443));

            ClientConnectionManager ccm = new ThreadSafeClientConnManager(params, registry);

            return new DefaultHttpClient(ccm, params);
        } catch (Exception e) {
            return new DefaultHttpClient();
        }
    }

    public static String callWebServiceWithMultiParam(Context con, List<NameValuePair> postParameters, String methodName, String pageName) {

        BufferedReader in = null;

        try {

           // HttpClient client = new DefaultHttpClient();
            HttpClient client;
            client = createHttpClient();

            String result = null;

            if (AppUtils.isNetworkAvailable(con)) {

                AppUtils.printQuery(pageName+"::"+methodName+":: ", postParameters);

                if (AppUtils.showLogs)
                    Log.e(pageName, "Executing URL..." + SPUtils.api+methodName);

                HttpPost request = new HttpPost(SPUtils.api+methodName);


                UrlEncodedFormEntity formEntity = null;
                try {
                    formEntity = new UrlEncodedFormEntity(postParameters);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                request.setEntity(formEntity);

                HttpResponse response = null;
                try {

                    response = client.execute(request);

                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
                } catch (Exception e) {
                    e.printStackTrace();
                }

                StringBuilder sb = new StringBuilder();
                String line;
                String NL = System.getProperty("line.separator");

                try {
                    while ((line = in.readLine()) != null) {
                        sb.append(line).append(NL);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

                try {
                    in.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                try {
                    if (AppUtils.showLogs) Log.e(pageName + "", methodName+"::: Response..... " + sb.toString());

                    result = sb.toString().trim();


                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                //   AppUtils.alertDialog(con, con.getResources().getString(R.string.txt_networkAlert));
            }

            return result;

        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }



    public static void printQuery(String pageName, List<NameValuePair> postParam) {
        try {
            String query = "";
            for (int i = 0; i < postParam.size(); i++) {
                query = query + " " + postParam.get(i).getName() + " : " + postParam.get(i).getValue();
            }

            if (AppUtils.showLogs) Log.e(pageName, "Executing Parameters..." + query);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static String getDateFromAPIDateTime(String date) {
        try {
            if (AppUtils.showLogs) Log.v("getFormatDate", "before date.." + date);
            Calendar cal = Calendar.getInstance(Locale.ENGLISH);

            if (date.contains("/Date("))
                cal.setTimeInMillis(Long.parseLong(date.replace("/Date(", "").replace(")/", "")));
            else
                cal.setTimeInMillis(Long.parseLong(date.replace("/date(", "").replace(")/", "")));

            date = DateFormat.format("dd-MMM-yyyy hh:mm:ss a", cal).toString();

            if (AppUtils.showLogs) Log.v("getFormatDate", "after date.." + date);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return date;
    }

}