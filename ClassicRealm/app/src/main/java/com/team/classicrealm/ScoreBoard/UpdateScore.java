package com.team.classicrealm.ScoreBoard;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.firebase.database.FirebaseDatabase;
import com.team.classicrealm.GameUtility.Constants;


public class UpdateScore {

    public static void updateUserScore( int score, Context context){
        FirebaseDatabase database=FirebaseDatabase.getInstance();

        SharedPreferences sharedpreferences = context.getSharedPreferences(Constants.PREF_USER, context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();

        String userName=sharedpreferences.getString(Constants.SHARED_PREF_KEY_USER_NAME,Constants.DEFAULT_VALUE_USER_NAME);

        int savedScore=0;
        if(!sharedpreferences.contains(Constants.SHARED_PREF_KEY_USER_SCORE)){
            setSPScore(score,editor);
        }else{
            savedScore=sharedpreferences.getInt(Constants.SHARED_PREF_KEY_USER_SCORE,Constants.DEFAULT_VALUE_SCORE);
            setSPScore(score+savedScore,editor);
        }
        Scores scores=new Scores(userName, score+savedScore);
        database.getReference().child(Constants.DATABASE_CHILD_SCORE_BOARD).child(userName).setValue(scores);
    }

    private static void setSPScore(int score,SharedPreferences.Editor editor){
        editor.putInt(Constants.SHARED_PREF_KEY_USER_SCORE, score);
        editor.apply();
    }
}
