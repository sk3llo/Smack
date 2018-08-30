package com.project.coolz.smack.models.gender

import io.realm.RealmObject
import io.realm.annotations.Index
import io.realm.annotations.PrimaryKey

open class MyGenderModel: RealmObject() {

    @PrimaryKey @Index
    var id: Int? = 0

    var maleGenderMy: Int? = 0
    var femaleGenderMy: Int? = 0
}