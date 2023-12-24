package com.team.classicrealm.ScoreBoard;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.team.classicrealm.GameUtility.Constants;
import com.team.classicrealm.GameUtility.MusicManager;
import com.team.classicrealm.MainScreen.MainMenu;
import com.team.classicrealm.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EventListener;

public class ScoreBoard extends AppCompatActivity {

    FirebaseDatabase database;
    DatabaseReference scoresRef;
    ValueEventListener listener;
    String userName;
    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score_board);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P)
            getWindow().getAttributes().layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
        hideSystemUI();

        userName=getIntent().getStringExtra(Constants.INTENT_KEY_USER_NAME);

        database=FirebaseDatabase.getInstance();
        ArrayList<Scores> data=new ArrayList<>();
        RecyclerView scoreBoardRecyclerView=findViewById(R.id.scoreBoardRecyclerView);
        ScoreBoardAdapter adapter=new ScoreBoardAdapter(data);

        ImageButton scoreboardBackButton=findViewById(R.id.scoreboardBackButton);

        scoreboardBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MusicManager.getInstance().play(getApplicationContext(),R.raw.button_sound);
                ((ImageButton)view).setImageDrawable(getDrawable(R.drawable.button_back_pressed));
                Intent i=new Intent(getApplicationContext(), MainMenu.class);
                i.putExtra(Constants.INTENT_KEY_USER_NAME,userName);
                startActivity(i);
                finish();
            }
        });

        scoreBoardRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        scoreBoardRecyclerView.setAdapter(adapter);
        scoresRef=database.getReference().child(Constants.DATABASE_CHILD_SCORE_BOARD);
        sharedpreferences = getSharedPreferences(Constants.PREF_USER, MODE_PRIVATE);

        if(sharedpreferences.contains(Constants.SHARED_PREF_KEY_USER_SCORE)) {
            int score=sharedpreferences.getInt(Constants.SHARED_PREF_KEY_USER_SCORE,0);
            Scores scores=new Scores(userName,score);
            database.getReference().child(Constants.DATABASE_CHILD_SCORE_BOARD).child(userName).setValue(scores);
        }

        listener =scoresRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                data.clear();
                for(DataSnapshot snapshots : snapshot.getChildren()){
                    Scores score=snapshots.getValue(Scores.class);
                    data.add(score);
                }
                Collections.sort(data);
                //Collections.sort(data);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ScoreBoard.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        });


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        scoresRef.removeEventListener(listener);
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