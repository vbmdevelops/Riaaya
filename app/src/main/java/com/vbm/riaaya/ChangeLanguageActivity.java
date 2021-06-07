package com.vbm.riaaya;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import com.vbm.riaaya.session.AppController;
import com.vbm.riaaya.session.SPUtils;
import java.util.Locale;

public class ChangeLanguageActivity extends Activity {

    Activity act = ChangeLanguageActivity.this;
    RelativeLayout tv_arabic, tv_english;
    ImageView img_hindi,img_english;
    //Shared Preferences Variables
    private static final String Locale_Preference = "Locale Preference";
    private static final String Locale_KeyValue = "Saved Locale";
    private static SharedPreferences sharedPreferences;
    private static SharedPreferences.Editor editor;
    private static Locale myLocale;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            if (Build.VERSION.SDK_INT >= 21) {
                getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            }
            changeStatusBarColor();
            setContentView(R.layout.activity_change_language);

            sharedPreferences = getSharedPreferences(Locale_Preference, Activity.MODE_PRIVATE);
            editor = sharedPreferences.edit();

            tv_arabic = findViewById(R.id.tv_arabic);
            tv_english = findViewById(R.id.tv_english);


            tv_arabic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    changeLocale("ar");
                }
            });
            tv_english.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    changeLocale("en");
                }
            });

           /* String language = AppController.getSpUserInfo().getString(SPUtils.USER_LANG, "");
            if (language.equalsIgnoreCase("ar")) {
                img_hindi.setVisibility(View.VISIBLE);
            } else if (language.equalsIgnoreCase("en")) {
                img_english.setVisibility(View.VISIBLE);
            } else {
                img_hindi.setVisibility(View.VISIBLE);
            }*/

        } catch (Exception e) {
            e.getMessage();
        }
    }

    //Change Locale
    public void changeLocale(String lang) {
        if (lang.equalsIgnoreCase(""))
            return;
        myLocale = new Locale(lang);//Set Selected Locale
        saveLocale(lang);//Save the selected locale
        Locale.setDefault(myLocale);//set new locale as default
        Configuration config = new Configuration();//get Configuration
        config.locale = myLocale;//set config locale as selected locale
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());//Update the config
        // updateTexts();//Update texts according to locale
    }

    //Save locale method in preferences
    public void saveLocale(String lang) {
        AppController.getSpUserInfo().edit().putString(SPUtils.USER_LANG, lang).commit();

      //  startActivity(new Intent(act, MainActivity.class));
      //  finish();
        Intent intent = new Intent(act, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    //Get locale method in preferences
    public void loadLocale() {
        String language = sharedPreferences.getString(Locale_KeyValue, "");
        changeLocale(language);
    }

    private void changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
    }
}