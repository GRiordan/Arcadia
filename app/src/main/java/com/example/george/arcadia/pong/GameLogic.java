package com.example.george.arcadia.pong;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.view.Display;
import android.view.MotionEvent;
import android.view.WindowManager;

import com.example.george.arcadia.R;

/**
 * Created by George on 25/09/2016.
 */

public class GameLogic {
    private int sc_width;
    private int sc_height;
    private int playerPointer;
    private int enemyPointer;
    private boolean twoPlayer;

    private PaddleObject.TYPE winner;

    private final int SPEED = 12;
    private final double MAXANGLE = Math.toRadians(75.0);
    private final int PADDLEHEIGHT = 300;
    private final Rect PLAYERTOUCHBOX = new Rect(0, sc_height/2, sc_width, sc_height);
    private final Rect ENEMYTOUCHBOX = new Rect(0, sc_height, sc_width, sc_height/2);

    public GameLogic(Context context){
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        sc_width = size.x;
        sc_height = size.y;
    }

    public boolean motionDetected(MotionEvent event, GameState state) {
        twoPlayer = false;
        playerPointer = event.getPointerId(0);

        // if a second touch is registered and is within the
        // enemies touch box move their paddle
        if(event.getPointerCount() > 1){
            twoPlayer = true;
            enemyPointer = event.getPointerId(1);
            int secondTouchX = (int)event.getX(enemyPointer);
            int secondTouchY = (int)event.getY(enemyPointer);
            int loPaddleX = state.getEnemy().getX_pos();
            int hiPaddleX = state.getEnemy().getX_pos() + state.getEnemy().getWIDTH();
            if(secondTouchX > loPaddleX && secondTouchX < hiPaddleX
                    && secondTouchY < (sc_height/2)-1){
                state.updatePaddle(state.getEnemy().getType(),(secondTouchX-(hiPaddleX-loPaddleX)/2));
            }
        }

        // if the player touches in there touch box
        // start to move towards the x
        int x = (int) event.getX();
        int y = (int) event.getY();

        int lower_x = state.getPlayer().getX_pos();
        int higher_x = state.getPlayer().getWIDTH()+state.getPlayer().getX_pos();

        // check to make sure the users finger is in the same x pos
        if(x > state.getPlayer().getX_pos() && x < state.getPlayer().getWIDTH()+state.getPlayer().getX_pos()
                && y > sc_height/2){
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
        if(!twoPlayer) {
            state.getEnemy().moveTowards(ball_x);
        }
    }

    public void draw(Canvas canvas, Paint paint, GameState state) {

        //Clear the screen
        canvas.drawColor(Color.parseColor("#FFFFFF"));
        //canvas.drawRGB(20, 20, 20);


        //set the colour
        //paint.setColor();
        paint.setColor( Color.parseColor("#FF5722"));
        //draw the ball
        canvas.drawRect(state.getBall().getRect(), paint);

        //draw the bats
        canvas.drawRect(state.getPlayer().getRect(), paint);
        canvas.drawRect(state.getEnemy().getRect(), paint);

        Paint textPaint = new Paint(Color.parseColor("#000000"));
        textPaint.setTextSize(48);

        canvas.drawText("Life: " + state.getEnemyPoints(), (float)(sc_width-180), (float)(sc_height-400), textPaint);
        if(twoPlayer){
            canvas.scale(-1f, -1f, 180f, 400f);
            canvas.drawText("Life: " + state.getPlayerPoints(), 180f, (400), textPaint);
        }
        else {
            canvas.drawText("Life: " + state.getPlayerPoints(), 15f, (400), textPaint);
        }



    }

    private void checkPaddles(GameState state) {
        int player_x = state.getPlayer().getX_pos();
        int player_y = state.getPlayer().getY_pos();
        int enemy_x = state.getEnemy().getX_pos();
        int enemy_y = state.getEnemy().getY_pos();
        int ball_y = state.getBall().getY_pos();

        int middleBall_x = state.getBall().getX_pos()+ (state.getBall().getRADIUS()/2);

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
        double middle_paddle_x = obj_x+(obj.getWIDTH()/2);                        //get the middle x pos of the paddle
        double relativeBallX = obj_middle_x-middle_paddle_x;                  //get the relative x pos compared the paddle middle x
        double normalisedRelativeBallX = relativeBallX / (obj.getWIDTH()/2);    //normalise that so it is in the range -1 to +1
        double angle = normalisedRelativeBallX * MAXANGLE;                      //calc the angle based on that
        double ball_dx = SPEED * Math.sin(angle);
        double ball_dy = SPEED * Math.cos(angle);

        if(obj.getType() == PaddleObject.TYPE.PLAYER){ball_dy *= -1;}

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
