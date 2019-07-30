package com.example.customviewtestproject.customView

import android.graphics.Canvas

interface Painter {
    fun draw(canvas: Canvas)

    fun setColor(color: Int)

    fun getColor(): Int

    fun onSizeChanged(height: Int, width: Int)
}


