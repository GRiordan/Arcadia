package com.example.george.arcadia.pong;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.shapes.Shape;

/**
 * Created by George on 21/09/2016.
 */

public class PaddleObject extends GameObject {

    public enum TYPE {
        PLAYER, ENEMY, NONE
    }

    private TYPE type;
    private int x_pos;
    private int y_pos;
    private Rect paddle;

    private final int WIDTH = 200;
    private final int HEIGHT = 45;

    public PaddleObject(int x_pos, int y_pos){
        this.x_pos = x_pos;
        this.y_pos = y_pos;
        paddle = new Rect(x_pos, y_pos, x_pos+WIDTH, y_pos+HEIGHT);
        type = TYPE.PLAYER;

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

    protected Rect getRect(){
        return paddle;
    }

    public int getWIDTH(){
        return WIDTH;
    }

    protected int getHEIGHT() {
        return HEIGHT;
    }

    protected void update(int x){
        this.x_pos = x;
        paddle = new Rect(x, y_pos, x_pos+WIDTH, y_pos+HEIGHT);
    }

    public void moveTowards(int x){
        if(x < x_pos){
            x_pos -= 6;
        }
        else if(x > x_pos){
            x_pos += 6;
        }
        paddle = new Rect(x_pos, y_pos, x_pos+WIDTH, y_pos+HEIGHT);
    }

    public TYPE getType(){
        return this.type;
    }

    public void setType(TYPE type){
        this.type = type;
    }

    public String toString(){
        if(type == TYPE.PLAYER) return "Player";
        else return "Enemy";
    }

}
