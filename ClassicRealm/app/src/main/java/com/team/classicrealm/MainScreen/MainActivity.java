package com.team.classicrealm.MainScreen;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.team.classicrealm.R;
import com.team.classicrealm.SpaceShooter.SpaceShooterActivity;

public class MainActivity extends AppCompatActivity {

    Button start;
    MediaPlayer bgMusic;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        start=findViewById(R.id.button);
        bgMusic = MediaPlayer.create(getApplicationContext(), R.raw.mainscreenbackgroundaudio);
        bgMusic.setLooping(true);
        bgMusic.start();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("message");

        myRef.setValue("Hello, World!").addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(MainActivity.this, "Done", Toast.LENGTH_SHORT).show();
                Log.d("","done");
            }
        });
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i= new Intent(getApplicationContext(),SpaceShooterActivity.class);
                startActivity(i);
            }
        });

    }

    protected void onResume() {
        super.onResume();
        if (!bgMusic.isPlaying()) {
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