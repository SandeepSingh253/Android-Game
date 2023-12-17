package com.team.classicrealm.GameUtility;

import android.graphics.Point;

public class HitBox {
    private Point upperLeftCorner,lowerRightCorner;
    public HitBox(Point upLeft,Point lowRight){
        this.upperLeftCorner=upLeft;
        this.lowerRightCorner=lowRight;
    }

    public HitBox(int upY , int lowRightX, int lowY, int upLeftX){
        this.upperLeftCorner.set(upLeftX,upY);
        this.lowerRightCorner.set(lowRightX,lowY);
    }

    public Point getUpLeftCorner() {
        return upperLeftCorner;
    }

    public Point getLowRightCorner() {
        return lowerRightCorner;
    }

    public boolean includePoint(Point p){
        return includePoint(p.x,p.y);
    }

    public boolean includePoint(int x, int y){
        return x>upperLeftCorner.x && x< lowerRightCorner.x
                && y > upperLeftCorner.y && y< lowerRightCorner.y;
    }
    public boolean checkCollision(HitBox h){
        return upperLeftCorner.x < h.getLowRightCorner().x &&
        lowerRightCorner.x > h.getUpLeftCorner().x &&
        upperLeftCorner.y < h.getLowRightCorner().y &&
        lowerRightCorner.y > h.getUpLeftCorner().y;
    }

    public void setUpperLeftCorner(Point upperLeftCorner) {
        this.upperLeftCorner = upperLeftCorner;
    }

    public void setLowerRightCorner(Point lowerRightCorner) {
        this.lowerRightCorner = lowerRightCorner;
    }
}
