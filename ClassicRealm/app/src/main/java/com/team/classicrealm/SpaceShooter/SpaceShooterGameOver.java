package com.team.classicrealm.SpaceShooter;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.team.classicrealm.GameUtility.Constants;
import com.team.classicrealm.GameUtility.MusicManager;
import com.team.classicrealm.GamesScreen.GamesScreen;
import com.team.classicrealm.MainScreen.MainMenu;
import com.team.classicrealm.R;
import com.team.classicrealm.ScoreBoard.UpdateScore;

public class SpaceShooterGameOver extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_space_shooter_game_over);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P)
            getWindow().getAttributes().layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
        hideSystemUI();
        MusicManager.getInstance().play(getApplicationContext(),R.raw.gameover_sound);
        ImageButton restart = findViewById(R.id.restartSpaceShooter);
        ImageButton back=findViewById(R.id.ssBackToMenu);
        int score=getIntent().getIntExtra(Constants.INTENT_KEY_SCORE,Constants.DEFAULT_VALUE_SCORE);
        ((TextView)findViewById(R.id.ssGOscore)).setText("SCORE: "+score);
        UpdateScore.updateUserScore(score,this);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MusicManager.getInstance().play(getApplicationContext(),R.raw.button_sound);
                ((ImageButton)view).setImageDrawable(getDrawable(R.drawable.button_back_pressed));
                Intent i=new Intent(getApplicationContext(), GamesScreen.class);
                startActivity(i);
                finish();
            }
        });

        restart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MusicManager.getInstance().play(getApplicationContext(),R.raw.button_sound);
                ((ImageButton)view).setImageDrawable(getDrawable(R.drawable.button_restart_pressed));
                Intent i=new Intent(getApplicationContext(), SpaceShooterActivity.class);
                startActivity(i);
                finish();
            }
        });
    }
    private void hideSystemUI() {
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN);

    }

    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            hideSystemUI();
        }
    }
}