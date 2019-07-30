package com.example.customviewtestproject.customView;

import android.graphics.*;

public class DashedCircleProgressPainter implements Painter {

    private RectF progressCircle;
    private Paint progressPaint;
    private Paint progressDotPaint;
    private Paint textPaint;
    private int textSize;
    private int color;
    private int plusAngle = 0;
    private int strokeWidth;
    private float dotRadius;
    private int min;
    private int max;
    private int width;
    private int height;
    private int padding;
    private int segmentsCount;

    DashedCircleProgressPainter(int color, int textSize, int min, int max, int progressStrokeWidth, int defaultPadding, int segmentsCount) {
        this.color = color;
        this.textSize = textSize;
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
        int dashWith = 5;
        int dashSpace = 8;
        progressPaint.setPathEffect(new DashPathEffect(new float[]{dashWith, dashSpace},
                dashSpace));

        progressDotPaint = new Paint();
        progressDotPaint.setAntiAlias(true);
        progressDotPaint.setStrokeCap(Paint.Cap.ROUND);
        int dotStrokeWidth = strokeWidth;
        progressDotPaint.setStrokeWidth(dotStrokeWidth);
        progressDotPaint.setColor(color);
        progressDotPaint.setStyle(Paint.Style.FILL_AND_STROKE);

        textPaint = new Paint();
        textPaint.setColor(Color.WHITE);
        textPaint.setStyle(Paint.Style.FILL);
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setTextSize(textSize);
    }

    private void initDashedCircleProgress() {
        progressCircle = new RectF();
        float halfOffset = strokeWidth / 2f;
        progressCircle.left = halfOffset + strokeWidth;
        progressCircle.top = halfOffset + strokeWidth;
        progressCircle.right = width - halfOffset - strokeWidth;
        progressCircle.bottom = height - halfOffset - strokeWidth;
        dotRadius = progressCircle.width() / 2f;
    }

    @Override
    public void draw(Canvas canvas) {
        int startAngle = 0;
        canvas.drawArc(progressCircle, startAngle, plusAngle, false, progressPaint);
        double angleRadians = Math.toRadians(startAngle + plusAngle + 180);
        float cos = (float) Math.cos(angleRadians);
        float sin = (float) Math.sin(angleRadians);
        float x = progressCircle.centerX() - dotRadius * cos;
        float y = progressCircle.centerY() - dotRadius * sin;
        canvas.drawPoint(x, y, progressDotPaint);

        String text = plusAngle + "°";
        canvas.drawText(text, progressCircle.centerX() - (textPaint.descent() + textPaint.ascent()) / 2 * textPaint.getTextSkewX(), progressCircle.centerY() - ((textPaint.descent() + textPaint.ascent()) / 2), textPaint);
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
        this.plusAngle = (value * segmentsCount) % 360;
    }

    @Override
    public void onSizeChanged(int height, int width) {
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
        progressDotPaint.setColor(color);
    }
}
