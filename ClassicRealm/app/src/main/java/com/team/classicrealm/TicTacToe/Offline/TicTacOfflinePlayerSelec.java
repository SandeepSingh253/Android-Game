package com.team.classicrealm.TicTacToe.Offline;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.team.classicrealm.GameUtility.Constants;
import com.team.classicrealm.GameUtility.MusicManager;
import com.team.classicrealm.GameUtility.Prompts;
import com.team.classicrealm.R;
import com.team.classicrealm.TicTacToe.TicTacToeMenu;

public class TicTacOfflinePlayerSelec extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tic_tac_offline_player_selec);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P)
            getWindow().getAttributes().layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
        hideSystemUI();
        EditText playerOne = findViewById(R.id.playerOne);
        EditText playerTwo = findViewById(R.id.playerTwo);
        Button startGameButton = findViewById(R.id.ticTacOfflineStartGameButton);
        Button ticTacOfflinePlayerSlecBackB = findViewById(R.id.ticTacOfflinePlayerSlecBackB);

        ticTacOfflinePlayerSlecBackB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MusicManager.getInstance().play(getApplicationContext(),R.raw.button_sound);
                Intent intent = new Intent(TicTacOfflinePlayerSelec.this, TicTacToeMenu.class);
                startActivity(intent);
                finish();
            }
        });
        startGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MusicManager.getInstance().play(getApplicationContext(),R.raw.button_sound);
                String getPlayerOneName = playerOne.getText().toString();
                String getPlayerTwoName = playerTwo.getText().toString();
                if (getPlayerOneName.isEmpty() || getPlayerTwoName.isEmpty()) {
                    Toast.makeText(TicTacOfflinePlayerSelec.this, Prompts.EMPTY_NAME_FEILD, Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(TicTacOfflinePlayerSelec.this, TicTacOffline.class);
                    intent.putExtra(Constants.INTENT_KEY_ROOM_PLAYER_ONE, getPlayerOneName);
                    intent.putExtra(Constants.INTENT_KEY_ROOM_PLAYER_TWO, getPlayerTwoName);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }

    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            hideSystemUI();
        }
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
}