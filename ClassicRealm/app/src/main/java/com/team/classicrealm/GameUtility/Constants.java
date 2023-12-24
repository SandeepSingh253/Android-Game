package com.team.classicrealm.GameUtility;

public class Constants {
    //tennis
    public static final double TENNIS_BALL_INITIAL_POS_PER=50.0;
    public static final int TENNIS_BALL_Y_VELOCITY=20;
    public static final int[] RANDOM_VELOCITIES = {-35, 30, -25 , 25, 30, 35};

    // Tic Tac
    public static final String ROOM_CODE_ELEMENTS= "abcdefghijklmnopqrstuvwxyz0123456789";
    public static final int ROOM_CODE_SIZE=6;
    public static final int PLAYER_NUM_1 = 1;
    public static final int PLAYER_NUM_2 = 2;

    //Space Shooter
    public static final int GAME_FPS=60;
    public static final int ONE_SEC=1000;
    public static final int SPACE_SHOOTER_SHOT_SPEED=30;
    public static final int SPACE_SHOOTER_SCORE_PER_UFO=10;
    public static final int SPACE_SHOOTER_HP = 4;

    //Firebase Realtime database childs
    public static final String DATABASE_CHILD_USERS = "Users";
    public static final String DATABASE_CHILD_SCORE_BOARD="scoreboard";
    public static final String DATABASE_CHILD_PLAYER_COUNT="playerCount";
    public static final String DATABASE_CHILD_PLAYER_DISCONNECT="playerDisconnect";
    public static final String DATABASE_CHILD_RESTART_GAME="restartGame";
    public static final String DATABASE_CHILD_IS_A_MOVE="isAMove";
    public static final String DATABASE_CHILD_MOVE_MADE="moveMade";
    public static final String DATABASE_CHILD_PLAYER_NUM="playerNum";
    public static final String DATABASE_CHILD_PLAYER_ONE_NAME="playerOneName";
    public static final String DATABASE_CHILD_PLAYER_TWO_NAME="playerTwoName";
    public static final String DATABASE_CHILD_BALL_X_PER = "enterPosPer";
    public static final String DATABASE_CHILD_BALL_X_VELOCITY = "xVelocity";
    public static final String DATABASE_CHILD_BALL_ENTER_SCREEN_PLAYER_NUM = "enterScreenPlayerNum";
    public static final String DATABASE_CHILD_GAME_END = "gameEnd";


    // General Constants
    public static final int USER_NAME_MIN_LENGTH=4;
    public static final int USER_NAME_MAX_LENGTH=8;

    //Intent Keys
    public static final String INTENT_KEY_USER_NAME="username";
    public static final String INTENT_KEY_ROOM_CODE_STRING="roomCode";
    public static final String INTENT_KEY_ROOM_PLAYER_ONE="playerOne";
    public static final String INTENT_KEY_ROOM_PLAYER_TWO="playerTwo";
    public static final String INTENT_KEY_PLAYER_NUM="playerNum";
    public static final String INTENT_KEY_OPPONENT_NAME="oppName";
    public static final String INTENT_KEY_SCORE="score";
    public static final String INTENT_KEY_PLAYER_WON_NAME = "playerWon";

    //Shared Preferences
    public static final String PREF_USER="USER_DATA_PREFERENCES";

    //Shared Preferences Keys
    public static final String SHARED_PREF_KEY_USER_NAME = "USERNAME";
    public static final String SHARED_PREF_KEY_USER_SCORE="USER_SCORE";

    //scores
    public static final int SCORES_TIC_TAC_TOE = 20;
    public static final int SCORES_BOUNCE = 20;
    public static final int BLOCK_BREAKER_SCORE = 10;

    //default values
    public static final String DEFAULT_VALUE_USER_NAME="username";
    public static final int DEFAULT_VALUE_SCORE = 0;
}
