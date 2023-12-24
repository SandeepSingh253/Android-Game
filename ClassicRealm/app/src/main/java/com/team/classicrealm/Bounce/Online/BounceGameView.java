package com.team.classicrealm.Bounce.Online;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Build;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.team.classicrealm.Bounce.BounceMenu;
import com.team.classicrealm.GameUtility.Constants;
import com.team.classicrealm.GameUtility.MusicManager;
import com.team.classicrealm.GameUtility.Prompts;
import com.team.classicrealm.R;

import java.util.HashMap;
import java.util.Map;


public class BounceGameView extends View {
    private final Context context;
    private Handler handler;
    private Runnable runnable;
    private Point displaySize;
    private BounceRacket myBounceRacket;
    private BounceBall ball;
    private FirebaseDatabase database;

    private String roomCode;

    private int thisPlayerNum;
    private int oppNum;

    private boolean ballOnScreen;

    public ValueEventListener eventListener;
    private String playerOneName;
    private String playerTwoName;

    private int[] scores=new int[2];
    public boolean finishedSelf=false;

    public BounceGameView(Context context) {
        super(context);
        this.context=context;
        init();
        getUserNames();
        runnable=new Runnable() {
            @Override
            public void run() {
                if(!finishedSelf)
                    invalidate();
            }
        };
    }

    private void getUserNames() {
        if(thisPlayerNum==Constants.PLAYER_NUM_1){
            oppNum=Constants.PLAYER_NUM_2;
            playerOneName=((Activity)context).getSharedPreferences(Constants.PREF_USER,((Activity)context).MODE_PRIVATE).getString(Constants.SHARED_PREF_KEY_USER_NAME,Constants.DEFAULT_VALUE_USER_NAME);
            playerTwoName=((Activity)context).getIntent().getStringExtra(Constants.INTENT_KEY_OPPONENT_NAME);
        }else{
            oppNum=Constants.PLAYER_NUM_1;
            playerTwoName=((Activity)context).getSharedPreferences(Constants.PREF_USER,((Activity)context).MODE_PRIVATE).getString(Constants.SHARED_PREF_KEY_USER_NAME,Constants.DEFAULT_VALUE_USER_NAME);
            playerOneName=((Activity)context).getIntent().getStringExtra(Constants.INTENT_KEY_OPPONENT_NAME);
        }
    }

    private void init() {
        Intent i=((Activity)context).getIntent();
        roomCode=i.getStringExtra(Constants.INTENT_KEY_ROOM_CODE_STRING);
        thisPlayerNum=i.getIntExtra(Constants.INTENT_KEY_PLAYER_NUM,Constants.PLAYER_NUM_1);
        database=FirebaseDatabase.getInstance();
        displaySize=new Point();
        int width= context.getResources().getDisplayMetrics().widthPixels;
        int height=context.getResources().getDisplayMetrics().heightPixels + getNavigationBarHeight();;
        displaySize.set(width,height);
        handler=new Handler();
        myBounceRacket =new BounceRacket(context);
        myBounceRacket.setLoc(displaySize.x/2 - myBounceRacket.getWidth()/2, displaySize.y- 5* myBounceRacket.getHeight());
        setEventListeners();
        ball=new BounceBall(BitmapFactory.decodeResource(getResources(), R.drawable.block_breaker_ball));
        if(thisPlayerNum==Constants.PLAYER_NUM_1){
            ball.setRandomXVelocity();
            spawnBall();
            ballOnScreen=true;
        }else{
            ballOnScreen=false;
        }
    }

    private void setEventListeners() {

        eventListener=getRoomRef(roomCode).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(!snapshot.exists())return;
                BounceEvent event = snapshot.getValue(BounceEvent.class);
                if (!event.getPlayerDisconnect()) {
                    if(!event.getGameEnd()) {
                        if (!ballOnScreen) {
                            if (snapshot.getValue() == null)
                                return;
                            if (event.getEnterScreenPlayerNum() == thisPlayerNum) {
                                ball.setxVelocity(-1*event.getxVelocity());
                                int x = displaySize.x - (int) (displaySize.x * event.getEnterPosPer() / 100.0);
                                ball.setPos(x, 0);
                                ballOnScreen = true;
                            }
                        }
                    }else{
                        finishedSelf=true;
                        getRoomRef(roomCode).removeEventListener(eventListener);
                        getRoomRef(roomCode).child(Constants.DATABASE_CHILD_PLAYER_DISCONNECT).onDisconnect().cancel();
                        getRoomRef(roomCode).removeValue();
                        Intent i=new Intent(context, BounceGameOver.class);
                        String playerWon;
                        if(thisPlayerNum==Constants.PLAYER_NUM_1){
                            playerWon=playerOneName;
                        }else{
                            playerWon=playerTwoName;
                        }
                        i.putExtra(Constants.INTENT_KEY_PLAYER_WON_NAME,playerWon);
                        context.startActivity(i);
                        ((Activity)context).finish();
                    }
                }else{
                    finishedSelf=true;
                    Toast.makeText(context, Prompts.PLAYER_DC, Toast.LENGTH_SHORT).show();
                    getRoomRef(roomCode).removeEventListener(eventListener);
                    getRoomRef(roomCode).child(Constants.DATABASE_CHILD_PLAYER_DISCONNECT).onDisconnect().cancel();
                    getRoomRef(roomCode).removeValue();
                    Intent i=new Intent(context, BounceMenu.class);
                    context.startActivity(i);
                    ((Activity)context).finish();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);
        drawBackground(canvas);
        drawRackets(canvas);
        drawBall(canvas);
        moveBall();
        checkCollision();
        checkPass();
        checkGameOver();
        handler.postDelayed(runnable,Constants.ONE_SEC/Constants.GAME_FPS);
    }

