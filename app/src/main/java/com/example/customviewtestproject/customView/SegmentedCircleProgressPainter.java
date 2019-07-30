package com.example.customviewtestproject.customView;

import android.graphics.*;

public class SegmentedCircleProgressPainter implements Painter {

    private RectF progressCircle;
    private Paint progressPaint;
    private int color;
    private int plusAngle = 0;
    private int strokeWidth;
    private int min;
    private int max;
    private int width;
    private int height;
    private int padding;
    private int segmentsCount;

    SegmentedCircleProgressPainter(int color, int min, int max, int progressStrokeWidth, int defaultPadding, int segmentsCount) {
        this.color = color;
        this.min = min;
        this.max = max;
        this.strokeWidth = progressStrokeWidth;
        this.padding = defaultPadding;
        this.segmentsCount = segmentsCount;
        init();
    }

    private void init() {
        progressPaint = new Paint();
        progressPaint.setAntiAlias(true);
        progressPaint.setStrokeWidth(strokeWidth);
        progressPaint.setColor(color);
        progressPaint.setStyle(Paint.Style.STROKE);
    }

    private void initDashedCircleProgress() {
        progressCircle = new RectF();
        float halfOffset = strokeWidth / 2f;
        progressCircle.left = halfOffset;
        progressCircle.top = halfOffset;
        progressCircle.right = width - halfOffset;
        progressCircle.bottom = height - halfOffset;
    }

    @Override
    public void draw(Canvas canvas) {
        int startAngle = 0;
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
        if (this.width == 0) {
            float dashWith = 10;
            float dashHeight = (float) ((Math.PI * (width - 2 * strokeWidth)) / segmentsCount - dashWith);
            int dashSpace = 0;
            progressPaint.setPathEffect(new DashPathEffect(new float[]{dashHeight, dashWith},
                    dashSpace));
        }
        this.width = width - padding;
        this.height = height - padding;
        initDashedCircleProgress();
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
        progressPaint.setColor(color);
    }
}
