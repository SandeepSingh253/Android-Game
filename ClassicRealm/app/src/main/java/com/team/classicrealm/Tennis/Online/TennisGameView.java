package com.team.classicrealm.Tennis.Online;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Point;
import android.os.Build;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.View;

import androidx.annotation.NonNull;

import com.team.classicrealm.GameUtility.Constants;


public class TennisGameView extends View {
    private final Context context;
    private Handler handler;
    private Runnable runnable;
    private Point displaySize;

    public TennisGameView(Context context) {
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
        int width= context.getResources().getDisplayMetrics().widthPixels;
        int height=context.getResources().getDisplayMetrics().heightPixels + getNavigationBarHeight();;
        displaySize.set(width,height);
        handler=new Handler();
    }

    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);

        handler.postDelayed(runnable,Constants.GAME_FPS/Constants.ONE_SEC);
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
