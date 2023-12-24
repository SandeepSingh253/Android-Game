package com.team.classicrealm.BlockBreaker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;

import com.team.classicrealm.GameUtility.MusicManager;
import com.team.classicrealm.GamesScreen.GamesScreen;
import com.team.classicrealm.MainScreen.MainMenu;
import com.team.classicrealm.R;

public class BlockBreakerMenu extends AppCompatActivity {

    ImageButton backButton;
    ImageButton playButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_block_breaker_menu);
        initViews();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P)
            getWindow().getAttributes().layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
        hideSystemUI();
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MusicManager.getInstance().play(getApplicationContext(),R.raw.button_sound);
                ((ImageButton)view).setImageDrawable(getDrawable(R.drawable.button_play_pressed));
                Intent i=new Intent(getApplicationContext(), BlockBreakerGame.class);
                startActivity(i);
                finish();
            }
        });
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MusicManager.getInstance().play(getApplicationContext(),R.raw.button_sound);
                ((ImageButton)view).setImageDrawable(getDrawable(R.drawable.button_back_pressed));
                Intent i=new Intent(getApplicationContext(), GamesScreen.class);
                startActivity(i);
                finish();
            }
        });
    }

    private void initViews() {
        playButton=findViewById(R.id.blockBreakerPlayButton);
        backButton=findViewById(R.id.blockBreakerBackButton);
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