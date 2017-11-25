package com.example.a_karpenko.smack.utils

import android.view.MotionEvent
import android.view.View
import android.widget.TextView
import com.example.a_karpenko.smack.R

open class CustomTouchListener:View.OnTouchListener {
    private var checkState : Boolean? = false
    override fun onTouch(view: View, motionEvent:MotionEvent):Boolean {
        if (motionEvent.action == MotionEvent.ACTION_DOWN && checkState == false) {
            (view as TextView).setBackgroundResource(R.drawable.main_background_shape_blue)
            checkState = true
        } else {
            (view as TextView).setBackgroundResource(R.drawable.main_background_shape_white)
            checkState = false
        }
        return false
    }
}