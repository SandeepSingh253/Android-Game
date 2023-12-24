package com.team.classicrealm.TicTacToe.Offline;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.team.classicrealm.GameUtility.Constants;
import com.team.classicrealm.GameUtility.MusicManager;
import com.team.classicrealm.R;
import com.team.classicrealm.databinding.ActivityTicTacOfflineBinding;

import java.util.ArrayList;
import java.util.List;

public class TicTacOffline extends AppCompatActivity {
    ActivityTicTacOfflineBinding binding;
    private final List<int[]> combinationList = new ArrayList<>();
    private int[] boxPositions = {0,0,0,0,0,0,0,0,0};
    private int playerTurn = 1;
    private int totalSelectedBoxes = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTicTacOfflineBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P)
            getWindow().getAttributes().layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
        hideSystemUI();
        combinationList.add(new int[] {0,1,2});
        combinationList.add(new int[] {3,4,5});
        combinationList.add(new int[] {6,7,8});
        combinationList.add(new int[] {0,3,6});
        combinationList.add(new int[] {1,4,7});
        combinationList.add(new int[] {2,5,8});
        combinationList.add(new int[] {2,4,6});
        combinationList.add(new int[] {0,4,8});
        String getPlayerOneName = getIntent().getStringExtra(Constants.INTENT_KEY_ROOM_PLAYER_ONE);
        String getPlayerTwoName = getIntent().getStringExtra(Constants.INTENT_KEY_ROOM_PLAYER_TWO);
        binding.playerOneName.setText(getPlayerOneName);
        binding.playerTwoName.setText(getPlayerTwoName);
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
        boxPositions[selectedBoxPosition] = playerTurn;
        if (playerTurn == 1) {
            MusicManager.getInstance().play(getApplicationContext(),R.raw.ttt_o_tone);
            imageView.setImageResource(R.drawable.tictactoe_ximage);
            if (checkResults()) {
                TicTacOfflineResultDialog TicTacOfflineResultDialog = new TicTacOfflineResultDialog(TicTacOffline.this, binding.playerOneName.getText().toString() + " is a Winner!", TicTacOffline.this);
                TicTacOfflineResultDialog.setCancelable(false);
                TicTacOfflineResultDialog.show();
            } else if(totalSelectedBoxes == 9) {
                TicTacOfflineResultDialog TicTacOfflineResultDialog = new TicTacOfflineResultDialog(TicTacOffline.this, "Match Draw", TicTacOffline.this);
                TicTacOfflineResultDialog.setCancelable(false);
                TicTacOfflineResultDialog.show();
            } else {
                changePlayerTurn(2);
                totalSelectedBoxes++;
            }
        } else {
            MusicManager.getInstance().play(getApplicationContext(),R.raw.ttt_x_tone);
            imageView.setImageResource(R.drawable.tictactoe_oimage);
            if (checkResults()) {
                TicTacOfflineResultDialog TicTacOfflineResultDialog = new TicTacOfflineResultDialog(TicTacOffline.this, binding.playerTwoName.getText().toString()
                        + " is a Winner!", TicTacOffline.this);
                TicTacOfflineResultDialog.setCancelable(false);
                TicTacOfflineResultDialog.show();
            } else if(totalSelectedBoxes == 9) {
                TicTacOfflineResultDialog TicTacOfflineResultDialog = new TicTacOfflineResultDialog(TicTacOffline.this, "Match Draw", TicTacOffline.this);
                TicTacOfflineResultDialog.setCancelable(false);
                TicTacOfflineResultDialog.show();
            } else {
                changePlayerTurn(1);
                totalSelectedBoxes++;
            }
        }
    }
    private void changePlayerTurn(int currentPlayerTurn) {
        playerTurn = currentPlayerTurn;
        if (playerTurn == 1) {
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