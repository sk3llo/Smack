package com.example.a_karpenko.smack.models

import android.widget.TextView
import com.google.firebase.firestore.ServerTimestamp
import java.util.*

class ChosenOptionsModel {

    @ServerTimestamp
    var timestamp: Date? = null
    //Gender
    var maleGenderMy: Int? = 0
    var femaleGenderMy: Int? = 0

    var maleGenderLookingFor: Int? = 0
    var femaleGenderLookingFor: Int? = 0

    //Age
    var under18My: Int? = 0
    var from19to22My: Int? = 0
    var from23to26My: Int? = 0
    var from27to35My: Int? = 0
    var over36My: Int? = 0

    var under18LookingFor: Int? = 0
    var from19to22LookingFor: Int? = 0
    var from23to26LookingFor: Int? = 0
    var from27to35LookingFor: Int? = 0
    var over36LookingFor: Int? = 0

    constructor() //For Firebase
    constructor(timestamp: Date?,
                maleGenderMy: Int?,
                femaleGenderMy: Int?,
                maleGenderLookingFor: Int?,
                femaleGenderLookingFor: Int?,
                under18My: Int?,
                from19to22My: Int?,
                from23to26My: Int?,
                from27to35My: Int?,
                over36My: Int?,
                under18LookingFor: Int?,
                from19to22LookingFor: Int?,
                from23to26LookingFor: Int?,
                from27to35LookingFor: Int?,
                over36LookingFor: Int?)

}