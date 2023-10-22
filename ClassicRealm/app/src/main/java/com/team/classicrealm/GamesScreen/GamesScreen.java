package com.team.classicrealm.GamesScreen;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;

import com.team.classicrealm.R;
import com.team.classicrealm.SpaceShooter.SpaceShooterActivity;

import java.util.ArrayList;

public class GamesScreen extends AppCompatActivity implements RecycleViewInterface{
    ArrayList<GameModel> Games=new ArrayList<>();
    RecyclerView rv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_games_screen);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        initGamesArray();
        rv=findViewById(R.id.rv);
        GameAdapter adapter= new GameAdapter(this,Games, this);
        rv.setAdapter(adapter);
        rv.setLayoutManager(new LinearLayoutManager(this));
    }
    private void initGamesArray() {
        GameModel game1=new GameModel( R.drawable.gamesscreen_spaceshooter);

        Games.add(game1);

    }

    @Override
    public void onClickItem(int pos) {
        Intent i;
        switch(pos){
            case 0 : i=new Intent(getApplicationContext(), SpaceShooterActivity.class);
                startActivity(i);
                return;
            default: return;
        }
    }
}