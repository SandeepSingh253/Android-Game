package com.team.classicrealm.SpaceShooter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;

import com.team.classicrealm.GameUtility.HitBox;
import com.team.classicrealm.R;

public class Shooter {
    private final Context context;
    private Bitmap shooter;

    private Point loc;

    private HitBox hitBox;

    public Shooter(Context context) {
        this.context=context;
        init();
    }

    private void init() {
        loc=new Point(0,0);
        shooter=BitmapFactory.decodeResource(context.getResources(), R.drawable.spaceshooter_shooter);
        hitBox=new HitBox(loc,new Point(loc.x+getWidth(),loc.y+getHeight()));
    }

    public Point getLoc(){
        return loc;
    }

    public void setLoc(int x, int y){
        loc.set(x,y);
    }

    public Bitmap getBitmap(){
        return shooter;
    }

    public int getWidth(){
        return shooter.getWidth();
    }

    public int getHeight(){
        return shooter.getHeight();
    }

    public HitBox getHitBox() {
        hitBox.setUpperLeftCorner(loc);
        hitBox.setLowerRightCorner(new Point(loc.x+getWidth(),loc.y+getHeight()));
        return hitBox;
    }
    public boolean hitboxTouched(int x,int y){
        return x<loc.x+getWidth() && x>loc.x && y<loc.y+getWidth() && y> loc.y;
    }

}
