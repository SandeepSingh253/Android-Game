package com.team.classicrealm.MainScreen;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.ValueAnimator;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.media.Image;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.team.classicrealm.GameUtility.Constants;
import com.team.classicrealm.GameUtility.MusicManager;
import com.team.classicrealm.GamesScreen.GamesScreen;
import com.team.classicrealm.R;
import com.team.classicrealm.ScoreBoard.ScoreBoard;

public class MainMenu extends AppCompatActivity {

    MediaPlayer bgMusic;
    ImageButton playButton,playPauseMusicButton,scoreButton;
    TextView userNameTV;
    private String userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P)
            getWindow().getAttributes().layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
        userName=getSharedPreferences(Constants.PREF_USER,MODE_PRIVATE).getString(Constants.SHARED_PREF_KEY_USER_NAME,Constants.DEFAULT_VALUE_USER_NAME);
        hideSystemUI();
        initViews();
        startBGAnim();
        startBGMusic();
    }

    private void startBGAnim() {
        AnimationDrawable animDrawable = (AnimationDrawable) findViewById(R.id.mainMenuRootLayout).getBackground();
        animDrawable.setEnterFadeDuration(10);
        animDrawable.setExitFadeDuration(5000);
        animDrawable.start();
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

    private void startBGMusic() {
        bgMusic = MediaPlayer.create(getApplicationContext(), R.raw.mainscreenbackgroundaudio);
        bgMusic.setLooping(true);
        bgMusic.start();
    }

    private void  initViews(){
        scoreButton=findViewById(R.id.scoreButton);
        playButton=findViewById(R.id.playButton);
        playPauseMusicButton= findViewById(R.id.pausePlayMusicButton);
        userNameTV=findViewById(R.id.mainMenuUserNameTV);
        userNameTV.setText(userName);

    }

    public void playButtonClickEvent(View view){
        MusicManager.getInstance().play(this,R.raw.button_sound);
        ((ImageButton)view).setImageDrawable(getDrawable(R.drawable.button_play_pressed));
        Intent i = new Intent(getApplicationContext(), GamesScreen.class);
        startActivity(i);
        finish();
    }

    public void scoreButtonClickEvent(View view){
        MusicManager.getInstance().play(this,R.raw.button_sound);
        ((ImageButton)view).setImageDrawable(getDrawable(R.drawable.button_score_pressed));
        Intent i = new Intent(getApplicationContext(), ScoreBoard.class);
        i.putExtra(Constants.INTENT_KEY_USER_NAME,userName);
        startActivity(i);
        finish();
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

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            hideSystemUI();
        }
    }
    protected void onResume() {
        super.onResume();
        hideSystemUI();
        if (!bgMusic.isPlaying()) {
            playPauseMusicButton.setImageDrawable(getDrawable(R.drawable.mainmenu_playmusic));
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