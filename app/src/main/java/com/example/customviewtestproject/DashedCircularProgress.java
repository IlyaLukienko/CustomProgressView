package com.example.customviewtestproject;

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
import com.example.customviewtestproject.painter.InternalCirclePainter;
import com.example.customviewtestproject.painter.ProgressPainter;

public class DashedCircularProgress extends RelativeLayout {

    private InternalCirclePainter internalCirclePainter;
    private ProgressPainter progressPainter;
    private Interpolator interpolator = new AccelerateDecelerateInterpolator();
    private ValueAnimator valueAnimator;
    private OnValueChangeListener valueChangeListener;
    private int externalColor;
    private int internalBaseColor;
    private int progressColor;
    private int min = 0;
    private int last = min;
    private int max = 100;
    private int value;
    private int progressStrokeWidth = 48;

    public DashedCircularProgress(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public DashedCircularProgress(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        final int count = getChildCount();
        int maxWidth = getWidth() / 2;
        int maxHeight = getHeight() / 2;

        for (int i = 0; i < count; i++) {
            final View child = getChildAt(i);

            int mesaureWidth = child.getMeasuredWidth();
            int measureHeight = child.getMeasuredWidth();

            ViewGroup.LayoutParams layoutParams = child.getLayoutParams();
            int padingTop = 22;
            child.setTranslationY(padingTop);

            LayoutParams relativeLayoutParams =
                    (LayoutParams) child.getLayoutParams();
            relativeLayoutParams.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
            child.setLayoutParams(relativeLayoutParams);

            if (mesaureWidth > maxWidth) {
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
                R.styleable.DashedCircularProgress);
        initAttributes(attributes);
        initPainters();
        initValueAnimator();
    }

    private void initAttributes(TypedArray attributes) {
        externalColor = attributes.getColor(R.styleable.DashedCircularProgress_external_color,
                externalColor);
        internalBaseColor = attributes.getColor(R.styleable.DashedCircularProgress_base_color,
                internalBaseColor);
        progressColor = attributes.getColor(R.styleable.DashedCircularProgress_progress_color,
                progressColor);
        max = attributes.getInt(R.styleable.DashedCircularProgress_max, max);
        progressStrokeWidth = attributes.getInt(R.styleable.DashedCircularProgress_progress_stroke_width,
                progressStrokeWidth);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        progressPainter.onSizeChanged(h, w);
        internalCirclePainter.onSizeChanged(h, w);
        animateValue();
    }

    private void initPainters() {
        progressPainter = new ProgressPainter(progressColor, min, max, progressStrokeWidth);
        internalCirclePainter = new InternalCirclePainter(internalBaseColor);
    }

    private void initValueAnimator() {
        valueAnimator = new ValueAnimator();
        valueAnimator.setInterpolator(interpolator);
        valueAnimator.addUpdateListener(new ValueAnimatorListenerImp());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        internalCirclePainter.draw(canvas);
        progressPainter.draw(canvas);
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
            int duration = 40;
            valueAnimator.setDuration(duration);
            valueAnimator.setIntValues(last, value);
            valueAnimator.start();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int heightNormalittation = 10;
        super.onMeasure(widthMeasureSpec, heightMeasureSpec + heightNormalittation);
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
        progressPainter.setMin(min);
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
        progressPainter.setMax(max);
    }

    private class ValueAnimatorListenerImp implements ValueAnimator.AnimatorUpdateListener {
        @Override
        public void onAnimationUpdate(ValueAnimator valueAnimator) {
            Integer value = (Integer) valueAnimator.getAnimatedValue();
            progressPainter.setValue(value);

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
        progressPainter.setColor(progressColor);
    }

    public int getInternalBaseColor() {
        return internalBaseColor;
    }

    public void setInternalBaseColor(int internalBaseColor) {
        this.internalBaseColor = internalBaseColor;
        internalCirclePainter.setColor(progressColor);
    }

}