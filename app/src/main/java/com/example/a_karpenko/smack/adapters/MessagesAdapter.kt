package com.example.a_karpenko.smack.adapters

import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.a_karpenko.smack.R
import com.example.a_karpenko.smack.models.firestore.ChatModel
import com.example.a_karpenko.smack.utils.RealmUtil
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.*

var uidMy = FirebaseAuth.getInstance().currentUser?.uid

val myRoomRef = FirebaseFirestore.getInstance()
                .collection("Users").document("${RealmUtil().foundUserUid()}")
                .collection("rooms").document("$uidMy")
                .collection("messages")


val send: Int? = 1
val receive: Int? = 2

open class MessagesAdapter(var messages: ArrayList<ChatModel>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == send) {
            Log.d("Messages adapter***** ", "$viewType == sent")
            val send = LayoutInflater.from(parent.context).inflate(R.layout.message_sent, parent, false)
            object : MessageSentViewHolder(send) {}
        } else  {
            Log.d("Messages adapter***** ", "$viewType == received")
            val receive = LayoutInflater.from(parent.context).inflate(R.layout.message_received, parent, false)
            object : MessageReceivedViewHolder(receive) {}
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        //Time
        val formatDate: SimpleDateFormat? = object: SimpleDateFormat("h:mm a") {}
        val time: String? = formatDate?.format(Date())
//        val time = messages[position].time

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