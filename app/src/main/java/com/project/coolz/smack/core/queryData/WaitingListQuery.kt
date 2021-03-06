package com.project.coolz.smack.core.queryData

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Handler
import android.util.Log
import android.widget.Toast
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.project.coolz.smack.ui.ChatActivity
import com.project.coolz.smack.ui.MainActivity
import com.project.coolz.smack.ui.WaitingActivity
import com.project.coolz.smack.utils.RealmUtil
import org.jetbrains.anko.doAsync

//                  Main class for querying Firestore and find suitable person for chat

open class WaitingListQuery(var context: Context, var activity: WaitingActivity) {

    //Current user gender and age
    //Taken from Realm
    private var MeMaleGenderMy = RealmUtil().maleGenderMy()
    private var MeFemaleGenderMy = RealmUtil().femaleGenderMy()

    private var MeMaleGenderLF = RealmUtil().maleGenderLookingFor()
    private var MeFemaleGenderLF = RealmUtil().femaleGenderLookingFor()

    //Age
    private var MeUnder18My = RealmUtil().under18My()
    private var MeFrom19to22My = RealmUtil().from19to22My()
    private var MeFrom23to26My = RealmUtil().from23to26My()
    private var MeFrom27to35My = RealmUtil().from27to35My()
    private var MeOver36My = RealmUtil().over36My()

    private var MeUnder18LF = RealmUtil().under18LookingFor()
    private var MeFrom19to22LF = RealmUtil().from19to22LookingFor()
    private var MeFrom23to26LF = RealmUtil().from23to26LookingFor()
    private var MeFrom27to35LF = RealmUtil().from27to35LookingFor()
    private var MeOver36LF = RealmUtil().over36LookingFor()


//    private val uidMy = RealmUtil().retrieveMyId()

    val myArray: MutableList<DocumentReference>? = ArrayList()

    //WL
//    private val WL = FirebaseFirestore.getInstance().collection("WL")


                            //Check if all users on waiting list
//    fun checkWL() {
//        WL.whereEqualTo("waitingListOn", true).get().addOnCompleteListener { doc ->
//            Log.d("WAITINGLIST_QUERY**** ", "MYUID********::::  $uidMy")
//            val list = doc.result.documents.toMutableList()
//            list.filter {
//                it.reference.id != uidMy
//            }.forEach {
//                if (myArray?.size!! <= 0 && myArray.isEmpty() && RealmUtil().retrieveIsUserFound()?.isUserFound!!) {
//                    doAsync { checkOptions(it.reference) }
//                    Log.d("WAITING_LISTQUERY**** ", "LF ID********::::  ${it.reference.id}")
//                }
//            }
//        }.addOnCompleteListener {
//            if (RealmUtil().retrieveIsUserFound()?.isUserFound!!) {
//                Log.d("WAITING_LISTQUERY**** ", "WL LISTENER CHECKED!!!!!!!")
//                activity.checkWListener()
//            }
//        }
//    }

