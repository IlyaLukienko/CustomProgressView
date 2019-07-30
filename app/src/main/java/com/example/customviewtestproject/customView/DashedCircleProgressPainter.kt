package com.example.customviewtestproject.customView

import android.graphics.*

class DashedCircleProgressPainter(
    private var color: Int,
    private val textSize: Int,
    private var min: Int,
    private var max: Int,
    private val strokeWidth: Int,
    private val padding: Int,
    private val segmentsCount: Int
) : Painter {

    private var progressCircle: RectF? = null
    private var progressPaint: Paint? = null
    private var progressDotPaint: Paint? = null
    private var horizontalLinePaint: Paint? = null
    private var progressHorizontalPaint: Paint? = null
    private var textAnglePaint: Paint? = null
    private var textLevelPaint: Paint? = null
    private var plusAngle = 0
    private var level = 0
    private var dotRadius: Float = 0.toFloat()
    private var width: Int = 0
    private var height: Int = 0

    init {
        progressPaint = Paint()
        progressPaint!!.isAntiAlias = true
        progressPaint!!.strokeWidth = strokeWidth.toFloat()
        progressPaint!!.color = color
        progressPaint!!.style = Paint.Style.STROKE
        val dashWith = 5
        val dashSpace = 8
        progressPaint!!.pathEffect = DashPathEffect(
            floatArrayOf(dashWith.toFloat(), dashSpace.toFloat()),
            dashSpace.toFloat()
        )

        progressDotPaint = Paint()
        progressDotPaint!!.isAntiAlias = true
        progressDotPaint!!.strokeCap = Paint.Cap.ROUND
        val dotStrokeWidth = strokeWidth - dashWith
        progressDotPaint!!.strokeWidth = dotStrokeWidth.toFloat()
        progressDotPaint!!.color = color
        progressDotPaint!!.style = Paint.Style.FILL_AND_STROKE

        textAnglePaint = Paint()
        textAnglePaint!!.color = Color.WHITE
        textAnglePaint!!.style = Paint.Style.FILL
        textAnglePaint!!.textAlign = Paint.Align.CENTER
        textAnglePaint!!.textSize = textSize.toFloat()

        horizontalLinePaint = Paint()
        horizontalLinePaint!!.isAntiAlias = true
        horizontalLinePaint!!.strokeCap = Paint.Cap.ROUND
        horizontalLinePaint!!.strokeWidth = dotStrokeWidth / 2f
        horizontalLinePaint!!.color = Color.DKGRAY
        horizontalLinePaint!!.style = Paint.Style.FILL_AND_STROKE

        progressHorizontalPaint = Paint()
        progressHorizontalPaint!!.isAntiAlias = true
        progressHorizontalPaint!!.strokeCap = Paint.Cap.ROUND
        progressHorizontalPaint!!.strokeWidth = dotStrokeWidth / 2f
        progressHorizontalPaint!!.color = color
        progressHorizontalPaint!!.style = Paint.Style.FILL_AND_STROKE

        textLevelPaint = Paint()
        textLevelPaint!!.color = color
        textLevelPaint!!.style = Paint.Style.FILL
        textLevelPaint!!.textAlign = Paint.Align.CENTER
        textLevelPaint!!.textSize = textSize.toFloat() / 4
    }

    private fun initDashedCircleProgress() {
        progressCircle = RectF()
        val halfOffset = strokeWidth / 2f
        progressCircle!!.left = halfOffset + strokeWidth
        progressCircle!!.top = halfOffset + strokeWidth
        progressCircle!!.right = width.toFloat() - halfOffset - strokeWidth.toFloat()
        progressCircle!!.bottom = height.toFloat() - halfOffset - strokeWidth.toFloat()
        dotRadius = progressCircle!!.width() / 2f
    }

    override fun draw(canvas: Canvas) {
        val startAngle = 0
        canvas.drawArc(progressCircle!!, startAngle.toFloat(), plusAngle.toFloat(), false, progressPaint!!)
        val angleRadians = Math.toRadians((startAngle + plusAngle + 180).toDouble())
        val cos = Math.cos(angleRadians).toFloat()
        val sin = Math.sin(angleRadians).toFloat()
        val x = progressCircle!!.centerX() - dotRadius * cos
        val y = progressCircle!!.centerY() - dotRadius * sin
        canvas.drawPoint(x, y, progressDotPaint!!)

        val textAngle = "$plusAngle°"
        canvas.drawText(
            textAngle,
            progressCircle!!.centerX() - (textAnglePaint!!.descent() + textAnglePaint!!.ascent()) / 2 * textAnglePaint!!.textSkewX,
            progressCircle!!.centerY() - (textAnglePaint!!.descent() + textAnglePaint!!.ascent()) / 2,
            textAnglePaint!!
        )
        canvas.drawLine(
            progressCircle!!.centerX() - progressCircle!!.centerX() / 3f,
            progressCircle!!.centerY() - progressCircle!!.centerY() / 3f,
            progressCircle!!.centerX() + progressCircle!!.centerX() / 3f,
            progressCircle!!.centerY() - progressCircle!!.centerY() / 3f,
            horizontalLinePaint!!
        )

        val startX = progressCircle!!.centerX() - progressCircle!!.centerX() / 3f
        val stopX = progressCircle!!.centerX() + progressCircle!!.centerX() / 3f

        canvas.drawLine(
            startX,
            progressCircle!!.centerY() - progressCircle!!.centerY() / 3f,
            startX + stopX * plusAngle / max.toFloat() / (stopX / startX),
            progressCircle!!.centerY() - progressCircle!!.centerY() / 3f,
            progressHorizontalPaint!!
        )

        val textLevel = "LEVEL $level"
        canvas.drawText(
            textLevel,
            progressCircle!!.centerX() - (textLevelPaint!!.descent() + textLevelPaint!!.ascent()) / 2 * textLevelPaint!!.textSkewX,
            progressCircle!!.centerY() - progressCircle!!.centerY() / 3f - 2f * strokeWidth - (textLevelPaint!!.descent() + textLevelPaint!!.ascent()),
            textLevelPaint!!
        )
    }

    fun getMin(): Float {
        return min.toFloat()
    }

    fun setMin(min: Int) {
        this.min = min
    }

    fun getMax(): Float {
        return max.toFloat()
    }

    fun setMax(max: Int) {
        this.max = max
    }

    fun setValue(value: Int) {
        level = calculateLevel(value)
        plusAngle = calculateAngle(value)
    }

    private fun calculateLevel(value: Int): Int {
        var level = 0
        if (value > 0) {
            level = value / (max / segmentsCount)
        }
        if (level >= segmentsCount) {
            return level
        }
        level++
        return level
    }

    private fun calculateAngle(value: Int): Int {
        var calculatedAngle = value * segmentsCount % max
        if (value > 0 && calculatedAngle == 0) {
            calculatedAngle = max
        }
        return calculatedAngle
    }

    override fun onSizeChanged(height: Int, width: Int) {
        this.width = width - padding
        this.height = height - padding
        initDashedCircleProgress()
    }

    override fun getColor(): Int {
        return color
    }

    override fun setColor(color: Int) {
        this.color = color
        progressPaint!!.color = color
        progressDotPaint!!.color = color
    }
}
