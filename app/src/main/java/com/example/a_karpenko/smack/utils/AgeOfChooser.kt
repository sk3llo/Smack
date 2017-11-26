package com.example.a_karpenko.smack.utils

import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.TextView
import com.example.a_karpenko.smack.R
import com.example.a_karpenko.smack.models.ChooseModel

open class AgeOfChooser : View.OnTouchListener {
    private var checkState: Boolean? = false
    override fun onTouch(view: View, motionEvent: MotionEvent): Boolean {
        checkState = if (motionEvent.action == MotionEvent.ACTION_DOWN && checkState == false) {
            //(view as TextView).setBackgroundResource(R.drawable.main_background_shape_blue)
            when {
                view.id == R.id.under18LookingFor -> {
                    (view as TextView).setBackgroundResource(R.drawable.main_background_shape_blue)
                    ChooseModel().under18 = 1
                    Log.d("blue back, under18 ", "${view.id} + ${ChooseModel().under18}")
                }
                view.id == R.id.from19to22LookingFor -> {
                    (view as TextView).setBackgroundResource(R.drawable.main_background_shape_blue)
                    ChooseModel().from19to22 = 1
                    Log.d("blue back, from19to22 ", "${view.id} + ${ChooseModel().from19to22}")
                }
                view.id == R.id.from23to26LookingFor -> {
                    (view as TextView).setBackgroundResource(R.drawable.main_background_shape_blue)
                    ChooseModel().from23to26 = 1
                    Log.d("blue back, from23to26 ", "${view.id} + ${ChooseModel().from23to26}")
                }
                view.id == R.id.from27to35LookingFor -> {
                    (view as TextView).setBackgroundResource(R.drawable.main_background_shape_blue)
                    ChooseModel().from27to35 = 1
                    Log.d("blue back, from27to35 ", "${view.id} + ${ChooseModel().from27to35}")
                }
                view.id == R.id.over36LookingFor -> {
                    (view as TextView).setBackgroundResource(R.drawable.main_background_shape_blue)
                    ChooseModel().over36 = 1
                    Log.d("blue back, over36 ", "${view.id} + ${ChooseModel().over36}")
                }
            }
            true
        } else {
            //(view as TextView).setBackgroundResource(R.drawable.main_background_shape_white)
            when {
                view.id == R.id.under18LookingFor -> {
                    (view as TextView).setBackgroundResource(R.drawable.main_background_shape_white)
                    ChooseModel().under18 = 0
                    Log.d("white back, under18 ", "${view.id} + ${ChooseModel().under18}")
                }
                view.id == R.id.from19to22LookingFor -> {
                    (view as TextView).setBackgroundResource(R.drawable.main_background_shape_white)
                    ChooseModel().from19to22 = 0
                    Log.d("white back, from19to22 ", "${view.id} + ${ChooseModel().from19to22}")
                }
                view.id == R.id.from23to26LookingFor -> {
                    (view as TextView).setBackgroundResource(R.drawable.main_background_shape_white)
                    ChooseModel().from23to26 = 0
                    Log.d("white back, from23to26 ", "${view.id} + ${ChooseModel().from23to26}")
                }
                view.id == R.id.from27to35LookingFor -> {
                    (view as TextView).setBackgroundResource(R.drawable.main_background_shape_white)
                    ChooseModel().from27to35 = 0
                    Log.d("white back, from27to35 ", "${view.id} + ${ChooseModel().from27to35}")
                }
                view.id == R.id.over36LookingFor -> {
                    (view as TextView).setBackgroundResource(R.drawable.main_background_shape_white)
                    ChooseModel().over36 = 0
                    Log.d("white back, over36 ", "${view.id} + ${ChooseModel().over36}")
                }
            }
            false
        }
        return false
    }
}