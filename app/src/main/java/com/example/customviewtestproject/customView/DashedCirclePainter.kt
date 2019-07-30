package com.example.customviewtestproject.customView

import android.graphics.Canvas
import android.graphics.DashPathEffect
import android.graphics.Paint
import android.graphics.RectF

class DashedCirclePainter(
    private var color: Int,
    private val startAngle: Int,
    private val finishAngle: Int,
    private val strokeWidth: Int,
    private val padding: Int
) : Painter {

    private var dashedCircle: RectF? = null
    private var dashedCirclePaint: Paint? = null
    private var width: Int = 0
    private var height: Int = 0

    init {
        dashedCirclePaint = Paint()
        dashedCirclePaint!!.isAntiAlias = true
        dashedCirclePaint!!.strokeWidth = strokeWidth.toFloat()
        dashedCirclePaint!!.color = color
        dashedCirclePaint!!.style = Paint.Style.STROKE
        val dashWith = 5
        val dashSpace = 8
        dashedCirclePaint!!.pathEffect = DashPathEffect(
            floatArrayOf(dashWith.toFloat(), dashSpace.toFloat()),
            dashSpace.toFloat()
        )
    }

    private fun initDashedCircle() {
        dashedCircle = RectF()
        val halfOffset = strokeWidth / 2f
        dashedCircle!!.left = halfOffset + strokeWidth
        dashedCircle!!.top = halfOffset + strokeWidth
        dashedCircle!!.right = width.toFloat() - halfOffset - strokeWidth.toFloat()
        dashedCircle!!.bottom = height.toFloat() - halfOffset - strokeWidth.toFloat()
    }

    override fun draw(canvas: Canvas) {
        canvas.drawArc(dashedCircle!!, startAngle.toFloat(), finishAngle.toFloat(), false, dashedCirclePaint!!)
    }

    override fun setColor(color: Int) {
        this.color = color
        dashedCirclePaint!!.color = color
    }

    override fun getColor(): Int {
        return color
    }

    override fun onSizeChanged(height: Int, width: Int) {
        this.width = width - padding
        this.height = height - padding
        initDashedCircle()
    }
}
