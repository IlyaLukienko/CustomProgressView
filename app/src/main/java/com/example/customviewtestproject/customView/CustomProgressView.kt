package com.example.customviewtestproject.customView

import android.animation.ValueAnimator
import android.content.Context
import android.content.res.TypedArray
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.Interpolator
import android.widget.RelativeLayout
import com.example.customviewtestproject.R

class CustomProgressView : RelativeLayout {
    private var dashedCirclePainter: DashedCirclePainter? = null
    private var dashedCircleProgressPainter: DashedCircleProgressPainter? = null
    private var segmentedCirclePainter: SegmentedCirclePainter? = null
    private var segmentedCircleProgressPainter: SegmentedCircleProgressPainter? = null
    private var interpolator: Interpolator = AccelerateDecelerateInterpolator()
    private var valueAnimator: ValueAnimator? = null
    private var valueChangeListener: OnValueChangeListener? = null
    private var internalDashedBaseColor: Int = 0
    private var internalSegmentedBaseColor: Int = 0
    private var progressSegmentedColor: Int = 0
    private var progressDashedColor: Int = 0
    private var min = 0
    private var last = min
    private var value: Int = 0
    private var progressStrokeWidth = 48
    private val defaultPadding = progressStrokeWidth
    private var textSize: Int = 0
    private var segmentsCount = 8
    internal var max = 360

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(context, attrs)
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        val count = childCount
        val maxWidth = width
        val maxHeight = height

        for (i in 0 until count) {
            val child = getChildAt(i)

            val measureWidth = child.measuredWidth
            val measureHeight = child.measuredWidth

            val layoutParams = child.layoutParams
            val relativeLayoutParams = child.layoutParams as RelativeLayout.LayoutParams
            relativeLayoutParams.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE)
            child.layoutParams = relativeLayoutParams

            if (measureWidth > maxWidth) {
                layoutParams.width = maxWidth
            }

            if (measureHeight > maxHeight) {
                layoutParams.height = maxHeight
            }
        }
    }

    private fun init(context: Context, attributeSet: AttributeSet) {
        setWillNotDraw(false)
        val attributes = context.obtainStyledAttributes(
            attributeSet,
            R.styleable.CustomProgressView
        )
        initAttributes(attributes)
        initPainters()
        initValueAnimator()
    }

    private fun initAttributes(attributes: TypedArray) {
        internalDashedBaseColor = attributes.getColor(
            R.styleable.CustomProgressView_base_dashed_color,
            internalDashedBaseColor
        )
        progressDashedColor = attributes.getColor(
            R.styleable.CustomProgressView_progress_dashed_color,
            progressDashedColor
        )
        internalSegmentedBaseColor = attributes.getColor(
            R.styleable.CustomProgressView_base_segmented_color,
            internalSegmentedBaseColor
        )
        progressSegmentedColor = attributes.getColor(
            R.styleable.CustomProgressView_progress_segmented_color,
            progressSegmentedColor
        )
        max = attributes.getInt(R.styleable.CustomProgressView_max, max)
        textSize = attributes.getInt(R.styleable.CustomProgressView_text_size, textSize)
        progressStrokeWidth = attributes.getInt(
            R.styleable.CustomProgressView_progress_stroke_width,
            progressStrokeWidth
        )
        segmentsCount = attributes.getInt(
            R.styleable.CustomProgressView_segments_count,
            segmentsCount
        )
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        segmentedCirclePainter!!.onSizeChanged(h, w)
        segmentedCircleProgressPainter!!.onSizeChanged(h, w)
        dashedCirclePainter!!.onSizeChanged(h, w)
        dashedCircleProgressPainter!!.onSizeChanged(h, w)
        animateValue()
    }

    private fun initPainters() {
        segmentedCircleProgressPainter = SegmentedCircleProgressPainter(
            progressSegmentedColor,
            max,
            progressStrokeWidth,
            defaultPadding,
            segmentsCount
        )
        segmentedCirclePainter = SegmentedCirclePainter(
            internalSegmentedBaseColor,
            min,
            max,
            progressStrokeWidth,
            defaultPadding,
            segmentsCount
        )
        dashedCircleProgressPainter = DashedCircleProgressPainter(
            progressDashedColor, (textSize * context.resources.displayMetrics.scaledDensity).toInt(),
            min,
            max,
            progressStrokeWidth,
            defaultPadding,
            segmentsCount
        )
        dashedCirclePainter =
            DashedCirclePainter(internalDashedBaseColor, min, max, progressStrokeWidth, defaultPadding)
    }

    private fun initValueAnimator() {
        valueAnimator = ValueAnimator()
        valueAnimator!!.interpolator = interpolator
        valueAnimator!!.addUpdateListener(ValueAnimatorListenerImp())
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        dashedCirclePainter!!.draw(canvas)
        dashedCircleProgressPainter!!.draw(canvas)
        segmentedCirclePainter!!.draw(canvas)
        segmentedCircleProgressPainter!!.draw(canvas)
        invalidate()
    }

    fun setValue(value: Int) {
        this.value = value
        if (value <= max || value >= min) {
            animateValue()
        }
    }

    private fun animateValue() {
        if (valueAnimator != null) {
            valueAnimator!!.duration = 0
            valueAnimator!!.setIntValues(last, value)
            valueAnimator!!.start()
        }
    }

    private inner class ValueAnimatorListenerImp : ValueAnimator.AnimatorUpdateListener {
        override fun onAnimationUpdate(valueAnimator: ValueAnimator) {
            val value = valueAnimator.animatedValue as Int
            segmentedCircleProgressPainter!!.setValue(value)
            dashedCircleProgressPainter!!.setValue(value)

            if (valueChangeListener != null) {
                valueChangeListener!!.onValueChange(value.toFloat())
            }

            last = value
        }
    }

    interface OnValueChangeListener {
        fun onValueChange(value: Float)
    }
}