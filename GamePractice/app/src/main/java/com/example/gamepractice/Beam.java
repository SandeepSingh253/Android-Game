package com.example.gamepractice;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;

public class Beam {
    private final Context context;
    private Bitmap laser;

    private Point loc;

    public Beam(Context context){
        this.context=context;
        loc=new Point();
        laser=BitmapFactory.decodeResource(context.getResources(),R.drawable.laserbullet);
    }

    public Bitmap getBitmap(){
        return laser;
    }

    public int getHeight(){
        return laser.getHeight();
    }

    public int getWidth(){
        return laser.getWidth();
    }

    public void setLoc(int x, int y){
        loc.set(x,y);
    }

    public Point getLoc(){
        return loc;
    }

    public HitBox getHitbox(){
        return new HitBox(loc,new Point(loc.x+getWidth(),loc.y+getHeight()));
    }
}
