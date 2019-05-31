package com.example.admin.sumapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.Locale;

public class SettingActivity extends AppCompatActivity {

    //Set the final variable
    public static final String FONT_PATH = "font/UVNBanhMi.TTF";
    public static final String MUSIC_ENABLE = "MUSIC";
    public static final String PREFS = "PREFS";
    public static final String MUSIC_MAIN = "MUSIC MAIN";

    //View
    private TextView tvMusic;
    private TextView tvLanguage;

    //Button
    private Button btnVi;
    private Button btnEng;
    private Button btnJa;
    private ToggleButton tgMusic;

    //SharedPreferences
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_setting);
        sharedPreferences = getSharedPreferences(PREFS, Context.MODE_PRIVATE);
        findViewById();
        setTypeFace();
    }

    //Get access to elements
    private void findViewById() {
        tvMusic = findViewById(R.id.tvMusic);
        tvLanguage = findViewById(R.id.tvLanguageChoose);
        btnVi = findViewById(R.id.btn_vi);
        btnEng = findViewById(R.id.btn_eng);
        btnJa = findViewById(R.id.btn_ja);
        tgMusic = findViewById(R.id.tgMusic);


        btnVi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Changelanguage("vi");
                sharedPreferences.edit().putBoolean(MUSIC_MAIN, false).apply();
                Intent intent = new Intent(SettingActivity.this, MainActivity.class);
                startActivity(intent);

            }
        });

        btnEng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Changelanguage("eng");
                sharedPreferences.edit().putBoolean(MUSIC_MAIN, false).apply();
                Intent intent = new Intent(SettingActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
        btnJa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Changelanguage("ja");
                sharedPreferences.edit().putBoolean(MUSIC_MAIN, false).apply();
                Intent intent = new Intent(SettingActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

    }


    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    public void onStart() {
        super.onStart();

        //Set the music button status
        tgMusic.setChecked(sharedPreferences.getBoolean(MUSIC_ENABLE, true));
        tgMusic.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!isChecked) {
                    Toast.makeText(SettingActivity.this, "The music is pause", Toast.LENGTH_SHORT).show();
                    stopService(new Intent(SettingActivity.this, MediaService.class));
                    sharedPreferences.edit().putBoolean(MUSIC_ENABLE, false).apply();
                    sharedPreferences.edit().putBoolean(MUSIC_MAIN, false).apply();

                } else {
                    Toast.makeText(SettingActivity.this, "The music is continue", Toast.LENGTH_SHORT).show();
                    startService(new Intent(SettingActivity.this, MediaService.class));
                    sharedPreferences.edit().putBoolean(MUSIC_ENABLE, true).apply();
                    sharedPreferences.edit().putBoolean(MUSIC_MAIN, false).apply();
                }
            }
        });
    }

    //Set text font
    private void setTypeFace() {
        Typeface typeface = Typeface.createFromAsset(getAssets(), FONT_PATH);
        tvMusic.setTypeface(typeface);
        tvLanguage.setTypeface(typeface);
        btnVi.setTypeface(typeface);
        btnEng.setTypeface(typeface);
        btnJa.setTypeface(typeface);
        tgMusic.setTypeface(typeface);
    }

    //Get and change language of game
    public void Changelanguage(String language) {
        Locale myLocale = new Locale(language);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        //Set Sharepreferences for MainActivity
        if (sharedPreferences.getBoolean(MUSIC_ENABLE, true)) {
            sharedPreferences.edit().putBoolean(MUSIC_MAIN, true).apply();
        }
    }
}
