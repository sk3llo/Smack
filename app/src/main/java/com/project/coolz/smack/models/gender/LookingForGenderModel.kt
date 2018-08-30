package com.project.coolz.smack.models.gender

import io.realm.RealmObject
import io.realm.annotations.Index
import io.realm.annotations.PrimaryKey

open class LookingForGenderModel : RealmObject() {

    @PrimaryKey
    @Index
    var id: Int? = 0

    var maleGenderLookingFor: Int? = 0
    var femaleGenderLookingFor: Int? = 0

}