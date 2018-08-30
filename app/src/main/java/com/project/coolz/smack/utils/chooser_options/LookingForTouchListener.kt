package com.project.coolz.smack.utils.chooser_options

import android.view.MotionEvent
import android.view.View
import android.widget.TextView
import com.project.coolz.smack.R

open class LookingForTouchListener :View.OnTouchListener {
    private var checkState : Boolean? = false
    override fun onTouch(view: View, motionEvent:MotionEvent):Boolean {
        checkState = if (motionEvent.action == MotionEvent.ACTION_DOWN && checkState == false) {
            (view as TextView).setBackgroundResource(R.drawable.main_background_shape_blue)
            true
        } else {
            (view as TextView).setBackgroundResource(R.drawable.main_background_shape_white)
            false
        }
        return false
    }
}