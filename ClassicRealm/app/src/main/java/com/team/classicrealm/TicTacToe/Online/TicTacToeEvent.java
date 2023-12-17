package com.team.classicrealm.TicTacToe.Online;

public class TicTacToeEvent {
    private int playerCount;

    private boolean playerDisconnect;

    private int playerNum;

    private boolean isAMove;

    private int moveMade;

    private boolean restartGame;

    private String playerOneName;
    private String playerTwoName;


    public TicTacToeEvent() {
    }

    public TicTacToeEvent(int playerCount, boolean playerDisconnect, int playerNum, boolean isAMove, int moveMade, boolean restartGame, String playerOneName, String playerTwoName) {
        this.playerCount = playerCount;
        this.playerDisconnect = playerDisconnect;
        this.playerNum = playerNum;
        this.isAMove = isAMove;
        this.moveMade = moveMade;
        this.restartGame = restartGame;
        this.playerOneName=playerOneName;
        this.playerTwoName=playerTwoName;
    }

    public String getPlayerOneName() {
        return playerOneName;
    }

    public void setPlayerOneName(String playerOneName) {
        this.playerOneName = playerOneName;
    }

    public String getPlayerTwoName() {
        return playerTwoName;
    }

    public void setPlayerTwoName(String playerTwoName) {
        this.playerTwoName = playerTwoName;
    }

    public boolean getRestartGame() {
        return restartGame;
    }

    public void setRestartGame(boolean restartGame) {
        this.restartGame = restartGame;
    }

    public boolean getIsAMove() {
        return isAMove;
    }

    public void setIsAMove(boolean isAMove) {
        this.isAMove = isAMove;
    }

    public int getPlayerNum() {
        return playerNum;
    }

    public void setPlayerNum(int playerNum) {
        this.playerNum = playerNum;
    }

    public int getMoveMade() {
        return moveMade;
    }

    public void setMoveMade(int moveMade) {
        this.moveMade = moveMade;
    }


    public boolean isPlayerDisconnect() {
        return playerDisconnect;
    }

    public void setPlayerDisconnect(boolean playerDisconnect) {
        this.playerDisconnect = playerDisconnect;
    }

    public int getPlayerCount() {
        return playerCount;
    }

    public void setPlayerCount(int playerCount) {
        this.playerCount = playerCount;
    }


}
