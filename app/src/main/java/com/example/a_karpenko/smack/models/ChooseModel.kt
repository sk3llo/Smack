package com.example.a_karpenko.smack.models

import android.app.Activity
import android.content.SharedPreferences
import android.support.v7.app.AppCompatActivity
import io.realm.RealmObject
import io.realm.annotations.RealmClass

@RealmClass
open class ChooseModel : RealmObject() {
    open var myGender: Int? = 0  // 1 for male, 2 for female


}
