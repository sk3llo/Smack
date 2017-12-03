package com.example.a_karpenko.smack.models

import android.app.Activity
import android.content.SharedPreferences
import android.support.v7.app.AppCompatActivity
import io.realm.RealmObject
import io.realm.annotations.Index
import io.realm.annotations.PrimaryKey
import io.realm.annotations.RealmClass

@RealmClass
open class MyAgeModel : RealmObject() {

    @PrimaryKey @Index
    var id : Int? = 0

    var under18: Int? = 0
    var from19to22: Int? = 0
    var from23to26: Int? = 0
    var from27to35: Int? = 0
    var over36: Int? = 0

}
