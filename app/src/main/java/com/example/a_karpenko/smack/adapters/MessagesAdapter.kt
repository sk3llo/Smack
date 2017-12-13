package com.example.a_karpenko.smack.adapters

import android.app.ActionBar
import android.support.constraint.ConstraintLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.util.Size
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import com.example.a_karpenko.smack.R
import com.example.a_karpenko.smack.core.MyOptionsChecker
import com.example.a_karpenko.smack.models.chat.ChatModel
import com.example.a_karpenko.smack.ui.ChatActivity
import com.example.a_karpenko.smack.utils.RealmUtil
import com.firebase.ui.firestore.FirestoreArray
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.firebase.ui.firestore.SnapshotParser
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import java.util.*
import kotlin.collections.ArrayList

var uidMy = FirebaseAuth.getInstance().currentUser?.uid

val myRoomRef = FirebaseFirestore.getInstance()
                .collection("Users").document("$uidMy")
                .collection("rooms").document("${RealmUtil().foundUserUid()}")
                .collection("messages")

val myQuery = myRoomRef.orderBy("timestamp")

val MYOPTIONS = FirestoreRecyclerOptions.Builder<ChatModel>()
.setQuery(myQuery, ChatModel::class.java)
.build()

val send: Int? = 1
val receive: Int? = 2

open class MessagesAdapter(var messages: ArrayList<ChatModel>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == send) {
            Log.d("Messages adapter***** ", "$viewType == sent")
            val send = LayoutInflater.from(parent?.context).inflate(R.layout.message_sent, parent, false)
            object : MessageSentViewHolder(send) {}
        } else  {
            Log.d("Messages adapter***** ", "$viewType == received")
            val receive = LayoutInflater.from(parent?.context).inflate(R.layout.message_received, parent, false)
            object : MessageReceivedViewHolder(receive) {}
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        //Time
        val c = Calendar.getInstance()
        val minutes = c.get(Calendar.MINUTE)
        val hour = c.get(Calendar.HOUR).toString()
        val time = "$hour:$minutes"
        //Array of items
        val sendHolder = messages[position]

        if (holder is MessageSentViewHolder) {
            holder.messageSent?.text = sendHolder.message
            holder.messageSentTime?.text = time
        } else if (holder is MessageReceivedViewHolder) {
            val receive: MessageReceivedViewHolder = holder
            receive.messageReceived?.text = sendHolder.message
            receive.messageReceivedTime?.text = time
        }

    }


    override fun getItemViewType(position: Int): Int {
        val getMessage = messages[position]
        return if(getMessage.from == uidMy){
            send!!
        } else {
            receive!!
        }
    }
//
    override fun getItemCount(): Int {
        return messages.size
    }

    fun add(item: ChatModel, position: Int){
        messages.add(position, item)
        notifyItemInserted(position)
    }

    fun remove(item: ChatModel){
        val position: Int? = messages.indexOf(item)
        messages.removeAt(position!!)
        notifyItemRemoved(position)
    }

}




open class MessageSentViewHolder(view: View?) : RecyclerView.ViewHolder(view) {
    var messageSent : TextView? = view?.findViewById(R.id.messageSentText)
    var messageSentTime : TextView? = view?.findViewById(R.id.messageSentTime)

}
open class MessageReceivedViewHolder(view: View?) : RecyclerView.ViewHolder(view) {
    var messageReceived : TextView? = view?.findViewById(R.id.messageReceivedText)
    var messageReceivedTime : TextView? = view?.findViewById(R.id.messageReceivedTime)

}