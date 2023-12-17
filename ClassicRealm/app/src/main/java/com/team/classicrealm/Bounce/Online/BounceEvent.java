package com.team.classicrealm.Bounce.Online;

public class BounceEvent {
    private int playerCount;
    private boolean playerDisconnect;
    private int enterPosPer;
    private int enterScreenPlayerNum;
    private String playerOneName="";
    private String playerTwoName="";
    private String playerWon="";
    private int xVelocity;

    private boolean gameEnd;
    public BounceEvent() {

    }

    public BounceEvent(int enterPosPer, int enterScreenPlayerNum, int xVelocity) {
        this.enterPosPer = enterPosPer;
        this.enterScreenPlayerNum = enterScreenPlayerNum;
        this.xVelocity = xVelocity;
    }

    public int getxVelocity() {
        return xVelocity;
    }

    public void setxVelocity(int xVelocity) {
        this.xVelocity = xVelocity;
    }

    public int getEnterPosPer() {
        return enterPosPer;
    }

    public void setEnterPosPer(int enterPosPer) {
        this.enterPosPer = enterPosPer;
    }

    public int getEnterScreenPlayerNum() {
        return enterScreenPlayerNum;
    }

    public void setEnterScreenPlayerNum(int enterScreenPlayerNum) {
        this.enterScreenPlayerNum = enterScreenPlayerNum;
    }

    public int getPlayerCount() {
        return playerCount;
    }

    public void setPlayerCount(int playerCount) {
        this.playerCount = playerCount;
    }

    public boolean getPlayerDisconnect() {
        return playerDisconnect;
    }

    public void setPlayerDisconnect(boolean playerDisconnect) {
        this.playerDisconnect = playerDisconnect;
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

    public boolean getGameEnd() {
        return gameEnd;
    }

    public void setGameEnd(boolean gameEnd) {
        this.gameEnd = gameEnd;
    }
}
