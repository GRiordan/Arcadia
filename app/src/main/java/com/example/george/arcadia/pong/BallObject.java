package com.example.george.arcadia.pong;

import android.graphics.Rect;


/**
 * Created by George on 21/09/2016.
 */

public class BallObject extends GameObject {

    public enum DIR {
        PLUS, MINUS
    }

    private DIR dir_x;
    private DIR dir_y;
    private int x_pos;
    private int y_pos;
    private int dy;
    private int dx;
    private Rect ball;

    private final int RADIUS = 45;

    public BallObject(int x_pos, int y_pos){
        this.dir_x = DIR.PLUS;
        this.dir_y = DIR.PLUS;
        this.y_pos = y_pos;
        this.x_pos = x_pos;
        dy = 5;
        dx = 0;
        ball = new Rect(x_pos, y_pos, x_pos+RADIUS, y_pos+RADIUS);
    }

    protected void setX_pos(int new_pos){
        x_pos = new_pos;
    }

    protected void setY_pos(int new_pos){
        y_pos = new_pos;
    }

    protected int getX_pos(){
        return x_pos;
    }

    protected int getY_pos(){
        return y_pos;
    }

    protected int getDX(){
        return dx;
    }

    protected int getDY(){
        return dy;
    }

    protected Rect getRect(){
        return ball;
    }

    protected void update(int x){
        if(dir_x == DIR.PLUS){
            x_pos += x;
        }
        else if(dir_x == DIR.MINUS){
            x_pos -= x;
        }

        ball = new Rect(x_pos, y_pos, x_pos+RADIUS, y_pos+RADIUS);
    }

    public void changeDX(){
        dx = dx * (-1);
    }

    public void update(){
        y_pos += dy;
        x_pos += dx;
        ball = new Rect(x_pos, y_pos, x_pos+RADIUS, y_pos+RADIUS);
    }


    public void setDirX(DIR dir){
        switch (dir){
            case MINUS:
                dir_x = DIR.MINUS;
                break;
            case PLUS:
                dir_x = DIR.PLUS;
                break;
        }
    }

    public void setDirY(DIR dir){
        switch (dir){
            case MINUS:
                dir_y = DIR.MINUS;
                break;
            case PLUS:
                dir_y = DIR.PLUS;
                break;
        }
    }

    public void setDXDY(int dx, int dy){
        this.dx = dx;
        this.dy = dy;
    }

    public int getWIDTH(){
        return RADIUS;
    }

    public int getRADIUS(){
        return RADIUS;
    }

    @Override
    public PaddleObject.TYPE getType() {
        return null;
    }
}
