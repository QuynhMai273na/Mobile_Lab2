package com.example.flappybirdclone;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.Color;
import android.graphics.Paint;

import java.util.ArrayList;

public class GameEngine {
    BackgroundImage backgroundImage;
    Bird bird;
    static int gameState;
    private ArrayList<Pipe> pipes;
    private int pipeGap = 400;
    private int pipeWidth = 200;

    public GameEngine() {
        backgroundImage = new BackgroundImage();
        bird = new Bird();
        pipes = new ArrayList<>();

        // 0 = chưa bắt đầu | 1 = đang chơi | 2 = game over
        gameState = 0;
        addPipe(); // khởi tạo pipe đầu tiên
    }

    private void addPipe() {
        int screenHeight = AppConstants.SCREEN_HEIGHT;
        int randomY = (int)(Math.random() * (screenHeight - pipeGap - 200)) + 100;

        // Cột trên
        pipes.add(new Pipe(AppConstants.SCREEN_WIDTH, 0, pipeWidth, randomY, true));
        // Cột dưới
        pipes.add(new Pipe(AppConstants.SCREEN_WIDTH, randomY + pipeGap, pipeWidth,
                screenHeight - (randomY + pipeGap), false));
    }

    public void updateAndDrawableBackgroundImage(Canvas canvas) {
        backgroundImage.setX(backgroundImage.getX() - backgroundImage.getVelocity());
        if (backgroundImage.getX() < -AppConstants.getBitmapBank().getBackgroundWidth()) {
            backgroundImage.setX(0);
        }

        canvas.drawBitmap(AppConstants.getBitmapBank().getBackground_game(), backgroundImage.getX(),
                backgroundImage.getY(), null);

        if (backgroundImage.getX() < -(AppConstants.getBitmapBank().getBackgroundWidth() - AppConstants.SCREEN_WIDTH)) {
            canvas.drawBitmap(AppConstants.getBitmapBank().getBackground_game(),
                    backgroundImage.getX() + AppConstants.getBitmapBank().getBackgroundWidth(),
                    backgroundImage.getY(), null);
        }
    }

    public void updateAndDrawBird(Canvas canvas) {
        // Cho chim rơi nếu đang chơi hoặc đã game over
        if (gameState == 1 || gameState == 2) {
            if (bird.getY() < (AppConstants.SCREEN_HEIGHT - AppConstants.getBitmapBank().getBirdHeight())
                    || bird.getVelocity() < 0) {
                bird.setVelocity(bird.getVelocity() + AppConstants.gravity);
                bird.setY(bird.getY() + bird.getVelocity());
            }
        }

        // Vẽ chim ra canvas
        int currentFrame = bird.getCurrentFrame();
        canvas.drawBitmap(AppConstants.getBitmapBank().getBird(currentFrame), bird.getX(), bird.getY(), null);

        currentFrame++;
        if (currentFrame >= Bird.maxFrame) {
            currentFrame = 0;
        }
        bird.setCurrentFrame(currentFrame);
    }

    public void updateAndDrawPipes(Canvas canvas) {
        // Kiểm tra va chạm TRƯỚC khi update pipe
        if (gameState == 1) {
            Rect birdRect = new Rect(
                    bird.getX(),
                    bird.getY(),
                    bird.getX() + AppConstants.getBitmapBank().getBirdWidth(),
                    bird.getY() + AppConstants.getBitmapBank().getBirdHeight()
            );

            for (Pipe pipe : pipes) {
                Rect pipeRect = pipe.getRect();
                if (Rect.intersects(birdRect, pipeRect)) {
                    gameState = 2; // Chim đụng ống → Game Over
                    break;
                }
            }

            // Chim chạm đất
            if (bird.getY() + AppConstants.getBitmapBank().getBirdHeight() >= AppConstants.SCREEN_HEIGHT) {
                gameState = 2;
            }
        }

        // Pipe vẫn update và vẽ kể cả khi Game Over
        if (gameState == 1 || gameState == 2) {
            for (Pipe pipe : pipes) {
                pipe.update();
                pipe.draw(canvas);
            }

            // Xoá pipe khi ra khỏi màn hình và thêm pipe mới
            if (!pipes.isEmpty() && pipes.get(0).getX() + pipeWidth < 0) {
                pipes.remove(0);
                pipes.remove(0); // mỗi lần add là 2 cột (trên + dưới)
                addPipe();
            }
        }
    }

    public void draw(Canvas canvas) {
        updateAndDrawableBackgroundImage(canvas);
        updateAndDrawBird(canvas);
        updateAndDrawPipes(canvas);

        if (gameState == 2) {
            drawGameOver(canvas);
        }
    }

    public void drawGameOver(Canvas canvas) {
        Paint backgroundPaint = new Paint();
        backgroundPaint.setColor(Color.argb(180, 0, 0, 0));
        canvas.drawRect(0, 0, AppConstants.SCREEN_WIDTH, AppConstants.SCREEN_HEIGHT, backgroundPaint);

        // Dòng chữ GAME OVER
        Paint textPaint = new Paint();
        textPaint.setColor(Color.WHITE);
        textPaint.setTextSize(120);
        textPaint.setFakeBoldText(true);
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setShadowLayer(8, 4, 4, Color.RED);
        canvas.drawText("GAME OVER", AppConstants.SCREEN_WIDTH / 2,
                AppConstants.SCREEN_HEIGHT / 2 - 100, textPaint);

        // Nút RESTART
        int buttonWidth = 400;
        int buttonHeight = 120;
        int centerX = AppConstants.SCREEN_WIDTH / 2;
        int centerY = AppConstants.SCREEN_HEIGHT / 2 + 100;

        Paint buttonPaint = new Paint();
        buttonPaint.setColor(Color.rgb(255, 204, 0)); // màu vàng sáng
        buttonPaint.setStyle(Paint.Style.FILL);
        canvas.drawRoundRect(centerX - buttonWidth / 2, centerY,
                centerX + buttonWidth / 2, centerY + buttonHeight,
                30, 30, buttonPaint);

        // Viền nút
        Paint borderPaint = new Paint();
        borderPaint.setColor(Color.RED);
        borderPaint.setStyle(Paint.Style.STROKE);
        borderPaint.setStrokeWidth(8);
        canvas.drawRoundRect(centerX - buttonWidth / 2, centerY,
                centerX + buttonWidth / 2, centerY + buttonHeight,
                30, 30, borderPaint);

        // Chữ RESTART
        Paint restartTextPaint = new Paint();
        restartTextPaint.setColor(Color.BLACK);
        restartTextPaint.setTextSize(60);
        restartTextPaint.setTextAlign(Paint.Align.CENTER);
        restartTextPaint.setFakeBoldText(true);
        canvas.drawText("RESTART", centerX, centerY + buttonHeight / 2 + 20, restartTextPaint);
    }


    // Xử lý chim nhảy khi chạm màn hình
    public void birdFlap() {
        bird.setVelocity(-35); // Chim bay lên
    }

    public void reset() {
        bird = new Bird();
        pipes.clear();
        addPipe();
    }

}
