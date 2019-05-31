package com.example.admin.sumapp;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.InputStream;
import java.util.Objects;

import twitter4j.StatusUpdate;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import twitter4j.conf.ConfigurationBuilder;

public class GameOverActivity extends AppCompatActivity implements View.OnClickListener {
    //Set the final variable
    public static final String FONT_PATH = "font/UVNBanhMi.TTF";
    private static final String PREF_FILE = "SCORE_PREF_FILE";

    //View
    private TextView tvGameOver;
    private TextView tvPlayerScore;

    //Button
    private Button btnTryAgain;
    private Button btnHome;
    private Button btnEnter;

    //Text and database variable
    private EditText edtName;
    private LeaderBoardDataBase leaderBoardDataBase;
    private int score;

    //Twitter variable
    private ProgressDialog pDialog;
    public static final int AUTHENTICATE = 1;
    private static RequestToken requestToken;
    private static Twitter twitter;
    private AccessToken accessToken;
    private String consumerKey;
    private String consumerSecretKey;
    private String callbackUrl;
    private String oAuthVerifier;
    private Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            new updateTwitterStatus().execute(accessToken);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);


        //Enabling strinct mode
        StrictMode.ThreadPolicy threadPolicy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(threadPolicy);
        setContentView(R.layout.activity_over);

        //Custom Toolbar
        Toolbar toolbar = findViewById(R.id.toolBar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        findViewById();
        setTypeFace();

        score = Objects.requireNonNull(getIntent().getExtras()).getInt(PlayActivity.SCORE);
        @SuppressLint("StringFormatMatches") String playerScore = getString(R.string.player_score, score);
        tvPlayerScore.setText(playerScore);

        twitterConfigs();
    }

    @Override
    protected void onPause() {
        super.onPause();
        saveScoreInfo();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.gameovermenu, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        Intent intent;

        switch (id) {
            case R.id.leaderboard_screen:
                intent = new Intent(this, LeaderBoardActivity.class);
                startActivity(intent);
                break;
            case R.id.twitter:
                saveScoreInfo();
                loginIntoTwitter();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(final int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AUTHENTICATE && resultCode == RESULT_OK) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    //Get data from intent
                    String verifier = data.getStringExtra(oAuthVerifier);

                    try {
                        accessToken = twitter.getOAuthAccessToken(requestToken, verifier); //
                        mHandler.sendEmptyMessage(0);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }

    //Get access to elements
    private void findViewById() {
        leaderBoardDataBase = new LeaderBoardDataBase(this);
        tvGameOver = findViewById(R.id.tvGameOver);
        tvPlayerScore = findViewById(R.id.tvPlayerScore);
        btnTryAgain = findViewById(R.id.btn_try_again);
        btnHome = findViewById(R.id.btn_home);
        edtName = findViewById(R.id.edit_playerName);
        btnEnter = findViewById(R.id.btn_enter);
        btnTryAgain.setOnClickListener(this);
        btnHome.setOnClickListener(this);
        btnEnter.setOnClickListener(this);
        btnEnter.setOnClickListener(this);
    }

    //Set text font
    private void setTypeFace() {
        Typeface typeface = Typeface.createFromAsset(getAssets(), FONT_PATH);
        tvGameOver.setTypeface(typeface);
        tvPlayerScore.setTypeface(typeface);
        btnTryAgain.setTypeface(typeface);
        btnHome.setTypeface(typeface);
        btnEnter.setTypeface(typeface);
        edtName.setTypeface(typeface);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_try_again:
                clickTryAgain();
                break;

            case R.id.btn_home:
                clickHome();
                break;

            case R.id.btn_enter:
                clickEnter();
                break;

            default:
                break;
        }
    }


    private void clickEnter() {
        String playerName = edtName.getText().toString();
        playerName = leaderBoardDataBase.capitalizeString(playerName);

        //Check the existence of score and anme
        boolean existed = leaderBoardDataBase.isRecordExisted(playerName);

        //Add score depending on specified conditions
        if (existed) { //Check if record existed
            Score recordedScore = leaderBoardDataBase.getScoreRecord(playerName);
            if (recordedScore.getScore() != score) {
                leaderBoardDataBase.addScoreRecord(new Score(1, playerName, score));
            }
        } else {
            leaderBoardDataBase.addScoreRecord(new Score(1, playerName, score));
        }

        Toast.makeText(this, "Your score have been saved to the leaderboard", Toast.LENGTH_SHORT).show();
    }


    private void clickTryAgain() {
        Intent intent = new Intent(GameOverActivity.this, PlayActivity.class);
        startActivity(intent);
        finish();
    }

    private void clickHome() {
        finish();
    }


    //Reading titter essential configuration parameters
    private void twitterConfigs() {
        consumerKey = getString(R.string.twitter_consumer_key);
        consumerSecretKey = getString(R.string.twitter_consumer_secret);
        callbackUrl = getString(R.string.twitter_callback_url);
        oAuthVerifier = getString(R.string.twitter_oauth_verifier);

    }

    private void saveScoreInfo() {
        SharedPreferences sharedPreferences = getSharedPreferences(GameOverActivity.PREF_FILE, 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("PREVIOUS_Score", score);
        editor.apply();
    }

    public void loginIntoTwitter() {

        //Set confirguration builer
        final ConfigurationBuilder builder = new ConfigurationBuilder();
        builder.setOAuthConsumerKey(consumerKey);
        builder.setOAuthConsumerSecret(consumerSecretKey);

        final twitter4j.conf.Configuration configuration = builder.build();
        final TwitterFactory twitterFactory = new TwitterFactory(configuration);
        twitter = twitterFactory.getInstance();

        try {
            //Get request token
            requestToken = twitter.getOAuthRequestToken(callbackUrl);

            //Launch Authentication Activity
            Intent inten = new Intent(this, AuthenticationActivity.class);
            inten.putExtra(AuthenticationActivity.AUTHENTICATION_URL, requestToken.getAuthenticationURL());
            startActivityForResult(inten, AUTHENTICATE);
        } catch (TwitterException e) {
            e.printStackTrace();
        }
    }

    @SuppressLint("StaticFieldLeak")
    class updateTwitterStatus extends AsyncTask<AccessToken, String, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            //Set up progress dialog
            pDialog = new ProgressDialog(GameOverActivity.this);
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override

        protected Void doInBackground(AccessToken... accessTokens) {
            AccessToken accessToken = accessTokens[0];

            try {
                twitter.setOAuthAccessToken(accessToken);

                //Message and image file
                String message = "I have achived a score of " + String.valueOf(score) + " in 'SumUp!'. Come and challenge me! :p";
                InputStream inputStream = getResources().openRawResource(+R.drawable.logo_app);

                //Set tweet
                StatusUpdate statusUpdate = new StatusUpdate(message);
                statusUpdate.setMedia("app_image_media.png", inputStream); //set status image
                twitter4j.Status status = twitter.updateStatus(statusUpdate); //tweet

                Log.d("Tweet status", status.toString());
            } catch (TwitterException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            pDialog.dismiss();
            Toast.makeText(GameOverActivity.this, "Tweet sent", Toast.LENGTH_LONG).show();
        }
    }


}
