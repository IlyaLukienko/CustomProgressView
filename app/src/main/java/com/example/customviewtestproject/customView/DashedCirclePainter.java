package com.example.customviewtestproject.customView;

import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.RectF;

public class DashedCirclePainter implements Painter {

    private RectF dashedCircle;
    private Paint dashedCirclePaint;
    private int color;
    private int startAngle;
    private int finishAngle;
    private int width;
    private int height;
    private int strokeWidth;
    private int padding;

    DashedCirclePainter(int color, int startAngle, int finishAngle, int strokeWidth, int defaultPadding) {
        this.color = color;
        this.startAngle = startAngle;
        this.finishAngle = finishAngle;
        this.strokeWidth = strokeWidth;
        this.padding = defaultPadding;
        init();
    }

    private void init() {
        initDashedCirclePainter();
    }

    private void initDashedCirclePainter() {
        dashedCirclePaint = new Paint();
        dashedCirclePaint.setAntiAlias(true);
        dashedCirclePaint.setStrokeWidth(strokeWidth);
        dashedCirclePaint.setColor(color);
        dashedCirclePaint.setStyle(Paint.Style.STROKE);
        int dashWith = 5;
        int dashSpace = 8;
        dashedCirclePaint.setPathEffect(new DashPathEffect(new float[]{dashWith, dashSpace},
                dashSpace));
    }

    private void initDashedCircle() {
        dashedCircle = new RectF();
        float halfOffset = strokeWidth / 2f;
        dashedCircle.left = halfOffset;
        dashedCircle.top = halfOffset;
        dashedCircle.right = width - halfOffset;
        dashedCircle.bottom = height - halfOffset;
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawArc(dashedCircle, startAngle, finishAngle, false, dashedCirclePaint);
    }

    public void setColor(int color) {
        this.color = color;
        dashedCirclePaint.setColor(color);
    }

    @Override
    public int getColor() {
        return color;
    }

    @Override
    public void onSizeChanged(int height, int width) {
        this.width = width - padding;
        this.height = height - padding;
        initDashedCircle();
    }
}
