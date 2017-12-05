package com.example.a_karpenko.smack.core

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.content.ContextCompat.startActivity
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.example.a_karpenko.smack.R
import com.example.a_karpenko.smack.ui.ChatActivity
import com.example.a_karpenko.smack.ui.MainActivity
import com.example.a_karpenko.smack.utils.RealmUtil

//Class to check all chosen options and send data to Firebase
open class CheckerAndSender(var view: View) {

    fun start(): Boolean{
        //My gender male
        if (RealmUtil().maleGenderMy() != 1 && RealmUtil().femaleGenderMy() != 1) {
            Snackbar.make(view, R.string.choose_your_gender, Snackbar.LENGTH_SHORT).show()
            return false
        } else if (RealmUtil().maleGenderMy() == 1) {
            //TODO: Send data to Firebase
            return true
        }
        //My gender female
        if (RealmUtil().femaleGenderMy() != 1 && RealmUtil().maleGenderMy() != 1) {
            Snackbar.make(view, R.string.choose_your_gender, Snackbar.LENGTH_SHORT).show()
            return false
        } else if (RealmUtil().femaleGenderMy() == 1) {
            //TODO: Send data to Firebase
            return true
        }
        //Looking for gender male
        if (RealmUtil().maleGenderLookingFor() != 1 && RealmUtil().femaleGenderLookingFor() != 1) {
            Snackbar.make(view, R.string.choose_looking_for_gender, Snackbar.LENGTH_SHORT).show()
            return false
        } else if (RealmUtil().maleGenderMy() == 1) {
            //TODO: Send data to Firebase
            return true
        }
        //Looking for gender female
        if (RealmUtil().femaleGenderLookingFor() != 1 && RealmUtil().maleGenderLookingFor() != 1) {
            Snackbar.make(view, R.string.choose_looking_for_gender, Snackbar.LENGTH_SHORT).show()
            return false
        } else if (RealmUtil().femaleGenderMy() == 1) {
            //TODO: Send data to Firebase
            return true
        }
        return start()
    }
}