package com.example.flappybirdclone;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class BitmapBank {
    Bitmap background_game;
    Bitmap [] bird;
    Bitmap topPipe;
    Bitmap bottomPipe;
    public BitmapBank(Resources resources){
        background_game = BitmapFactory.decodeResource(resources, R.drawable.background_game);
        background_game = scaleImage(background_game);
        bird = new Bitmap[4];
        bird [0] = BitmapFactory.decodeResource(resources, R.drawable.bird_frame1);
        bird [1] = BitmapFactory.decodeResource(resources, R.drawable.bird_frame2);
        bird [2] = BitmapFactory.decodeResource(resources, R.drawable.bird_frame3);
        bird [3] = BitmapFactory.decodeResource(resources, R.drawable.bird_frame4);

        topPipe = BitmapFactory.decodeResource(resources, R.drawable.top_pipe);
        bottomPipe = BitmapFactory.decodeResource(resources, R.drawable.bottom_pipe);

        // Scale ảnh pipe
        topPipe = scalePipeImage(topPipe);
        bottomPipe = scalePipeImage(bottomPipe);

    }

    public Bitmap getBird(int frame){
        return bird[frame];
    }

    public int getBirdWidth(){
        return bird[0].getWidth();
    }
    public int getBirdHeight(){
        return bird[0].getHeight();
    }

    public Bitmap getBackground_game(){
        return background_game;
    }
    public int getBackgroundWidth(){
        return background_game.getWidth();
    }
    public int getBackgroundHeight(){
        return background_game.getHeight();
    }

    public Bitmap getTopPipe() {
        return topPipe;
    }

    public Bitmap getBottomPipe() {
        return bottomPipe;
    }
    public Bitmap scaleImage(Bitmap bitmap){
        float widthHeightRatio = (float) getBackgroundWidth() /getBackgroundHeight();
        int backgroundScaleWidth = (int) (widthHeightRatio * AppConstants.SCREEN_HEIGHT);
        return Bitmap.createScaledBitmap(bitmap, backgroundScaleWidth, AppConstants.SCREEN_HEIGHT, false);
    }
    public Bitmap scalePipeImage(Bitmap bitmap) {
        int newWidth = AppConstants.SCREEN_WIDTH / 6;  // điều chỉnh chiều rộng của pipe (bằng 1/8 màn hình)
        float aspectRatio = (float) bitmap.getHeight() / bitmap.getWidth();
        int newHeight = (int) (newWidth * aspectRatio);  // giữ tỉ lệ ảnh ban đầu
        return Bitmap.createScaledBitmap(bitmap, newWidth, newHeight, false);
    }

}
