package com.team.classicrealm.Bounce.Online;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.team.classicrealm.Bounce.BounceMenu;
import com.team.classicrealm.GameUtility.Constants;
import com.team.classicrealm.GameUtility.MusicManager;
import com.team.classicrealm.GameUtility.NetworkUtil;
import com.team.classicrealm.GameUtility.Prompts;
import com.team.classicrealm.R;
import com.team.classicrealm.TicTacToe.Online.TicTacToeEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class BounceRoom extends AppCompatActivity {
    NetworkUtil networkUtil;
    EditText gameCodeEditText;
    Button hostButton,joinButton,bounceRoomBackButton;

    FirebaseDatabase database;
    private String userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bounce_room);
        userName=getSharedPreferences(Constants.PREF_USER,MODE_PRIVATE).getString(Constants.SHARED_PREF_KEY_USER_NAME,Constants.DEFAULT_VALUE_USER_NAME);
        hideSystemUI();
        initObjec();
        joinEvent();
        hostEvent();
        backButtonClickListener();
    }

    private void backButtonClickListener() {
        MusicManager.getInstance().play(getApplicationContext(),R.raw.button_sound);
        bounceRoomBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MusicManager.getInstance().play(getApplicationContext(),R.raw.button_sound);
                startActivity(new Intent(getApplicationContext(), BounceMenu.class));
                finish();
            }
        });
    }

    private void hostEvent() {

        hostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MusicManager.getInstance().play(getApplicationContext(),R.raw.button_sound);
                if(!networkUtil.isOnline()){
                    Toast.makeText(BounceRoom.this, Prompts.NO_INTERNET, Toast.LENGTH_SHORT).show();
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
                            Toast.makeText(getApplicationContext(), Prompts.RETRY, Toast.LENGTH_SHORT).show();
                        }else{
                            createGameStruct(roomCode);
                            Intent intent = new Intent(BounceRoom.this, BounceWaitingScreen.class);
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

    private void createGameStruct(String roomCode) {
        BounceEvent e=new BounceEvent((int)Constants.TENNIS_BALL_INITIAL_POS_PER,Constants.PLAYER_NUM_1,-1);
        e.setPlayerOneName(userName);
        e.setPlayerDisconnect(true);
        getRoomReference(roomCode).setValue(e);
    }

    private void joinEvent() {
        MusicManager.getInstance().play(getApplicationContext(),R.raw.button_sound);
        joinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!networkUtil.isOnline()){
                    Toast.makeText(BounceRoom.this, Prompts.NO_INTERNET, Toast.LENGTH_SHORT).show();
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
                                joinRoom(roomCode);
                                Intent intent = new Intent(BounceRoom.this, BounceGame.class);
                                intent.putExtra(Constants.INTENT_KEY_PLAYER_NUM,Constants.PLAYER_NUM_2);
                                intent.putExtra(Constants.INTENT_KEY_ROOM_CODE_STRING,roomCode);
                                startActivity(intent);
                                finish();
                            }else{
                                Toast.makeText(BounceRoom.this, Prompts.ENTER_VALID_CODE, Toast.LENGTH_SHORT).show();
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(BounceRoom.this, error.toString(), Toast.LENGTH_SHORT).show();
                        }

                    });
                }else {
                    Toast.makeText(BounceRoom.this, Prompts.ENTER_VALID_CODE, Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    private void joinRoom(String roomCode){
        DatabaseReference ref=getRoomReference(roomCode);
        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put(Constants.DATABASE_CHILD_PLAYER_COUNT,2);
        childUpdates.put(Constants.DATABASE_CHILD_PLAYER_DISCONNECT,false);
        childUpdates.put(Constants.DATABASE_CHILD_PLAYER_TWO_NAME,userName);
        ref.updateChildren(childUpdates);
    }

    private void initObjec() {
        gameCodeEditText = findViewById(R.id.tennisOnlineGameCodeET);
        joinButton = findViewById(R.id.tennisOnlineJoinB);
        hostButton = findViewById(R.id.tennisOnlineHostB);
        bounceRoomBackButton = findViewById(R.id.bounceRoomBackButton);
        networkUtil=new NetworkUtil(this);
        database=FirebaseDatabase.getInstance();
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
    private void hideSystemUI() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P)
            getWindow().getAttributes().layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            hideSystemUI();
        }
    }
}