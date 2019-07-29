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
    private Interpolator interpolator = new AccelerateDecelerateInterpolator();
    private ValueAnimator valueAnimator;
    private OnValueChangeListener valueChangeListener;
    private int externalColor;
    private int internalBaseColor;
    private int progressColor;
    private int min = 0;
    private int last = min;
    private int max = 360;
    private int value;
    private int progressStrokeWidth = 48;
    private int defaultPadding = 24;

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
        externalColor = attributes.getColor(R.styleable.CustomProgressView_external_color,
                externalColor);
        internalBaseColor = attributes.getColor(R.styleable.CustomProgressView_base_color,
                internalBaseColor);
        progressColor = attributes.getColor(R.styleable.CustomProgressView_progress_color,
                progressColor);
        max = attributes.getInt(R.styleable.CustomProgressView_max, max);
        progressStrokeWidth = attributes.getInt(R.styleable.CustomProgressView_progress_stroke_width,
                progressStrokeWidth);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        dashedCircleProgressPainter.onSizeChanged(h, w);
        dashedCirclePainter.onSizeChanged(h, w);
        animateValue();
    }

    private void initPainters() {
        dashedCircleProgressPainter = new DashedCircleProgressPainter(progressColor, min, max, progressStrokeWidth, defaultPadding);
        dashedCirclePainter = new DashedCirclePainter(internalBaseColor, min, max, progressStrokeWidth, defaultPadding);
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
        dashedCircleProgressPainter.setMin(min);
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
        dashedCircleProgressPainter.setMax(max);
    }

    private class ValueAnimatorListenerImp implements ValueAnimator.AnimatorUpdateListener {
        @Override
        public void onAnimationUpdate(ValueAnimator valueAnimator) {
            Integer value = (Integer) valueAnimator.getAnimatedValue();
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

    public int getProgressColor() {
        return progressColor;
    }

    public void setProgressColor(int progressColor) {
        this.progressColor = progressColor;
        dashedCircleProgressPainter.setColor(progressColor);
    }

    public int getInternalBaseColor() {
        return internalBaseColor;
    }

    public void setInternalBaseColor(int internalBaseColor) {
        this.internalBaseColor = internalBaseColor;
        dashedCirclePainter.setColor(internalBaseColor);
    }
}