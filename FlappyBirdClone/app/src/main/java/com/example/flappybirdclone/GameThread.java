package com.example.flappybirdclone;

import android.graphics.Canvas;
import android.os.SystemClock;
import android.util.Log;
import android.view.SurfaceHolder;

public class GameThread extends Thread {
    private SurfaceHolder surfaceHolder;
    private boolean isRunning = true;
    private long startTime, loopTime;
    private final long DELAY = 33; // ~30 FPS

    public GameThread(SurfaceHolder surfaceHolder) {
        this.surfaceHolder = surfaceHolder;
    }

    @Override
    public void run() {
        while (isRunning) {
            Canvas canvas = surfaceHolder.lockCanvas();
            if (canvas != null) {
                synchronized (surfaceHolder) {
                    if (AppConstants.getGameEngine().gameState == 1) {
                        AppConstants.getGameEngine().updateAndDrawableBackgroundImage(canvas);
                        AppConstants.getGameEngine().updateAndDrawBird(canvas);
                        AppConstants.getGameEngine().updateAndDrawPipes(canvas);
                    }

                    // VẼ luôn mọi frame
                    AppConstants.getGameEngine().draw(canvas); // gọi vẽ toàn bộ (kể cả Game Over UI)

                    surfaceHolder.unlockCanvasAndPost(canvas);
                }
            }

            try {
                Thread.sleep(33);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    public boolean isRunning() {
        return isRunning;
    }

    public void setRunning(boolean state) {
        isRunning = state;
    }
}
