package com.team.classicrealm.SpaceShooter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Build;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.team.classicrealm.GameUtility.Constants;
import com.team.classicrealm.GameUtility.MusicManager;
import com.team.classicrealm.R;

import java.util.ArrayList;
import java.util.Random;

public class space_shooter_game_view extends View {

    private final Context context;
    private Point displaySize;
    private Rect backgroundRect;

    private Paint textPaint;
    private int numUFO=3;
    private UFO[] ufos=new UFO[numUFO];

    private int curScore=0;

    private ArrayList<Beam> beamsShot=new ArrayList<Beam>();

    private Random ran= new Random();

    private Shooter shooter;
    private Handler handler;
    private Runnable runnable;

    private int frameNum=0;
    private int frameMark=10;
    private int heartOverlap=20;
    private int heartPadding=70;

    private final float textSize=50;

    private int hp=Constants.SPACE_SHOOTER_HP;

    private boolean isFinished=false;
    public space_shooter_game_view(Context context) {
        super(context);
        this.context=context;
        init();
        runnable=new Runnable() {
            @Override
            public void run() {
                if(!isFinished) {
                    if (frameNum == frameMark) {
                        frameNum = 0;
                        shotBeam();
                    }
                    frameNum++;
                    invalidate();
                }
            }
        };
    }

    private void shotBeam() {
        Beam shot= new Beam(context);
        shot.setLoc(shooter.getLoc().x+shooter.getWidth()/2-(shot.getWidth()/2),shooter.getLoc().y-(shot.getHeight()));
        beamsShot.add(shot);
        MusicManager.getInstance().play(context,R.raw.laser_sound_3);
    }

    private void init() {
        displaySize=new Point();
        int width= context.getResources().getDisplayMetrics().widthPixels;
        int height=context.getResources().getDisplayMetrics().heightPixels + getNavigationBarHeight();;
        displaySize.set(width,height);
        textPaint=new Paint();
        textPaint.setTextSize(textSize);
        textPaint.setColor(Color.GRAY);
        handler=new Handler();
        shooter=new Shooter(context);
        shooter.setLoc(displaySize.x/2 - shooter.getWidth()/2, displaySize.y- 2*shooter.getHeight());
        for(int i=0;i<numUFO;i++) {
            ufos[i] = new UFO(context);
            ufos[i].setLoc(displaySize.x+ran.nextInt(UFO.SPAWN_MAX_X),ran.nextInt(UFO.SPAWN_MAX_Y));
        }
    }


    @Override
    protected void onDraw(Canvas canvas) {
        setBackground(canvas);
        drawShooter(canvas);
        runUfoAnim(canvas);
        runShootAnim(canvas);
        drawHP(canvas);
        drawScore(canvas);
        checkGameOverStatus();
        handler.postDelayed(runnable, Constants.ONE_SEC/Constants.GAME_FPS);
    }

    private void drawScore(Canvas canvas) {
        canvas.drawText("score:"+curScore,20,40,textPaint);
    }

    private void checkGameOverStatus() {
        if(hp<1){
            isFinished=true;
            Intent i= new Intent(context,SpaceShooterGameOver.class);
            ((Activity)context).finish();
            i.putExtra(Constants.INTENT_KEY_SCORE,curScore);
            context.startActivity(i);
        }
    }

    private void drawHP(Canvas canvas) {
        for(int i =1;i<=hp;i++){
            Bitmap heart = BitmapFactory.decodeResource(getResources(), R.drawable.spaceshooter_hp);
            int left=displaySize.x-(i*heart.getWidth())-heartPadding;
            Rect heartRect = new Rect(left, 0,left+heart.getWidth()+heartOverlap,heart.getHeight());
            canvas.drawBitmap(heart,null,heartRect,null);
        }
    }

    private void runShootAnim(Canvas canvas) {

        for(int i =0; i<beamsShot.size();i++){
            canvas.drawBitmap(beamsShot.get(i).getBitmap(),beamsShot.get(i).getLoc().x,beamsShot.get(i).getLoc().y,null);
            //beamsShot.get(i).setLoc(beamsShot.get(i).getLoc().x,beamsShot.get(i).getLoc().y-Constants.SHOT_SPEED);
            beamsShot.get(i).move();
            boolean collided=false;
            for(int j =0; j<ufos.length;j++){
                if(beamsShot.get(i).getHitbox().checkCollision(ufos[j].getHitbox())){
                    ufos[j].reSpawn(canvas.getWidth());
                    collided=true;
                }
            }
            if(collided) {
                beamsShot.remove(beamsShot.get(i));
                curScore+=Constants.SPACE_SHOOTER_SCORE_PER_UFO;
            }
        }
        removeOutOfScreenBeam();
    }

    private void removeOutOfScreenBeam(){
        for(int i =0; i<beamsShot.size();i++){
            if(beamsShot.get(i).getLoc().y<0) beamsShot.remove(beamsShot.get(i));
        }
    }
    private void drawShooter(Canvas canvas) {
        canvas.drawBitmap(shooter.getBitmap(),shooter.getLoc().x,shooter.getLoc().y,null);
    }

    private void runUfoAnim(Canvas canvas) {
        for(UFO ufo : ufos) {
            canvas.drawBitmap(ufo.nextFrame(), ufo.getLoc().x, ufo.getLoc().y, null);
            ufo.getLoc().x = ufo.getLoc().x - ufo.getVelocity();
            if (ufo.getLoc().x < -ufo.getWidth()) {
                hp--;
                ufo.reSpawn(canvas.getWidth());
            }
        }
    }

    private void setBackground(Canvas canvas){
        Bitmap background = BitmapFactory.decodeResource(getResources(), R.drawable.spaceshooter_background);
        backgroundRect = new Rect(0, 0, displaySize.x,displaySize.y);
        canvas.drawBitmap(background,null,backgroundRect,null);
    }

    public void resetGame(){
        curScore=0;
        hp=Constants.SPACE_SHOOTER_HP;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int x = (int)event.getX();
        int y = (int)event.getY();
        //Log.d("",""+shooter.getHitBox().includePoint(x, y));
        if(event.getAction()==MotionEvent.ACTION_MOVE && x>shooter.getWidth()/2 && x<displaySize.x-shooter.getWidth()/2 && shooter.getHitBox().includePoint(x, y)){
            shooter.setLoc(x-shooter.getWidth()/2,shooter.getLoc().y);
        }
        return true;
    }


    private int getNavigationBarHeight() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            DisplayMetrics metrics = new DisplayMetrics();
            ((Activity) getContext()).getWindowManager().getDefaultDisplay().getMetrics(metrics);
            int usableHeight = metrics.heightPixels;
            ((Activity) getContext()).getWindowManager().getDefaultDisplay().getRealMetrics(metrics);
            int realHeight = metrics.heightPixels;
            if (realHeight > usableHeight)
                return realHeight - usableHeight;
            else
                return 0;
        }
        return 0;
    }
}
