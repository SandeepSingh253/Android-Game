package com.team.classicrealm.TicTacToe.Online;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.team.classicrealm.GameUtility.Constants;
import com.team.classicrealm.R;
import com.team.classicrealm.ScoreBoard.UpdateScore;

public class TicTacOnlineResultDialog extends Dialog {
    private  String winner;
    private  TicTacOnline game;
    private  String roomCode;
    private  boolean thisPlayerWon;
    private  Context context;

    public TicTacOnlineResultDialog(@NonNull Context context, String winner, TicTacOnline game, String roomCode,boolean thisPlayerWon) {
        super(context);
        this.context=context;
        this.winner = winner;
        this.game = game;
        this.roomCode=roomCode;
        this.thisPlayerWon=thisPlayerWon;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tic_tac_online_result_dialog);
        TextView messageText = findViewById(R.id.messageText);
        Button startAgainButton = findViewById(R.id.tttOnlineStartAgainButton);
        Button exit = findViewById(R.id.tttOnlineExitButton);
        messageText.setText(winner);
        if(thisPlayerWon){
            UpdateScore.updateUserScore(Constants.SCORES_TIC_TAC_TOE,context);
        }
        startAgainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getRoomReference(roomCode).child(Constants.DATABASE_CHILD_RESTART_GAME).setValue(true);
                game.restartMatch();
                dismiss();
            }
        });

        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getRoomReference(roomCode).child(Constants.DATABASE_CHILD_PLAYER_DISCONNECT).setValue(true);
                Intent i=new Intent(game.getApplicationContext(), TicTacOnlinePlayerSelec.class);
                game.startActivity(i);
                dismiss();
                game.finish();
            }
        });


        getRoomReference(roomCode).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                TicTacToeEvent e =snapshot.getValue(TicTacToeEvent.class);
                if(e.getRestartGame()){
                    getRoomReference(roomCode).child(Constants.DATABASE_CHILD_RESTART_GAME).setValue(false);
                    game.restartMatch();
                    dismiss();
                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(game, error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public DatabaseReference getRoomReference(String roomCode){
        return FirebaseDatabase.getInstance().getReference().child(roomCode);
    }
}