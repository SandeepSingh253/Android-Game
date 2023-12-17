package com.team.classicrealm.Bounce.Online;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.team.classicrealm.GameUtility.Constants;

public class BounceGame extends AppCompatActivity {

    private String roomCode;
    BounceGameView gameView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gameView = new BounceGameView(this);
        setContentView(gameView);
        Intent i=getIntent();
        roomCode=i.getStringExtra(Constants.INTENT_KEY_ROOM_CODE_STRING);
        getRoomReference(roomCode).child(Constants.DATABASE_CHILD_PLAYER_DISCONNECT).onDisconnect().setValue(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P)
            getWindow().getAttributes().layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
        hideSystemUI();
        this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    public DatabaseReference getRoomReference(String roomCode){
        return FirebaseDatabase.getInstance().getReference().child(roomCode);
    }

    @Override
    protected void onStop() {
        super.onStop();
        getRoomReference(roomCode).removeEventListener(gameView.eventListener);
        if(!gameView.finishedSelf)getRoomReference(roomCode).child(Constants.DATABASE_CHILD_PLAYER_DISCONNECT).setValue(true);
        getRoomReference(roomCode).child(Constants.DATABASE_CHILD_PLAYER_DISCONNECT).onDisconnect().cancel();
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