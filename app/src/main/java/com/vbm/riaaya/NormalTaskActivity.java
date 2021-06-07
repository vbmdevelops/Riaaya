package com.vbm.riaaya;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Base64OutputStream;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class NormalTaskActivity extends Activity {
    ImageView internet_conn;
    Button btn_submit;
    TextView txt_upload_file;
    Activity act = NormalTaskActivity.this;
    LinearLayout ll_data_found, ll_no_data_found;
    String str_remark = "", str_exten = "";
    EditText edtxt_title;
    ArrayList<Uri> imagesUriList;
    ArrayList<String> encodedImageList;
    String imageURI = "", str_type = "";
    ImageView user_profile, img_select_pdf, img_select_doc;
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    String userChoosenTask = "";
    Bitmap main_bitmap;
    private final static int FILE_REQUEST_CODE = 123;

    String str_sts = "F", str_msg = "";
    String sts = "N", resvalue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_profile);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        try {

            user_profile = (ImageView)findViewById(R.id.user_profile);
            user_profile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    str_type = "I";
                    str_exten = "PNG";
                    selectImage();
                }
            });


        } catch (Exception ex) {
            ex.getMessage();
        }

    }

    public void onBackPressed() {
        super.onBackPressed();
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    public static String getExtension(String uri) {
        if (uri == null) {
            return null;
        }

        int dot = uri.lastIndexOf(".");
        if (dot >= 0) {
            return uri.substring(dot);
        } else {
            // No extension.
            return "";
        }
    }

    /*Code added by mukesh photo uploading*/
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

        File destination = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis() + ".jpg");
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
        txt_upload_file.setText("" + imageStoragePath);
        // executePostImageUploadRequest(bitmap);
        main_bitmap = bitmap;
        // profileImage.setImageBitmap(bitmap);
        // iv_Profile_Pic_dash.setImageBitmap(bitmap);
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
        //  iv_Profile_Pic_dash.setImageBitmap(bm);
        String imagepath = bm.toString();
        txt_upload_file.setText("" + imagepath);
        Log.e("from gallery data", imagepath);
    }

    public static byte[] convert(String path) throws IOException {

        FileInputStream fis = new FileInputStream(path);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] b = new byte[1024];

        for (int readNum; (readNum = fis.read(b)) != -1; ) {
            bos.write(b, 0, readNum);
        }

        byte[] bytes = bos.toByteArray();

        return bytes;
    }


    public class UploaderAsyn extends AsyncTask<String, Void, Object> {
        @Override
        protected Object doInBackground(String... parms) {
            uploadFile();
            return null;
        }

        @Override
        protected void onPreExecute() {
            //AppUtils.showProgressDialog(act);
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Object result) {
           // AppUtils.dismissProgressDialog();
            if (!TextUtils.isEmpty(resvalue)) {
                Log.e("resvalue1", resvalue);
                try {
                    JSONObject jobj = new JSONObject(resvalue);
                    String status = jobj.getString("status");
                    String message = jobj.getString("message");

                    if (status.equals("S")) {
                        Toast.makeText(act, "succ" , Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(act, "Your Status is...." , Toast.LENGTH_LONG).show();
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
            String videopath = txt_upload_file.getText().toString();
            multipart.addFilePart("image", new File(videopath));
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
    }



}
