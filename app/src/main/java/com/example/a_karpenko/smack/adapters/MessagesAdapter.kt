package com.example.a_karpenko.smack.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.a_karpenko.smack.R
import com.example.a_karpenko.smack.models.Chat
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import java.util.*

var collectionRef : CollectionReference? = FirebaseFirestore.getInstance().collection("chats")

var query: Query? = collectionRef?.
        orderBy("timeStamp")

val options: FirestoreRecyclerOptions<Chat>? = FirestoreRecyclerOptions.Builder<Chat>()
        .setQuery(query, Chat::class.java)
        .build()


open class MessagesAdapter : FirestoreRecyclerAdapter<Chat, MessageSentViewHolder>(options) {

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): MessageSentViewHolder {
        val v = LayoutInflater.from(parent?.context).inflate(R.layout.message_sent, parent, false)
        return object : MessageSentViewHolder(v) {}
    }

    override fun onBindViewHolder(holder: MessageSentViewHolder?, position: Int, model: Chat?) {

        //Time
        val c = Calendar.getInstance()
        val minutes = c.get(Calendar.MINUTE)
        val hour = c.get(Calendar.HOUR).toString()
        val time = "$hour:$minutes"

        holder?.messageSent?.text = model?.message
        holder?.messageSentTime?.text = time

    }

}


open class MessageReceivedViewHolder(view: View?) : RecyclerView.ViewHolder(view) {
    var messageReceived : TextView? = view?.findViewById(R.id.messageReceivedText)
    var messageReceivedTime : TextView? = view?.findViewById(R.id.messageReceivedTime)
}


open class MessageSentViewHolder(view: View?) : RecyclerView.ViewHolder(view) {
    var messageSent : TextView? = view?.findViewById(R.id.messageSentText)
    var messageSentTime : TextView? = view?.findViewById(R.id.messageSentTime)
}