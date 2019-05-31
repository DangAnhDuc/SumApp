package com.example.admin.sumapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.hardware.Sensor;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import java.util.Random;

public class PlayActivity extends AppCompatActivity implements View.OnClickListener, Runnable {

    //Set the final variable
    public static final String FONT_PATH = "font/UVNBanhMi.TTF";
    public static final int TIMER = 5;
    public static final String SCORE = "SCORE";

    //Sensor variable
    private SensorManager sensorManager;
    private Sensor sensor;
    private Rect horizontalRect;
    private Paint rectPaint, iconPaint;
    float cx;

    //View
    private TextView tvScore;
    private TextView tvBest;
    private TextView tvTimer;
    private TextView tvQuestion;

    //Button
    private Button[] btnAns;

    //Game variable
    private int number0;
    private int number1;
    private int result;
    private int correctAns;
    private Random random;
    private Handler handler;
    private int mbest;
    private int mscore;
    private int timer;

    //SharedPreferences
    private SharedPreferences sharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_play);
        findViewById();
        setTypeFace();
        random = new Random();
        handler = new Handler();
        mscore = 0;
        setGameScore();
        generateQuestion();
    }

    //Set the game score and best score
    private void setGameScore() {
        @SuppressLint("StringFormatMatches") String score = getString(R.string.score, mscore);
        tvScore.setText(score);

        sharedPreferences = getSharedPreferences(SCORE, Context.MODE_PRIVATE);
        mbest = sharedPreferences.getInt(SCORE, 0);
        @SuppressLint("StringFormatMatches") String best = getString(R.string.best, mbest);
        tvBest.setText(best);
    }

    //Main game
    @SuppressLint({"SetTextI18n", "DefaultLocale"})
    private void generateQuestion() {
        handler.removeCallbacksAndMessages(null);
        timer = TIMER;
        //Random the 2 number
        number0 = random.nextInt(100);
        number1 = random.nextInt(100);
        result = number0 + number1;
        correctAns = random.nextInt(3);

        //Random and display 3 answer
        int[] ans = new int[3];
        for (int i = 0; i < ans.length; i++) {
            if (i == correctAns) {
                ans[i] = result;
            } else {
                int other = random.nextInt(10);
                if (random.nextBoolean()) {
                    if (i % 2 == 0) {
                        ans[i] = result - other + 10;
                    } else {
                        ans[i] = result + other - 25;
                    }
                } else {
                    if (i % 2 == 1) {
                        ans[i] = result - other + 10;
                    } else {
                        ans[i] = result + other - 25;
                    }
                }
            }
        }
        String question = number0 + "+" + number1 + "= ?";
        tvQuestion.setText(question);
        for (int index = 0; index < btnAns.length; index++) {
            btnAns[index].setText(String.format("%d", ans[index]));

        }

        //time count
        int time = TIMER + 1;
        for (int index = 0; index < time; index++) {
            handler.postDelayed(this, index * 1000);
        }

    }

    //Get access to elements
    private void findViewById() {
        tvScore = findViewById(R.id.tvScore);
        tvBest = findViewById(R.id.tvBest);
        tvTimer = findViewById(R.id.tvTimer);
        tvQuestion = findViewById(R.id.tvQuestion);
        btnAns = new Button[3];
        for (int i = 0; i < btnAns.length; i++) {
            btnAns[i] = findViewById(R.id.btn_ans0 + i);
            btnAns[i].setOnClickListener(this);
        }

    }

    //Set text font
    private void setTypeFace() {
        Typeface typeface = Typeface.createFromAsset(getAssets(), FONT_PATH);
        tvScore.setTypeface(typeface);
        tvBest.setTypeface(typeface);
        tvTimer.setTypeface(typeface);
        tvQuestion.setTypeface(typeface);
        for (Button btnAn : btnAns) {
            btnAn.setTypeface(typeface);
        }
    }

    @Override
    public void onClick(View v) {
        int i = v.getId() - R.id.btn_ans0;
        /* TODO game over */
        if (i == correctAns) {
            /* TODO add score and move to next question */
            mscore++;
            setGameScore();
            generateQuestion();
        } else gotoGameOver();

    }

    //Compare the score and move to GameOverActivity
    private void gotoGameOver() {
        handler.removeCallbacksAndMessages(null);

        if (mscore > mbest) {
            sharedPreferences.edit().putInt(SCORE, mscore).apply();
        }
        playGameOverSound();
        Intent intent = new Intent(PlayActivity.this, GameOverActivity.class);
        intent.putExtra(SCORE, mscore);
        startActivity(intent);
        finish();
    }

    //Display the time
    @SuppressLint("DefaultLocale")
    @Override
    public void run() {
        tvTimer.setText(String.format("%d", timer));
        timer--;
        if (timer == -1) {
            gotoGameOver();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    //Game over sound
    private void playGameOverSound() {
        SharedPreferences preferences = getSharedPreferences(SettingActivity.PREFS, Context.MODE_PRIVATE);
        if (preferences.getBoolean(SettingActivity.MUSIC_ENABLE, true)) {
            MediaPlayerManager mediaPlayerManager = new MediaPlayerManager(this);
            mediaPlayerManager.create(R.raw.sound_gameover, false);
            mediaPlayerManager.play();
        }
    }


    @Override
    protected void onResume(){
        super.onResume();
        sensorManager =(SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensor= sensorManager !=null? sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER):null;
        assert sensorManager != null;
    }

}
