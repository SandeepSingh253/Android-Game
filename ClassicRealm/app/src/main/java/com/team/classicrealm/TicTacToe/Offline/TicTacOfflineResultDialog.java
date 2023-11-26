package com.team.classicrealm.TicTacToe.Offline;

import androidx.annotation.NonNull;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.team.classicrealm.R;

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
        messageText.setText(winner);
        startAgainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                game.restartMatch();
                dismiss();
            }
        });
    }
}