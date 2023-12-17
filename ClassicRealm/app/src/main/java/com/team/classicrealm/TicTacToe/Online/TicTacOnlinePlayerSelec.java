package com.team.classicrealm.TicTacToe.Online;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.team.classicrealm.GameUtility.Constants;
import com.team.classicrealm.GameUtility.NetworkUtil;
import com.team.classicrealm.GameUtility.Warnings;
import com.team.classicrealm.R;
import com.team.classicrealm.TicTacToe.Offline.TicTacOfflinePlayerSelec;
import com.team.classicrealm.TicTacToe.TicTacToeMenu;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class TicTacOnlinePlayerSelec extends AppCompatActivity {
    private FirebaseDatabase database;
    private String userName;

    private NetworkUtil networkUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tic_tac_online_player_selection);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P)
            getWindow().getAttributes().layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
        hideSystemUI();

        userName=getSharedPreferences(Constants.PREF_USER,MODE_PRIVATE).getString(Constants.SHARED_PREF_KEY_USER_NAME,Constants.DEFAULT_VALUE_USER_NAME);

        database = FirebaseDatabase.getInstance();

        EditText gameCodeEditText = findViewById(R.id.gameCodeEditText);
        Button joinButton = findViewById(R.id.tttOnlineJoinGameButton);
        Button hostButton = findViewById(R.id.tttOnlineHostGameButton);
        Button back = findViewById(R.id.tttOnlineRoomBackButton);
        networkUtil=new NetworkUtil(this);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), TicTacToeMenu.class);
                startActivity(intent);
                finish();
            }
        });
        joinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!networkUtil.isOnline()){
                    Toast.makeText(TicTacOnlinePlayerSelec.this, Warnings.NO_INTERNET, Toast.LENGTH_SHORT).show();
                    return;
                }
                String roomCode=gameCodeEditText.getText().toString();
                if(!roomCode.isBlank()) {
                    DatabaseReference rootRef = getRoomReference(roomCode);
                    rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot snapshot) {
                            if(snapshot.exists()){
                                TicTacToeEvent event=snapshot.getValue(TicTacToeEvent.class);
                                if((event.getPlayerCount()==1)){
                                    joinRoom(roomCode);
                                    Intent intent = new Intent(TicTacOnlinePlayerSelec.this, TicTacOnline.class);
                                    intent.putExtra(Constants.INTENT_KEY_PLAYER_NUM,Constants.PLAYER_NUM_2);
                                    intent.putExtra(Constants.INTENT_KEY_OPPONENT_NAME,event.getPlayerOneName());
                                    intent.putExtra(Constants.INTENT_KEY_ROOM_CODE_STRING,roomCode);
                                    startActivity(intent);
                                    finish();
                                }else {
                                    Toast.makeText(TicTacOnlinePlayerSelec.this, Warnings.ENTER_VALID_CODE, Toast.LENGTH_SHORT).show();
                                }
                            }else{
                                Toast.makeText(TicTacOnlinePlayerSelec.this, Warnings.ENTER_VALID_CODE, Toast.LENGTH_SHORT).show();
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(TicTacOnlinePlayerSelec.this, error.toString(), Toast.LENGTH_SHORT).show();
                        }

                    });
                }else {
                    Toast.makeText(TicTacOnlinePlayerSelec.this, Warnings.ENTER_VALID_CODE, Toast.LENGTH_LONG).show();
                }
            }
        });

        hostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!networkUtil.isOnline()){
                    Toast.makeText(TicTacOnlinePlayerSelec.this, Warnings.NO_INTERNET, Toast.LENGTH_SHORT).show();
                    return;
                }
                String roomCode;
                roomCode=generateRandomCode(Constants.ROOM_CODE_SIZE);

                //check if room already exist with that code
                DatabaseReference rootRef = getRoomReference(roomCode);
                rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            Toast.makeText(getApplicationContext(), Warnings.RETRY, Toast.LENGTH_SHORT).show();
                        }else{
                            //make a tic tac toe structure in database
                            createTTTStruc(roomCode);
                            Intent intent = new Intent(TicTacOnlinePlayerSelec.this, TicTacOnlineWaitingRoom.class);
                            intent.putExtra(Constants.INTENT_KEY_ROOM_CODE_STRING,roomCode);
                            startActivity(intent);
                            finish();
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
                    }

                });
            }
        });
    }



    private void createTTTStruc(String roomCode) {
        DatabaseReference roomRef=getRoomReference(roomCode);
        roomRef.child(Constants.DATABASE_CHILD_PLAYER_COUNT).setValue(1);
        roomRef.child(Constants.DATABASE_CHILD_PLAYER_DISCONNECT).setValue(true);
        roomRef.child(Constants.DATABASE_CHILD_IS_A_MOVE).setValue(false);
        roomRef.child(Constants.DATABASE_CHILD_PLAYER_NUM).setValue(-1);
        roomRef.child(Constants.DATABASE_CHILD_MOVE_MADE).setValue(-1);
        roomRef.child(Constants.DATABASE_CHILD_RESTART_GAME).setValue(false);
        roomRef.child(Constants.DATABASE_CHILD_PLAYER_ONE_NAME).setValue(userName);

    }

    private void joinRoom(String roomCode){
        DatabaseReference ref=getRoomReference(roomCode);
        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put(Constants.DATABASE_CHILD_PLAYER_COUNT,2);
        childUpdates.put(Constants.DATABASE_CHILD_PLAYER_DISCONNECT,false);
        childUpdates.put(Constants.DATABASE_CHILD_IS_A_MOVE,false);
        childUpdates.put(Constants.DATABASE_CHILD_PLAYER_TWO_NAME,userName);
        ref.updateChildren(childUpdates);
    }

    private DatabaseReference getRoomReference(String roomCode){
        return database.getReference().child(roomCode);
    }
    private String generateRandomCode(int length) {
        String characters =Constants.ROOM_CODE_ELEMENTS;
        StringBuilder randomCode = new StringBuilder();

        Random random = new Random();
        for (int i = 0; i < length; i++) {
            int index = random.nextInt(characters.length());
            randomCode.append(characters.charAt(index));
        }

        return randomCode.toString();
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