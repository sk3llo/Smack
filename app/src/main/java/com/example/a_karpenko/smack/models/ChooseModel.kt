package com.example.a_karpenko.smack.models

import android.app.Activity
import android.content.SharedPreferences
import android.support.v7.app.AppCompatActivity
import io.realm.RealmObject
import io.realm.annotations.RealmClass

@RealmClass
open class ChooseModel : RealmObject() {
    open var myGender: Int? = 0  // 1 for male, 2 for female
    open var lookingForGender: Int? = 0 //1 for male, 2 for female

    //My age
    open var myAge: Int? = 0 //1 for under 18, 2 for 19 - 22, 3 for 23 - 26, 4 for 27 - 35, 5 for over 36

    //Looking for age vars
    open var under18: Int? = 0
    open var from19to22: Int? = 0
    open var from23to26: Int? = 0
    open var from27to35: Int? = 0
    open var over36: Int? = 0

}
