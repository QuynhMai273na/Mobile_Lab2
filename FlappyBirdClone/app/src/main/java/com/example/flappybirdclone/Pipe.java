package com.example.flappybirdclone;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

public class Pipe {
    private int x, y, width, height;
    private boolean isTop;

    public Pipe(int x, int y, int width, int height, boolean isTop) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.isTop = isTop;
    }

    public void draw(Canvas canvas) {
        Bitmap pipeBitmap;
        if (isTop) {
            pipeBitmap = AppConstants.getBitmapBank().getTopPipe();
            canvas.drawBitmap(pipeBitmap, x, y - height, null);
        } else {
            pipeBitmap = AppConstants.getBitmapBank().getBottomPipe();
            canvas.drawBitmap(pipeBitmap, x, y, null);
        }
    }

    public void update() {
        x -= 10; // tốc độ di chuyển pipe
    }

    public Rect getRect() {
        Bitmap pipeBitmap;
        if (isTop) {
            pipeBitmap = AppConstants.getBitmapBank().getTopPipe();
            return new Rect(x, y - pipeBitmap.getHeight(), x + pipeBitmap.getWidth(), y);
        } else {
            pipeBitmap = AppConstants.getBitmapBank().getBottomPipe();
            return new Rect(x, y, x + pipeBitmap.getWidth(), y + pipeBitmap.getHeight());
        }
    }

    public boolean isOffScreen() {
        return x + width < 0;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public boolean isTop() {
        return isTop;
    }
}
