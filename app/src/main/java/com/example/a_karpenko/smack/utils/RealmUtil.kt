package com.example.a_karpenko.smack.utils

import android.view.View
import com.example.a_karpenko.smack.R
import com.example.a_karpenko.smack.models.age_looking_for.*
import com.vicpin.krealmextensions.queryLast
import io.realm.*

open class RealmUtil {

    var realm: Realm? = Realm.getDefaultInstance()

    //Add 0 or 1 to realm based on choose option
    fun add(int: Int?, view: View?) {

        //increment id by 1
        fun getNextKey(real: RealmObject):Int {
            return try {
                val number = realm?.where(real::class.java)?.max("id")
                if (number != null) {
                    number.toInt() + 1
                } else {
                    0
                }
            } catch (e:ArrayIndexOutOfBoundsException) {
                0
            }
        }

        try {
                when {
                    view?.id == R.id.under18LookingFor -> {
                        realm?.beginTransaction()
                        val under18: Under18? = realm?.createObject(Under18::class.java, getNextKey(Under18()))
                        under18?.under18 = int
                        realm?.commitTransaction()
                    }
                    view?.id == R.id.from19to22LookingFor -> {
                        realm?.beginTransaction()
                        val from19: From19to22? = realm?.createObject(From19to22::class.java, getNextKey(From19to22()))
                        from19?.from19to22 = int
                        realm?.commitTransaction()
                    }
                    view?.id == R.id.from23to26LookingFor -> {
                        realm?.beginTransaction()
                        val from23: From23to26? = realm?.createObject(From23to26::class.java, getNextKey(From23to26()))
                        from23?.from23to26 = int
                        realm?.commitTransaction()
                    }
                    view?.id == R.id.from27to35LookingFor -> {
                        realm?.beginTransaction()
                        val from27: From27to35? = realm?.createObject(From27to35::class.java, getNextKey(From27to35()))
                        from27?.from27to35 = int
                        realm?.commitTransaction()
                    }
                    view?.id == R.id.over36LookingFor -> {
                        realm?.beginTransaction()
                        val over36: Over36? = realm?.createObject(Over36::class.java, getNextKey(Over36()))
                        over36?.over36 = int
                        realm?.commitTransaction()
                    }
            }
        } finally {
            realm?.close()
        }
    }

    //Query last choice
    fun under18LookingFor(): Int? = Under18().queryLast()?.under18

    fun from19to22LookingFor(): Int? = From19to22().queryLast()?.from19to22

    fun from23to26LookingFor(): Int? = From23to26().queryLast()?.from23to26

    fun from27to35LookingFor(): Int? = From27to35().queryLast()?.from27to35

    fun over36LookingFor(): Int? = Over36().queryLast()?.over36



}
