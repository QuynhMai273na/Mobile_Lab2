package com.example.flappybirdclone;

import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.content.Context;

import androidx.annotation.NonNull;

public class GameView extends SurfaceView implements SurfaceHolder.Callback {
    GameThread gameThread;

    public GameView(Context context) {
        super(context);
        InitView();
    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder surfaceHolder) {
        gameThread = new GameThread(surfaceHolder);
        gameThread.setRunning(true);
        gameThread.start();
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {
        // Không cần xử lý gì ở đây cho game này
    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder surfaceHolder) {
        if (gameThread != null && gameThread.isRunning()) {
            gameThread.setRunning(false);
            boolean retry = true;
            while (retry) {
                try {
                    gameThread.join();
                    retry = false;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    void InitView() {
        SurfaceHolder holder = getHolder();
        holder.addCallback(this);
        setFocusable(true);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            int x = (int) event.getX();
            int y = (int) event.getY();

            int buttonWidth = 400;
            int buttonHeight = 120;
            int centerX = AppConstants.SCREEN_WIDTH / 2;
            int centerY = AppConstants.SCREEN_HEIGHT / 2 + 100;

            int left = centerX - buttonWidth / 2;
            int right = centerX + buttonWidth / 2;
            int top = centerY;
            int bottom = centerY + buttonHeight;

            if (AppConstants.getGameEngine().gameState == 2) {
                if (x >= left && x <= right && y >= top && y <= bottom) {
                    AppConstants.getGameEngine().reset(); // reset lại game
                    AppConstants.getGameEngine().gameState = 1;
                }
            } else if (AppConstants.getGameEngine().gameState == 0) {
                // Bắt đầu game
                AppConstants.getGameEngine().gameState = 1;
                AppConstants.getGameEngine().birdFlap();
            } else if (AppConstants.getGameEngine().gameState == 1) {
                // Chim nhảy
                AppConstants.getGameEngine().birdFlap();
            }
        }
        return true;
    }
}
