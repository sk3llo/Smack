package com.example.a_karpenko.smack.models.firestore

import com.google.firebase.firestore.ServerTimestamp
import java.util.*

class OptionsMyModel {

    //Gender
    var maleGenderMy: Int? = 0
    var femaleGenderMy: Int? = 0

    //Age
    var under18My: Int? = 0
    var from19to22My: Int? = 0
    var from23to26My: Int? = 0
    var from27to35My: Int? = 0
    var over36My: Int? = 0



    constructor() //For Firebase
    constructor(maleGenderMy: Int?,
                femaleGenderMy: Int?,
                under18My: Int?,
                from19to22My: Int?,
                from23to26My: Int?,
                from27to35My: Int?,
                over36My: Int?){

        //Gender
        this.maleGenderMy = maleGenderMy
        this.femaleGenderMy = femaleGenderMy

        //Age
        this.under18My = under18My
        this.from19to22My = from19to22My
        this.from23to26My = from23to26My
        this.from27to35My = from27to35My
        this.over36My = over36My

    }

}