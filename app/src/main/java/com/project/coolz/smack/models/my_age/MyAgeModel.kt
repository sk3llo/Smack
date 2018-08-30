package com.project.coolz.smack.models.my_age

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
