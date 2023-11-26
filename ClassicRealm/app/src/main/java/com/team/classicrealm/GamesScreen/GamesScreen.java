package com.team.classicrealm.GamesScreen;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import com.team.classicrealm.BlockBreaker.BlockBreakerMenu;
import com.team.classicrealm.MainScreen.MainMenu;
import com.team.classicrealm.R;
import com.team.classicrealm.SpaceShooter.SpaceShooterMenu;
import com.team.classicrealm.TicTacToe.TicTacToeMenu;

import java.util.ArrayList;

public class GamesScreen extends AppCompatActivity implements RecycleViewInterface{
    ArrayList<GameModel> Games=new ArrayList<>();
    RecyclerView rv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_games_screen);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P)
            getWindow().getAttributes().layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
        hideSystemUI();

        findViewById(R.id.gamesScreenBackButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                startActivity(new Intent(getApplicationContext(),MainMenu.class));
            }
        });

        initGamesArray();
        rv=findViewById(R.id.rv);
        GameAdapter adapter= new GameAdapter(this,Games, this);
        rv.setAdapter(adapter);
        rv.setLayoutManager(new LinearLayoutManager(this));
    }
    private void initGamesArray() {
        GameModel spaceShooter=new GameModel( R.drawable.gamesscreen_spaceshooter);
        GameModel ticTacToe=new GameModel( R.drawable.gamesscreen_tictactoe);
        GameModel blockBreaker=new GameModel( R.drawable.gamescreen_blockbreaker);

        Games.add(spaceShooter);
        Games.add(ticTacToe);
        Games.add(blockBreaker);
    }

    @Override
    public void onClickItem(int pos) {
        Intent i;
        switch(pos){
            case 0 : i=new Intent(getApplicationContext(), SpaceShooterMenu.class);
                startActivity(i);
                return;
            case 1 : i=new Intent(getApplicationContext(), TicTacToeMenu.class);
                startActivity(i);
                return;
            case 2 : i=new Intent(getApplicationContext(), BlockBreakerMenu.class);
                startActivity(i);
                return;
            default: return;
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

    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            hideSystemUI();
        }
    }

}