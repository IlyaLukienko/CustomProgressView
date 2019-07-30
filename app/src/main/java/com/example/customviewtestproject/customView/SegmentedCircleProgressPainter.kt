package com.example.customviewtestproject.customView

import android.graphics.*

class SegmentedCircleProgressPainter(
    private var color: Int,
    private var max: Int,
    private val strokeWidth: Int,
    private val padding: Int,
    private val segmentsCount: Int
) : Painter {

    private var progressCircle: RectF? = null
    private var progressPaint: Paint? = null
    private var plusAngle = 0
    private var width: Int = 0
    private var height: Int = 0

    init {
        progressPaint = Paint()
        progressPaint!!.isAntiAlias = true
        progressPaint!!.strokeWidth = strokeWidth.toFloat()
        progressPaint!!.color = color
        progressPaint!!.style = Paint.Style.STROKE
    }

    private fun initDashedCircleProgress() {
        progressCircle = RectF()
        val halfOffset = strokeWidth / 2f
        progressCircle!!.left = halfOffset
        progressCircle!!.top = halfOffset
        progressCircle!!.right = width - halfOffset
        progressCircle!!.bottom = height - halfOffset
    }

    override fun draw(canvas: Canvas) {
        val startAngle = 0
        canvas.drawArc(progressCircle!!, startAngle.toFloat(), plusAngle.toFloat(), false, progressPaint!!)
    }

    fun setValue(value: Int) {
        this.plusAngle = 360 * value / max
    }

    override fun onSizeChanged(height: Int, width: Int) {
        if (this.width == 0) {
            val dashWith = 10f
            val dashHeight = (Math.PI * (width - 2 * strokeWidth) / segmentsCount - dashWith).toFloat()
            val dashSpace = 0
            progressPaint!!.pathEffect = DashPathEffect(
                floatArrayOf(dashHeight, dashWith),
                dashSpace.toFloat()
            )
        }
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
    }
}
