package com.team.classicrealm.ScoreBoard;

import androidx.annotation.NonNull;

public class Scores implements Comparable<Scores>{
    String uName;
    int score;

    public Scores() {
    }

    public Scores(String uName, int score) {
        this.uName = uName;
        this.score = score;
    }

    public String getuName() {
        return uName;
    }

    public void setuName(String uName) {
        this.uName = uName;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    @NonNull
    @Override
    public String toString() {
        return uName+" "+score;
    }

    @Override
    public int compareTo(Scores scores) {
        return scores.getScore()-score;
    }
}
