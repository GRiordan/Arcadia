package com.example.george.arcadia.pong;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Handler;
import android.view.SurfaceHolder;

/**
 * Created by George on 21/09/2016.
 */

public class GameThread extends Thread {

    private boolean isRunning;
    private SurfaceHolder surfaceHolder;
    private Paint paint;
    private GameState state;
    private PongView view;

    public GameThread(SurfaceHolder surfaceHolder, Context context, Handler handler, PongView view)
    {
        this.surfaceHolder = surfaceHolder;
        paint = new Paint();
        state = new GameState(context);
        this.view = view;
        isRunning = true;
    }

    @Override
    public void run() {
        while(isRunning)
        {
            Canvas canvas = surfaceHolder.lockCanvas();
            state.update();

            if(state.checkWinCond()){
                System.out.println("here");
                isRunning = false;
                break;
            }

            state.draw(canvas,paint);
            surfaceHolder.unlockCanvasAndPost(canvas);
        }
        String winner = state.getWinner();
        view.displayWinDialog(winner);
    }

    public void kill(){
        isRunning = false;
    }


    public GameState getGameState(){
        return state;
    }
}
