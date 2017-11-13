package com.example.a_karpenko.smack

import android.app.Activity
import android.app.Application
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.CoordinatorLayout
import android.support.design.widget.Snackbar
import android.support.v4.app.ActivityCompat.startActivityForResult
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.AppCompatImageButton
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.example.a_karpenko.smack.models.Chat
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.ErrorCodes
import com.firebase.ui.auth.IdpResponse
import com.firebase.ui.firestore.FirestoreArray
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import java.text.DateFormat
import java.util.*

class ChatActivity : AppCompatActivity(), FirebaseAuth.AuthStateListener {

    var messageSent : TextView? = null
    var messageReceived : TextView? = null
    var messageSentTime : TextView? = null
    var messageReceivedTime : TextView? = null
    var messageInputText : EditText? = null
    var sendMessageButton : AppCompatImageButton? = null
    var recyclerView : RecyclerView? = null


    var collectionRef : CollectionReference? = FirebaseFirestore.getInstance().collection("chats")

    var query: Query? = collectionRef
            ?.orderBy("timeStamp")
            ?.limit(50)


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


        //init recyclerview adapter
        var manager = LinearLayoutManager(this)
        manager.orientation = LinearLayout.VERTICAL
        manager.stackFromEnd = true
        manager.reverseLayout = true

        recyclerView?.hasFixedSize()
        recyclerView?.layoutManager = manager
        recyclerView?.adapter = MessagesAdapter()


        //send message btn clicked
        sendMessageButton?.setOnClickListener {


            val uid = FirebaseAuth.getInstance().currentUser!!.uid
            val user = FirebaseAuth.getInstance().currentUser?.displayName
            val name = "$user " + uid.substring(0, 6)

            val chat = Chat(name, messageInputText?.text.toString(), uid, Calendar.getInstance().time)

            messageSent?.text = messageInputText?.text.toString()
            messageSentTime?.text = DateFormat.getTimeInstance().format(Date()).toString()

            onSendClick(chat)

            messageInputText?.text = null

        }


    }

    override fun onStart() {
        super.onStart()
//        if (isSignedIn()) { attachRecyclerViewAdapter(); }
        MessagesAdapter().startListening()
    }

    override fun onStop() {
        super.onStop()
        MessagesAdapter().stopListening()
    }

    override fun onAuthStateChanged(auth: FirebaseAuth) {

    }


    fun onSendClick(chat: Chat) {
        collectionRef?.add(chat)
        }


    }





