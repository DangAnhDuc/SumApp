package com.example.admin.sumapp;

public class Score {
    private int id;
    private int score;
    private String playerName;

    public Score(int id, String playerName, int score) {
        this.id = id;
        this.score = score;
        this.playerName = playerName;
    }

    public int getScore() {
        return score;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
