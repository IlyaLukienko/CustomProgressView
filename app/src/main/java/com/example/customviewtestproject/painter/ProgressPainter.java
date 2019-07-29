package com.example.customviewtestproject.painter;

import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.RectF;

public class ProgressPainter implements Painter {

    private RectF progressCircle;
    private Paint progressPaint;
    private int color;
    private int startAngle = 270;
    private int plusAngle = 0;
    private int internalStrokeWidth;
    private int dashWith = 5;
    private int dashSpace = 8;
    private int marginTop = 45;
    private float padding;
    private int min;
    private int max;
    private int width;
    private int height;

    public ProgressPainter(int color, int min, int max, int progressStrokeWidth) {
        this.color = color;
        this.min = min;
        this.max = max;
        this.internalStrokeWidth = progressStrokeWidth;
        init();
    }

    private void init() {
        initInternalCirclePainter();
    }

    private void initInternalCirclePainter() {
        progressPaint = new Paint();
        progressPaint.setAntiAlias(true);
        progressPaint.setStrokeWidth(internalStrokeWidth);
        progressPaint.setColor(color);
        progressPaint.setStyle(Paint.Style.STROKE);
        progressPaint.setPathEffect(new DashPathEffect(new float[]{dashWith, dashSpace},
                dashSpace));
    }

    private void initInternalCircle() {
        progressCircle = new RectF();
        padding = internalStrokeWidth * 1.7f;
        progressCircle.set(padding, padding + marginTop, width - padding, height - padding);
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawArc(progressCircle, startAngle, plusAngle, false, progressPaint);
    }

    public float getMin() {
        return min;
    }

    public void setMin(int min) {
        this.min = min;
    }

    public float getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public void setValue(int value) {
        this.plusAngle = (360 * value) / max;
    }

    @Override
    public void onSizeChanged(int height, int width) {
        this.width = width;
        this.height = height;
        initInternalCircle();
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
        progressPaint.setColor(color);
    }
}
