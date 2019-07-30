package com.example.customviewtestproject.customView;

import android.graphics.*;

public class DashedCircleProgressPainter implements Painter {

    private RectF progressCircle;
    private Paint progressPaint;
    private Paint progressDotPaint;
    private Paint horizontalLinePaint;
    private Paint progressHorizontalPaint;
    private Paint textAnglePaint;
    private Paint textLevelPaint;
    private int textSize;
    private int color;
    private int plusAngle = 0;
    private int level = 0;
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
        int dotStrokeWidth = strokeWidth - dashWith;
        progressDotPaint.setStrokeWidth(dotStrokeWidth);
        progressDotPaint.setColor(color);
        progressDotPaint.setStyle(Paint.Style.FILL_AND_STROKE);

        textAnglePaint = new Paint();
        textAnglePaint.setColor(Color.WHITE);
        textAnglePaint.setStyle(Paint.Style.FILL);
        textAnglePaint.setTextAlign(Paint.Align.CENTER);
        textAnglePaint.setTextSize(textSize);

        horizontalLinePaint = new Paint();
        horizontalLinePaint.setAntiAlias(true);
        horizontalLinePaint.setStrokeCap(Paint.Cap.ROUND);
        horizontalLinePaint.setStrokeWidth(dotStrokeWidth / 2f);
        horizontalLinePaint.setColor(Color.DKGRAY);
        horizontalLinePaint.setStyle(Paint.Style.FILL_AND_STROKE);

        progressHorizontalPaint = new Paint();
        progressHorizontalPaint.setAntiAlias(true);
        progressHorizontalPaint.setStrokeCap(Paint.Cap.ROUND);
        progressHorizontalPaint.setStrokeWidth(dotStrokeWidth / 2f);
        progressHorizontalPaint.setColor(color);
        progressHorizontalPaint.setStyle(Paint.Style.FILL_AND_STROKE);

        textLevelPaint = new Paint();
        textLevelPaint.setColor(color);
        textLevelPaint.setStyle(Paint.Style.FILL);
        textLevelPaint.setTextAlign(Paint.Align.CENTER);
        textLevelPaint.setTextSize((float) textSize / 4);
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

        String textAngle = plusAngle + "°";
        canvas.drawText(textAngle, progressCircle.centerX() - (textAnglePaint.descent() + textAnglePaint.ascent()) / 2 * textAnglePaint.getTextSkewX(), progressCircle.centerY() - ((textAnglePaint.descent() + textAnglePaint.ascent()) / 2), textAnglePaint);
        canvas.drawLine(
                progressCircle.centerX() - progressCircle.centerX() / 3f,
                progressCircle.centerY() - progressCircle.centerY() / 3f,
                progressCircle.centerX() + progressCircle.centerX() / 3f,
                progressCircle.centerY() - progressCircle.centerY() / 3f,
                horizontalLinePaint);

        float startX = progressCircle.centerX() - progressCircle.centerX() / 3f;
        float stopX = progressCircle.centerX() + progressCircle.centerX() / 3f;

        canvas.drawLine(
                startX,
                progressCircle.centerY() - progressCircle.centerY() / 3f,
                startX + (stopX * plusAngle / max / (stopX / startX)),
                progressCircle.centerY() - progressCircle.centerY() / 3f,
                progressHorizontalPaint);

        String textLevel = "LEVEL " + level;
        canvas.drawText(textLevel,
                progressCircle.centerX() - (textLevelPaint.descent() + textLevelPaint.ascent()) / 2 * textLevelPaint.getTextSkewX(),
                (progressCircle.centerY() - progressCircle.centerY() / 3f) - (2f * strokeWidth) - (textLevelPaint.descent() + textLevelPaint.ascent()),
                textLevelPaint);
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
        level = calculateLevel(value);
        plusAngle = calculateAngle(value);
    }

    private int calculateLevel(int value) {
        int level = 0;
        if (value > 0) {
            level = value / (max / segmentsCount);
        }
        if (level >= segmentsCount) {
            return level;
        }
        level++;
        return level;
    }

    private int calculateAngle(int value) {
        int calculatedAngle = (value * segmentsCount) % max;
        if (value > 0 && calculatedAngle == 0) {
            calculatedAngle = max;
        }
        return calculatedAngle;
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