    //    Check options for user who's true on waiting list
    fun checkOptions(foundUser: DocumentReference) {

            //Network
            val cm: ConnectivityManager? = activity.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val ni: NetworkInfo? = cm?.activeNetworkInfo
            val isWifi: Boolean? = ni?.type == ConnectivityManager.TYPE_WIFI
            val isMobile: Boolean? = ni?.type == ConnectivityManager.TYPE_MOBILE

            val optionsLF = FirebaseFirestore.getInstance()
                    .collection("Users").document(foundUser.id)
                    .collection("options").document("optionsLF")

            val optionsMY = FirebaseFirestore.getInstance()
                    .collection("Users").document(foundUser.id)
                    .collection("options").document("optionsMy")

            optionsMY.get().addOnCompleteListener { optMy ->
                optionsLF.get().addOnCompleteListener { optLF ->
                    if (ni != null && ni.isConnected || isWifi!! || isMobile!!) {
                        if (optMy.exception == null && optLF.exception == null
                                && optMy.result.exists() && optLF.result.exists()) {
                            doAsync {

                                //Add true to search for user
                                RealmUtil().addIsUserFound(true)

                                //Found user's refs
                                    val maleGenderMy = optMy.result["maleGenderMy"]
                                    val femaleGenderMy = optMy.result["femaleGenderMy"]
                                    val under18My = optMy.result["under18My"]
                                    val from19to22My = optMy.result["from19to22My"]
                                    val from23to26My = optMy.result["from23to26My"]
                                    val from27to35My = optMy.result["from27to35My"]
                                    val over36My = optMy.result["over36My"]

                                    //LF refs
                                    val maleGenderLF = optLF.result["maleGenderLookingFor"]
                                    val femaleGenderLF = optLF.result["femaleGenderLookingFor"]
                                    val under18LF = optLF.result["under18LookingFor"]
                                    val from19to22LF = optLF.result["from19to22LookingFor"]
                                    val from23to26LF = optLF.result["from23to26LookingFor"]
                                    val from27to35LF = optLF.result["from27to35LookingFor"]
                                    val over36LF = optLF.result["over36LookingFor"]

                                    //                                            ***Start checking options here***

                                    //                      If I'm male LF MALE
                                    //Weird, but LF male <18
                                    if (MeMaleGenderMy == 1 && MeUnder18My == 1 && MeMaleGenderLF == 1 && MeUnder18LF == 1
                                            && maleGenderMy.toString() == "1" && under18My.toString() == "1"
                                            && maleGenderLF.toString() == "1" && under18LF.toString() == "1") {
                                        if (myArray?.isEmpty()!! && activity.snapshotList?.isEmpty()!!) {
                                            activity.snapshotList?.add(foundUser)
                                            myArray.add(foundUser)
                                            checkOut(myArray.elementAt(0))
                                            Log.d("WAITING_LIST_QUERY*: ", "USER ID: ${myArray.elementAt(0)}")
                                        }
                                    } else if (MeMaleGenderMy == 1 && MeFrom19to22My == 1 && MeMaleGenderLF == 1 && MeUnder18LF == 1
                                            && maleGenderMy.toString() == "1" && under18My.toString() == "1"
                                            && maleGenderLF.toString() == "1" && from19to22LF.toString() == "1") {
                                        if (myArray?.isEmpty()!! && activity.snapshotList?.isEmpty()!!) {
                                            activity.snapshotList?.add(foundUser)
                                            myArray.add(foundUser)
                                            checkOut(myArray.elementAt(0))
                                        }
                                    } else if (MeMaleGenderMy == 1 && MeFrom23to26LF == 1 && MeMaleGenderLF == 1 && MeUnder18LF == 1
                                            && maleGenderMy.toString() == "1" && under18My.toString() == "1"
                                            && maleGenderLF.toString() == "1" && from23to26LF.toString() == "1") {
                                        if (myArray?.isEmpty()!! && activity.snapshotList?.isEmpty()!!) {
                                            activity.snapshotList?.add(foundUser)
                                            myArray.add(foundUser)
                                            checkOut(myArray.elementAt(0))
                                        }
                                    } else if (MeMaleGenderMy == 1 && MeFrom27to35My == 1 && MeMaleGenderLF == 1 && MeUnder18LF == 1
                                            && maleGenderMy.toString() == "1" && under18My.toString() == "1"
                                            && maleGenderLF.toString() == "1" && from27to35LF.toString() == "1") {
                                        if (myArray?.isEmpty()!! && activity.snapshotList?.isEmpty()!!) {
                                            activity.snapshotList?.add(foundUser)
                                            myArray.add(foundUser)
                                            checkOut(myArray.elementAt(0))
                                        }
                                    } else if (MeMaleGenderMy == 1 && MeOver36My == 1 && MeMaleGenderLF == 1 && MeUnder18LF == 1
                                            && maleGenderMy.toString() == "1" && under18My.toString() == "1"
                                            && maleGenderLF.toString() == "1" && over36LF.toString() == "1") {
                                        if (myArray?.isEmpty()!! && activity.snapshotList?.isEmpty()!!) {
                                            activity.snapshotList?.add(foundUser)
                                            myArray.add(foundUser)
                                            checkOut(myArray.elementAt(0))
                                        }
                                        //From 19 to 22 male LF
                                    } else if (MeMaleGenderMy == 1 && MeUnder18My == 1 && MeMaleGenderLF == 1 && MeFrom19to22LF == 1
                                            && maleGenderMy.toString() == "1" && from19to22My.toString() == "1"
                                            && maleGenderLF.toString() == "1" && under18LF.toString() == "1") {
                                        if (myArray?.isEmpty()!! && activity.snapshotList?.isEmpty()!!) {
                                            activity.snapshotList?.add(foundUser)
                                            myArray.add(foundUser)
                                            checkOut(myArray.elementAt(0))
                                        }
                                    } else if (MeMaleGenderMy == 1 && MeFrom19to22My == 1 && MeMaleGenderLF == 1 && MeFrom19to22LF == 1
                                            && maleGenderMy.toString() == "1" && from19to22My.toString() == "1"
                                            && maleGenderLF.toString() == "1" && from19to22LF.toString() == "1") {
                                        if (myArray?.isEmpty()!! && activity.snapshotList?.isEmpty()!!) {
                                            activity.snapshotList?.add(foundUser)
                                            myArray.add(foundUser)
                                            checkOut(myArray.elementAt(0))
                                        }
                                    } else if (MeMaleGenderMy == 1 && MeFrom23to26My == 1 && MeMaleGenderLF == 1 && MeFrom19to22LF == 1
                                            && maleGenderMy.toString() == "1" && from19to22My.toString() == "1"
                                            && maleGenderLF.toString() == "1" && from23to26LF.toString() == "1") {
                                        if (myArray?.isEmpty()!! && activity.snapshotList?.isEmpty()!!) {
                                            activity.snapshotList?.add(foundUser)
                                            myArray.add(foundUser)
                                            checkOut(myArray.elementAt(0))
                                        }
                                    } else if (MeMaleGenderMy == 1 && MeFrom27to35My == 1 && MeMaleGenderLF == 1 && MeFrom19to22LF == 1
                                            && maleGenderMy.toString() == "1" && from19to22My.toString() == "1"
                                            && maleGenderLF.toString() == "1" && from27to35My.toString() == "1") {
                                        if (myArray?.isEmpty()!! && activity.snapshotList?.isEmpty()!!) {
                                            activity.snapshotList?.add(foundUser)
                                            myArray.add(foundUser)
                                            checkOut(myArray.elementAt(0))
                                        }
                                    } else if (MeMaleGenderMy == 1 && MeOver36My == 1 && MeMaleGenderLF == 1 && MeFrom19to22LF == 1
                                            && maleGenderMy.toString() == "1" && from19to22My.toString() == "1"
                                            && maleGenderLF.toString() == "1" && over36LF.toString() == "1") {
                                        if (myArray?.isEmpty()!! && activity.snapshotList?.isEmpty()!!) {
                                            activity.snapshotList?.add(foundUser)
                                            myArray.add(foundUser)
                                            checkOut(myArray.elementAt(0))
                                        }
                                        //From 23 to 26 male LF
                                    } else if (MeMaleGenderMy == 1 && MeUnder18My == 1 && MeMaleGenderLF == 1 && MeFrom23to26LF == 1
                                            && maleGenderMy.toString() == "1" && from23to26My.toString() == "1"
                                            && maleGenderLF.toString() == "1" && under18LF.toString() == "1") {
                                        if (myArray?.isEmpty()!! && activity.snapshotList?.isEmpty()!!) {
                                            activity.snapshotList?.add(foundUser)
                                            myArray.add(foundUser)
                                            checkOut(myArray.elementAt(0))
                                        }
                                    } else if (MeMaleGenderMy == 1 && MeFrom19to22My == 1 && MeMaleGenderLF == 1 && MeFrom23to26LF == 1
                                            && maleGenderMy.toString() == "1" && from23to26My.toString() == "1"
                                            && maleGenderLF.toString() == "1" && from19to22LF.toString() == "1") {
                                        if (myArray?.isEmpty()!! && activity.snapshotList?.isEmpty()!!) {
                                            activity.snapshotList?.add(foundUser)
                                            myArray.add(foundUser)
                                            checkOut(myArray.elementAt(0))
                                        }
                                    } else if (MeMaleGenderMy == 1 && MeFrom23to26My == 1 && MeMaleGenderLF == 1 && MeFrom23to26LF == 1
                                            && maleGenderMy.toString() == "1" && from23to26My.toString() == "1"
                                            && maleGenderLF.toString() == "1" && from23to26LF.toString() == "1") {
                                        if (myArray?.isEmpty()!! && activity.snapshotList?.isEmpty()!!) {
                                            activity.snapshotList?.add(foundUser)
                                            myArray.add(foundUser)
                                            checkOut(myArray.elementAt(0))
                                        }
                                    } else if (MeMaleGenderMy == 1 && MeFrom27to35My == 1 && MeMaleGenderLF == 1 && MeFrom23to26LF == 1
                                            && maleGenderMy.toString() == "1" && from23to26My.toString() == "1"
                                            && maleGenderLF.toString() == "1" && from27to35My.toString() == "1") {
                                        if (myArray?.isEmpty()!! && activity.snapshotList?.isEmpty()!!) {
                                            activity.snapshotList?.add(foundUser)
                                            myArray.add(foundUser)
                                            checkOut(myArray.elementAt(0))
                                        }
                                    } else if (MeMaleGenderMy == 1 && MeOver36My == 1 && MeMaleGenderLF == 1 && MeFrom23to26LF == 1
                                            && maleGenderMy.toString() == "1" && from23to26My.toString() == "1"
                                            && maleGenderLF.toString() == "1" && over36LF.toString() == "1") {
                                        if (myArray?.isEmpty()!! && activity.snapshotList?.isEmpty()!!) {
                                            activity.snapshotList?.add(foundUser)
                                            myArray.add(foundUser)
                                            checkOut(myArray.elementAt(0))
                                        }
                                        //From 27 to 35 male LF
                                    } else if (MeMaleGenderMy == 1 && MeUnder18My == 1 && MeMaleGenderLF == 1 && MeFrom27to35LF == 1
                                            && maleGenderMy.toString() == "1" && from27to35My.toString() == "1"
                                            && maleGenderLF.toString() == "1" && under18LF.toString() == "1") {
                                        if (myArray?.isEmpty()!! && activity.snapshotList?.isEmpty()!!) {
                                            activity.snapshotList?.add(foundUser)
                                            myArray.add(foundUser)
                                            checkOut(myArray.elementAt(0))
                                        }
                                    } else if (MeMaleGenderMy == 1 && MeFrom19to22My == 1 && MeMaleGenderLF == 1 && MeFrom27to35LF == 1
                                            && maleGenderMy.toString() == "1" && from27to35My.toString() == "1"
                                            && maleGenderLF.toString() == "1" && from19to22LF.toString() == "1") {
                                        if (myArray?.isEmpty()!! && activity.snapshotList?.isEmpty()!!) {
                                            activity.snapshotList?.add(foundUser)
                                            myArray.add(foundUser)
                                            checkOut(myArray.elementAt(0))
                                        }
                                    } else if (MeMaleGenderMy == 1 && MeFrom23to26My == 1 && MeMaleGenderLF == 1 && MeFrom27to35LF == 1
                                            && maleGenderMy.toString() == "1" && from27to35My.toString() == "1"
                                            && maleGenderLF.toString() == "1" && from23to26LF.toString() == "1") {
                                        if (myArray?.isEmpty()!! && activity.snapshotList?.isEmpty()!!) {
                                            activity.snapshotList?.add(foundUser)
                                            myArray.add(foundUser)
                                            checkOut(myArray.elementAt(0))
                                        }
                                    } else if (MeMaleGenderMy == 1 && MeFrom27to35My == 1 && MeMaleGenderLF == 1 && MeFrom27to35LF == 1
                                            && maleGenderMy.toString() == "1" && from27to35My.toString() == "1"
                                            && maleGenderLF.toString() == "1" && from27to35My.toString() == "1") {
                                        if (myArray?.isEmpty()!! && activity.snapshotList?.isEmpty()!!) {
                                            activity.snapshotList?.add(foundUser)
                                            myArray.add(foundUser)
                                            checkOut(myArray.elementAt(0))
                                        }
                                    } else if (MeMaleGenderMy == 1 && MeOver36My == 1 && MeMaleGenderLF == 1 && MeFrom27to35LF == 1
                                            && maleGenderMy.toString() == "1" && from27to35My.toString() == "1"
                                            && maleGenderLF.toString() == "1" && over36LF.toString() == "1") {
                                        if (myArray?.isEmpty()!! && activity.snapshotList?.isEmpty()!!) {
                                            activity.snapshotList?.add(foundUser)
                                            myArray.add(foundUser)
                                            checkOut(myArray.elementAt(0))
                                        }
                                        //Over 36 male LF
                                    } else if (MeMaleGenderMy == 1 && MeUnder18My == 1 && MeMaleGenderLF == 1 && MeOver36LF == 1
                                            && maleGenderMy.toString() == "1" && over36My.toString() == "1"
                                            && maleGenderLF.toString() == "1" && under18LF.toString() == "1") {
                                        if (myArray?.isEmpty()!! && activity.snapshotList?.isEmpty()!!) {
                                            activity.snapshotList?.add(foundUser)
                                            myArray.add(foundUser)
                                            checkOut(myArray.elementAt(0))
                                        }
                                    } else if (MeMaleGenderMy == 1 && MeFrom19to22My == 1 && MeMaleGenderLF == 1 && MeOver36LF == 1
                                            && maleGenderMy.toString() == "1" && over36My.toString() == "1"
                                            && maleGenderLF.toString() == "1" && from19to22LF.toString() == "1") {
                                        if (myArray?.isEmpty()!! && activity.snapshotList?.isEmpty()!!) {
                                            activity.snapshotList?.add(foundUser)
                                            myArray.add(foundUser)
                                            checkOut(myArray.elementAt(0))
                                        }
                                    } else if (MeMaleGenderMy == 1 && MeFrom23to26My == 1 && MeMaleGenderLF == 1 && MeOver36LF == 1
                                            && maleGenderMy.toString() == "1" && over36My.toString() == "1"
                                            && maleGenderLF.toString() == "1" && from23to26LF.toString() == "1") {
                                        if (myArray?.isEmpty()!! && activity.snapshotList?.isEmpty()!!) {
                                            activity.snapshotList?.add(foundUser)
                                            myArray.add(foundUser)
                                            checkOut(myArray.elementAt(0))
                                        }
                                    } else if (MeMaleGenderMy == 1 && MeFrom27to35My == 1 && MeMaleGenderLF == 1 && MeOver36LF == 1
                                            && maleGenderMy.toString() == "1" && over36My.toString() == "1"
                                            && maleGenderLF.toString() == "1" && from27to35My.toString() == "1") {
                                        if (myArray?.isEmpty()!! && activity.snapshotList?.isEmpty()!!) {
                                            activity.snapshotList?.add(foundUser)
                                            myArray.add(foundUser)
                                            checkOut(myArray.elementAt(0))
                                        }
                                    } else if (MeMaleGenderMy == 1 && MeOver36My == 1 && MeMaleGenderLF == 1 && MeOver36LF == 1
                                            && maleGenderMy.toString() == "1" && over36My.toString() == "1"
                                            && maleGenderLF.toString() == "1" && over36LF.toString() == "1") {
                                        if (myArray?.isEmpty()!! && activity.snapshotList?.isEmpty()!!) {
                                            activity.snapshotList?.add(foundUser)
                                            myArray.add(foundUser)
                                            checkOut(myArray.elementAt(0))
                                        }
                                    }

                                    //ME MALE LF FEM check
                                    //LF Fem <18
                                    else if (MeMaleGenderMy == 1 && MeUnder18My == 1 && MeFemaleGenderLF == 1 && MeUnder18LF == 1
                                            && femaleGenderMy.toString() == "1" && under18My.toString() == "1"
                                            && maleGenderLF.toString() == "1" && under18LF.toString() == "1") {
                                        if (myArray?.isEmpty()!! && activity.snapshotList?.isEmpty()!!) {
                                            activity.snapshotList?.add(foundUser)
                                            myArray.add(foundUser)
                                            checkOut(myArray.elementAt(0))
                                        }
                                    } else if (MeMaleGenderMy == 1 && MeFrom19to22My == 1 && MeFemaleGenderLF == 1 && MeUnder18LF == 1
                                            && femaleGenderMy.toString() == "1" && under18My.toString() == "1"
                                            && maleGenderLF.toString() == "1" && from19to22LF.toString() == "1") {
                                        if (myArray?.isEmpty()!! && activity.snapshotList?.isEmpty()!!) {
                                            activity.snapshotList?.add(foundUser)
                                            myArray.add(foundUser)
                                            checkOut(myArray.elementAt(0))
                                        }
                                    } else if (MeMaleGenderMy == 1 && MeFrom23to26LF == 1 && MeFemaleGenderLF == 1 && MeUnder18LF == 1
                                            && femaleGenderMy.toString() == "1" && under18My.toString() == "1"
                                            && maleGenderLF.toString() == "1" && from23to26LF.toString() == "1") {
                                        if (myArray?.isEmpty()!! && activity.snapshotList?.isEmpty()!!) {
                                            activity.snapshotList?.add(foundUser)
                                            myArray.add(foundUser)
                                            checkOut(myArray.elementAt(0))
                                        }
                                    } else if (MeMaleGenderMy == 1 && MeFrom27to35My == 1 && MeFemaleGenderLF == 1 && MeUnder18LF == 1
                                            && femaleGenderMy.toString() == "1" && under18My.toString() == "1"
                                            && maleGenderLF.toString() == "1" && from27to35LF.toString() == "1") {
                                        if (myArray?.isEmpty()!! && activity.snapshotList?.isEmpty()!!) {
                                            activity.snapshotList?.add(foundUser)
                                            myArray.add(foundUser)
                                            checkOut(myArray.elementAt(0))
                                        }
                                    } else if (MeMaleGenderMy == 1 && MeOver36My == 1 && MeFemaleGenderLF == 1 && MeUnder18LF == 1
                                            && femaleGenderMy.toString() == "1" && under18My.toString() == "1"
                                            && maleGenderLF.toString() == "1" && over36LF.toString() == "1") {
                                        if (myArray?.isEmpty()!! && activity.snapshotList?.isEmpty()!!) {
                                            activity.snapshotList?.add(foundUser)
                                            myArray.add(foundUser)
                                            checkOut(myArray.elementAt(0))
                                        }
                                        //From 19 to 22 female LF
                                    } else if (MeMaleGenderMy == 1 && MeUnder18My == 1 && MeFemaleGenderLF == 1 && MeFrom19to22LF == 1
                                            && femaleGenderMy.toString() == "1" && from19to22My.toString() == "1"
                                            && maleGenderLF.toString() == "1" && under18LF.toString() == "1") {
                                        if (myArray?.isEmpty()!! && activity.snapshotList?.isEmpty()!!) {
                                            activity.snapshotList?.add(foundUser)
                                            myArray.add(foundUser)
                                            checkOut(myArray.elementAt(0))
                                        }
                                    } else if (MeMaleGenderMy == 1 && MeFrom19to22My == 1 && MeFemaleGenderLF == 1 && MeFrom19to22LF == 1
                                            && femaleGenderMy.toString() == "1" && from19to22My.toString() == "1"
                                            && maleGenderLF.toString() == "1" && from19to22LF.toString() == "1") {
                                        if (myArray?.isEmpty()!! && activity.snapshotList?.isEmpty()!!) {
                                            activity.snapshotList?.add(foundUser)
                                            myArray.add(foundUser)
                                            checkOut(myArray.elementAt(0))
                                        }
                                    } else if (MeMaleGenderMy == 1 && MeFrom23to26My == 1 && MeFemaleGenderLF == 1 && MeFrom19to22LF == 1
                                            && femaleGenderMy.toString() == "1" && from19to22My.toString() == "1"
                                            && maleGenderLF.toString() == "1" && from23to26LF.toString() == "1") {
                                        if (myArray?.isEmpty()!! && activity.snapshotList?.isEmpty()!!) {
                                            activity.snapshotList?.add(foundUser)
                                            myArray.add(foundUser)
                                            checkOut(myArray.elementAt(0))
                                        }
                                    } else if (MeMaleGenderMy == 1 && MeFrom27to35My == 1 && MeFemaleGenderLF == 1 && MeFrom19to22LF == 1
                                            && femaleGenderMy.toString() == "1" && from19to22My.toString() == "1"
                                            && maleGenderLF.toString() == "1" && from27to35My.toString() == "1") {
                                        if (myArray?.isEmpty()!! && activity.snapshotList?.isEmpty()!!) {
                                            activity.snapshotList?.add(foundUser)
                                            myArray.add(foundUser)
                                            checkOut(myArray.elementAt(0))
                                        }
                                    } else if (MeMaleGenderMy == 1 && MeOver36My == 1 && MeFemaleGenderLF == 1 && MeFrom19to22LF == 1
                                            && femaleGenderMy.toString() == "1" && from19to22My.toString() == "1"
                                            && maleGenderLF.toString() == "1" && over36LF.toString() == "1") {
                                        if (myArray?.isEmpty()!! && activity.snapshotList?.isEmpty()!!) {
                                            activity.snapshotList?.add(foundUser)
                                            myArray.add(foundUser)
                                            checkOut(myArray.elementAt(0))
                                        }
                                        //From 23 to 26 female LF
                                    } else if (MeMaleGenderMy == 1 && MeUnder18My == 1 && MeFemaleGenderLF == 1 && MeFrom23to26LF == 1
                                            && femaleGenderMy.toString() == "1" && from23to26My.toString() == "1"
                                            && maleGenderLF.toString() == "1" && under18LF.toString() == "1") {
                                        if (myArray?.isEmpty()!! && activity.snapshotList?.isEmpty()!!) {
                                            activity.snapshotList?.add(foundUser)
                                            myArray.add(foundUser)
                                            checkOut(myArray.elementAt(0))
                                        }
                                    } else if (MeMaleGenderMy == 1 && MeFrom19to22My == 1 && MeFemaleGenderLF == 1 && MeFrom23to26LF == 1
                                            && femaleGenderMy.toString() == "1" && from23to26My.toString() == "1"
                                            && maleGenderLF.toString() == "1" && from19to22LF.toString() == "1") {
                                        if (myArray?.isEmpty()!! && activity.snapshotList?.isEmpty()!!) {
                                            activity.snapshotList?.add(foundUser)
                                            myArray.add(foundUser)
                                            checkOut(myArray.elementAt(0))
                                        }
                                    } else if (MeMaleGenderMy == 1 && MeFrom23to26My == 1 && MeFemaleGenderLF == 1 && MeFrom23to26LF == 1
                                            && femaleGenderMy.toString() == "1" && from23to26My.toString() == "1"
                                            && maleGenderLF.toString() == "1" && from23to26LF.toString() == "1") {
                                        if (myArray?.isEmpty()!! && activity.snapshotList?.isEmpty()!!) {
                                            activity.snapshotList?.add(foundUser)
                                            myArray.add(foundUser)
                                            checkOut(myArray.elementAt(0))
                                        }
                                    } else if (MeMaleGenderMy == 1 && MeFrom27to35My == 1 && MeFemaleGenderLF == 1 && MeFrom23to26LF == 1
                                            && femaleGenderMy.toString() == "1" && from23to26My.toString() == "1"
                                            && maleGenderLF.toString() == "1" && from27to35My.toString() == "1") {
                                        if (myArray?.isEmpty()!! && activity.snapshotList?.isEmpty()!!) {
                                            activity.snapshotList?.add(foundUser)
                                            myArray.add(foundUser)
                                            checkOut(myArray.elementAt(0))
                                        }
                                    } else if (MeMaleGenderMy == 1 && MeOver36My == 1 && MeFemaleGenderLF == 1 && MeFrom23to26LF == 1
                                            && femaleGenderMy.toString() == "1" && from23to26My.toString() == "1"
                                            && maleGenderLF.toString() == "1" && over36LF.toString() == "1") {
                                        if (myArray?.isEmpty()!! && activity.snapshotList?.isEmpty()!!) {
                                            activity.snapshotList?.add(foundUser)
                                            myArray.add(foundUser)
                                            checkOut(myArray.elementAt(0))
                                        }
                                        //From 27 to 35 female LF
                                    } else if (MeMaleGenderMy == 1 && MeUnder18My == 1 && MeFemaleGenderLF == 1 && MeFrom27to35LF == 1
                                            && femaleGenderMy.toString() == "1" && from27to35My.toString() == "1"
                                            && maleGenderLF.toString() == "1" && under18LF.toString() == "1") {
                                        if (myArray?.isEmpty()!! && activity.snapshotList?.isEmpty()!!) {
                                            activity.snapshotList?.add(foundUser)
                                            myArray.add(foundUser)
                                            checkOut(myArray.elementAt(0))
                                        }
                                    } else if (MeMaleGenderMy == 1 && MeFrom19to22My == 1 && MeFemaleGenderLF == 1 && MeFrom27to35LF == 1
                                            && femaleGenderMy.toString() == "1" && from27to35My.toString() == "1"
                                            && maleGenderLF.toString() == "1" && from19to22LF.toString() == "1") {
                                        if (myArray?.isEmpty()!! && activity.snapshotList?.isEmpty()!!) {
                                            activity.snapshotList?.add(foundUser)
                                            myArray.add(foundUser)
                                            checkOut(myArray.elementAt(0))
                                        }
                                    } else if (MeMaleGenderMy == 1 && MeFrom23to26My == 1 && MeFemaleGenderLF == 1 && MeFrom27to35LF == 1
                                            && femaleGenderMy.toString() == "1" && from27to35My.toString() == "1"
                                            && maleGenderLF.toString() == "1" && from23to26LF.toString() == "1") {
                                        if (myArray?.isEmpty()!! && activity.snapshotList?.isEmpty()!!) {
                                            activity.snapshotList?.add(foundUser)
                                            myArray.add(foundUser)
                                            checkOut(myArray.elementAt(0))
                                        }
                                    } else if (MeMaleGenderMy == 1 && MeFrom27to35My == 1 && MeFemaleGenderLF == 1 && MeFrom27to35LF == 1
                                            && femaleGenderMy.toString() == "1" && from27to35My.toString() == "1"
                                            && maleGenderLF.toString() == "1" && from27to35My.toString() == "1") {
                                        if (myArray?.isEmpty()!! && activity.snapshotList?.isEmpty()!!) {
                                            activity.snapshotList?.add(foundUser)
                                            myArray.add(foundUser)
                                            checkOut(myArray.elementAt(0))
                                        }
                                    } else if (MeMaleGenderMy == 1 && MeOver36My == 1 && MeFemaleGenderLF == 1 && MeFrom27to35LF == 1
                                            && femaleGenderMy.toString() == "1" && from27to35My.toString() == "1"
                                            && maleGenderLF.toString() == "1" && over36LF.toString() == "1") {
                                        if (myArray?.isEmpty()!! && activity.snapshotList?.isEmpty()!!) {
                                            activity.snapshotList?.add(foundUser)
                                            myArray.add(foundUser)
                                            checkOut(myArray.elementAt(0))
                                        }
                                        //Over 36 female LF
                                    } else if (MeMaleGenderMy == 1 && MeUnder18My == 1 && MeFemaleGenderLF == 1 && MeOver36LF == 1
                                            && femaleGenderMy.toString() == "1" && over36My.toString() == "1"
                                            && maleGenderLF.toString() == "1" && under18LF.toString() == "1") {
                                        if (myArray?.isEmpty()!! && activity.snapshotList?.isEmpty()!!) {
                                            activity.snapshotList?.add(foundUser)
                                            myArray.add(foundUser)
                                            checkOut(myArray.elementAt(0))
                                        }
                                    } else if (MeMaleGenderMy == 1 && MeFrom19to22My == 1 && MeFemaleGenderLF == 1 && MeOver36LF == 1
                                            && femaleGenderMy.toString() == "1" && over36My.toString() == "1"
                                            && maleGenderLF.toString() == "1" && from19to22LF.toString() == "1") {
                                        if (myArray?.isEmpty()!! && activity.snapshotList?.isEmpty()!!) {
                                            activity.snapshotList?.add(foundUser)
                                            myArray.add(foundUser)
                                            checkOut(myArray.elementAt(0))
                                        }
                                    } else if (MeMaleGenderMy == 1 && MeFrom23to26My == 1 && MeFemaleGenderLF == 1 && MeOver36LF == 1
                                            && femaleGenderMy.toString() == "1" && over36My.toString() == "1"
                                            && maleGenderLF.toString() == "1" && from23to26LF.toString() == "1") {
                                        if (myArray?.isEmpty()!! && activity.snapshotList?.isEmpty()!!) {
                                            activity.snapshotList?.add(foundUser)
                                            myArray.add(foundUser)
                                            checkOut(myArray.elementAt(0))
                                        }
                                    } else if (MeMaleGenderMy == 1 && MeFrom27to35My == 1 && MeFemaleGenderLF == 1 && MeOver36LF == 1
                                            && femaleGenderMy.toString() == "1" && over36My.toString() == "1"
                                            && maleGenderLF.toString() == "1" && from27to35My.toString() == "1") {
                                        if (myArray?.isEmpty()!! && activity.snapshotList?.isEmpty()!!) {
                                            activity.snapshotList?.add(foundUser)
                                            myArray.add(foundUser)
                                            checkOut(myArray.elementAt(0))
                                        }
                                    } else if (MeMaleGenderMy == 1 && MeOver36My == 1 && MeFemaleGenderLF == 1 && MeOver36LF == 1
                                            && femaleGenderMy.toString() == "1" && over36My.toString() == "1"
                                            && maleGenderLF.toString() == "1" && over36LF.toString() == "1") {
                                        if (myArray?.isEmpty()!! && activity.snapshotList?.isEmpty()!!) {
                                            activity.snapshotList?.add(foundUser)
                                            myArray.add(foundUser)
                                            checkOut(myArray.elementAt(0))
                                        }
                                    }

                                    //                  ***ME FEMALE LF MALE
                                    //LF male <18
                                    else if (MeFemaleGenderMy == 1 && MeUnder18My == 1 && MeMaleGenderLF == 1 && MeUnder18LF == 1
                                            && maleGenderMy.toString() == "1" && under18My.toString() == "1"
                                            && femaleGenderLF.toString() == "1" && under18LF.toString() == "1") {
                                        if (myArray?.isEmpty()!! && activity.snapshotList?.isEmpty()!!) {
                                            activity.snapshotList?.add(foundUser)
                                            myArray.add(foundUser)
                                            checkOut(myArray.elementAt(0))
                                        }
                                    } else if (MeFemaleGenderMy == 1 && MeFrom19to22My == 1 && MeMaleGenderLF == 1 && MeUnder18LF == 1
                                            && maleGenderMy.toString() == "1" && under18My.toString() == "1"
                                            && femaleGenderLF.toString() == "1" && from19to22LF.toString() == "1") {
                                        if (myArray?.isEmpty()!! && activity.snapshotList?.isEmpty()!!) {
                                            activity.snapshotList?.add(foundUser)
                                            myArray.add(foundUser)
                                            checkOut(myArray.elementAt(0))
                                        }
                                    } else if (MeFemaleGenderMy == 1 && MeFrom23to26LF == 1 && MeMaleGenderLF == 1 && MeUnder18LF == 1
                                            && maleGenderMy.toString() == "1" && under18My.toString() == "1"
                                            && femaleGenderLF.toString() == "1" && from23to26LF.toString() == "1") {
                                        if (myArray?.isEmpty()!! && activity.snapshotList?.isEmpty()!!) {
                                            activity.snapshotList?.add(foundUser)
                                            myArray.add(foundUser)
                                            checkOut(myArray.elementAt(0))
                                        }
                                    } else if (MeFemaleGenderMy == 1 && MeFrom27to35My == 1 && MeMaleGenderLF == 1 && MeUnder18LF == 1
                                            && maleGenderMy.toString() == "1" && under18My.toString() == "1"
                                            && femaleGenderLF.toString() == "1" && from27to35LF.toString() == "1") {
                                        if (myArray?.isEmpty()!! && activity.snapshotList?.isEmpty()!!) {
                                            activity.snapshotList?.add(foundUser)
                                            myArray.add(foundUser)
                                            checkOut(myArray.elementAt(0))
                                        }
                                    } else if (MeFemaleGenderMy == 1 && MeOver36My == 1 && MeMaleGenderLF == 1 && MeUnder18LF == 1
                                            && maleGenderMy.toString() == "1" && under18My.toString() == "1"
                                            && femaleGenderLF.toString() == "1" && over36LF.toString() == "1") {
                                        if (myArray?.isEmpty()!! && activity.snapshotList?.isEmpty()!!) {
                                            activity.snapshotList?.add(foundUser)
                                            myArray.add(foundUser)
                                            checkOut(myArray.elementAt(0))
                                        }
                                        //From 19 to 22 male LF
                                    } else if (MeFemaleGenderMy == 1 && MeUnder18My == 1 && MeMaleGenderLF == 1 && MeFrom19to22LF == 1
                                            && maleGenderMy.toString() == "1" && from19to22My.toString() == "1"
                                            && femaleGenderLF.toString() == "1" && under18LF.toString() == "1") {
                                        if (myArray?.isEmpty()!! && activity.snapshotList?.isEmpty()!!) {
                                            activity.snapshotList?.add(foundUser)
                                            myArray.add(foundUser)
                                            checkOut(myArray.elementAt(0))
                                        }
                                    } else if (MeFemaleGenderMy == 1 && MeFrom19to22My == 1 && MeMaleGenderLF == 1 && MeFrom19to22LF == 1
                                            && maleGenderMy.toString() == "1" && from19to22My.toString() == "1"
                                            && femaleGenderLF.toString() == "1" && from19to22LF.toString() == "1") {
                                        if (myArray?.isEmpty()!! && activity.snapshotList?.isEmpty()!!) {
                                            activity.snapshotList?.add(foundUser)
                                            myArray.add(foundUser)
                                            checkOut(myArray.elementAt(0))
                                        }
                                    } else if (MeFemaleGenderMy == 1 && MeFrom23to26My == 1 && MeMaleGenderLF == 1 && MeFrom19to22LF == 1
                                            && maleGenderMy.toString() == "1" && from19to22My.toString() == "1"
                                            && femaleGenderLF.toString() == "1" && from23to26LF.toString() == "1") {
                                        if (myArray?.isEmpty()!! && activity.snapshotList?.isEmpty()!!) {
                                            activity.snapshotList?.add(foundUser)
                                            myArray.add(foundUser)
                                            checkOut(myArray.elementAt(0))
                                        }
                                    } else if (MeFemaleGenderMy == 1 && MeFrom27to35My == 1 && MeMaleGenderLF == 1 && MeFrom19to22LF == 1
                                            && maleGenderMy.toString() == "1" && from19to22My.toString() == "1"
                                            && femaleGenderLF.toString() == "1" && from27to35My.toString() == "1") {
                                        if (myArray?.isEmpty()!! && activity.snapshotList?.isEmpty()!!) {
                                            activity.snapshotList?.add(foundUser)
                                            myArray.add(foundUser)
                                            checkOut(myArray.elementAt(0))
                                        }
                                    } else if (MeFemaleGenderMy == 1 && MeOver36My == 1 && MeMaleGenderLF == 1 && MeFrom19to22LF == 1
                                            && maleGenderMy.toString() == "1" && from19to22My.toString() == "1"
                                            && femaleGenderLF.toString() == "1" && over36LF.toString() == "1") {
                                        if (myArray?.isEmpty()!! && activity.snapshotList?.isEmpty()!!) {
                                            activity.snapshotList?.add(foundUser)
                                            myArray.add(foundUser)
                                            checkOut(myArray.elementAt(0))
                                        }
                                        //From 23 to 26 male LF
                                    } else if (MeFemaleGenderMy == 1 && MeUnder18My == 1 && MeMaleGenderLF == 1 && MeFrom23to26LF == 1
                                            && maleGenderMy.toString() == "1" && from23to26My.toString() == "1"
                                            && femaleGenderLF.toString() == "1" && under18LF.toString() == "1") {
                                        if (myArray?.isEmpty()!! && activity.snapshotList?.isEmpty()!!) {
                                            activity.snapshotList?.add(foundUser)
                                            myArray.add(foundUser)
                                            checkOut(myArray.elementAt(0))
                                        }
                                    } else if (MeFemaleGenderMy == 1 && MeFrom19to22My == 1 && MeMaleGenderLF == 1 && MeFrom23to26LF == 1
                                            && maleGenderMy.toString() == "1" && from23to26My.toString() == "1"
                                            && femaleGenderLF.toString() == "1" && from19to22LF.toString() == "1") {
                                        if (myArray?.isEmpty()!! && activity.snapshotList?.isEmpty()!!) {
                                            activity.snapshotList?.add(foundUser)
                                            myArray.add(foundUser)
                                            checkOut(myArray.elementAt(0))
                                        }
                                    } else if (MeFemaleGenderMy == 1 && MeFrom23to26My == 1 && MeMaleGenderLF == 1 && MeFrom23to26LF == 1
                                            && maleGenderMy.toString() == "1" && from23to26My.toString() == "1"
                                            && femaleGenderLF.toString() == "1" && from23to26LF.toString() == "1") {
                                        if (myArray?.isEmpty()!! && activity.snapshotList?.isEmpty()!!) {
                                            activity.snapshotList?.add(foundUser)
                                            myArray.add(foundUser)
                                            checkOut(myArray.elementAt(0))
                                        }
                                    } else if (MeFemaleGenderMy == 1 && MeFrom27to35My == 1 && MeMaleGenderLF == 1 && MeFrom23to26LF == 1
                                            && maleGenderMy.toString() == "1" && from23to26My.toString() == "1"
                                            && femaleGenderLF.toString() == "1" && from27to35My.toString() == "1") {
                                        if (myArray?.isEmpty()!! && activity.snapshotList?.isEmpty()!!) {
                                            activity.snapshotList?.add(foundUser)
                                            myArray.add(foundUser)
                                            checkOut(myArray.elementAt(0))
                                        }
                                    } else if (MeFemaleGenderMy == 1 && MeOver36My == 1 && MeMaleGenderLF == 1 && MeFrom23to26LF == 1
                                            && maleGenderMy.toString() == "1" && from23to26My.toString() == "1"
                                            && femaleGenderLF.toString() == "1" && over36LF.toString() == "1") {
                                        if (myArray?.isEmpty()!! && activity.snapshotList?.isEmpty()!!) {
                                            activity.snapshotList?.add(foundUser)
                                            myArray.add(foundUser)
                                            checkOut(myArray.elementAt(0))
                                        }
                                        //From 27 to 35 male LF
                                    } else if (MeFemaleGenderMy == 1 && MeUnder18My == 1 && MeMaleGenderLF == 1 && MeFrom27to35LF == 1
                                            && maleGenderMy.toString() == "1" && from27to35My.toString() == "1"
                                            && femaleGenderLF.toString() == "1" && under18LF.toString() == "1") {
                                        if (myArray?.isEmpty()!! && activity.snapshotList?.isEmpty()!!) {
                                            activity.snapshotList?.add(foundUser)
                                            myArray.add(foundUser)
                                            checkOut(myArray.elementAt(0))
                                        }
                                    } else if (MeFemaleGenderMy == 1 && MeFrom19to22My == 1 && MeMaleGenderLF == 1 && MeFrom27to35LF == 1
                                            && maleGenderMy.toString() == "1" && from27to35My.toString() == "1"
                                            && femaleGenderLF.toString() == "1" && from19to22LF.toString() == "1") {
                                        if (myArray?.isEmpty()!! && activity.snapshotList?.isEmpty()!!) {
                                            activity.snapshotList?.add(foundUser)
                                            myArray.add(foundUser)
                                            checkOut(myArray.elementAt(0))
                                        }
                                    } else if (MeFemaleGenderMy == 1 && MeFrom23to26My == 1 && MeMaleGenderLF == 1 && MeFrom27to35LF == 1
                                            && maleGenderMy.toString() == "1" && from27to35My.toString() == "1"
                                            && femaleGenderLF.toString() == "1" && from23to26LF.toString() == "1") {
                                        if (myArray?.isEmpty()!! && activity.snapshotList?.isEmpty()!!) {
                                            activity.snapshotList?.add(foundUser)
                                            myArray.add(foundUser)
                                            checkOut(myArray.elementAt(0))
                                        }
                                    } else if (MeFemaleGenderMy == 1 && MeFrom27to35My == 1 && MeMaleGenderLF == 1 && MeFrom27to35LF == 1
                                            && maleGenderMy.toString() == "1" && from27to35My.toString() == "1"
                                            && femaleGenderLF.toString() == "1" && from27to35My.toString() == "1") {
                                        if (myArray?.isEmpty()!! && activity.snapshotList?.isEmpty()!!) {
                                            activity.snapshotList?.add(foundUser)
                                            myArray.add(foundUser)
                                            checkOut(myArray.elementAt(0))
                                        }
                                    } else if (MeFemaleGenderMy == 1 && MeOver36My == 1 && MeMaleGenderLF == 1 && MeFrom27to35LF == 1
                                            && maleGenderMy.toString() == "1" && from27to35My.toString() == "1"
                                            && femaleGenderLF.toString() == "1" && over36LF.toString() == "1") {
                                        if (myArray?.isEmpty()!! && activity.snapshotList?.isEmpty()!!) {
                                            activity.snapshotList?.add(foundUser)
                                            myArray.add(foundUser)
                                            checkOut(myArray.elementAt(0))
                                        }
                                        //Over 36 male LF
                                    } else if (MeFemaleGenderMy == 1 && MeUnder18My == 1 && MeMaleGenderLF == 1 && MeOver36LF == 1
                                            && maleGenderMy.toString() == "1" && over36My.toString() == "1"
                                            && femaleGenderLF.toString() == "1" && under18LF.toString() == "1") {
                                        if (myArray?.isEmpty()!! && activity.snapshotList?.isEmpty()!!) {
                                            activity.snapshotList?.add(foundUser)
                                            myArray.add(foundUser)
                                            checkOut(myArray.elementAt(0))
                                        }
                                    } else if (MeFemaleGenderMy == 1 && MeFrom19to22My == 1 && MeMaleGenderLF == 1 && MeOver36LF == 1
                                            && maleGenderMy.toString() == "1" && over36My.toString() == "1"
                                            && femaleGenderLF.toString() == "1" && from19to22LF.toString() == "1") {
                                        if (myArray?.isEmpty()!! && activity.snapshotList?.isEmpty()!!) {
                                            activity.snapshotList?.add(foundUser)
                                            myArray.add(foundUser)
                                            checkOut(myArray.elementAt(0))
                                        }
                                    } else if (MeFemaleGenderMy == 1 && MeFrom23to26My == 1 && MeMaleGenderLF == 1 && MeOver36LF == 1
                                            && maleGenderMy.toString() == "1" && over36My.toString() == "1"
                                            && femaleGenderLF.toString() == "1" && from23to26LF.toString() == "1") {
                                        if (myArray?.isEmpty()!! && activity.snapshotList?.isEmpty()!!) {
                                            activity.snapshotList?.add(foundUser)
                                            myArray.add(foundUser)
                                            checkOut(myArray.elementAt(0))
                                        }
                                    } else if (MeFemaleGenderMy == 1 && MeFrom27to35My == 1 && MeMaleGenderLF == 1 && MeOver36LF == 1
                                            && maleGenderMy.toString() == "1" && over36My.toString() == "1"
                                            && femaleGenderLF.toString() == "1" && from27to35My.toString() == "1") {
                                        if (myArray?.isEmpty()!! && activity.snapshotList?.isEmpty()!!) {
                                            activity.snapshotList?.add(foundUser)
                                            myArray.add(foundUser)
                                            checkOut(myArray.elementAt(0))
                                        }
                                    } else if (MeFemaleGenderMy == 1 && MeOver36My == 1 && MeMaleGenderLF == 1 && MeOver36LF == 1
                                            && maleGenderMy.toString() == "1" && over36My.toString() == "1"
                                            && femaleGenderLF.toString() == "1" && over36LF.toString() == "1") {
                                        if (myArray?.isEmpty()!! && activity.snapshotList?.isEmpty()!!) {
                                            activity.snapshotList?.add(foundUser)
                                            myArray.add(foundUser)
                                            checkOut(myArray.elementAt(0))
                                        }
                                    }

                                    //                   *** ME FEMALE LF MALE check ***
                                    //LF Fem <18
                                    else if (MeFemaleGenderMy == 1 && MeUnder18My == 1 && MeFemaleGenderLF == 1 && MeUnder18LF == 1
                                            && femaleGenderMy.toString() == "1" && under18My.toString() == "1"
                                            && femaleGenderLF.toString() == "1" && under18LF.toString() == "1") {
                                        if (myArray?.isEmpty()!! && activity.snapshotList?.isEmpty()!!) {
                                            activity.snapshotList?.add(foundUser)
                                            myArray.add(foundUser)
                                            checkOut(myArray.elementAt(0))
                                        }
                                    } else if (MeFemaleGenderMy == 1 && MeFrom19to22My == 1 && MeFemaleGenderLF == 1 && MeUnder18LF == 1
                                            && femaleGenderMy.toString() == "1" && under18My.toString() == "1"
                                            && femaleGenderLF.toString() == "1" && from19to22LF.toString() == "1") {
                                        if (myArray?.isEmpty()!! && activity.snapshotList?.isEmpty()!!) {
                                            activity.snapshotList?.add(foundUser)
                                            myArray.add(foundUser)
                                            checkOut(myArray.elementAt(0))
                                        }
                                    } else if (MeFemaleGenderMy == 1 && MeFrom23to26LF == 1 && MeFemaleGenderLF == 1 && MeUnder18LF == 1
                                            && femaleGenderMy.toString() == "1" && under18My.toString() == "1"
                                            && femaleGenderLF.toString() == "1" && from23to26LF.toString() == "1") {
                                        if (myArray?.isEmpty()!! && activity.snapshotList?.isEmpty()!!) {
                                            activity.snapshotList?.add(foundUser)
                                            myArray.add(foundUser)
                                            checkOut(myArray.elementAt(0))
                                        }
                                    } else if (MeFemaleGenderMy == 1 && MeFrom27to35My == 1 && MeFemaleGenderLF == 1 && MeUnder18LF == 1
                                            && femaleGenderMy.toString() == "1" && under18My.toString() == "1"
                                            && femaleGenderLF.toString() == "1" && from27to35LF.toString() == "1") {
                                        if (myArray?.isEmpty()!! && activity.snapshotList?.isEmpty()!!) {
                                            activity.snapshotList?.add(foundUser)
                                            myArray.add(foundUser)
                                            checkOut(myArray.elementAt(0))
                                        }
                                    } else if (MeFemaleGenderMy == 1 && MeOver36My == 1 && MeFemaleGenderLF == 1 && MeUnder18LF == 1
                                            && femaleGenderMy.toString() == "1" && under18My.toString() == "1"
                                            && femaleGenderLF.toString() == "1" && over36LF.toString() == "1") {
                                        if (myArray?.isEmpty()!! && activity.snapshotList?.isEmpty()!!) {
                                            activity.snapshotList?.add(foundUser)
                                            myArray.add(foundUser)
                                            checkOut(myArray.elementAt(0))
                                        }
                                        //From 19 to 22 female LF
                                    } else if (MeFemaleGenderMy == 1 && MeUnder18My == 1 && MeFemaleGenderLF == 1 && MeFrom19to22LF == 1
                                            && femaleGenderMy.toString() == "1" && from19to22My.toString() == "1"
                                            && femaleGenderLF.toString() == "1" && under18LF.toString() == "1") {
                                        if (myArray?.isEmpty()!! && activity.snapshotList?.isEmpty()!!) {
                                            activity.snapshotList?.add(foundUser)
                                            myArray.add(foundUser)
                                            checkOut(myArray.elementAt(0))
                                        }
                                    } else if (MeFemaleGenderMy == 1 && MeFrom19to22My == 1 && MeFemaleGenderLF == 1 && MeFrom19to22LF == 1
                                            && femaleGenderMy.toString() == "1" && from19to22My.toString() == "1"
                                            && femaleGenderLF.toString() == "1" && from19to22LF.toString() == "1") {
                                        if (myArray?.isEmpty()!! && activity.snapshotList?.isEmpty()!!) {
                                            activity.snapshotList?.add(foundUser)
                                            myArray.add(foundUser)
                                            checkOut(myArray.elementAt(0))
                                        }
                                    } else if (MeFemaleGenderMy == 1 && MeFrom23to26My == 1 && MeFemaleGenderLF == 1 && MeFrom19to22LF == 1
                                            && femaleGenderMy.toString() == "1" && from19to22My.toString() == "1"
                                            && femaleGenderLF.toString() == "1" && from23to26LF.toString() == "1") {
                                        if (myArray?.isEmpty()!! && activity.snapshotList?.isEmpty()!!) {
                                            activity.snapshotList?.add(foundUser)
                                            myArray.add(foundUser)
                                            checkOut(myArray.elementAt(0))
                                        }
                                    } else if (MeFemaleGenderMy == 1 && MeFrom27to35My == 1 && MeFemaleGenderLF == 1 && MeFrom19to22LF == 1
                                            && femaleGenderMy.toString() == "1" && from19to22My.toString() == "1"
                                            && femaleGenderLF.toString() == "1" && from27to35My.toString() == "1") {
                                        if (myArray?.isEmpty()!! && activity.snapshotList?.isEmpty()!!) {
                                            activity.snapshotList?.add(foundUser)
                                            myArray.add(foundUser)
                                            checkOut(myArray.elementAt(0))
                                        }
                                    } else if (MeFemaleGenderMy == 1 && MeOver36My == 1 && MeFemaleGenderLF == 1 && MeFrom19to22LF == 1
                                            && femaleGenderMy.toString() == "1" && from19to22My.toString() == "1"
                                            && femaleGenderLF.toString() == "1" && over36LF.toString() == "1") {
                                        if (myArray?.isEmpty()!! && activity.snapshotList?.isEmpty()!!) {
                                            activity.snapshotList?.add(foundUser)
                                            myArray.add(foundUser)
                                            checkOut(myArray.elementAt(0))
                                        }
                                        //From 23 to 26 female LF
                                    } else if (MeFemaleGenderMy == 1 && MeUnder18My == 1 && MeFemaleGenderLF == 1 && MeFrom23to26LF == 1
                                            && femaleGenderMy.toString() == "1" && from23to26My.toString() == "1"
                                            && femaleGenderLF.toString() == "1" && under18LF.toString() == "1") {
                                        if (myArray?.isEmpty()!! && activity.snapshotList?.isEmpty()!!) {
                                            activity.snapshotList?.add(foundUser)
                                            myArray.add(foundUser)
                                            checkOut(myArray.elementAt(0))
                                        }
                                    } else if (MeFemaleGenderMy == 1 && MeFrom19to22My == 1 && MeFemaleGenderLF == 1 && MeFrom23to26LF == 1
                                            && femaleGenderMy.toString() == "1" && from23to26My.toString() == "1"
                                            && femaleGenderLF.toString() == "1" && from19to22LF.toString() == "1") {
                                        if (myArray?.isEmpty()!! && activity.snapshotList?.isEmpty()!!) {
                                            activity.snapshotList?.add(foundUser)
                                            myArray.add(foundUser)
                                            checkOut(myArray.elementAt(0))
                                        }
                                    } else if (MeFemaleGenderMy == 1 && MeFrom23to26My == 1 && MeFemaleGenderLF == 1 && MeFrom23to26LF == 1
                                            && femaleGenderMy.toString() == "1" && from23to26My.toString() == "1"
                                            && femaleGenderLF.toString() == "1" && from23to26LF.toString() == "1") {
                                        if (myArray?.isEmpty()!! && activity.snapshotList?.isEmpty()!!) {
                                            activity.snapshotList?.add(foundUser)
                                            myArray.add(foundUser)
                                            checkOut(myArray.elementAt(0))
                                        }
                                    } else if (MeFemaleGenderMy == 1 && MeFrom27to35My == 1 && MeFemaleGenderLF == 1 && MeFrom23to26LF == 1
                                            && femaleGenderMy.toString() == "1" && from23to26My.toString() == "1"
                                            && femaleGenderLF.toString() == "1" && from27to35My.toString() == "1") {
                                        if (myArray?.isEmpty()!! && activity.snapshotList?.isEmpty()!!) {
                                            activity.snapshotList?.add(foundUser)
                                            myArray.add(foundUser)
                                            checkOut(myArray.elementAt(0))
                                        }
                                    } else if (MeFemaleGenderMy == 1 && MeOver36My == 1 && MeFemaleGenderLF == 1 && MeFrom23to26LF == 1
                                            && femaleGenderMy.toString() == "1" && from23to26My.toString() == "1"
                                            && femaleGenderLF.toString() == "1" && over36LF.toString() == "1") {
                                        if (myArray?.isEmpty()!! && activity.snapshotList?.isEmpty()!!) {
                                            activity.snapshotList?.add(foundUser)
                                            myArray.add(foundUser)
                                            checkOut(myArray.elementAt(0))
                                        }
                                        //From 27 to 35 female LF
                                    } else if (MeFemaleGenderMy == 1 && MeUnder18My == 1 && MeFemaleGenderLF == 1 && MeFrom27to35LF == 1
                                            && femaleGenderMy.toString() == "1" && from27to35My.toString() == "1"
                                            && femaleGenderLF.toString() == "1" && under18LF.toString() == "1") {
                                        if (myArray?.isEmpty()!! && activity.snapshotList?.isEmpty()!!) {
                                            activity.snapshotList?.add(foundUser)
                                            myArray.add(foundUser)
                                            checkOut(myArray.elementAt(0))
                                        }
                                    } else if (MeFemaleGenderMy == 1 && MeFrom19to22My == 1 && MeFemaleGenderLF == 1 && MeFrom27to35LF == 1
                                            && femaleGenderMy.toString() == "1" && from27to35My.toString() == "1"
                                            && femaleGenderLF.toString() == "1" && from19to22LF.toString() == "1") {
                                        if (myArray?.isEmpty()!! && activity.snapshotList?.isEmpty()!!) {
                                            activity.snapshotList?.add(foundUser)
                                            myArray.add(foundUser)
                                            checkOut(myArray.elementAt(0))
                                        }
                                    } else if (MeFemaleGenderMy == 1 && MeFrom23to26My == 1 && MeFemaleGenderLF == 1 && MeFrom27to35LF == 1
                                            && femaleGenderMy.toString() == "1" && from27to35My.toString() == "1"
                                            && femaleGenderLF.toString() == "1" && from23to26LF.toString() == "1") {
                                        if (myArray?.isEmpty()!! && activity.snapshotList?.isEmpty()!!) {
                                            activity.snapshotList?.add(foundUser)
                                            myArray.add(foundUser)
                                            checkOut(myArray.elementAt(0))
                                        }
                                    } else if (MeFemaleGenderMy == 1 && MeFrom27to35My == 1 && MeFemaleGenderLF == 1 && MeFrom27to35LF == 1
                                            && femaleGenderMy.toString() == "1" && from27to35My.toString() == "1"
                                            && femaleGenderLF.toString() == "1" && from27to35My.toString() == "1") {
                                        if (myArray?.isEmpty()!! && activity.snapshotList?.isEmpty()!!) {
                                            activity.snapshotList?.add(foundUser)
                                            myArray.add(foundUser)
                                            checkOut(myArray.elementAt(0))
                                        }
                                    } else if (MeFemaleGenderMy == 1 && MeOver36My == 1 && MeFemaleGenderLF == 1 && MeFrom27to35LF == 1
                                            && femaleGenderMy.toString() == "1" && from27to35My.toString() == "1"
                                            && femaleGenderLF.toString() == "1" && over36LF.toString() == "1") {
                                        if (myArray?.isEmpty()!! && activity.snapshotList?.isEmpty()!!) {
                                            activity.snapshotList?.add(foundUser)
                                            myArray.add(foundUser)
                                            checkOut(myArray.elementAt(0))
                                        }
                                        //Over 36 female LF
                                    } else if (MeFemaleGenderMy == 1 && MeUnder18My == 1 && MeFemaleGenderLF == 1 && MeOver36LF == 1
                                            && femaleGenderMy.toString() == "1" && over36My.toString() == "1"
                                            && maleGenderLF.toString() == "1" && under18LF.toString() == "1") {
                                        if (myArray?.isEmpty()!! && activity.snapshotList?.isEmpty()!!) {
                                            activity.snapshotList?.add(foundUser)
                                            myArray.add(foundUser)
                                            checkOut(myArray.elementAt(0))
                                        }
                                    } else if (MeFemaleGenderMy == 1 && MeFrom19to22My == 1 && MeFemaleGenderLF == 1 && MeOver36LF == 1
                                            && femaleGenderMy.toString() == "1" && over36My.toString() == "1"
                                            && femaleGenderLF.toString() == "1" && from19to22LF.toString() == "1") {
                                        if (myArray?.isEmpty()!! && activity.snapshotList?.isEmpty()!!) {
                                            activity.snapshotList?.add(foundUser)
                                            myArray.add(foundUser)
                                            checkOut(myArray.elementAt(0))
                                        }
                                    } else if (MeFemaleGenderMy == 1 && MeFrom23to26My == 1 && MeFemaleGenderLF == 1 && MeOver36LF == 1
                                            && femaleGenderMy.toString() == "1" && over36My.toString() == "1"
                                            && femaleGenderLF.toString() == "1" && from23to26LF.toString() == "1") {
                                        if (myArray?.isEmpty()!! && activity.snapshotList?.isEmpty()!!) {
                                            activity.snapshotList?.add(foundUser)
                                            myArray.add(foundUser)
                                            checkOut(myArray.elementAt(0))
                                        }
                                    } else if (MeFemaleGenderMy == 1 && MeFrom27to35My == 1 && MeFemaleGenderLF == 1 && MeOver36LF == 1
                                            && femaleGenderMy.toString() == "1" && over36My.toString() == "1"
                                            && femaleGenderLF.toString() == "1" && from27to35My.toString() == "1") {
                                        if (myArray?.isEmpty()!! && activity.snapshotList?.isEmpty()!!) {
                                            activity.snapshotList?.add(foundUser)
                                            myArray.add(foundUser)
                                            checkOut(myArray.elementAt(0))
                                        }
                                    } else if (MeFemaleGenderMy == 1 && MeOver36My == 1 && MeFemaleGenderLF == 1 && MeOver36LF == 1
                                            && femaleGenderMy.toString() == "1" && over36My.toString() == "1"
                                            && femaleGenderLF.toString() == "1" && over36LF.toString() == "1") {
                                        if (myArray?.isEmpty()!! && activity.snapshotList?.isEmpty()!!) {
                                            activity.snapshotList?.add(foundUser)
                                            myArray.add(foundUser)
                                            checkOut(myArray.elementAt(0))
                                        }
                                    }
                                }
                            }

                        //Check if user was found
//                                    else {
//                                        checkRetry()
//                                    }

                    } else {
                        activity.startActivity(Intent(activity.context, MainActivity::class.java))
                        Toast.makeText(activity.context, "Please, check your Internet connection", Toast.LENGTH_SHORT).show()
                        activity.finish()
                    }
                }
            }
        }

