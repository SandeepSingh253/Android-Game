package com.team.classicrealm.Bounce.Online;

import android.graphics.Bitmap;
import android.graphics.Point;

import com.team.classicrealm.GameUtility.Constants;
import com.team.classicrealm.GameUtility.HitBox;

import java.util.Random;

public class BounceBall {
    private Bitmap ballImage;
    private Point pos;

    private int xVelocity;

    private int yVelocity;

    private HitBox hitBox;

    public BounceBall(Bitmap ballImage) {
        this.ballImage=ballImage;
        yVelocity=Constants.TENNIS_BALL_Y_VELOCITY;
        pos=new Point();
        hitBox=new HitBox(pos,new Point(pos.x+getWidth(),pos.y+getHeight()));
    }

    public Bitmap getBallImage() {
        return ballImage;
    }

    public void setBallImage(Bitmap ballImage) {
        this.ballImage = ballImage;
    }

    public Point getPos() {
        return pos;
    }

    public void setPos(Point pos) {
        this.pos = pos;
    }
    public void setPos(int x,int y) {
        pos.set(x,y);
    }
    public int getxVelocity() {
        return xVelocity;
    }

    public void setxVelocity(int xVelocity) {
        this.xVelocity = xVelocity;
    }

    public int getyVelocity() {
        return yVelocity;
    }
    public void setRandomXVelocity() {
        Random random=new Random();
        int index = random.nextInt(Constants.RANDOM_VELOCITIES.length);
        xVelocity= Constants.RANDOM_VELOCITIES[index];
    }

    public void setyVelocity(int yVelocity) {
        this.yVelocity = yVelocity;
    }

    public int getWidth(){
        return ballImage.getWidth();
    }

    public int getHeight(){
        return ballImage.getHeight();
    }
    public HitBox getHitBox(){
        hitBox.setUpperLeftCorner(pos);
        hitBox.setLowerRightCorner(new Point(pos.x+getWidth(),pos.y+getHeight()));
        return hitBox;
    }

}
