package com.example.a_karpenko.smack.adapters

import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.example.a_karpenko.smack.R
import com.example.a_karpenko.smack.models.chat.EndMessagesSize
import com.example.a_karpenko.smack.models.chat.SavedChatsTime
import com.example.a_karpenko.smack.models.firestore.ChatModel
import com.example.a_karpenko.smack.ui.SavedChats
import com.example.a_karpenko.smack.ui.SavedMessages
import com.example.a_karpenko.smack.utils.RealmUtil
import com.vicpin.krealmextensions.queryAll
import io.realm.*

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

            Log.d("SavedChatAdapter****** ", "endSize 0: ${EndMessagesSize().queryAll()[0].endMessagesSize}")

            if (v?.id == R.id.trash){

                RealmUtil().changeEndMessages(pos)
                RealmUtil().changeStartMessages(pos)
                realm?.beginTransaction()

                val list: RealmResults<ChatModel>? = realm?.where(ChatModel::class.java)?.findAll()

                if (!RealmUtil().getStartMessagesSize()?.isEmpty()!! && !RealmUtil().getEndMessagesSize()?.isEmpty()!!) {
                    RealmUtil().getStartMessagesSize()!![0]?.startMessagesSize = 0
                }
//
//                val endMessage = EndMessagesSize().queryAll()
//
//                endMessage.forEach {
//                    it.endMessagesSize = it.endMessagesSize!! - EndMessagesSize().queryAll()[pos].endMessagesSize!!
//                    Log.d("MainActivity****** ", "endMessageSize*************: ${it.endMessagesSize}")
//                }
//
//                realm?.copyToRealmOrUpdate(endMessage)
//
//                realm?.beginTransaction()
//                //Substract deleted row from other rows
//
//                val startMessage = realm?.where(StartMessagesSize::class.java)?.findAll()
//
////                when {
////                    pos == 0 && startMessage?.size!! <= 2 -> startMessage.forEach { it?.startMessagesSize = 0 }
//////                    RealmUtil().getStartMessagesSize()!![pos]?.startMessagesSize!! == 0 -> startMessage?.forEach{
//////                        it?.startMessagesSize = 0
//////                    }
////                    pos != 0 -> {
//                        startMessage?.forEach {
//                            it?.startMessagesSize = it?.startMessagesSize!! - RealmUtil().getStartMessagesSize()!![pos]?.startMessagesSize!!
//                            Log.d("MainActivity****** ", "startMessageSize*************: ${it.startMessagesSize}")
//                        }
//                        if (!RealmUtil().getStartMessagesSize()?.isEmpty()!!) {
//                            RealmUtil().getStartMessagesSize()!![0]?.startMessagesSize = 0
//                        }
////                    }
////                }

                for (i in RealmUtil().getStartMessagesSize()!![pos]?.startMessagesSize!! + 1..RealmUtil().getEndMessagesSize()!![pos]?.endMessagesSize!!) {
                    if (list?.isNotEmpty()!!) {
                        Log.d("SavedChatAdapter*** ", "$i")
                        list?.deleteFromRealm(i)
                    }
                }
                RealmUtil().getStartMessagesSize()?.deleteFromRealm(pos)
                RealmUtil().getEndMessagesSize()?.deleteFromRealm(pos)
                RealmUtil().getSavedChatTime()?.deleteFromRealm(pos)
//                data?.deleteFromRealm(pos)
                realm?.commitTransaction()
                realm?.close()
                recyclerView.removeViewAt(pos)
                notifyItemRemoved(pos)
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