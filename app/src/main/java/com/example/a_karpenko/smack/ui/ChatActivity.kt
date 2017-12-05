package com.example.a_karpenko.smack.ui

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.AppCompatImageButton
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.*
import com.example.a_karpenko.smack.adapters.MessagesAdapter
import com.example.a_karpenko.smack.R
import com.example.a_karpenko.smack.models.chat.ChatModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*
import java.util.*

class ChatActivity : AppCompatActivity(), FirebaseAuth.AuthStateListener {

    var messageSent : TextView? = null
    var messageReceived : TextView? = null
    var messageSentTime : TextView? = null
    var messageReceivedTime : TextView? = null
    var messageInputText : EditText? = null
    var sendMessageButton : AppCompatImageButton? = null
    var recyclerView : RecyclerView? = null
    var adapter : MessagesAdapter? = null


    var collectionRef : CollectionReference? = FirebaseFirestore.getInstance().collection("chats")

    var query: Query? = collectionRef
            ?.orderBy("timeStamp")


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_messages)

        messageSent = findViewById(R.id.messageSentText)
        messageReceived = findViewById(R.id.messageReceivedText)
        messageSentTime = findViewById(R.id.messageSentTime)
        messageReceivedTime = findViewById(R.id.messageReceivedTime)

        messageInputText = findViewById(R.id.messageInputText)
        sendMessageButton = findViewById(R.id.sendMessagebutton)
        recyclerView = findViewById(R.id.messageList)
        adapter = MessagesAdapter()


        //init recyclerview adapter
        var manager = LinearLayoutManager(this)
        manager.stackFromEnd = true

        recyclerView?.setHasFixedSize(true)
        recyclerView?.smoothScrollToPosition(adapter?.itemCount!!)
        recyclerView?.layoutManager = manager
        recyclerView?.adapter = adapter


        //send message btn clicked
        sendMessageButton?.setOnClickListener {


            if (messageInputText?.length() != 0) {
                recyclerView?.smoothScrollToPosition(adapter?.itemCount!!)

                //User's uid,name etc
                val uid = FirebaseAuth.getInstance().currentUser!!.uid
                val user = FirebaseAuth.getInstance().currentUser?.displayName
                val name = "$user"
                val chat = ChatModel(name, messageInputText?.text.toString(), uid, Calendar.getInstance().time)

                messageSent?.text = messageInputText?.text.toString()

                onSendClick(chat)

                messageInputText?.text = null
            }

        }


    }


    override fun onStart() {
        super.onStart()
//        if (isSignedIn()) { attachRecyclerViewAdapter(); }
        adapter?.startListening()
    }

    override fun onStop() {
        super.onStop()
        adapter?.stopListening()
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }

    override fun onAuthStateChanged(auth: FirebaseAuth) {
    }

    fun onSendClick(chatModel: ChatModel) {
        collectionRef?.add(chatModel)
        }


    }





