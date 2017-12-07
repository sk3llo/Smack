package com.example.a_karpenko.smack.models.firestore

class OptionsLFModel {

    var maleGenderLookingFor: Int? = 0
    var femaleGenderLookingFor: Int? = 0

    var under18LookingFor: Int? = 0
    var from19to22LookingFor: Int? = 0
    var from23to26LookingFor: Int? = 0
    var from27to35LookingFor: Int? = 0
    var over36LookingFor: Int? = 0

    constructor() //Firebase needed

    constructor(
            maleGenderLookingFor: Int?,
            femaleGenderLookingFor: Int?,
            under18LookingFor: Int?,
            from19to22LookingFor: Int?,
            from23to26LookingFor: Int?,
            from27to35LookingFor: Int?,
            over36LookingFor: Int?
    ) {
        this.maleGenderLookingFor = maleGenderLookingFor
        this.femaleGenderLookingFor = femaleGenderLookingFor

        this.under18LookingFor = under18LookingFor
        this.from19to22LookingFor = from19to22LookingFor
        this.from23to26LookingFor = from23to26LookingFor
        this.from27to35LookingFor = from27to35LookingFor
        this.over36LookingFor = over36LookingFor
    }

}