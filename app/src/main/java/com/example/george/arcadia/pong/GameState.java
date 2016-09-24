package com.example.george.arcadia.pong;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.MotionEvent;
import android.view.WindowManager;

import com.example.george.arcadia.R;

import java.io.Console;

import static java.lang.Math.random;
import static java.lang.Math.sqrt;

/**
 * Created by George on 21/09/2016.
 */

public class GameState {
    private BallObject ball;
    private PaddleObject enemy;
    private PaddleObject player;
    private Context context;
    private int sc_width;
    private int sc_height;
    private int enemyPoints;
    private int playerPoints;
    private PaddleObject.TYPE winner;

    private final int SPEED = 10;
    private final double MAXANGLE = Math.toRadians(75.0);
    private final int PADDLEHEIGHT = 300;

    public GameState(Context context){
        this.context = context;

        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        sc_width = size.x;
        sc_height = size.y;

        // init the objects with starting positions
        ball = new BallObject(sc_width/2, sc_height/2);
        enemy = new PaddleObject(210, PADDLEHEIGHT);
        player = new PaddleObject(250, sc_height - PADDLEHEIGHT);


        enemy.setType(PaddleObject.TYPE.ENEMY);
        player.setType(PaddleObject.TYPE.PLAYER);

        enemyPoints = 2;
        playerPoints = 2;
    }

    public void reset(){
        ball = new BallObject(sc_width/2, sc_height/2);
        enemy = new PaddleObject(210, PADDLEHEIGHT);
        player = new PaddleObject(250, sc_height - PADDLEHEIGHT);
        enemyPoints = 2;
        playerPoints = 2;
    }

    public void updatePaddle(PaddleObject.TYPE type, int newX){
        if(type == PaddleObject.TYPE.PLAYER){
            player.update(newX);
        } else if (type == PaddleObject.TYPE.ENEMY){
            enemy.update(newX);
        }
    }

    public void updateBallDXDY(int dx, int dy){
        System.out.println("here " + dx + " " + dy);
        ball.setDXDY(dx,dy);
    }

    public void updateBall(){
        ball.update();
    }

    public void changeBallDX(){
        ball.changeDX();
    }

    public BallObject getBall(){
        return ball;
    }

    public void newBall(){
        ball = new BallObject(sc_width/2, sc_height/2);
    }

    public PaddleObject getEnemy(){
        return enemy;
    }

    public PaddleObject getPlayer(){
        return player;
    }

    public int getEnemyPoints(){
        return enemyPoints;
    }

    public int getPlayerPoints(){
        return playerPoints;
    }

    public void decrementPoints(PaddleObject.TYPE type){
        if(type == PaddleObject.TYPE.ENEMY){
            playerPoints--;
        }
        else if(type == PaddleObject.TYPE.PLAYER){
            enemyPoints--;
        }
    }

    public PaddleObject.TYPE getWinner(){
        return winner;
    }

    public void setWinner(PaddleObject.TYPE type){
        winner = type;
    }



}

