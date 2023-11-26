package com.team.classicrealm.GameUtility;

public class Constants {
    // Tic Tac
    public static final String ROOM_CODE_ELEMENTS= "abcdefghijklmnopqrstuvwxyz0123456789";
    public static final int ROOM_CODE_SIZE=3;
    public static final int PLAYER_NUM_1 = 1;
    public static final int PLAYER_NUM_2 = 2;

    //Space Shooter
    public static final int GAME_FPS=30;
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


    //Shared Preferences
    public static final String PREF_USER="USER_DATA_PREFERENCES";

    //Shared Preferences Keys
    public static final String SHARED_PREF_KEY_USER_NAME = "USERNAME";
    public static final String SHARED_PREF_KEY_USER_SCORE="USER_SCORE";

    //scores
    public static final int SCORES_TIC_TAC_TOE = 20;
    public static final int BLOCK_BREAKER_SCORE = 1;

    //default values
    public static final String DEFAULT_VALUE_USER_NAME="username";
    public static final int DEFAULT_VALUE_SCORE = 0;

}
