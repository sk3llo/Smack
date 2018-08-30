package com.project.coolz.smack.adapters

import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.project.coolz.smack.R
import com.project.coolz.smack.models.firestore.ChatModel
import com.project.coolz.smack.utils.RealmUtil
import io.realm.OrderedRealmCollection
import io.realm.RealmRecyclerViewAdapter
import io.realm.Sort

open class SavedMessagesAdapter(var messages: OrderedRealmCollection<ChatModel>, var intent: Int)
    : RealmRecyclerViewAdapter<ChatModel,
        RecyclerView.ViewHolder>(messages, true, true) {

    var uidMy = RealmUtil().retrieveMyId()

    val send: Int? = 1
    val receive: Int? = 2


    fun startList(intent: Int?): Int? {
        return try {
            if (intent != null && intent == 0) {
                0
            } else if (intent != null
                    && RealmUtil().getStartMessagesSize().isNotEmpty()
                    && RealmUtil().getStartMessagesSize().last()?.startMessagesSize == RealmUtil().getStartMessagesSize()[intent]?.startMessagesSize!!
                    && intent != 0) {
                RealmUtil().getStartMessagesSize().last()?.startMessagesSize
            } else if (intent != null
                    && RealmUtil().getStartMessagesSize().isNotEmpty()
                    && RealmUtil().getStartMessagesSize().last()?.startMessagesSize != RealmUtil().getStartMessagesSize()[intent]?.startMessagesSize!!
                    && intent != 0) {
                RealmUtil().getStartMessagesSize()[intent]?.startMessagesSize
            } else {
                RealmUtil().getStartMessagesSize()[intent!!]?.startMessagesSize
            }
        } catch (e: java.lang.IndexOutOfBoundsException){
            RealmUtil().getStartMessagesSize()[intent!!]?.startMessagesSize
        }
    }

    var messagess = messages.sort("id", Sort.ASCENDING)
            ?.subList(startList(intent)!!, RealmUtil().getEndMessagesSize()[intent]?.endMessagesSize!!)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == send) {
            val send = LayoutInflater.from(parent.context).inflate(R.layout.message_sent, parent, false)
            object : MessageSentViewHolder(send) {}
        } else  {
            val receive = LayoutInflater.from(parent.context).inflate(R.layout.message_received, parent, false)
            object : MessageReceivedViewHolder(receive) {}
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        //Array of items
        val sendHolder = messagess!![position]
        Log.d("SAVED_MESSAGES_ADAPTER", ":  \n $messagess \n")

        if (holder is MessageSentViewHolder) {
            holder.messageSent?.text = sendHolder?.message
            holder.messageSentTime?.text = messages[position]?.time
        } else if (holder is MessageReceivedViewHolder) {
            val receive: MessageReceivedViewHolder = holder
            receive.messageReceived?.text = sendHolder?.message
            receive.messageReceivedTime?.text = messages[position]?.time
        }
    }


    override fun getItemViewType(position: Int): Int {
        val getMessage = messagess!![position]
        return if(getMessage?.from == uidMy){
            send!!
        } else {
            receive!!
        }
    }
    //
    override fun getItemCount(): Int {
        return messagess!!.size
    }

    open class MessageSentViewHolder(view: View?) : RecyclerView.ViewHolder(view) {
        var messageSent : TextView? = view?.findViewById(R.id.messageSentText)
        var messageSentTime : TextView? = view?.findViewById(R.id.messageSentTime)

    }
    open class MessageReceivedViewHolder(view: View?) : RecyclerView.ViewHolder(view) {
        var messageReceived : TextView? = view?.findViewById(R.id.messageReceivedText)
        var messageReceivedTime : TextView? = view?.findViewById(R.id.messageReceivedTime)

    }
}