package com.team.classicrealm.TicTacToe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;

import com.team.classicrealm.GameUtility.MusicManager;
import com.team.classicrealm.GamesScreen.GamesScreen;
import com.team.classicrealm.R;
import com.team.classicrealm.TicTacToe.Offline.TicTacOfflinePlayerSelec;
import com.team.classicrealm.TicTacToe.Online.TicTacOnlineRoom;

public class TicTacToeMenu extends AppCompatActivity {

    ImageButton offlineStart, onlineStartButton,backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tic_tac_toe_menu);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P)
            getWindow().getAttributes().layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
        hideSystemUI();
        offlineStart=findViewById(R.id.tttOfflineStartButton);
        onlineStartButton=findViewById(R.id.tttOnlineStartButton);
        backButton=findViewById(R.id.tttOnlineBackButton);

        offlineStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MusicManager.getInstance().play(getApplicationContext(),R.raw.button_sound);
                ((ImageButton)view).setImageDrawable(getDrawable(R.drawable.button_offline_pressed));
                Intent i= new Intent(getApplicationContext(), TicTacOfflinePlayerSelec.class);
                startActivity(i);
                finish();
            }
        });

        onlineStartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MusicManager.getInstance().play(getApplicationContext(),R.raw.button_sound);
                ((ImageButton)view).setImageDrawable(getDrawable(R.drawable.button_online_pressed));
                Intent i= new Intent(getApplicationContext(), TicTacOnlineRoom.class);
                startActivity(i);
                finish();
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MusicManager.getInstance().play(getApplicationContext(),R.raw.button_sound);
                ((ImageButton)view).setImageDrawable(getDrawable(R.drawable.button_back_pressed));
                Intent i= new Intent(getApplicationContext(), GamesScreen.class);
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