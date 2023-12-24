package com.team.classicrealm.Bounce.Online;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;

import com.team.classicrealm.Bounce.BounceMenu;
import com.team.classicrealm.GameUtility.Constants;
import com.team.classicrealm.GameUtility.MusicManager;
import com.team.classicrealm.GamesScreen.GamesScreen;
import com.team.classicrealm.R;
import com.team.classicrealm.ScoreBoard.UpdateScore;

public class BounceGameOver extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bounce_game_over);
        ImageButton bounceGameoverRestartB=findViewById(R.id.bounceGameoverRestartB);
        ImageButton bounceGameoverBackB=findViewById(R.id.bounceGameoverBackB);
        TextView winnerText=findViewById(R.id.bouncePlayerWonText);
        hideSystemUI();
        MusicManager.getInstance().play(getApplicationContext(),R.raw.gameover_sound);
        Intent i=getIntent();
        String winner=i.getStringExtra(Constants.INTENT_KEY_PLAYER_WON_NAME);
        winnerText.setText(winner+" Won!!");
        String thisPlayer=getSharedPreferences(Constants.PREF_USER,MODE_PRIVATE).getString(Constants.SHARED_PREF_KEY_USER_NAME,Constants.DEFAULT_VALUE_USER_NAME);
        if(thisPlayer.equals(winner)){
            UpdateScore.updateUserScore(Constants.SCORES_BOUNCE,this);
        }
        bounceGameoverRestartB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MusicManager.getInstance().play(getApplicationContext(),R.raw.button_sound);
                ((ImageButton)view).setImageDrawable(getDrawable(R.drawable.button_restart_pressed));
                Intent i=new Intent(getApplicationContext(), BounceRoom.class);
                startActivity(i);
                finish();
            }
        });
        bounceGameoverBackB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MusicManager.getInstance().play(getApplicationContext(),R.raw.button_sound);
                ((ImageButton)view).setImageDrawable(getDrawable(R.drawable.button_back_pressed));
                Intent i=new Intent(getApplicationContext(), GamesScreen.class);
                finish();
                startActivity(i);
            }
        });

    }
    private void hideSystemUI() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P)
            getWindow().getAttributes().layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            hideSystemUI();
        }
    }
}