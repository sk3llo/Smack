package com.example.a_karpenko.smack.utils

import android.util.Log
import android.view.View
import com.example.a_karpenko.smack.R
import com.example.a_karpenko.smack.models.ChooseModel
import com.vicpin.krealmextensions.*
import io.realm.Realm
import io.realm.RealmQuery
import io.realm.RealmResults
import io.realm.Sort

open class RealmUtil {

    var realm: Realm? = Realm.getDefaultInstance()

    //Add 0 or 1 to realm based on choose option
    fun add(int: Int?, view: View?){
        realm?.beginTransaction()
        val change = realm?.createObject(ChooseModel::class.java)
        when {
            view?.id == R.id.under18LookingFor -> {
                change?.under18 = change?.under18?.plus(int!!)
            }
            view?.id == R.id.from19to22LookingFor -> {
                change?.from19to22 = change?.from19to22?.plus(int!!)
            }
            view?.id == R.id.from23to26LookingFor -> {
                change?.from23to26 = change?.from23to26?.plus(int!!)
            }
            view?.id == R.id.from27to35LookingFor -> {
                change?.from27to35 = change?.from27to35?.plus(int!!)
            }
            view?.id == R.id.over36LookingFor -> {
                change?.over36 = change?.over36?.plus(int!!)
            }
        }
        realm?.commitTransaction()
    }

    //Query last choice
    fun under18LookingFor(): Int? = ChooseModel()
            .queryLast { realmQuery -> realmQuery.between("under18", 0, 1) }
            ?.under18

    fun from19to22LookingFor(): Int? = ChooseModel()
            .queryLast { realmQuery -> realmQuery.between("under18", 0, 1) }
            ?.from19to22

    fun from23to26LookingFor(): Int? = ChooseModel()
            .queryLast { realmQuery -> realmQuery.between("under18", 0, 1) }
            ?.from23to26

    fun from27to35LookingFor(): Int? = ChooseModel()
            .queryLast { realmQuery -> realmQuery.between("under18", 0, 1) }
            ?.from27to35

    fun over36LookingFor(): Int? = ChooseModel()
            .queryLast { realmQuery -> realmQuery.between("under18", 0, 1) }
            ?.over36



}
