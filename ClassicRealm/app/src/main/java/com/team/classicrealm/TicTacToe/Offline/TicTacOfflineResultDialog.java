package com.team.classicrealm.TicTacToe.Offline;

import androidx.annotation.NonNull;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.team.classicrealm.GameUtility.MusicManager;
import com.team.classicrealm.GamesScreen.GamesScreen;
import com.team.classicrealm.R;
import com.team.classicrealm.TicTacToe.TicTacToeMenu;

public class TicTacOfflineResultDialog extends Dialog {
    private final String winner;
    private final TicTacOffline game;
    public TicTacOfflineResultDialog(@NonNull Context context, String winner, TicTacOffline game) {
        super(context);
        this.winner = winner;
        this.game = game;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tic_tac_offline_result_dialog);
        TextView messageText = findViewById(R.id.messageText);
        Button startAgainButton = findViewById(R.id.startAgainButton);
        Button tttGameOverOfflineExitButton = findViewById(R.id.tttGameOverOfflineExitButton);
        messageText.setText(winner);
        tttGameOverOfflineExitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MusicManager.getInstance().play(game,R.raw.button_sound);
                game.startActivity(new Intent(game, GamesScreen.class));
                game.finish();
                dismiss();
            }
        });
        startAgainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MusicManager.getInstance().play(game,R.raw.button_sound);
                game.restartMatch();
                dismiss();
            }
        });
    }
}