package com.example.a_karpenko.smack.utils

import android.view.View
import com.example.a_karpenko.smack.R
import com.example.a_karpenko.smack.models.chat.SearchForChatUtil
import com.example.a_karpenko.smack.models.gender.LookingForGenderModel
import com.example.a_karpenko.smack.models.my_age.MyAgeModel
import com.example.a_karpenko.smack.models.age_looking_for.From19to22
import com.example.a_karpenko.smack.models.age_looking_for.From23to26
import com.example.a_karpenko.smack.models.age_looking_for.From27to35
import com.example.a_karpenko.smack.models.age_looking_for.Over36
import com.example.a_karpenko.smack.models.age_looking_for.Under18
import com.example.a_karpenko.smack.models.chat.FoundUserUid
import com.example.a_karpenko.smack.models.gender.MyGenderModel
import com.vicpin.krealmextensions.*
import io.realm.*

open class RealmUtil {

    var realm: Realm? = Realm.getDefaultInstance()

    //increment id by 1
    open fun getNextKey(realmObject: RealmObject): Int?{
        val number = realm?.where(realmObject::class.java)?.max("id")

        return try {
            if (number != null) {
                number.toInt() + 1
            } else {
                0
            }
        } catch (e:ArrayIndexOutOfBoundsException){
            0
        }
    }

    //Age you are looking for
    //Add 1(yes) or 0(no) to realm based on option
    fun lookingForAge(int: Int?, view: View?){

        try {
            when {
                view?.id == R.id.under18LookingFor -> {
                    realm?.beginTransaction()
                    val under18 = realm?.createObject(Under18::class.java, getNextKey(Under18()))
                    under18?.under18 = int
                    realm?.commitTransaction()
                }
                view?.id == R.id.from19to22LookingFor -> {
                    realm?.beginTransaction()
                    val from19 = realm?.createObject(From19to22::class.java, getNextKey(From19to22()))
                    from19?.from19to22 = int
                    realm?.commitTransaction()
                }
                view?.id == R.id.from23to26LookingFor -> {
                    realm?.beginTransaction()
                    val from23 = realm?.createObject(From23to26::class.java, getNextKey(From23to26()))
                    from23?.from23to26 = int
                    realm?.commitTransaction()
                }
                view?.id == R.id.from27to35LookingFor -> {
                    realm?.beginTransaction()
                    val from27 = realm?.createObject(From27to35::class.java, getNextKey(From27to35()))
                    from27?.from27to35 = int
                    realm?.commitTransaction()
                }
                view?.id == R.id.over36LookingFor -> {
                    realm?.beginTransaction()
                    val over36 = realm?.createObject(Over36::class.java, getNextKey(Over36()))
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

    //My age
    //Add 1(yes) or 0(no) based on option
    fun myAge(int: Int?, view: View?){

        try {
            when {
                view?.id == R.id.under18My -> {
                    realm?.beginTransaction()
                    val under18 = realm?.createObject(MyAgeModel::class.java, getNextKey(MyAgeModel()))
                    under18?.under18 = int
                    realm?.commitTransaction()
                }
                view?.id == R.id.from19to22My -> {
                    realm?.beginTransaction()
                    val from19 = realm?.createObject(MyAgeModel::class.java, getNextKey(MyAgeModel()))
                    from19?.from19to22 = int
                    realm?.commitTransaction()
                }
                view?.id == R.id.from23to26My -> {
                    realm?.beginTransaction()
                    val from23 = realm?.createObject(MyAgeModel::class.java, getNextKey(MyAgeModel()))
                    from23?.from23to26 = int
                    realm?.commitTransaction()
                }
                view?.id == R.id.from27to35My -> {
                    realm?.beginTransaction()
                    val from27 = realm?.createObject(MyAgeModel::class.java, getNextKey(MyAgeModel()))
                    from27?.from27to35 = int
                    realm?.commitTransaction()
                }
                view?.id == R.id.over36My -> {
                    realm?.beginTransaction()
                    val over36 = realm?.createObject(MyAgeModel::class.java, getNextKey(MyAgeModel()))
                    over36?.over36 = int
                    realm?.commitTransaction()
                }
            }
        } finally {
            realm?.close()
        }
    }

    fun under18My(): Int? = MyAgeModel().queryLast()?.under18
    fun from19to22My(): Int? = MyAgeModel().queryLast()?.from19to22
    fun from23to26My(): Int? = MyAgeModel().queryLast()?.from23to26
    fun from27to35My(): Int? = MyAgeModel().queryLast()?.from27to35
    fun over36My(): Int? = MyAgeModel().queryLast()?.over36

    //Choose my and looking for gender
    //Add 1(yes) or 0(no) based on option
    fun gender(int: Int?, view: View){

        try{
            when (view.id){
                R.id.maleGenderMy -> {
                    realm?.beginTransaction()
                    val maleGenderMy = realm?.createObject(MyGenderModel::class.java, getNextKey(MyGenderModel()))
                    maleGenderMy?.maleGenderMy = int
                    realm?.commitTransaction()
                }
                R.id.femaleGenderMy -> {
                    realm?.beginTransaction()
                    val femaleGenderMy = realm?.createObject(MyGenderModel::class.java, getNextKey(MyGenderModel()))
                    femaleGenderMy?.femaleGenderMy = int
                    realm?.commitTransaction()
                }
                R.id.maleGenderLookingFor -> {
                    realm?.beginTransaction()
                    val maleGenderLookingFor = realm?.createObject(LookingForGenderModel::class.java, getNextKey(LookingForGenderModel()))
                    maleGenderLookingFor?.maleGenderLookingFor = int
                    realm?.commitTransaction()
                }
                R.id.femaleGenderLookingFor -> {
                    realm?.beginTransaction()
                    val femaleGenderLookingFor = realm?.createObject(LookingForGenderModel::class.java, getNextKey(LookingForGenderModel()))
                    femaleGenderLookingFor?.femaleGenderLookingFor = int
                    realm?.commitTransaction()
                }
            }
        } finally {
            realm?.close()
        }

    }

    fun maleGenderMy(): Int? = MyGenderModel().queryLast()?.maleGenderMy
    fun femaleGenderMy(): Int? = MyGenderModel().queryLast()?.femaleGenderMy
    fun maleGenderLookingFor(): Int? = LookingForGenderModel().queryLast()?.maleGenderLookingFor
    fun femaleGenderLookingFor(): Int? = LookingForGenderModel().queryLast()?.femaleGenderLookingFor

    //Retry search for chat
    fun retrySearch(retry: Boolean?){
        try {
            realm?.beginTransaction()
            val retrySearchForChat = realm?.createObject(SearchForChatUtil::class.java, getNextKey(SearchForChatUtil()))
            retrySearchForChat?.retryChatSearch = retry
            realm?.commitTransaction()
        } finally {
            realm?.close()
        }
    }

    fun retrySearchForChat(): Boolean? = SearchForChatUtil().queryLast()?.retryChatSearch

    //Add found user uid to Realm
    fun addFounduserUid(uid: String?){
        try {
            realm?.beginTransaction()
            var foundUserUid = realm?.createObject(FoundUserUid::class.java, getNextKey((FoundUserUid())))
            foundUserUid?.foundUserUid = uid
            realm?.commitTransaction()
        } finally {
            realm?.close()
        }
    }

    fun foundUserUid(): String? = FoundUserUid().queryLast()?.foundUserUid

}
