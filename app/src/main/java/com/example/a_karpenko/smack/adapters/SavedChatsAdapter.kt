package com.example.a_karpenko.smack.adapters

import android.content.Context
import android.content.Intent
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import com.example.a_karpenko.smack.R
import com.example.a_karpenko.smack.models.chat.EndMessagesSize
import com.example.a_karpenko.smack.models.chat.SavedChatsTime
import com.example.a_karpenko.smack.models.chat.StartMessagesSize
import com.example.a_karpenko.smack.models.firestore.ChatModel
import com.example.a_karpenko.smack.ui.SavedChats
import com.example.a_karpenko.smack.ui.SavedMessages
import com.example.a_karpenko.smack.utils.RealmUtil
import com.vicpin.krealmextensions.*
import io.realm.*
import org.jetbrains.anko.collections.forEachByIndex
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.sdk25.coroutines.onClick

open class SavedChatsAdapter(var recyclerView: RecyclerView,
                             var activity: SavedChats,
                             var context: Context,
                             data: OrderedRealmCollection<SavedChatsTime>,
                             animResults: Boolean):
        RealmRecyclerViewAdapter<SavedChatsTime, SavedChatsAdapter.ViewHolder>(data, animResults){

    var realm = Realm.getDefaultInstance()



    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        val savedChats: SavedChatsTime? = data!![position]
        holder?.date?.text = savedChats?.time.toString()
        holder?.trash?.setOnClickListener(ClickListener(position))
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        val v: View = LayoutInflater.from(context).inflate(R.layout.saved_chat_row, parent, false)
        v.setOnClickListener(ClickListener(recyclerView.indexOfChild(v)))
        return ViewHolder(v)
    }



    open inner  class ViewHolder(var container: View) : RecyclerView.ViewHolder(container) {
        var date: TextView? = container.findViewById(R.id.savedChatDate)
        var trash: Button? = container.findViewById(R.id.trash)

        var pos = adapterPosition

//        var click = container.onClick {
//            if (container.id == R.id.trash) {
//                val list = RealmUtil().retrieveMessages()
//                        ?.subList(SavedMessages().startList(pos)!!, RealmUtil().getEndMessagesSize()!![pos]?.endMessagesSize!!)
//
//                Toast.makeText(activity.applicationContext, "pos: $pos, list: $list", Toast.LENGTH_SHORT).show()
//
////                list?.clear()
//                RealmUtil().getStartMessagesSize()?.deleteFromRealm(pos)
//                RealmUtil().getEndMessagesSize()?.deleteFromRealm(pos)
//                recyclerView.removeViewAt(pos)
//                data?.removeAt(pos)
//                notifyItemRemoved(pos)
//                notifyItemChanged(pos)
//            } else {
//                val intent: Intent? = Intent(activity.applicationContext, SavedMessages::class.java)
//                intent?.putExtra("id", recyclerView.indexOfChild(container))
//                activity.startActivity(intent)
//                activity.finish()
//            }
//        }
    }

    open inner class ClickListener(private var pos: Int): View.OnClickListener{
        override fun onClick(v: View?) {

            if (v?.id == R.id.trash) {
                val list: RealmResults<ChatModel>? = realm?.where(ChatModel::class.java)?.findAll()
                val endMessage = realm?.where(EndMessagesSize::class.java)
                        ?.greaterThan("endMessagesSize", EndMessagesSize().queryAll()[pos].endMessagesSize!!)?.findAll()
//                        .query { it.greaterThan("endMessagesSize", EndMessagesSize().queryAll()[pos].endMessagesSize!!) }
                val startMessage = realm?.where(StartMessagesSize::class.java)
                        ?.greaterThan("startMessagesSize", StartMessagesSize().queryAll()[pos].startMessagesSize!!)?.findAll()
//                        .query{ it.greaterThan("startMessagesSize", StartMessagesSize().queryAll()[pos].startMessagesSize!!) }

                var x = StartMessagesSize().query {it.greaterThan("startMessagesSize", StartMessagesSize().queryAll()[pos].startMessagesSize!!).findAll()}
                var y = EndMessagesSize().query {it.greaterThan("endMessagesSize", EndMessagesSize().queryAll()[pos].endMessagesSize!!).findAll()}

                    y?.forEach {
                        if (it.endMessagesSize!! != EndMessagesSize().queryAll()[pos].endMessagesSize!!) {
                            it.endMessagesSize = it.endMessagesSize!! - EndMessagesSize().queryAll()[pos].endMessagesSize!!
                            Log.d("SavedChatsAdapter**** ", "end sizeeeeeeeeeeeeeeeeeeeEEEEEEE: ${it.endMessagesSize}")
                            realm?.beginTransaction()
                            realm?.insertOrUpdate(it)
                            realm?.commitTransaction()
                            realm?.close()
                        }
                    }
                    x?.forEach {
                        if (it.startMessagesSize!! != StartMessagesSize().queryAll()[pos].startMessagesSize!!) {
                            it.startMessagesSize = it.startMessagesSize!! - StartMessagesSize().queryAll()[pos].startMessagesSize!!
                            Log.d("SavedChatsAdapter**** ", "start sizeeeeeeeeeeeeeeeeeeeEEEEEEE: ${it.startMessagesSize}")
                            realm?.beginTransaction()
                            realm?.insertOrUpdate(it)
                            realm?.commitTransaction()
                            realm?.close()
                        }
                    }


                realm?.executeTransaction {
                    if (pos >= 1) {
                        for (i in RealmUtil().getEndMessagesSize()!![pos]?.endMessagesSize!!.minus(1) downTo (RealmUtil().getStartMessagesSize()!![pos]?.startMessagesSize!!)) {
                            Log.d("SavedChatAdapter*** ", "i >= 1: ${i}")
                            list?.deleteFromRealm(i)
                        }
                    } else if (pos == 0 && StartMessagesSize().queryAll().size > 1 && EndMessagesSize().queryAll().size > 1) {
                        for (i in RealmUtil().getEndMessagesSize()!![pos]?.endMessagesSize!!.minus(1) downTo 0) {
                            Log.d("SavedChatAdapter*** ", "i >= 1: $i")
                            list?.deleteFromRealm(i)
                        }
                    } else if (pos == 0 && StartMessagesSize().queryAll().size == 1 && EndMessagesSize().queryAll().size == 1) {
                        list?.deleteAllFromRealm()
                    }
                }



                realm?.beginTransaction()
                //Substract deleted row from other rows

//                when {
//                    pos == 0 && startMessage?.size!! >= 1 -> startMessage.forEach { it?.startMessagesSize = 0 }
////                    RealmUtil().getStartMessagesSize()!![pos]?.startMessagesSize!! == 0 -> startMessage?.forEach{
////                        it?.startMessagesSize = 0
////                    }
//                    RealmUtil().getStartMessagesSize()!![pos]?.startMessagesSize!! != 0 -> startMessage?.forEach{
//                        it?.startMessagesSize!! - RealmUtil().getStartMessagesSize()!![pos]?.startMessagesSize!!
//                    }
//                }


//TODO: messages

                RealmUtil().getStartMessagesSize()?.deleteFromRealm(pos)
                RealmUtil().getEndMessagesSize()?.deleteFromRealm(pos)
                RealmUtil().getSavedChatTime()?.deleteFromRealm(pos)
//                data?.deleteFromRealm(pos)
                realm?.commitTransaction()
                realm?.close()
                recyclerView.removeViewAt(pos)
//                notifyItemRemoved(pos)

                if (pos == 0 && StartMessagesSize().queryAll().size > 1){
                    val start = StartMessagesSize().queryAll()
                    start[0].startMessagesSize = 0
                    realm?.beginTransaction()
                    realm?.insertOrUpdate(start)
                    realm?.commitTransaction()
                    realm?.close()
                }

//                notifyItemRangeChanged(pos, data?.size!!)

            } else {
//                val list = RealmUtil().retrieveMessages()
//                list?.get(recyclerView.indexOfChild(v))

                val intent: Intent? = Intent(context, SavedMessages::class.java)
                intent?.putExtra("id", recyclerView.indexOfChild(v))
                activity.startActivity(intent)
                activity.finish()
            }
//
//            val list = RealmUtil().retrieveMessages()
//            list?.get(recyclerView.indexOfChild(v))
//
//            val intent: Intent? = Intent(context, SavedMessages::class.java)
//            intent?.putExtra("id", recyclerView.indexOfChild(v))
//            activity.startActivity(intent)
//            activity.finish()

        }
    }


    override fun getItemCount(): Int {
        return data?.size!!
    }





}