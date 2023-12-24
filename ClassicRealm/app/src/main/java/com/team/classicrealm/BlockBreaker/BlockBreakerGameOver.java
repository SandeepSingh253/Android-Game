package com.team.classicrealm.BlockBreaker;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.team.classicrealm.GameUtility.Constants;
import com.team.classicrealm.GameUtility.MusicManager;
import com.team.classicrealm.GamesScreen.GamesScreen;
import com.team.classicrealm.R;
import com.team.classicrealm.ScoreBoard.UpdateScore;

public class BlockBreakerGameOver extends AppCompatActivity {

    TextView tvPoints;
    ImageView ivNewHighest;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_block_breaker_game_over);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P)
            getWindow().getAttributes().layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
        hideSystemUI();
        MusicManager.getInstance().play(getApplicationContext(),R.raw.gameover_sound);
        tvPoints = findViewById(R.id.blockBreakerGOScoreNum);
        int points = getIntent().getExtras().getInt(Constants.INTENT_KEY_SCORE);
        tvPoints.setText(""+points);
        UpdateScore.updateUserScore(points,this);
    }
    public void restart(View view) {
        MusicManager.getInstance().play(getApplicationContext(),R.raw.button_sound);
        ((ImageButton)view).setImageDrawable(getDrawable(R.drawable.button_restart_pressed));
        Intent intent = new Intent(BlockBreakerGameOver.this, BlockBreakerGame.class);
        startActivity(intent);
        finish();
    }

    public void exit(View view) {
        MusicManager.getInstance().play(getApplicationContext(),R.raw.button_sound);
        ((ImageButton)view).setImageDrawable(getDrawable(R.drawable.button_back_pressed));
        Intent intent = new Intent(BlockBreakerGameOver.this, GamesScreen.class);
        startActivity(intent);
        finish();
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
