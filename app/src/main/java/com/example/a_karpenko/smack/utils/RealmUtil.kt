package com.example.a_karpenko.smack.utils

import com.example.a_karpenko.smack.R
import com.example.a_karpenko.smack.models.ChooseModel
import com.vicpin.krealmextensions.queryFirst
import com.vicpin.krealmextensions.queryLast
import com.vicpin.krealmextensions.querySorted
import io.realm.Realm
import io.realm.RealmQuery
import io.realm.RealmResults
import io.realm.Sort

open class RealmUtil {

    var realm: Realm? = Realm.getDefaultInstance()

    fun under18(int: Int?){
        realm?.beginTransaction()
        val change = realm?.createObject(ChooseModel::class.java)
        change?.under18 = int
        realm?.commitTransaction()
    }

    fun getObject(): ChooseModel? {
        var change = realm?.where(ChooseModel::class.java)
                ?.between("under18", 0, 1)?.findFirst()
        return change
    }

}
