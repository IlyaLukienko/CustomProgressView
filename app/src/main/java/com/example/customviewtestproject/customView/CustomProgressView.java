package com.example.customviewtestproject.customView;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.RelativeLayout;
import com.example.customviewtestproject.R;

public class CustomProgressView extends RelativeLayout {

    private DashedCirclePainter dashedCirclePainter;
    private DashedCircleProgressPainter dashedCircleProgressPainter;
    private SegmentedCirclePainter segmentedCirclePainter;
    private SegmentedCircleProgressPainter segmentedCircleProgressPainter;
    private Interpolator interpolator = new AccelerateDecelerateInterpolator();
    private ValueAnimator valueAnimator;
    private OnValueChangeListener valueChangeListener;
    private int internalDashedBaseColor;
    private int internalSegmentedBaseColor;
    private int progressSegmentedColor;
    private int progressDashedColor;
    private int min = 0;
    private int last = min;
    private int max = 360;
    private int value;
    private int progressStrokeWidth = 48;
    private int defaultPadding = progressStrokeWidth;
    private int textSize;
    private int segmentsCount = 8;

    public CustomProgressView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public CustomProgressView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        final int count = getChildCount();
        int maxWidth = getWidth();
        int maxHeight = getHeight();

        for (int i = 0; i < count; i++) {
            final View child = getChildAt(i);

            int measureWidth = child.getMeasuredWidth();
            int measureHeight = child.getMeasuredWidth();

            ViewGroup.LayoutParams layoutParams = child.getLayoutParams();
            LayoutParams relativeLayoutParams =
                    (LayoutParams) child.getLayoutParams();
            relativeLayoutParams.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
            child.setLayoutParams(relativeLayoutParams);

            if (measureWidth > maxWidth) {
                layoutParams.width = maxWidth;
            }

            if (measureHeight > maxHeight) {
                layoutParams.height = maxHeight;
            }
        }
    }

    private void init(Context context, AttributeSet attributeSet) {
        setWillNotDraw(false);
        TypedArray attributes = context.obtainStyledAttributes(attributeSet,
                R.styleable.CustomProgressView);
        initAttributes(attributes);
        initPainters();
        initValueAnimator();
    }

    private void initAttributes(TypedArray attributes) {
        internalDashedBaseColor = attributes.getColor(R.styleable.CustomProgressView_base_dashed_color,
                internalDashedBaseColor);
        progressDashedColor = attributes.getColor(R.styleable.CustomProgressView_progress_dashed_color,
                progressDashedColor);
        internalSegmentedBaseColor = attributes.getColor(R.styleable.CustomProgressView_base_segmented_color,
                internalSegmentedBaseColor);
        progressSegmentedColor = attributes.getColor(R.styleable.CustomProgressView_progress_segmented_color,
                progressSegmentedColor);
        max = attributes.getInt(R.styleable.CustomProgressView_max, max);
        textSize = attributes.getInt(R.styleable.CustomProgressView_text_size, textSize);
        progressStrokeWidth = attributes.getInt(R.styleable.CustomProgressView_progress_stroke_width,
                progressStrokeWidth);
        segmentsCount = attributes.getInt(R.styleable.CustomProgressView_segments_count,
                segmentsCount);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        segmentedCirclePainter.onSizeChanged(h, w);
        segmentedCircleProgressPainter.onSizeChanged(h, w);
        dashedCirclePainter.onSizeChanged(h, w);
        dashedCircleProgressPainter.onSizeChanged(h, w);
        animateValue();
    }

    private void initPainters() {
        segmentedCircleProgressPainter = new SegmentedCircleProgressPainter(progressSegmentedColor, min, max, progressStrokeWidth, defaultPadding, segmentsCount);
        segmentedCirclePainter = new SegmentedCirclePainter(internalSegmentedBaseColor, min, max, progressStrokeWidth, defaultPadding, segmentsCount);
        dashedCircleProgressPainter =
                new DashedCircleProgressPainter(
                        progressDashedColor, (int) (textSize * getContext().getResources().getDisplayMetrics().scaledDensity),
                        min,
                        max,
                        progressStrokeWidth,
                        defaultPadding,
                        segmentsCount);
        dashedCirclePainter = new DashedCirclePainter(internalDashedBaseColor, min, max, progressStrokeWidth, defaultPadding);
    }

    private void initValueAnimator() {
        valueAnimator = new ValueAnimator();
        valueAnimator.setInterpolator(interpolator);
        valueAnimator.addUpdateListener(new ValueAnimatorListenerImp());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        dashedCirclePainter.draw(canvas);
        dashedCircleProgressPainter.draw(canvas);
        segmentedCirclePainter.draw(canvas);
        segmentedCircleProgressPainter.draw(canvas);
        invalidate();
    }

    public void setValue(int value) {
        this.value = value;
        if (value <= max || value >= min) {
            animateValue();
        }
    }

    private void animateValue() {
        if (valueAnimator != null) {
            valueAnimator.setDuration(0);
            valueAnimator.setIntValues(last, value);
            valueAnimator.start();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public void setOnValueChangeListener(OnValueChangeListener valueChangeListener) {
        this.valueChangeListener = valueChangeListener;
    }

    public void setInterpolator(Interpolator interpolator) {
        this.interpolator = interpolator;

        if (valueAnimator != null) {
            valueAnimator.setInterpolator(interpolator);
        }
    }

    public float getMin() {
        return min;
    }

    public void setMin(int min) {
        this.min = min;
        segmentedCircleProgressPainter.setMin(min);
        dashedCircleProgressPainter.setMin(min);
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
        segmentedCircleProgressPainter.setMax(max);
        dashedCircleProgressPainter.setMax(max);
    }

    private class ValueAnimatorListenerImp implements ValueAnimator.AnimatorUpdateListener {
        @Override
        public void onAnimationUpdate(ValueAnimator valueAnimator) {
            Integer value = (Integer) valueAnimator.getAnimatedValue();
            segmentedCircleProgressPainter.setValue(value);
            dashedCircleProgressPainter.setValue(value);

            if (valueChangeListener != null) {
                valueChangeListener.onValueChange(value);
            }

            last = value;
        }
    }

    public interface OnValueChangeListener {
        void onValueChange(float value);
    }

    public void reset() {
        last = min;
    }

    public void setDashedProgressColor(int progressColor) {
        this.internalDashedBaseColor = progressColor;
        dashedCircleProgressPainter.setColor(progressColor);
    }

    public void setSegmentedProgressColor(int progressColor) {
        this.progressSegmentedColor = progressColor;
        segmentedCircleProgressPainter.setColor(progressColor);
    }

    public void setDashedInternalBaseColor(int internalBaseColor) {
        this.internalDashedBaseColor = internalBaseColor;
        dashedCirclePainter.setColor(internalBaseColor);
    }

    public void setSegmentedInternalBaseColor(int internalBaseColor) {
        this.internalSegmentedBaseColor = internalBaseColor;
        segmentedCirclePainter.setColor(internalBaseColor);
    }

    public void setSegmentesCount(int segmentsCount) {
        this.segmentsCount = segmentsCount;
    }
}