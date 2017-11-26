package com.example.a_karpenko.smack.models

open class ChooseModel {
    var myGender: Int? = 0  // 1 for male, 2 for female
    var lookingForGender: Int? = 0 //1 for male, 2 for female

    //My age
    var myAge: Int? = 0 //1 for under 18, 2 for 19 - 22, 3 for 23 - 26, 4 for 27 - 35, 5 for over 36

    //Looking for age vars
    var under18: Int? = null
    var from19to22: Int? = 0
    var from23to26: Int? = 0
    var from27to35: Int? = 0
    var over36: Int? = 0

}