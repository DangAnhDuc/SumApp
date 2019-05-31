package com.example.admin.sumapp;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.WindowManager;
import android.widget.TextView;

public class IntroActivity extends AppCompatActivity {

    //Set the final variable
    public static final String FONT_PATH = "font/UVNBanhMi.TTF";

    float x1,y1,x2,y2;
    private TextView tv1,tv2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_intro);
        findViewById();
        setTypeFace();
    }

    //Get access to the elements
    private void findViewById() {
        tv1 = findViewById(R.id.intro);
        tv2 = findViewById(R.id.description);

    }

    //Set text font
    private void setTypeFace() {
        Typeface typeface = Typeface.createFromAsset(getAssets(), FONT_PATH);
        tv1.setTypeface(typeface);
        tv2.setTypeface(typeface);
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
                if(x1>x2){
                    Intent intent=new Intent(this,MainActivity.class);
                    startActivity(intent);
                }
        }
        return false;
    }
}
