package com.example.gamepractice;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.Random;

public class GameView extends View {

    private final Context context;
    private Point displaySize;
    private Rect backgroundRect;
    private int numUFO=2;
    private UFO[] ufos=new UFO[numUFO];

    private ArrayList<Beam> beamsShot=new ArrayList<Beam>();

    private Random ran= new Random();

    private Shooter shooter;
    private Handler handler;
    private Runnable runnable;
    public GameView(Context context) {
        super(context);
        this.context=context;
        init();
        runnable=new Runnable() {
            @Override
            public void run() {
                invalidate();
            }
        };
    }
    private void init() {
        displaySize=new Point();
        ((Activity) getContext()).getWindowManager().getDefaultDisplay().getSize(displaySize);
        handler=new Handler();
        shooter=new Shooter(context);
        shooter.setLoc(displaySize.x/2 - shooter.getWidth()/2, displaySize.y- shooter.getHeight());
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
    }

    private void runShootAnim(Canvas canvas) {
        for(int i =0; i<beamsShot.size();i++){
            canvas.drawBitmap(beamsShot.get(i).getBitmap(),beamsShot.get(i).getLoc().x,beamsShot.get(i).getLoc().y,null);
            beamsShot.get(i).setLoc(beamsShot.get(i).getLoc().x,beamsShot.get(i).getLoc().y-Constants.SHOT_SPEED);
            for(int j =0; j<ufos.length;j++){
                if(beamsShot.get(i).getHitbox().checkCollision(ufos[j].getHitbox())){
                    ufos[j].reSpawn(canvas.getWidth());
                    beamsShot.remove(beamsShot.get(i));
                }
            }
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
                ufo.reSpawn(canvas.getWidth());
            }
        }
        handler.postDelayed(runnable,Constants.ONE_SEC/Constants.GAME_FPS);
    }

    private void setBackground(Canvas canvas){
        Bitmap background = BitmapFactory.decodeResource(getResources(),R.drawable.background);
        backgroundRect = new Rect(0, 0, canvas.getWidth(), canvas.getHeight());
        canvas.drawBitmap(background,null,backgroundRect,null);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int x = (int)event.getX();
        int y = (int)event.getY();

        //Log.d("",""+shooter.getHitBox().includePoint(x, y));

        if(event.getAction()==MotionEvent.ACTION_MOVE && x>shooter.getWidth()/2 && x<displaySize.x-shooter.getWidth()/2 && shooter.getHitBox().includePoint(x, y)){
            shooter.setLoc(x-shooter.getWidth()/2,shooter.getLoc().y);
        }

        if(event.getAction() == MotionEvent.ACTION_UP){
            Beam shot= new Beam(context);
            shot.setLoc(shooter.getLoc().x+shooter.getWidth()/2,shooter.getLoc().y);
            beamsShot.add(shot);
        }

        return true;
    }
}