    private void drawBackground(Canvas canvas) {
        Bitmap background;
        if(thisPlayerNum==Constants.PLAYER_NUM_1){
            background = BitmapFactory.decodeResource(getResources(), R.drawable.bounce_game_red_bg);
        }else{
            background = BitmapFactory.decodeResource(getResources(), R.drawable.bounce_game_blue_bg);
        }
        Rect backgroundRect = new Rect(0, 0, displaySize.x,displaySize.y);
        canvas.drawBitmap(background,null,backgroundRect,null);
    }
    private void checkGameOver() {
        if(ballOnScreen && ball.getPos().y> myBounceRacket.getLoc().y){
            finishedSelf=true;
            getRoomRef(roomCode).child(Constants.DATABASE_CHILD_PLAYER_DISCONNECT).onDisconnect().cancel();
            getRoomRef(roomCode).child(Constants.DATABASE_CHILD_GAME_END).setValue(true);
            getRoomRef(roomCode).removeEventListener(eventListener);
            Intent i=new Intent(context, BounceGameOver.class);
            String playerWon;
            if(thisPlayerNum==Constants.PLAYER_NUM_1){
                playerWon=playerTwoName;
            }else{
                playerWon=playerOneName;
            }
            i.putExtra(Constants.INTENT_KEY_PLAYER_WON_NAME,playerWon);
            ((Activity)context).finish();
            context.startActivity(i);
        }
    }

    private void checkPass() {
        if (ball.getPos().y<0 && ballOnScreen) {
            ballOnScreen=false;
            ball.setyVelocity(Constants.TENNIS_BALL_Y_VELOCITY);
            int x=ball.getPos().x;
            double percent=(x/(double)displaySize.x)*100;
            Map<String,Object> update=new HashMap<>();
            update.put(Constants.DATABASE_CHILD_BALL_X_PER,(int) percent);
            update.put(Constants.DATABASE_CHILD_BALL_ENTER_SCREEN_PLAYER_NUM,thisPlayerNum==Constants.PLAYER_NUM_1?Constants.PLAYER_NUM_2:Constants.PLAYER_NUM_1);
            update.put(Constants.DATABASE_CHILD_BALL_X_VELOCITY,ball.getxVelocity());
            getRoomRef(roomCode).setValue(update);
        }
    }
    private void checkCollision() {
        if(ballOnScreen) {
            int x = ball.getPos().x;
            int y = ball.getPos().y;
            if (x < 0 || x + ball.getWidth() > displaySize.x) {
                MusicManager.getInstance().play(context,R.raw.ball_bounce);
                ball.setxVelocity(-1 * ball.getxVelocity());
            }
            if (ball.getHitBox().checkCollision(myBounceRacket.getHitBox())) {
                MusicManager.getInstance().play(context,R.raw.ball_bounce);
                ball.setyVelocity(-1 * Math.abs(ball.getyVelocity()));
            }
        }
    }

    private void moveBall() {
        if(ballOnScreen) {
            ball.setPos(ball.getPos().x + ball.getxVelocity(), ball.getPos().y + ball.getyVelocity());
        }
    }

    private void drawBall(Canvas canvas) {
        if(ballOnScreen){
            canvas.drawBitmap(ball.getBallImage(),ball.getPos().x,ball.getPos().y,null);
        }
    }

    private void drawRackets(Canvas canvas) {
        canvas.drawBitmap(myBounceRacket.getBitmap(), myBounceRacket.getLoc().x, myBounceRacket.getLoc().y,null);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        int x = (int)ev.getX();
        int y = (int)ev.getY();
        //Log.d("",""+shooter.getHitBox().includePoint(x, y));
        if(ev.getAction()==MotionEvent.ACTION_MOVE && x> myBounceRacket.getWidth()/2 && x<displaySize.x- myBounceRacket.getWidth()/2 && myBounceRacket.getLoc().y<y){
            myBounceRacket.setLoc(x- myBounceRacket.getWidth()/2, myBounceRacket.getLoc().y);
        }
        return true;
    }

    private void spawnBall(){
        int x = (int) (displaySize.x * Constants.TENNIS_BALL_INITIAL_POS_PER / 100.0);
        ball.setPos(x,ball.getHeight());
    }
    private DatabaseReference getRoomRef(String roomCode){
        return database.getReference().child(roomCode);
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
