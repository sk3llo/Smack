package com.example.a_karpenko.smack.core

import android.support.design.widget.Snackbar
import android.view.View
import com.example.a_karpenko.smack.R
import com.example.a_karpenko.smack.utils.RealmUtil
import io.realm.Realm

open class CheckerAndSender {

    fun start(): Boolean{

        when {
            RealmUtil().maleGenderMy() != 1 -> {
             //Snackbar
            return false
            }
        }
        return false
    }
}