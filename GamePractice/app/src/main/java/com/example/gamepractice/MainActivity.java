package com.example.gamepractice;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    Button startGAmeButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startGAmeButton=findViewById(R.id.game);
    }
    public void startGame(View v){
        Intent intent=new Intent(this,GameStart.class);
        startActivity(intent);
    }
}