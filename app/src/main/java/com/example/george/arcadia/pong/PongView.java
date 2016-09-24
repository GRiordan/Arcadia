package com.example.george.arcadia.pong;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewParent;
import android.widget.Toast;

/**
 * Created by George on 21/09/2016.
 */

public class PongView extends SurfaceView implements SurfaceHolder.Callback {
    private Context context;
    private PongActivity parent;
    private SurfaceHolder holder;
    private GameLogic logic;
    private Handler handler;
    private GameState state;
    private Paint paint;

    public PongView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        handler = new Handler();
        //So we can listen for events...
        holder  = getHolder();
        state = new GameState(context);
        paint = new Paint();

        init();
    }

    public void init(){
        System.out.println("Here");
        handler.removeCallbacks(gameThread);
        handler.postDelayed(gameThread, 2000);
    }

    public void setLogic(GameLogic logic){
        this.logic = logic;
    }

    public boolean onTouchEvent(MotionEvent event) {
        return logic.motionDetected(event, state);
    }


    //Implemented as part of the SurfaceHolder.Callback interface
    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {
        //Mandatory, just swallowing it for this example

    }

    //Implemented as part of the SurfaceHolder.Callback interface
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        this.holder = holder;
        parent.setHolder(holder);
        parent.setGameState(state);
        holder.addCallback(this);
        setFocusable(true);
    }

    //Implemented as part of the SurfaceHolder.Callback interface
    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
    }

    public SurfaceHolder getHolder2(){
        return holder;
    }

    public void setParent(PongActivity parent){
        this.parent = parent;
    }

    public void displayWinDialog(String winner){
        parent.displayWinDialogFragment(winner);
    }

    private Runnable gameThread = new Runnable() {

        public void run() {

            if (state.getWinner() == null) {
                Canvas canvas = holder.lockCanvas();
                logic.update(state);
                logic.checkWinCond(state);
                logic.draw(canvas, paint, state);
                holder.unlockCanvasAndPost(canvas);
                handler.postDelayed(this, 1);
            } else {
                PaddleObject.TYPE winner = state.getWinner();
                if (winner == PaddleObject.TYPE.PLAYER) {
                    displayWinDialog("Player");
                } else {
                    displayWinDialog("Enemy");
                }
            }
        }
    };
}
