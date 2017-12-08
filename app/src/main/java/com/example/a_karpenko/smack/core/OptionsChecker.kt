package com.example.a_karpenko.smack.core

import android.support.design.widget.Snackbar
import android.view.View
import com.example.a_karpenko.smack.R
import com.example.a_karpenko.smack.utils.RealmUtil

//Check users options and if empty return Snackbar
open class OptionsChecker(var view: View) {
    //Gender
    val maleGenderMy: Int? = RealmUtil().maleGenderMy()
    val femaleGenderMy: Int? = RealmUtil().femaleGenderMy()
    val maleGenderLookingFor: Int? = RealmUtil().maleGenderLookingFor()
    val femaleGenderLookingFor: Int? = RealmUtil().femaleGenderLookingFor()
    //My Age
    val under18My: Int? = RealmUtil().under18My()
    val from19My: Int? = RealmUtil().from19to22My()
    val from23My: Int? = RealmUtil().from23to26My()
    val from27My: Int? = RealmUtil().from27to35My()
    val over36My: Int? = RealmUtil().over36My()
    //Looking for age
    val under18LookingFor: Int? = RealmUtil().under18LookingFor()
    val from19LookingFor: Int? = RealmUtil().from19to22LookingFor()
    val from23LookingFor: Int? = RealmUtil().from23to26LookingFor()
    val from27LookingFor: Int? = RealmUtil().from27to35LookingFor()
    val over36LookingFor: Int? = RealmUtil().over36LookingFor()



    private fun myGender(): Boolean{
        if (maleGenderMy != 1 && femaleGenderMy != 1){
            Snackbar.make(view, R.string.choose_your_gender, Snackbar.LENGTH_SHORT).show()
            return false
        } else if (maleGenderMy == 1 || femaleGenderMy == 1) {
            return true
        }
        return false
    }

    private fun lookingForGender(): Boolean {
        if (maleGenderLookingFor != 1 && femaleGenderLookingFor != 1){
            Snackbar.make(view, R.string.choose_looking_for_gender, Snackbar.LENGTH_SHORT).show()
            return false
        } else if (maleGenderLookingFor == 1 || femaleGenderLookingFor == 1) {
            return true
        }
        return false
    }

    private fun myAge(): Boolean {
        if (under18My != 1 && from19My != 1 && from23My != 1 && from27My != 1 && over36My != 1){
            Snackbar.make(view, R.string.choose_your_age, Snackbar.LENGTH_SHORT).show()
            return false
        } else if(under18My == 1 || from19My == 1 || from23My == 1 || from27My == 1 || over36My == 1){
            return true
        }
        return false
    }

    private fun lookingForAge(): Boolean {
        if (under18LookingFor != 1 && from19LookingFor != 1
                && from23LookingFor != 1 && from27LookingFor != 1 && over36LookingFor != 1){
            Snackbar.make(view, R.string.choose_looking_for_age, Snackbar.LENGTH_SHORT).show()
            return false
        } else if(under18LookingFor == 1 || from19LookingFor == 1
                || from23LookingFor == 1 || from27LookingFor == 1 || over36LookingFor == 1){
            return true
        }
        return false
    }


    fun checkAndSend(): Boolean {
        return myGender() && myAge() && lookingForGender() && lookingForAge()
    }

}