package com.example.a_karpenko.smack.utils

import android.view.View
import android.widget.TextView
import com.example.a_karpenko.smack.R

open class GenderChooser: View.OnClickListener {

    override fun onClick(v: View?) {

        val parent: View? = v?.parent as View?

        if (parent != null) {
            val maleGenderMy = parent.findViewById<TextView>(R.id.maleGenderMy)
            val femaleGenderMy = parent.findViewById<TextView>(R.id.femaleGenderMy)

            val maleGenderLookingFor = parent.findViewById<TextView>(R.id.maleGenderLookingFor)
            val femaleGenderLookingFor = parent.findViewById<TextView>(R.id.femaleGenderLookingFor)

            when (v?.id) {
                R.id.maleGenderMy -> {
                    if (RealmUtil().maleGenderMy() != 1){
                        (v as TextView).setBackgroundResource(R.drawable.main_background_shape_blue)
                        femaleGenderMy?.setBackgroundResource(R.drawable.main_background_shape_white)
                        RealmUtil().gender(1, v)
                    } else {
                        (v as TextView).setBackgroundResource(R.drawable.main_background_shape_white)
                        RealmUtil().gender(2, v)
                    }
                }
                R.id.femaleGenderMy -> {
                    if (RealmUtil().maleGenderMy() != 1){
                        (v as TextView).setBackgroundResource(R.drawable.main_background_shape_blue)
                        maleGenderMy?.setBackgroundResource(R.drawable.main_background_shape_white)
                        RealmUtil().gender(1, v)
                    } else {
                        (v as TextView).setBackgroundResource(R.drawable.main_background_shape_white)
                        RealmUtil().gender(2, v)
                    }
                }
                R.id.maleGenderLookingFor -> {
                    if (RealmUtil().maleGenderMy() != 1){
                        (v as TextView).setBackgroundResource(R.drawable.main_background_shape_blue)
                        femaleGenderLookingFor?.setBackgroundResource(R.drawable.main_background_shape_white)
                        RealmUtil().gender(1, v)
                    } else {
                        (v as TextView).setBackgroundResource(R.drawable.main_background_shape_white)
                        RealmUtil().gender(2, v)
                    }
                }
                R.id.femaleGenderLookingFor -> {
                    if (RealmUtil().maleGenderMy() != 1){
                        (v as TextView).setBackgroundResource(R.drawable.main_background_shape_blue)
                        maleGenderLookingFor?.setBackgroundResource(R.drawable.main_background_shape_white)
                        RealmUtil().gender(1, v)
                    } else {
                        (v as TextView).setBackgroundResource(R.drawable.main_background_shape_white)
                        RealmUtil().gender(2, v)
                    }
                }

            }
        }
    }
}