    //Start Chat activity
    private fun checkOut(foundUser: DocumentReference) {
        doAsync {
            var check: Boolean? = true
//            Log.d("WAITING_QUERY*: ", "ARRAY: ${myArray?.size!!}")
//            Log.d("WAITING_QUERY*: ", "SNAP: ${activity.snapshotList?.size!!}")
//            Log.d("WAITING_QUERY*: ", "isUserFound: ${RealmUtil().retrieveIsUserFound()?.isUserFound!!}")
            if (myArray?.size == 1 && check == true && RealmUtil().retrieveIsUserFound()?.isUserFound!! && activity.snapshotList?.size!! == 1) {
                check = false
                Log.d("WAITING_QUERY*: ", "CHECK: $check")
                RealmUtil().addIsUserFound(false)
                Log.d("WaitingListQuery**** ", "CONNECTED USER: ${myArray.elementAt(0).id/*foundUser.id*/}")
                //Start Chat Activity
                val intent = Intent(context, ChatActivity::class.java)
                intent.putExtra("foundUser", myArray.elementAt(0).id)
                context.startActivity(intent)
                activity.finish()
            }
            if (!RealmUtil().retrieveIsUserFound()?.isUserFound!! && myArray?.size == 1 && activity.snapshotList?.size!! == 1 && check == true) {
//                Log.d("WL_QUERY*!*!*!: ", "FALSE, 1 AND 1***********")
                RealmUtil().addIsUserFound(true)
                checkOut(foundUser)
//                activity.checkWListener()?.remove()
//                activity.checkWListener()
            }
        }
    }
}