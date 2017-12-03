package com.example.a_karpenko.smack.utils

import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.TextView
import com.example.a_karpenko.smack.R

open class AgeOfChooser : View.OnTouchListener {

    //Bool for color
    private var checkState: Boolean? = false


    override fun onTouch(view: View, motionEvent: MotionEvent): Boolean {

        checkState = if (motionEvent.action == MotionEvent.ACTION_DOWN && checkState == false) {

            //Adds to realm 1 or 2 based on view and chosen option

            when {
                view.id == R.id.under18LookingFor -> {
                    if (RealmUtil().under18LookingFor() != 1) {
                        (view as TextView).setBackgroundResource(R.drawable.main_background_shape_blue)
                        RealmUtil().lookingForAge(1, view)
                    } else{
                        (view as TextView).setBackgroundResource(R.drawable.main_background_shape_white)
                        RealmUtil().lookingForAge(2, view)
                    }
                    Log.d("blue back, under18 ", "${view.id} + ${RealmUtil().under18LookingFor()}")
                }

                view.id == R.id.from19to22LookingFor -> {
                    if (RealmUtil().from19to22LookingFor() != 1) {
                        (view as TextView).setBackgroundResource(R.drawable.main_background_shape_blue)
                        RealmUtil().lookingForAge(1, view)
                    } else {
                        (view as TextView).setBackgroundResource(R.drawable.main_background_shape_white)
                        RealmUtil().lookingForAge(2, view)
                    }
                    Log.d("blue back, from19to22 ", "${view.id} + ${RealmUtil().from19to22LookingFor()}")
                }
                view.id == R.id.from23to26LookingFor -> {
                    if (RealmUtil().from23to26LookingFor() != 1) {
                        (view as TextView).setBackgroundResource(R.drawable.main_background_shape_blue)
                        RealmUtil().lookingForAge(1, view)
                    } else {
                        (view as TextView).setBackgroundResource(R.drawable.main_background_shape_white)
                        RealmUtil().lookingForAge(2, view)
                    }
                    Log.d("blue back, from23to26 ", "${view.id} + ${RealmUtil().from23to26LookingFor()}")
                }
                view.id == R.id.from27to35LookingFor -> {
                    if (RealmUtil().from27to35LookingFor() != 1){
                        (view as TextView).setBackgroundResource(R.drawable.main_background_shape_blue)
                        RealmUtil().lookingForAge(1, view)
                    } else {
                        (view as TextView).setBackgroundResource(R.drawable.main_background_shape_white)
                        RealmUtil().lookingForAge(2, view)
                    }
                    Log.d("blue back, from27to35 ", "${view.id} + ${RealmUtil().from27to35LookingFor()}")
                }
                view.id == R.id.over36LookingFor -> {
                    if (RealmUtil().over36LookingFor() != 1) {
                        (view as TextView).setBackgroundResource(R.drawable.main_background_shape_blue)
                        RealmUtil().lookingForAge(1, view)
                    } else {
                        (view as TextView).setBackgroundResource(R.drawable.main_background_shape_white)
                        RealmUtil().lookingForAge(2, view)
                    }
                    Log.d("blue back, over36 ", "${view.id} + ${RealmUtil().over36LookingFor()}")
                }
            }
            true
        } else {
            when {
                view.id == R.id.under18LookingFor -> {
                    (view as TextView).setBackgroundResource(R.drawable.main_background_shape_white)
                    RealmUtil().lookingForAge(2, view)
                    Log.d("white back, under18 ", "${view.id} + ${RealmUtil().under18LookingFor()}")
                }
                view.id == R.id.from19to22LookingFor -> {
                    (view as TextView).setBackgroundResource(R.drawable.main_background_shape_white)
                    RealmUtil().lookingForAge(2, view)
                    Log.d("white back, from19to22 ", "${view.id} + ${RealmUtil().from19to22LookingFor()}")
                }
                view.id != R.id.from23to26LookingFor -> {
                    (view as TextView).setBackgroundResource(R.drawable.main_background_shape_white)
                    RealmUtil().lookingForAge(2, view)
                    Log.d("white back, from23to26 ", "${view.id} + ${RealmUtil().from23to26LookingFor()}")
                }
                view.id != R.id.from27to35LookingFor -> {
                    (view as TextView).setBackgroundResource(R.drawable.main_background_shape_white)
                    RealmUtil().lookingForAge(2, view)
                    Log.d("white back, from27to35 ", "${view.id} + ${RealmUtil().from27to35LookingFor()}")
                }
                view.id != R.id.over36LookingFor -> {
                    (view as TextView).setBackgroundResource(R.drawable.main_background_shape_white)
                    RealmUtil().lookingForAge(2, view)
                    Log.d("white back, over36 ", "${view.id} + ${RealmUtil().over36LookingFor()}")
                }
            }
            false
        }
        return false
    }


}