package com.example.a_karpenko.smack.utils

import android.view.View
import android.widget.TextView
import com.example.a_karpenko.smack.R
import com.example.a_karpenko.smack.utils.RealmUtil

open class MyAgeChooser: View.OnClickListener {

    //Let user choose only one row with his age
    //Change color to white on all other rows

    override fun onClick(v: View?) {

        val parent: View? = v?.parent as View?

        if (parent != null) {
            val under18 = parent.findViewById<TextView?>(R.id.under18My)
            val from19 = parent.findViewById<TextView?>(R.id.from19to22My)
            val from23 = parent.findViewById<TextView?>(R.id.from23to26My)
            val from27 = parent.findViewById<TextView?>(R.id.from27to35My)
            val over36 = parent.findViewById<TextView?>(R.id.over36My)

            when (v?.id) {
                R.id.under18My -> {
                    if (RealmUtil().under18My() != 1) {
                        (v as TextView).setBackgroundResource(R.drawable.main_background_shape_blue)
                        from19?.setBackgroundResource(R.drawable.main_background_shape_white)
                        from23?.setBackgroundResource(R.drawable.main_background_shape_white)
                        from27?.setBackgroundResource(R.drawable.main_background_shape_white)
                        over36?.setBackgroundResource(R.drawable.main_background_shape_white)
                        RealmUtil().myAge(1, v)
                    } else {
                        (v as TextView).setBackgroundResource(R.drawable.main_background_shape_white)
                        RealmUtil().myAge(2, v)
                    }
                }
                R.id.from19to22My -> {
                if (RealmUtil().from19to22My() != 1) {
                    v.setBackgroundResource(R.drawable.main_background_shape_blue)
                    under18?.setBackgroundResource(R.drawable.main_background_shape_white)
                    from23?.setBackgroundResource(R.drawable.main_background_shape_white)
                    from27?.setBackgroundResource(R.drawable.main_background_shape_white)
                    over36?.setBackgroundResource(R.drawable.main_background_shape_white)
                    RealmUtil().myAge(1, v)
                } else {
                    (v as TextView).setBackgroundResource(R.drawable.main_background_shape_white)
                    RealmUtil().myAge(2, v)
                }
                }
                R.id.from23to26My -> {
                    if (RealmUtil().from23to26My() != 1) {
                        v.setBackgroundResource(R.drawable.main_background_shape_blue)
                        under18?.setBackgroundResource(R.drawable.main_background_shape_white)
                        from19?.setBackgroundResource(R.drawable.main_background_shape_white)
                        from27?.setBackgroundResource(R.drawable.main_background_shape_white)
                        over36?.setBackgroundResource(R.drawable.main_background_shape_white)
                        RealmUtil().myAge(1, v)
                    } else {
                        (v as TextView).setBackgroundResource(R.drawable.main_background_shape_white)
                        RealmUtil().myAge(2, v)
                    }
                }
                R.id.from27to35My -> {
                    if (RealmUtil().from27to35My() != 1) {
                        v.setBackgroundResource(R.drawable.main_background_shape_blue)
                        under18?.setBackgroundResource(R.drawable.main_background_shape_white)
                        from19?.setBackgroundResource(R.drawable.main_background_shape_white)
                        from23?.setBackgroundResource(R.drawable.main_background_shape_white)
                        over36?.setBackgroundResource(R.drawable.main_background_shape_white)
                        RealmUtil().myAge(1, v)
                    } else {
                        (v as TextView).setBackgroundResource(R.drawable.main_background_shape_white)
                        RealmUtil().myAge(2, v)
                    }
                }
                R.id.over36My -> {
                    if (RealmUtil().over36My() != 1) {
                        v.setBackgroundResource(R.drawable.main_background_shape_blue)
                        under18?.setBackgroundResource(R.drawable.main_background_shape_white)
                        from19?.setBackgroundResource(R.drawable.main_background_shape_white)
                        from23?.setBackgroundResource(R.drawable.main_background_shape_white)
                        from27?.setBackgroundResource(R.drawable.main_background_shape_white)
                        RealmUtil().myAge(1, v)
                    } else {
                        (v as TextView).setBackgroundResource(R.drawable.main_background_shape_white)
                        RealmUtil().myAge(2, v)
                    }
                }
            }
        }
    }
}