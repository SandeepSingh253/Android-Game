package com.team.classicrealm.TicTacToe.Online;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
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
import com.team.classicrealm.GameUtility.Prompts;
import com.team.classicrealm.R;
import com.team.classicrealm.databinding.ActivityTicTacOnlineBinding;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TicTacOnline extends AppCompatActivity {
    ActivityTicTacOnlineBinding binding;
    private final List<int[]> combinationList = new ArrayList<>();
    private int[] boxPositions = {0,0,0,0,0,0,0,0,0};
    private int playerTurn = Constants.PLAYER_NUM_1;

    public static final int PLAYER_NUM_1 = 1;
    public static final int PLAYER_NUM_2 = 2;

    private int thisPlayer;
    private String roomCode;
    private int totalSelectedBoxes = 1;

    ImageView[] gameBoardIcons;

    private boolean thisPlayerWon=false;

    public ValueEventListener eventListener;
    private FirebaseDatabase database=FirebaseDatabase.getInstance();
    private String userName;
    private String playerOneName;
    private String playerTwoName;
    private boolean finishedSelf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTicTacOnlineBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P)
            getWindow().getAttributes().layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
        hideSystemUI();

        gameBoardIcons = new ImageView[]{binding.image1,binding.image2,binding.image3,binding.image4,binding.image5,binding.image6,binding.image7,binding.image8,binding.image9};

        combinationList.add(new int[] {0,1,2});
        combinationList.add(new int[] {3,4,5});
        combinationList.add(new int[] {6,7,8});
        combinationList.add(new int[] {0,3,6});
        combinationList.add(new int[] {1,4,7});
        combinationList.add(new int[] {2,5,8});
        combinationList.add(new int[] {2,4,6});
        combinationList.add(new int[] {0,4,8});

        Intent i=getIntent();

        roomCode=i.getStringExtra(Constants.INTENT_KEY_ROOM_CODE_STRING);

        thisPlayer=i.getIntExtra(Constants.INTENT_KEY_PLAYER_NUM,Constants.PLAYER_NUM_1);
        changePlayerTurnTo(PLAYER_NUM_1);

        if(thisPlayer==Constants.PLAYER_NUM_1){
            playerOneName=getSharedPreferences(Constants.PREF_USER,MODE_PRIVATE).getString(Constants.SHARED_PREF_KEY_USER_NAME,Constants.DEFAULT_VALUE_USER_NAME);
            playerTwoName=getIntent().getStringExtra(Constants.INTENT_KEY_OPPONENT_NAME);
        }else{
            playerTwoName=getSharedPreferences(Constants.PREF_USER,MODE_PRIVATE).getString(Constants.SHARED_PREF_KEY_USER_NAME,Constants.DEFAULT_VALUE_USER_NAME);
            playerOneName=getIntent().getStringExtra(Constants.INTENT_KEY_OPPONENT_NAME);
        }

        binding.playerOneName.setText(playerOneName);
        binding.playerTwoName.setText(playerTwoName);

        getRoomReference(roomCode).child(Constants.DATABASE_CHILD_PLAYER_DISCONNECT).onDisconnect().setValue(true);

        eventListener=getRoomReference(roomCode).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                TicTacToeEvent e = snapshot.getValue(TicTacToeEvent.class);

                if (!e.isPlayerDisconnect()) {
                    if (e.getIsAMove()) {
                        if (e.getPlayerNum() == playerTurn && e.getPlayerNum()!=thisPlayer) {
                            MusicManager.getInstance().play(getApplicationContext(),R.raw.ttt_x_tone);
                            getRoomReference(roomCode).child(Constants.DATABASE_CHILD_IS_A_MOVE).setValue(false);
                            int move = e.getMoveMade();
                            boxPositions[move] = playerTurn;
                            if (e.getPlayerNum() == PLAYER_NUM_1) {
                                gameBoardIcons[move].setImageResource(R.drawable.tictactoe_ximage);
                            } else {
                                gameBoardIcons[move].setImageResource(R.drawable.tictactoe_oimage);
                            }
                            if (checkResults()) {
                                thisPlayerWon=false;
                                String winner;
                                if(playerTurn==1){
                                    winner=playerOneName;
                                }else{
                                    winner=playerTwoName;
                                }
                                TicTacOnlineResultDialog TicTacOnlineResultDialog = new TicTacOnlineResultDialog(TicTacOnline.this, winner + " is a Winner!", TicTacOnline.this,roomCode,thisPlayerWon);
                                TicTacOnlineResultDialog.setCancelable(false);
                                TicTacOnlineResultDialog.show();
                            } else if (totalSelectedBoxes == 9) {
                                TicTacOnlineResultDialog TicTacOnlineResultDialog = new TicTacOnlineResultDialog(TicTacOnline.this, "Match Draw", TicTacOnline.this,roomCode,thisPlayerWon);
                                TicTacOnlineResultDialog.setCancelable(false);
                                TicTacOnlineResultDialog.show();
                            } else {
                                changePlayerTurnTo(playerTurn == PLAYER_NUM_1 ? PLAYER_NUM_2 : PLAYER_NUM_1);
                                totalSelectedBoxes++;
                            }
                        }
                    }
                }else{
                    Toast.makeText(TicTacOnline.this, Prompts.PLAYER_DC, Toast.LENGTH_SHORT).show();
                    getRoomReference(roomCode).removeEventListener(eventListener);
                    getRoomReference(roomCode).child(Constants.DATABASE_CHILD_PLAYER_DISCONNECT).onDisconnect().cancel();
                    getRoomReference(roomCode).removeValue();
                    finishedSelf=true;
                    Intent i= new Intent(getApplicationContext(), TicTacOnlineRoom.class);
                    startActivity(i);
                    finish();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(TicTacOnline.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        });


        binding.image1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isBoxSelectable(0)){
                    performAction((ImageView) view, 0);
                }
            }
        });
        binding.image2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isBoxSelectable(1)){
                    performAction((ImageView) view, 1);
                }
            }
        });
        binding.image3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isBoxSelectable(2)){
                    performAction((ImageView) view, 2);
                }
            }
        });
        binding.image4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isBoxSelectable(3)){
                    performAction((ImageView) view, 3);
                }
            }
        });
        binding.image5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isBoxSelectable(4)){
                    performAction((ImageView) view, 4);
                }
            }
        });
        binding.image6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isBoxSelectable(5)){
                    performAction((ImageView) view, 5);
                }
            }
        });
        binding.image7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isBoxSelectable(6)){
                    performAction((ImageView) view, 6);
                }
            }
        });
        binding.image8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isBoxSelectable(7)){
                    performAction((ImageView) view, 7);
                }
            }
        });
        binding.image9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isBoxSelectable(8)){
                    performAction((ImageView) view, 8);
                }
            }
        });
    }

    private void performAction(ImageView  imageView, int selectedBoxPosition) {
        MusicManager.getInstance().play(getApplicationContext(),R.raw.ttt_o_tone);
        if (playerTurn == thisPlayer) {
            boxPositions[selectedBoxPosition] = playerTurn;
            if(thisPlayer==PLAYER_NUM_1){
                imageView.setImageResource(R.drawable.tictactoe_ximage);
            }else{
                imageView.setImageResource(R.drawable.tictactoe_oimage);
            }

            DatabaseReference ref= getRoomReference(roomCode);

            Map<String, Object> values = new HashMap<>();
            values.put(Constants.DATABASE_CHILD_IS_A_MOVE, true);
            values.put(Constants.DATABASE_CHILD_MOVE_MADE, selectedBoxPosition);
            values.put(Constants.DATABASE_CHILD_PLAYER_NUM, thisPlayer);
            ref.updateChildren(values);

            if (checkResults()) {
                thisPlayerWon=true;
                String winner;
                if(playerTurn==1){
                    winner=playerOneName;
                }else{
                    winner=playerTwoName;
                }
                TicTacOnlineResultDialog TicTacOnlineResultDialog = new TicTacOnlineResultDialog(TicTacOnline.this,  winner+ " is a Winner!", TicTacOnline.this,roomCode,thisPlayerWon);
                TicTacOnlineResultDialog.setCancelable(false);
                TicTacOnlineResultDialog.show();
            } else if (totalSelectedBoxes == 9) {
                thisPlayerWon=false;
                TicTacOnlineResultDialog TicTacOnlineResultDialog = new TicTacOnlineResultDialog(TicTacOnline.this, "Match Draw", TicTacOnline.this,roomCode,thisPlayerWon);
                TicTacOnlineResultDialog.setCancelable(false);
                TicTacOnlineResultDialog.show();
            } else {
                changePlayerTurnTo(playerTurn==PLAYER_NUM_1?PLAYER_NUM_2:PLAYER_NUM_1);
                totalSelectedBoxes++;
            }
        }
    }

    private void changePlayerTurnTo(int currentPlayerTurn) {
        playerTurn = currentPlayerTurn;
        if (playerTurn == PLAYER_NUM_1) {
            binding.playerOneLayout.setBackgroundResource(R.drawable.tictactoe_black_border);
            binding.playerTwoLayout.setBackgroundResource(R.drawable.tictactoe_white_box);
        } else {
            binding.playerTwoLayout.setBackgroundResource(R.drawable.tictactoe_black_border);
            binding.playerOneLayout.setBackgroundResource(R.drawable.tictactoe_white_box);
        }
    }
    private boolean checkResults(){
        boolean response = false;
        for (int i = 0; i < combinationList.size(); i++){
            final int[] combination = combinationList.get(i);
            if (boxPositions[combination[0]] == playerTurn && boxPositions[combination[1]] == playerTurn &&
                    boxPositions[combination[2]] == playerTurn) {
                response = true;
            }
        }
        return response;
    }
    private boolean isBoxSelectable(int boxPosition) {
        boolean response = false;
        if (boxPositions[boxPosition] == 0) {
            response = true;
        }
        return response;
    }
    public void restartMatch(){
        boxPositions = new int[] {0,0,0,0,0,0,0,0,0}; //9 zero
        playerTurn = 1;
        totalSelectedBoxes = 1;
        thisPlayerWon=false;
        binding.image1.setImageResource(R.drawable.tictactoe_white_box);
        binding.image2.setImageResource(R.drawable.tictactoe_white_box);
        binding.image3.setImageResource(R.drawable.tictactoe_white_box);
        binding.image4.setImageResource(R.drawable.tictactoe_white_box);
        binding.image5.setImageResource(R.drawable.tictactoe_white_box);
        binding.image6.setImageResource(R.drawable.tictactoe_white_box);
        binding.image7.setImageResource(R.drawable.tictactoe_white_box);
        binding.image8.setImageResource(R.drawable.tictactoe_white_box);
        binding.image9.setImageResource(R.drawable.tictactoe_white_box);
    }

    public DatabaseReference getRoomReference(String roomCode){
        return database.getReference().child(roomCode);
    }

    @Override
    protected void onStop() {
        super.onStop();
        getRoomReference(roomCode).removeEventListener(eventListener);
        if(!finishedSelf)getRoomReference(roomCode).child(Constants.DATABASE_CHILD_PLAYER_DISCONNECT).setValue(true);
        getRoomReference(roomCode).child(Constants.DATABASE_CHILD_PLAYER_DISCONNECT).onDisconnect().cancel();
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