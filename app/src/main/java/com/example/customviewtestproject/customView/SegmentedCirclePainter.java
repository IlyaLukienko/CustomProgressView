package com.example.customviewtestproject.customView;

import android.graphics.*;

public class SegmentedCirclePainter implements Painter {

    private RectF segmentedCircle;
    private Paint segmentedCirclePaint;
    private int color;
    private int startAngle;
    private int finishAngle;
    private int width;
    private int height;
    private int strokeWidth;
    private int padding;
    private int segmentsCount;

    SegmentedCirclePainter(int color, int startAngle, int finishAngle, int strokeWidth, int padding, int segmentsCount) {
        this.color = color;
        this.startAngle = startAngle;
        this.finishAngle = finishAngle;
        this.strokeWidth = strokeWidth;
        this.padding = padding;
        this.segmentsCount = segmentsCount;
        init();
    }

    private void init() {
        initDashedCirclePainter();
    }

    private void initDashedCirclePainter() {
        segmentedCirclePaint = new Paint();
        segmentedCirclePaint.setAntiAlias(true);
        segmentedCirclePaint.setStrokeWidth(strokeWidth);
        segmentedCirclePaint.setColor(color);
        segmentedCirclePaint.setStyle(Paint.Style.STROKE);
    }

    private void initSegmentedCircle() {
        segmentedCircle = new RectF();
        float halfOffset = strokeWidth / 2f;
        segmentedCircle.left = halfOffset;
        segmentedCircle.top = halfOffset;
        segmentedCircle.right = width - halfOffset;
        segmentedCircle.bottom = height - halfOffset;
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawArc(segmentedCircle, startAngle, finishAngle, false, segmentedCirclePaint);
    }

    public void setColor(int color) {
        this.color = color;
        segmentedCirclePaint.setColor(color);
    }

    @Override
    public int getColor() {
        return color;
    }

    @Override
    public void onSizeChanged(int height, int width) {
        if (this.width == 0) {
            float dashWith = 10;
            float dashHeight = (float) ((Math.PI * (width - 2 * strokeWidth)) / segmentsCount - dashWith);
            int dashSpace = 0;
            segmentedCirclePaint.setPathEffect(new DashPathEffect(new float[]{dashHeight, dashWith},
                    dashSpace));
        }
        this.width = width - padding;
        this.height = height - padding;
        initSegmentedCircle();
    }
}
