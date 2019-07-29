package com.example.customviewtestproject

import android.app.Activity
import android.os.Bundle
import android.widget.SeekBar
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        seekBar.max = 360
        progressBar.max = 360
        progressBar.internalBaseColor = ContextCompat.getColor(this, R.color.background_dark)
        progressBar.progressColor = ContextCompat.getColor(this, R.color.holo_green_dark)
        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                progressBar.setValue(progress)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }
        })
    }
}
