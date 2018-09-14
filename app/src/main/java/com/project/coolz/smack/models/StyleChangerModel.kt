package com.project.coolz.smack.models

import io.realm.RealmObject
import io.realm.annotations.Index
import io.realm.annotations.PrimaryKey
import io.realm.annotations.RealmClass

@RealmClass
open class StyleChangerModel: RealmObject() {
    @PrimaryKey
    @Index
    var id: Int? = null

    var setStyle: Int? = null
}