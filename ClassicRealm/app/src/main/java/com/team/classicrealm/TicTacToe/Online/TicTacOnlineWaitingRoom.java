package com.team.classicrealm.TicTacToe.Online;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.team.classicrealm.GameUtility.Constants;
import com.team.classicrealm.GameUtility.MusicManager;
import com.team.classicrealm.R;

public class TicTacOnlineWaitingRoom extends AppCompatActivity {

    String roomCode;

    ValueEventListener eventListener;

    DatabaseReference roomRef;

    boolean joined=false;

    FirebaseDatabase database=FirebaseDatabase.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tic_tac_online_waiting_room);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P)
            getWindow().getAttributes().layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
        hideSystemUI();

        Intent i= getIntent();
        roomCode=i.getStringExtra(Constants.INTENT_KEY_ROOM_CODE_STRING);

        ImageButton back=findViewById(R.id.tttWaitingRoomBackButton);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MusicManager.getInstance().play(getApplicationContext(),R.raw.button_sound);
                Intent i= new Intent(getApplicationContext(), TicTacOnlineRoom.class);
                startActivity(i);
                finish();
            }
        });

        TextView tttRoomCodeTextView= findViewById(R.id.tttRoomCodeTextView);
        tttRoomCodeTextView.setText(roomCode);
        roomRef=getRoomReference(roomCode);


        eventListener=roomRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                TicTacToeEvent event = snapshot.getValue(TicTacToeEvent.class);
                if (event.getPlayerCount() == 2 && !event.getIsAMove()) {
                    joined=true;
                    Intent i = new Intent(getApplicationContext(), TicTacOnline.class);
                    i.putExtra(Constants.INTENT_KEY_PLAYER_NUM, Constants.PLAYER_NUM_1);
                    i.putExtra(Constants.INTENT_KEY_OPPONENT_NAME,event.getPlayerTwoName());
                    i.putExtra(Constants.INTENT_KEY_ROOM_CODE_STRING, roomCode);
                    roomRef.removeEventListener(eventListener);
                    startActivity(i);
                    finish();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(TicTacOnlineWaitingRoom.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public DatabaseReference getRoomReference(String roomCode){
        return database.getReference().child(roomCode);
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

    @Override
    protected void onStop() {
        super.onStop();
        if(!joined){
            getRoomReference(roomCode).removeEventListener(eventListener);
            getRoomReference(roomCode).removeValue();
        }
    }
}