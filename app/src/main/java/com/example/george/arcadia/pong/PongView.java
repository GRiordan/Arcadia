package com.example.george.arcadia.pong;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
    private GameThread thread;
    private Context context;
    private PongActivity parent;

    public PongView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;

        //So we can listen for events...
        SurfaceHolder holder = getHolder();
        holder.addCallback(this);
        setFocusable(true);

        //and instantiate the thread
        thread = new GameThread(holder, context, new Handler(), this);
    }

    public boolean onTouchEvent(MotionEvent event) {
        return thread.getGameState().motionDetected(event);
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
        thread.start();
    }

    //Implemented as part of the SurfaceHolder.Callback interface
    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
    }

    public void setParent(PongActivity parent){
        this.parent = parent;
    }

    public void displayWinDialog(String winner){
        thread.interrupt();
        parent.displayWinDialogFragment(winner);
    }
}
