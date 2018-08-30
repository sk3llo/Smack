package com.project.coolz.smack.utils.chooser_options

import android.view.View
import android.widget.TextView
import com.project.coolz.smack.R
import com.project.coolz.smack.utils.RealmUtil

//Choose gender and remember it
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
                        RealmUtil().gender(0, v)
                    }
                }
                R.id.femaleGenderMy -> {
                    if (RealmUtil().femaleGenderMy() != 1){
                        (v as TextView).setBackgroundResource(R.drawable.main_background_shape_blue)
                        maleGenderMy?.setBackgroundResource(R.drawable.main_background_shape_white)
                        RealmUtil().gender(1, v)
                    } else {
                        (v as TextView).setBackgroundResource(R.drawable.main_background_shape_white)
                        RealmUtil().gender(0, v)
                    }
                }
                R.id.maleGenderLookingFor -> {
                    if (RealmUtil().maleGenderLookingFor() != 1){
                        (v as TextView).setBackgroundResource(R.drawable.main_background_shape_blue)
                        femaleGenderLookingFor?.setBackgroundResource(R.drawable.main_background_shape_white)
                        RealmUtil().gender(1, v)
                    } else {
                        (v as TextView).setBackgroundResource(R.drawable.main_background_shape_white)
                        RealmUtil().gender(0, v)
                    }
                }
                R.id.femaleGenderLookingFor -> {
                    if (RealmUtil().femaleGenderLookingFor() != 1){
                        (v as TextView).setBackgroundResource(R.drawable.main_background_shape_blue)
                        maleGenderLookingFor?.setBackgroundResource(R.drawable.main_background_shape_white)
                        RealmUtil().gender(1, v)
                    } else {
                        (v as TextView).setBackgroundResource(R.drawable.main_background_shape_white)
                        RealmUtil().gender(0, v)
                    }
                }

            }
        }
    }
}