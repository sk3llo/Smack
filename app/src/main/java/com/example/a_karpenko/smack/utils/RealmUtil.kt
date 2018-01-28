package com.example.a_karpenko.smack.utils

import android.view.View
import com.example.a_karpenko.smack.R
import com.example.a_karpenko.smack.models.age_looking_for.*
import com.example.a_karpenko.smack.models.chat.EndMessagesSize
import com.example.a_karpenko.smack.models.chat.FoundUserUid
import com.example.a_karpenko.smack.models.chat.SavedChatsTime
import com.example.a_karpenko.smack.models.chat.StartMessagesSize
import com.example.a_karpenko.smack.models.firestore.ChatModel
import com.example.a_karpenko.smack.models.gender.LookingForGenderModel
import com.example.a_karpenko.smack.models.gender.MyGenderModel
import com.example.a_karpenko.smack.models.my_age.MyAgeModel
import com.vicpin.krealmextensions.queryLast
import io.realm.Realm
import io.realm.RealmModel
import io.realm.RealmResults
import io.realm.Sort
import java.util.*

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
open class RealmUtil {

    var realm: Realm? = Realm.getDefaultInstance()

    //increment id by 1
    open fun getNextKey(realmObject: RealmModel): Int?{

        val number = realm?.where(realmObject::class.java)?.max("id")

        return try {
            if (number != null) {
                number.toInt() + 1
            } else {
                0
            }
        } catch (e: ArrayIndexOutOfBoundsException) {
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

    //Add found user uid to Realm
    fun addFounduserUid(uid: String?){
        try {
            realm?.beginTransaction()
            val foundUserUid = realm?.createObject(FoundUserUid::class.java, getNextKey((FoundUserUid())))
            foundUserUid?.foundUserUid = uid
            realm?.commitTransaction()
        } finally {
            realm?.close()
        }
    }
    fun foundUserUid(): String? = FoundUserUid().queryLast()?.foundUserUid

    //Save and retrieve chat Realm
    fun savedChatTime(time: String?){
        try {
            realm?.beginTransaction()
            realm?.createObject(SavedChatsTime::class.java, getNextKey(SavedChatsTime()))?.time = time
            realm?.commitTransaction()
        } finally {
            realm?.close()
        }
    }
    fun getSavedChatTime(): RealmResults<SavedChatsTime>? = realm?.where(SavedChatsTime::class.java)
            ?.sort("id", Sort.ASCENDING)?.findAll()?.sort("id", Sort.ASCENDING)

//    //Save messags time
//    fun saveMessagesTime(time: Date?){
//        realm?.executeTransaction {
//            var mTime = realm?.cop
//        }
//    }

    //Save messages to Realm
    fun saveMessages(savedMessages: ArrayList<ChatModel>){
        realm?.executeTransaction {
                 realm?.insert(savedMessages)
        }
    }
    fun retrieveMessages(): MutableList<ChatModel>? {
        realm?.beginTransaction()
        val list = realm?.where(ChatModel::class.java)?.sort("id", Sort.ASCENDING)?.findAll()
        realm?.commitTransaction()
        realm?.close()
        return list
    }

    //Save messages
    fun saveStartMessagesSize(size: Int?) {
        try {
            if (getStartMessagesSize()?.size!! <= 0) {
                realm?.beginTransaction()
                realm?.createObject(StartMessagesSize::class.java, getNextKey(StartMessagesSize()))?.startMessagesSize = 0
                realm?.commitTransaction()
            } else if (getStartMessagesSize()?.size!! > 0
                    && getStartMessagesSize()?.last()?.startMessagesSize!! != size) {
                realm?.beginTransaction()
                realm?.createObject(StartMessagesSize::class.java, getNextKey(StartMessagesSize()))?.startMessagesSize = size
                realm?.commitTransaction()
            }
            } finally {
                realm?.close()
            }
        }
    fun saveEndMessagesSize(size: Int?){
        try {
            realm?.beginTransaction()
            realm?.createObject(EndMessagesSize::class.java, getNextKey(EndMessagesSize()))?.endMessagesSize = size
            realm?.commitTransaction()
        } finally {
            realm?.close()
        }
    }
    fun getStartMessagesSize() = realm?.where(StartMessagesSize::class.java)?.sort("id", Sort.ASCENDING)?.findAll()
    fun getEndMessagesSize() = realm?.where(EndMessagesSize::class.java)?.sort("id", Sort.ASCENDING)?.findAll()

    //Change all messages if some row is deleted
    fun changeEndMessages(pos: Int?) {
        realm?.beginTransaction()
        val end = realm?.where(EndMessagesSize::class.java)
                ?.greaterThan("endMessagesSize", RealmUtil().getEndMessagesSize()!![pos!!]?.endMessagesSize!!)?.findAll()
        if (!end?.isEmpty()!!) {
            end.forEach {
                it?.endMessagesSize = it?.endMessagesSize!! - RealmUtil().getEndMessagesSize()!![pos!!]?.endMessagesSize!!
                realm?.insertOrUpdate(it)
            }
        }
        realm?.commitTransaction()
        realm?.close()
    }
    fun changeStartMessages(pos: Int?) {
        realm?.beginTransaction()
        val start = realm?.where(StartMessagesSize::class.java)
                ?.greaterThan("startMessagesSize", RealmUtil().getStartMessagesSize()!![pos!!]?.startMessagesSize!!)?.findAll()
        if (!start?.isEmpty()!!){
            start.forEach {
                it?.startMessagesSize = it?.startMessagesSize!! - RealmUtil().getStartMessagesSize()!![pos!!]?.startMessagesSize!!
                realm?.insertOrUpdate(it)
            }
        }
        realm?.commitTransaction()
        realm?.close()
    }

}
