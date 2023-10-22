package com.team.classicrealm.MainScreen;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;

import com.team.classicrealm.GamesScreen.GamesScreen;
import com.team.classicrealm.R;

public class MainMenu extends AppCompatActivity {

    MediaPlayer bgMusic;
    Button playButton, scoreButton;
    ImageButton playPauseMusicButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        initViews();
        startBGMusic();
    }

    private void startBGMusic() {
        bgMusic = MediaPlayer.create(getApplicationContext(), R.raw.mainscreenbackgroundaudio);
        bgMusic.setLooping(true);
        bgMusic.start();
    }

    private void  initViews(){
        scoreButton=findViewById(R.id.scoreButton);
        playButton=findViewById(R.id.playButton);
        playPauseMusicButton= findViewById(R.id.pausePlayMusicButton);
    }

    public void playButtonClickEvent(View view){
        Intent i = new Intent(getApplicationContext(), GamesScreen.class);
        startActivity(i);
    }

    public void playPauseMusic(View view){
        if (!bgMusic.isPlaying()) {
            bgMusic.start();
            playPauseMusicButton.setImageDrawable(getDrawable(R.drawable.mainmenu_playmusic));
        }else{
            bgMusic.pause();
            playPauseMusicButton.setImageDrawable(getDrawable(R.drawable.mainmenu_pausemusic));
        }
    }

    protected void onResume() {
        super.onResume();
        if (!bgMusic.isPlaying()) {
            bgMusic.start();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (bgMusic.isPlaying()) {
            bgMusic.pause();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (bgMusic.isPlaying()) {
            bgMusic.stop();
        }
        bgMusic.release();
    }
}