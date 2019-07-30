package com.example.customviewtestproject.customView

import android.graphics.*

class SegmentedCirclePainter(
    private var color: Int,
    private val startAngle: Int,
    private val finishAngle: Int,
    private val strokeWidth: Int,
    private val padding: Int,
    private val segmentsCount: Int
) : Painter {

    private var segmentedCircle: RectF? = null
    private var segmentedCirclePaint: Paint? = null
    private var width: Int = 0
    private var height: Int = 0

    init {
        segmentedCirclePaint = Paint()
        segmentedCirclePaint!!.isAntiAlias = true
        segmentedCirclePaint!!.strokeWidth = strokeWidth.toFloat()
        segmentedCirclePaint!!.color = color
        segmentedCirclePaint!!.style = Paint.Style.STROKE
    }

    private fun initSegmentedCircle() {
        segmentedCircle = RectF()
        val halfOffset = strokeWidth / 2f
        segmentedCircle!!.left = halfOffset
        segmentedCircle!!.top = halfOffset
        segmentedCircle!!.right = width - halfOffset
        segmentedCircle!!.bottom = height - halfOffset
    }

    override fun draw(canvas: Canvas) {
        canvas.drawArc(segmentedCircle!!, startAngle.toFloat(), finishAngle.toFloat(), false, segmentedCirclePaint!!)
    }

    override fun setColor(color: Int) {
        this.color = color
        segmentedCirclePaint!!.color = color
    }

    override fun getColor(): Int {
        return color
    }

    override fun onSizeChanged(height: Int, width: Int) {
        if (this.width == 0) {
            val dashWith = 10f
            val dashHeight = (Math.PI * (width - 2 * strokeWidth) / segmentsCount - dashWith).toFloat()
            val dashSpace = 0
            segmentedCirclePaint!!.pathEffect = DashPathEffect(
                floatArrayOf(dashHeight, dashWith),
                dashSpace.toFloat()
            )
        }
        this.width = width - padding
        this.height = height - padding
        initSegmentedCircle()
    }
}
