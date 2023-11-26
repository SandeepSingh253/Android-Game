package com.team.classicrealm.SpaceShooter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;

import com.team.classicrealm.GameUtility.HitBox;
import com.team.classicrealm.R;

import java.util.Random;

public class UFO {

    private final int NUMBER_OF_FRAME=15;
    public static final int SPAWN_MAX_Y=700;
    public static final int SPAWN_MAX_X=500;
    private final int MIN_VELOCITY=10;
    private final int MAX_VELOCITY=25;

    private final Context context;
    private Bitmap[] ufoAnim=new Bitmap[NUMBER_OF_FRAME];
    private Point loc;
    private int velocity;

    private int curFrameNum=0;
    private int width;
    private int height;


    private Random ran= new Random();

    public UFO(Context context){
        this.context=context;
        init();
    }

    private void init() {
        loc=new Point();
        loadBitmap();
        height=ufoAnim[0].getHeight();
        width=ufoAnim[0].getWidth();
        randomVelocity();
    }

    private void randomVelocity() {
        velocity=MIN_VELOCITY+ran.nextInt(MAX_VELOCITY);
    }

    private void loadBitmap() {
        ufoAnim[0]= BitmapFactory.decodeResource(context.getResources(), R.drawable.spaceshooter_frame0);
        ufoAnim[1]=BitmapFactory.decodeResource(context.getResources(),R.drawable.spaceshooter_frame1);
        ufoAnim[2]=BitmapFactory.decodeResource(context.getResources(),R.drawable.spaceshooter_frame2);
        ufoAnim[3]=BitmapFactory.decodeResource(context.getResources(),R.drawable.spaceshooter_frame3);
        ufoAnim[4]=BitmapFactory.decodeResource(context.getResources(),R.drawable.spaceshooter_frame4);
        ufoAnim[5]=BitmapFactory.decodeResource(context.getResources(),R.drawable.spaceshooter_frame5);
        ufoAnim[6]=BitmapFactory.decodeResource(context.getResources(),R.drawable.spaceshooter_frame6);
        ufoAnim[7]=BitmapFactory.decodeResource(context.getResources(),R.drawable.spaceshooter_frame7);
        ufoAnim[8]=BitmapFactory.decodeResource(context.getResources(),R.drawable.spaceshooter_frame8);
        ufoAnim[9]=BitmapFactory.decodeResource(context.getResources(),R.drawable.spaceshooter_frame9);
        ufoAnim[10]=BitmapFactory.decodeResource(context.getResources(),R.drawable.spaceshooter_frame10);
        ufoAnim[11]=BitmapFactory.decodeResource(context.getResources(),R.drawable.spaceshooter_frame11);
        ufoAnim[12]=BitmapFactory.decodeResource(context.getResources(),R.drawable.spaceshooter_frame12);
        ufoAnim[13]=BitmapFactory.decodeResource(context.getResources(),R.drawable.spaceshooter_frame13);
        ufoAnim[14]=BitmapFactory.decodeResource(context.getResources(),R.drawable.spaceshooter_frame14);
    }

    public void reSpawn(int screenWidth){
        getLoc().x=screenWidth+ran.nextInt(SPAWN_MAX_X);
        getLoc().y=ran.nextInt(SPAWN_MAX_Y);
    }

    public Bitmap nextFrame(){
        if(curFrameNum>NUMBER_OF_FRAME-1) curFrameNum=0;
        return ufoAnim[curFrameNum++];
    }

    public HitBox getHitbox(){
        return new HitBox(loc,new Point(loc.x+getWidth(),loc.y+getHeight()));
    }
    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public void setLoc(int x, int y) {
        this.loc.set(x,y);
    }

    public Point getLoc() {
        return loc;
    }

    public int getVelocity() {
        return velocity;
    }
}
