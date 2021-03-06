package com.project.coolz.smack.utils

import android.view.View
import com.project.coolz.smack.R
import com.project.coolz.smack.models.StyleChangerModel
import com.project.coolz.smack.models.age_looking_for.*
import com.project.coolz.smack.models.chat.*
import com.project.coolz.smack.models.firestore.ChatModel
import com.project.coolz.smack.models.gender.LookingForGenderModel
import com.project.coolz.smack.models.gender.MyGenderModel
import com.project.coolz.smack.models.my_age.MyAgeModel
import io.realm.*

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
open class RealmUtil {

    var realm: Realm? = Realm.getDefaultInstance()

    //increment id by 1
    open fun getNextKey(realmObject: RealmModel): Int? {

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
    fun lookingForAge(int: Int?, view: View?) {

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
    fun under18LookingFor(): Int? = realm?.where(Under18::class.java)?.findAll()?.last()?.under18

    fun from19to22LookingFor(): Int? = realm?.where(From19to22::class.java)?.findAll()?.last()?.from19to22
    fun from23to26LookingFor(): Int? = realm?.where(From23to26::class.java)?.findAll()?.last()?.from23to26
    fun from27to35LookingFor(): Int? = realm?.where(From27to35::class.java)?.findAll()?.last()?.from27to35
    fun over36LookingFor(): Int? = realm?.where(Over36::class.java)?.findAll()?.last()?.over36

    //My age
    //Add 1(yes) or 0(no) based on option
    fun myAge(int: Int?, view: View?) {

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

    fun under18My(): Int? = realm?.where(MyAgeModel::class.java)?.findAll()?.last()?.under18
    fun from19to22My(): Int? = realm?.where(MyAgeModel::class.java)?.findAll()?.last()?.from19to22
    fun from23to26My(): Int? = realm?.where(MyAgeModel::class.java)?.findAll()?.last()?.from23to26
    fun from27to35My(): Int? = realm?.where(MyAgeModel::class.java)?.findAll()?.last()?.from27to35
    fun over36My(): Int? = realm?.where(MyAgeModel::class.java)?.findAll()?.last()?.over36

    //Choose my and looking for gender
    //Add 1(yes) or 0(no) based on option
    fun gender(int: Int?, view: View) {

        try {
            when (view.id) {
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

    fun maleGenderMy(): Int? = realm?.where(MyGenderModel::class.java)?.findAll()?.last()?.maleGenderMy
    fun femaleGenderMy(): Int? = realm?.where(MyGenderModel::class.java)?.findAll()?.last()?.femaleGenderMy
    fun maleGenderLookingFor(): Int? = realm?.where(LookingForGenderModel::class.java)?.findAll()?.last()?.maleGenderLookingFor
    fun femaleGenderLookingFor(): Int? = realm?.where(LookingForGenderModel::class.java)?.findAll()?.last()?.femaleGenderLookingFor

    //Add or retrive boolean if user is found
    fun addIsUserFound(isFound: Boolean){
        realm?.executeTransaction {
            realm?.createObject(ChatWall::class.java, getNextKey(ChatWall()))?.isUserFound = isFound
        }
    }
    fun retrieveIsUserFound(): ChatWall? {
        return realm?.where(ChatWall::class.java)?.sort("id", Sort.ASCENDING)?.findAll()?.last()
        }

    //Save and retrieve chat Realm
    fun savedChatTime(time: String?) {
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


    //Save messages to Realm
    fun saveMessages(savedMessages: RealmList<ChatModel>) {
        realm?.executeTransaction {
            realm?.copyToRealm(savedMessages)
        }
    }

    fun retrieveMessages(): RealmResults<ChatModel>? {
        realm?.beginTransaction()
        val list = realm?.where(ChatModel::class.java)?.sort("id", Sort.ASCENDING)?.findAll()
        realm?.commitTransaction()
        realm?.close()
        return list
    }

    //Save messages
    fun saveStartMessagesSize(size: Int?) {
        try {
            if (getStartMessagesSize().size == 0) {
                realm?.beginTransaction()
                realm?.createObject(StartMessagesSize::class.java, getNextKey(StartMessagesSize()))?.startMessagesSize = 0
                realm?.commitTransaction()
            } else if (getStartMessagesSize().size > 0
                    && getStartMessagesSize().last()?.startMessagesSize!! != size) {
                realm?.beginTransaction()
                realm?.createObject(StartMessagesSize::class.java, getNextKey(StartMessagesSize()))?.startMessagesSize = size
                realm?.commitTransaction()
            }
        } finally {
            realm?.close()
        }
    }

    fun saveEndMessagesSize(size: Int?) {
        try {
            realm?.beginTransaction()
            realm?.createObject(EndMessagesSize::class.java, getNextKey(EndMessagesSize()))?.endMessagesSize = size
            realm?.commitTransaction()
        } finally {
            realm?.close()
        }
    }

    fun saveMyId(id: String?){
        try {
            realm?.beginTransaction()
            realm?.createObject(MyUniqueId::class.java)?.id = id
            realm?.commitTransaction()
        } finally {
            realm?.close()
        }
    }

    fun retrieveMyId(): String? {
        realm?.beginTransaction()
        val myId = realm?.where(MyUniqueId::class.java)?.findFirst()?.id
        realm?.commitTransaction()
        realm?.close()
        return myId
    }

    fun getStartMessagesSize() = realm?.where(StartMessagesSize::class.java)?.sort("id")?.findAll()!!
    fun getEndMessagesSize() = realm?.where(EndMessagesSize::class.java)?.sort("id")?.findAll()!!

    fun saveStyle(style: Int?){
        realm?.beginTransaction()
        realm?.createObject(StyleChangerModel::class.java, getNextKey(StyleChangerModel()))?.setStyle = style
        realm?.commitTransaction()
        realm?.close()
    }
    fun getStyle() = realm?.where(StyleChangerModel::class.java)?.sort("id")?.findAll()?.last()?.setStyle

}