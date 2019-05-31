package com.example.admin.sumapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by anhducdang on 1/5/18.
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    //Set the final variable
    public static final String FONT_PATH = "font/UVNBanhMi.TTF";

    //View
    private TextView tvBestScore;

    //Button
    private Button btnPlay;
    private Button btnSetting;
    private Button btnLeaderBoard;

    //Swipe variable
    float x1,x2,y1,y2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        findViewById();
        setTypeFace();
    }

    @Override
    protected void onStart() {
        super.onStart();

        //Get and display the high score
        SharedPreferences sharedPreferences = getSharedPreferences(PlayActivity.SCORE, Context.MODE_PRIVATE);
        int bestScore = sharedPreferences.getInt(PlayActivity.SCORE, 0);
        @SuppressLint("StringFormatMatches") String best = getString(R.string.best_score, bestScore);
        tvBestScore.setText(best);

        //Get music status from setting acvitity
        SharedPreferences preferences = getSharedPreferences(SettingActivity.PREFS, Context.MODE_PRIVATE);
        if (preferences.getBoolean(SettingActivity.MUSIC_MAIN, true)) {
            stopService(new Intent(this, MediaService.class));
            startService(new Intent(this, MediaService.class));
        }
    }


    //Get access to the elements
    private void findViewById() {
        tvBestScore = findViewById(R.id.tvBestScore);
        btnPlay = findViewById(R.id.btn_play);
        btnSetting = findViewById(R.id.btn_setting);
        btnLeaderBoard = findViewById(R.id.btn_leaderboard);
        btnPlay.setOnClickListener(this);
        btnSetting.setOnClickListener(this);
        btnLeaderBoard.setOnClickListener(this);
    }

    //Set text font
    private void setTypeFace() {
        Typeface typeface = Typeface.createFromAsset(getAssets(), FONT_PATH);
        tvBestScore.setTypeface(typeface);
        btnPlay.setTypeface(typeface);
        btnSetting.setTypeface(typeface);
        btnLeaderBoard.setTypeface(typeface);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_play:
                clickPlay();
                break;

            case R.id.btn_setting:
                clickSetting();
                break;
            case R.id.btn_leaderboard:
                clickLeaderBoard();
                break;
            default:
                break;
        }
    }


    private void clickPlay() {
        Intent intent = new Intent(MainActivity.this, PlayActivity.class);
        startActivity(intent);
    }

    private void clickSetting() {
        Intent intent = new Intent(MainActivity.this, SettingActivity.class);
        startActivity(intent);
    }

    private void clickLeaderBoard() {
        Intent intent = new Intent(MainActivity.this, LeaderBoardActivity.class);
        startActivity(intent);
    }

    public boolean onTouchEvent(MotionEvent touchevent){
        switch (touchevent.getAction()){
            case MotionEvent.ACTION_DOWN:
                x1=touchevent.getX();
                y1=touchevent.getY();
                break;
            case MotionEvent.ACTION_UP:
                x2=touchevent.getX();
                y2=touchevent.getY();
                if(x1<x2){
                    Intent intent=new Intent(this,IntroActivity.class);
                    startActivity(intent);
                }
        }
    return false;
    }
}
