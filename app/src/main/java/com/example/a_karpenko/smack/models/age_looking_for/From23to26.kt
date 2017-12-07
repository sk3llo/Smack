package com.example.a_karpenko.smack.models.age_looking_for

import io.realm.RealmObject
import io.realm.annotations.Index
import io.realm.annotations.PrimaryKey

open class From23to26 : RealmObject() {

    @PrimaryKey @Index
    var id: Int? = 0

    var from23to26: Int? = 0
}