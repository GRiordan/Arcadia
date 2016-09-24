package com.example.george.arcadia.pong;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.view.Display;
import android.view.MotionEvent;
import android.view.WindowManager;

/**
 * Created by George on 25/09/2016.
 */

public class GameLogic {
    private int sc_width;
    private int sc_height;

    private PaddleObject.TYPE winner;

    private final int SPEED = 10;
    private final double MAXANGLE = Math.toRadians(75.0);
    private final int PADDLEHEIGHT = 300;

    public GameLogic(Context context){
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        sc_width = size.x;
        sc_height = size.y;
    }

    public boolean motionDetected(MotionEvent event, GameState state) {
        // if the player touches start to move towards the x
        int x = (int) event.getX();

        int lower_x = state.getPlayer().getX_pos();
        int higher_x = state.getPlayer().getWIDTH()+state.getPlayer().getX_pos();

        // check to make sure the users finger is in the same x pos
        if(x > state.getPlayer().getX_pos() && x < state.getPlayer().getWIDTH()+state.getPlayer().getX_pos()){
            state.updatePaddle(state.getPlayer().getType(),(x-(higher_x-lower_x)/2));
        }

        // = (int)x – (_paddleLength/2);
        return true;
    }

    public void update(GameState state){
        int ball_x = state.getBall().getX_pos();
        int ball_y = state.getBall().getY_pos();

        // check to see if its past the paddles
        //checkWinCond(ball_y, player.getY_pos(), enemy.getY_pos());

        // if the ball hits a wall change its direction
        if ((ball_x+state.getBall().getRADIUS()) >= sc_width || ball_x <= 0){
            //change the balls dx to minus
            state.changeBallDX();
        }

        // check if the ball has hit a paddle, if true set its new dx,dy
        checkPaddles(state);


        // update the AI, move towards the balls current x pos
        state.getEnemy().moveTowards(ball_x);
    }

    public void draw(Canvas canvas, Paint paint, GameState state) {

        //Clear the screen
        canvas.drawRGB(20, 20, 20);

        //set the colour
        //paint.setColor();
        paint.setARGB(200, 0, 200, 0);

        //draw the ball
        canvas.drawRect(state.getBall().getRect(), paint);

        //draw the bats
        canvas.drawRect(state.getPlayer().getRect(), paint);
        canvas.drawRect(state.getEnemy().getRect(), paint);
    }

    private void checkPaddles(GameState state) {
        int player_x = state.getPlayer().getX_pos();
        int player_y = state.getPlayer().getY_pos();
        int enemy_x = state.getEnemy().getX_pos();
        int enemy_y = state.getEnemy().getY_pos();
        int ball_y = state.getBall().getY_pos();

        int middleBall_x = state.getBall().getX_pos()+ (state.getBall().getRADIUS()/2);
        int middleBall_y = state.getBall().getY_pos()+ (state.getBall().getRADIUS()/2);

        // if the ball hits a paddle change its dy, dx depending on where it hits
        if((player_x < middleBall_x && player_x+state.getPlayer().getWIDTH() > middleBall_x)
                && (ball_y+state.getBall().getRADIUS() > player_y && ball_y+state.getBall().getRADIUS() < player_y+state.getPlayer().getHEIGHT())){
            // has hit the player paddle so calc new angle and directions
            updateDYDX(state, state.getPlayer(),middleBall_x,player_x);
        }
        if((middleBall_x > enemy_x && middleBall_x < enemy_x+state.getEnemy().getWIDTH())
                && (ball_y > enemy_y && ball_y <= enemy_y+state.getEnemy().getHEIGHT())){
            // has hit the enemy paddle so calc new angle and directions
            updateDYDX(state, state.getEnemy(),middleBall_x,enemy_x);
        }
        state.updateBall();
    }

    private void updateDYDX(GameState state, PaddleObject obj, int obj_middle_x, int obj_x){
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
        state.updateBallDXDY((int)ball_dx, (int)ball_dy);
    }

    public boolean checkWinCond(GameState state){
        int ball_y = state.getBall().getY_pos();
        int player_y = state.getPlayer().getY_pos();
        int enemy_y = state.getEnemy().getY_pos();
        // check if ball is below enemy paddle
        if(ball_y < 0){
            state.decrementPoints(PaddleObject.TYPE.ENEMY);
            state.newBall();
        }
        // check if ball is below player paddle
        else if(ball_y > sc_height){
            state.decrementPoints(PaddleObject.TYPE.PLAYER);
            state.newBall();
        }

        // check if enemy or player is below 0 or less points
        if(state.getPlayerPoints() <= 0){
            //enemy wins, display dialog
            state.setWinner(PaddleObject.TYPE.ENEMY);
            return true;
        }
        else if(state.getEnemyPoints() <= 0){
            //player wins, display dialog
            state.setWinner(PaddleObject.TYPE.PLAYER);
            return true;
        }
        return false;
    }
}
