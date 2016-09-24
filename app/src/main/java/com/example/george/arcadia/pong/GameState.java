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
    private String winner;

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

    public boolean motionDetected(MotionEvent event) {
        // if the player touches start to move towards the x
        int x = (int) event.getX();

        int lower_x = player.getX_pos();
        int higher_x = player.getWIDTH()+player.getX_pos();


        // check to make sure the users finger is in the same x pos
        if(x > player.getX_pos() && x < player.getWIDTH()+player.getX_pos()){
            player.update(x-(higher_x-lower_x)/2);
        }

        // = (int)x – (_paddleLength/2);
        return true;
    }

    public void draw(Canvas canvas, Paint paint) {

        //Clear the screen
        canvas.drawRGB(20, 20, 20);

        //set the colour
        //paint.setColor();
        paint.setARGB(200, 0, 200, 0);

        //draw the ball
        canvas.drawRect(ball.getRect(), paint);

        //draw the bats
        canvas.drawRect(player.getRect(), paint);
        canvas.drawRect(enemy.getRect(), paint);
    }

    public void update(){
        int ball_x = ball.getX_pos();
        int ball_y = ball.getY_pos();

        // check to see if its past the paddles
        //checkWinCond(ball_y, player.getY_pos(), enemy.getY_pos());

        // if the ball hits a wall change its direction
        if ((ball_x+ball.getRADIUS()) >= sc_width || ball_x <= 0){
            //change the balls dx to minus
            ball.changeDX();
        }

        // check if the ball has hit a paddle, if true set its new dx,dy
        checkPaddles(ball_y);
        ball.update();

        // update the AI, move towards the balls current x pos
        enemy.moveTowards(ball_x);
    }

    private void checkPaddles(int ball_y) {
        int player_x = player.getX_pos();
        int player_y = player.getY_pos();
        int enemy_x = enemy.getX_pos();
        int enemy_y = enemy.getY_pos();

        int middleBall_x = ball.getX_pos()+ (ball.getRADIUS()/2);
        int middleBall_y = ball.getY_pos()+ (ball.getRADIUS()/2);

        // if the ball hits a paddle change its dy, dx depending on where it hits
        if((player_x < middleBall_x && player_x+player.getWIDTH() > middleBall_x)
                && (ball_y+ball.getRADIUS() > player_y && ball_y+ball.getRADIUS() < player_y+player.getHEIGHT())){
            // has hit the player paddle so calc new angle and directions
            calcDYDX(player,middleBall_x,player_x);
        }
        if((middleBall_x > enemy_x && middleBall_x < enemy_x+enemy.getWIDTH())
                && (ball_y > enemy_y && ball_y <= enemy_y+enemy.getHEIGHT())){
            // has hit the enemy paddle so calc new angle and directions
            calcDYDX(enemy,middleBall_x,enemy_x);
        }
    }

    private void calcDYDX(PaddleObject obj, int obj_middle_x, int obj_x){
        double middle_paddle_x = obj_x+obj.getWIDTH()/2;                        //get the middle x pos of the paddle
        double relativeBallX = middle_paddle_x - obj_middle_x;                  //get the relative x pos compared the paddle middle x
        double normalisedRelativeBallX = relativeBallX / (obj.getWIDTH()/2);    //normalise that so it is in the range -1 to +1
        double angle = normalisedRelativeBallX * MAXANGLE;                      //calc the angle based on that
        double ball_dx = SPEED * Math.cos(angle);
        double ball_dy = SPEED * Math.sin(angle);

        if(relativeBallX >= 0){
            if(obj.getType() == PaddleObject.TYPE.ENEMY){ball_dx *= -1;}
            else if(obj.getType() == PaddleObject.TYPE.PLAYER){ball_dy *= -1;}
        }
        else {
            if(obj.getType() == PaddleObject.TYPE.ENEMY){ball_dy *= -1;}
            else if(obj.getType() == PaddleObject.TYPE.PLAYER){ball_dx *= -1;}
        }
        ball.setDXDY((int)ball_dx, (int)ball_dy);
    }

    public boolean checkWinCond(){
        int ball_y = ball.getY_pos();
        int player_y = player.getY_pos();
        int enemy_y = enemy.getY_pos();
        // check if ball is below enemy paddle
        if(ball_y < enemy_y){
            enemyPoints -= 1;
            ball = new BallObject(sc_width/2, sc_height/2);
        }
        // check if ball is below player paddle
        else if(ball_y > player_y){
            playerPoints -= 1;
            ball = new BallObject(sc_width/2, sc_height/2);

        }

        // check if enemy or player is below 0 or less points
        if(playerPoints <= 0){
            //enemy wins, display dialog
            winner = "Enemy";
            return true;
        }
        else if(enemyPoints <= 0){
            //player wins, display dialog
            winner = "Player";
            return true;
        }
        return false;
    }

    public String getWinner(){
        return winner;
    }
}

