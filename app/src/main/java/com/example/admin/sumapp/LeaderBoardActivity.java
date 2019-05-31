package com.example.admin.sumapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.GridLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LeaderBoardActivity extends AppCompatActivity {

    private GridLayout leaderboardLayout;
    private LeaderBoardDataBase leaderBoardDataBase;
    private TextView textViewId, textViewName, textViewScore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_leader_board);

        //Variable Assignement
        leaderboardLayout = findViewById(R.id.leaderboard_layout);
        leaderBoardDataBase = new LeaderBoardDataBase(this);
        //Process
        displayScoreRecords(leaderBoardDataBase.getScoreRecords());
    }

    private void displayScoreRecords(List<Score> records) {
        List<Score> sortedRecords = sortScoreRecords(records); //Sort in descending order

        for (int i = -1; i < sortedRecords.size(); i++) {
            //Set textViews
            setTextViews();

            //Get text
            String id, name, score;
            if (i == -1) {  //Set Title
                textViewId.setPadding(100, 100, 50, 50);
                textViewName.setPadding(100, 80, 50, 50);
                textViewScore.setPadding(100, 80, 50, 50);
                id = "ID";
                name = "PLAYER";
                score = "SCORE";
            } else {
                Score record = sortedRecords.get(i);

                id = (i + 1) + ". ";
                name = record.getPlayerName();
                score = String.valueOf(record.getScore());
            }

            //Set Text
            textViewId.setText(id);
            textViewName.setText(name);
            textViewScore.setText(score);

            //Gridlayout add view
            leaderboardLayout.addView(textViewId);
            leaderboardLayout.addView(textViewName);
            leaderboardLayout.addView(textViewScore);
        }
    }

    private List<Score> sortScoreRecords(List<Score> records) {

        List<Integer> scorelist = new ArrayList<>();
        List<Score> newRecords = new ArrayList<>();

        for (Score record : records) {
            scorelist.add(record.getScore());
        }

        Collections.sort(scorelist); //List of sorted scores
        Collections.reverse(scorelist); //List of reverse sorted scores

        int playerScore;
        for (int score : scorelist) {
            for (Score record : records) {
                playerScore = record.getScore();

                if (playerScore == score) {
                    newRecords.add(record);
                }
            }
        }

        return newRecords;
    }

    private void setTextViews() {
        textViewId = new TextView(this);
        textViewName = new TextView(this);
        textViewScore = new TextView(this);
        textViewId.setGravity(Gravity.CENTER_VERTICAL);
        textViewName.setGravity(Gravity.CENTER);
        textViewScore.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        textViewId.setTextAppearance(R.style.TextAppearance_AppCompat_Medium);
        textViewName.setTextAppearance(R.style.TextAppearance_AppCompat_Medium);
        textViewScore.setTextAppearance(R.style.TextAppearance_AppCompat_Medium);
        textViewId.setPadding(100, 50, 50, 50);
        textViewName.setPadding(50, 50, 130, 50);
        textViewScore.setPadding(100, 50, 100, 50);
        textViewId.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        textViewName.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        textViewScore.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
    }

    public void test() { //This method is for testing purposes
        Score score1 = new Score(1, "Stacey", 7);
        check(score1);

        Score score2 = new Score(2, "James", 3);
        check(score2);

    }

    public void check(Score score) { //This method is for testing purposes
        boolean exist = leaderBoardDataBase.isRecordExisted(score.getPlayerName());

        if (exist) { //Check if record existed
            Score recordedScore = leaderBoardDataBase.getScoreRecord(score.getPlayerName());
            if (recordedScore.getScore() != score.getScore()) {
                leaderBoardDataBase.addScoreRecord(score);
            }
        } else {
            leaderBoardDataBase.addScoreRecord(score);
        }
    }
}
