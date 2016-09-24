package com.example.george.arcadia.pong;

import android.graphics.Rect;

/**
 * Created by George on 21/09/2016.
 */

public abstract class GameObject {

    protected abstract void setX_pos(int new_pos);
    protected abstract void setY_pos(int new_pos);

    protected abstract int getX_pos();
    protected abstract int getY_pos();

    protected abstract Rect getRect();

    protected abstract void update(int x);

    public abstract int getWIDTH();

    public abstract PaddleObject.TYPE getType();
}
