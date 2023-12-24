package com.team.classicrealm.BlockBreaker;

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
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;

import com.team.classicrealm.GameUtility.Constants;
import com.team.classicrealm.GameUtility.MusicManager;
import com.team.classicrealm.R;

import java.util.Random;

public class BlockBreakerGameView extends View {

    Context context;
    float ballX, ballY;
    BlockBreakerVelocity blockBreakerVelocity = new BlockBreakerVelocity(25,32);
    Handler handler;
    final long UPDATE_MILLIS = 20;
    Runnable runnable;
    Paint textPaint = new Paint();
    Paint healthPaint = new Paint();
    Paint brickPaint = new Paint();
    float TEXT_SIZE = 120;
    float paddleX, paddleY;
    float oldX, oldPaddleX;
    int points =0;
    int life = 3;
    Bitmap ball, paddle;
    int dWidth, dHeight;
    int ballWidth, ballHeight;
    Random random;
    BlockBreakerBrick[] blockBreakerBricks = new BlockBreakerBrick[30];
    int numBricks = 0;
    int brokenBricks = 0;
    boolean gameOver = false;

    public BlockBreakerGameView(Context context) {
        super(context);
        this.context = context;
        ball = BitmapFactory.decodeResource(getResources(), R.drawable.block_breaker_ball);
        paddle = BitmapFactory.decodeResource(getResources(), R.drawable.block_breaker_paddle);

        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                invalidate();
            }
        };

        textPaint.setColor(Color.RED);
        textPaint.setTextSize(TEXT_SIZE);
        textPaint.setTextAlign(Paint.Align.LEFT);
        healthPaint.setColor(Color.GREEN);
        brickPaint.setColor(Color.argb(255,249,129,0));
        Display display = ((Activity) getContext()).getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        dWidth = size.x;
        dHeight = size.y;
        random = new Random();
        ballX= random.nextInt(dWidth - 50);
        ballY = dHeight/3;
        paddleY = (dHeight * 4)/5;
        paddleX = dWidth/2 - (paddle.getWidth()/2);
        ballWidth = ball.getWidth();
        ballHeight = ball.getHeight();

        createBricks();
    }

    private void createBricks() {
        int brickWidth = dWidth / 8;
        int brickHeight = dHeight / 16;
        for (int column = 0; column < 8; column++) {
            for (int row = 0; row < 3; row++) {
                blockBreakerBricks[numBricks++] = new BlockBreakerBrick(row, column, brickWidth, brickHeight);
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawBackground(canvas);
        ballX += blockBreakerVelocity.getX();
        ballY += blockBreakerVelocity.getY();
        if ((ballX >= dWidth - ball.getWidth()) || ballX <= 0) {
            MusicManager.getInstance().play(context,R.raw.ball_bounce);
            blockBreakerVelocity.setX(blockBreakerVelocity.getX() * -1);
        }
        if (ballY <= 0) {
            MusicManager.getInstance().play(context,R.raw.ball_bounce);
            blockBreakerVelocity.setY(blockBreakerVelocity.getY() * -1);
        }
        if (ballY > paddleY + paddle.getHeight()) {
            ballX = 1 + random.nextInt(dWidth - ball.getWidth() - 1);
            ballY = dHeight / 3;
            blockBreakerVelocity.setX(xVelocity());
            blockBreakerVelocity.setY(32);
            life--;
            if (life == 0) {
                gameOver = true;
                launchGameOver();
            }
        }
        if (((ballX + ball.getWidth()) >= paddleX) && (ballX <= paddleX + paddle.getWidth() && (ballY + ball.getHeight() >= paddleY)  && (ballY + ball.getHeight() <= paddleY + paddle.getHeight()))) {
            MusicManager.getInstance().play(context,R.raw.ball_bounce);
            blockBreakerVelocity.setX(blockBreakerVelocity.getX() + 1);
            blockBreakerVelocity.setY((blockBreakerVelocity.getY() + 1) * -1);
        }
        canvas.drawBitmap(ball, ballX, ballY, null);
        canvas.drawBitmap(paddle, paddleX, paddleY, null);
        for (int i=0; i<numBricks; i++)  {
            if (blockBreakerBricks[i].isVisible()) {
                canvas.drawRect(blockBreakerBricks[i].column * blockBreakerBricks[i].width + 1, blockBreakerBricks[i].row * blockBreakerBricks[i].height + 1, blockBreakerBricks[i].column * blockBreakerBricks[i].width + blockBreakerBricks[i].width -1, blockBreakerBricks[i].row * blockBreakerBricks[i].height + blockBreakerBricks[i].height -1, brickPaint);
            }
        }
        canvas.drawText("" + points , 20 , TEXT_SIZE , textPaint);
        if (life == 2) {
            healthPaint.setColor(Color.YELLOW);
        } else if ( life == 1) {
            healthPaint.setColor(Color.RED);
        }
        canvas.drawRect(dWidth - 200, 30, dWidth-200 + 60 * life , 80, healthPaint);
        for (int i=0; i<numBricks; i++) {
            if (blockBreakerBricks[i].isVisible()) {
                if (ballX + ballWidth >= blockBreakerBricks[i].column * blockBreakerBricks[i].width && ballX <= blockBreakerBricks[i].column * blockBreakerBricks[i].width + blockBreakerBricks[i].width && ballY <= blockBreakerBricks[i].row * blockBreakerBricks[i].height + blockBreakerBricks[i].height && ballY >= blockBreakerBricks[i].row * blockBreakerBricks[i].height) {
                    blockBreakerVelocity.setY((blockBreakerVelocity.getY() + 1) * -1);
                    blockBreakerBricks[i].setInvisible();
                    points += Constants.BLOCK_BREAKER_SCORE;
                    brokenBricks++;
                    if (brokenBricks == 24) {
                     launchGameOver();
                    }
                }
            }
        }
        if (brokenBricks == numBricks) {
            gameOver = true;
        }
        if (!gameOver) {
            handler.postDelayed(runnable, UPDATE_MILLIS);
        }
    }

    private void drawBackground(Canvas canvas) {
        Bitmap background;
        background = BitmapFactory.decodeResource(getResources(), R.drawable.block_breaker_game_bg);
        Rect backgroundRect = new Rect(0, 0, dWidth,dHeight+getNavigationBarHeight());
        canvas.drawBitmap(background,null,backgroundRect,null);
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
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float touchX = event.getX();
        float touchY = event.getY();
        if (touchY >= paddleY) {
            int action = event.getAction();
            if (action == MotionEvent.ACTION_DOWN) {
                oldX = event.getX();
                oldPaddleX = paddleX;
            }
            if (action == MotionEvent.ACTION_MOVE) {
                float shift = oldX - touchX;
                float newPaddleX = oldPaddleX - shift;
                if (newPaddleX <= 0) {
                    paddleX = 0;
                } else if (newPaddleX >= dWidth - paddle.getWidth()) {
                    paddleX = dWidth - paddle.getWidth();
                } else {
                    paddleX = newPaddleX;
                }
            }
        }
        return true;
    }

    private void launchGameOver() {
        handler.removeCallbacksAndMessages(null);
        Intent intent = new Intent(context , BlockBreakerGameOver.class);
        intent.putExtra(Constants.INTENT_KEY_SCORE, points);
        context.startActivity(intent);
        ((Activity) context).finish();
    }

    private int xVelocity() {
        int index = random.nextInt(Constants.RANDOM_VELOCITIES.length);
        return Constants.RANDOM_VELOCITIES[index];
    }
}
