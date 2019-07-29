package com.example.customviewtestproject.customView;

import android.graphics.Canvas;

public interface Painter {

    void draw(Canvas canvas);

    void setColor(int color);

    int getColor();

    void onSizeChanged(int height, int width);
}
