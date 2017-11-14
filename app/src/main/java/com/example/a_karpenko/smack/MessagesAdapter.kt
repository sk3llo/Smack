package com.example.a_karpenko.smack

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import butterknife.BindView
import com.example.a_karpenko.smack.models.Chat
import com.firebase.ui.firestore.FirestoreArray
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.common.collect.Ordering
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import java.util.*
import java.util.zip.Inflater

var collectionRef : CollectionReference? = FirebaseFirestore.getInstance().collection("chats")

var query: Query? = collectionRef
        ?.orderBy("timeStamp")


val options: FirestoreRecyclerOptions<Chat>? = FirestoreRecyclerOptions.Builder<Chat>()
        .setQuery(query, Chat::class.java)
        .build()


open class MessagesAdapter : FirestoreRecyclerAdapter<Chat, viewHolder>(options) {

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): viewHolder {
        val v = LayoutInflater.from(parent?.context).inflate(R.layout.message_sent, parent, false)
        return object : viewHolder(v) {}
    }

    override fun onBindViewHolder(holder: viewHolder?, position: Int, model: Chat?) {

        //Time
        val c = Calendar.getInstance()
        val minutes = c.get(Calendar.MINUTE)
        val hour = c.get(Calendar.HOUR).toString()
        val time = "$hour:$minutes"

        holder?.messageSent?.text = model?.message
        holder?.messageSentTime?.text = time

    }

}

open class viewHolder(view: View?) : RecyclerView.ViewHolder(view) {

    var messageSent : TextView? = view?.findViewById(R.id.messageSentText)
    var messageReceived : TextView? = view?.findViewById(R.id.messageReceivedText)
    var messageSentTime : TextView? = view?.findViewById(R.id.messageSentTime)
    var messageReceivedTime : TextView? = view?.findViewById(R.id.messageReceivedTime)
    var messageList : RecyclerView? = view?.findViewById(R.id.messageList)


}