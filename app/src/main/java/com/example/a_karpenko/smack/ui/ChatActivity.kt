package com.example.a_karpenko.smack.ui

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.AppCompatImageButton
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.widget.*
import com.example.a_karpenko.smack.adapters.MessagesAdapter
import com.example.a_karpenko.smack.R
import com.example.a_karpenko.smack.models.chat.ChatModel
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*
import java.util.*
import kotlin.collections.ArrayList

class ChatActivity : AppCompatActivity() {

    var messageSent: TextView? = null
    var messageReceived: TextView? = null
    var messageSentTime: TextView? = null
    var messageReceivedTime: TextView? = null
    var messageInputText: EditText? = null
    var sendMessageButton: AppCompatImageButton? = null
    var recyclerView: RecyclerView? = null
    var adapter: MessagesAdapter? = null

    var currentTime: Date? = null

    var uidMy: String? = ""
    var uidLF: String? = ""

    var foundUserRef: CollectionReference? = null
    var myRoomRef: CollectionReference? = null
    var myQuery: Query? = null
    var MYOPTIONS: FirestoreRecyclerOptions<ChatModel>? = null
    var messages: ArrayList<ChatModel>? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_messages)

        //RecyclerView Array
        messages = ArrayList()

        currentTime = Calendar.getInstance().time

        //Uid's
        uidMy = FirebaseAuth.getInstance().currentUser!!.uid
        uidLF = intent.getStringExtra("foundUser")

        //Found user reference
        foundUserRef = FirebaseFirestore.getInstance()
                .collection("Users").document("$uidLF")
                .collection("rooms").document("$uidMy")
                .collection("messages")
        //My chat room reference
        myRoomRef = FirebaseFirestore.getInstance()
                .collection("Users").document("$uidMy")
                .collection("rooms").document("$uidLF")
                .collection("messages")


        messageSent = findViewById(R.id.messageSentText)
        messageReceived = findViewById(R.id.messageReceivedText)
        messageSentTime = findViewById(R.id.messageSentTime)
        messageReceivedTime = findViewById(R.id.messageReceivedTime)

        messageInputText = findViewById(R.id.messageInputText)
        sendMessageButton = findViewById(R.id.sendMessagebutton)
        recyclerView = findViewById(R.id.messageList)


        adapter = MessagesAdapter(messages!!)
        val manager = object : LinearLayoutManager(this) {}
        manager.orientation = LinearLayoutManager.VERTICAL
        manager.stackFromEnd = true

        recyclerView?.setHasFixedSize(true)
        recyclerView?.layoutManager = manager
        recyclerView?.adapter = adapter

        if(messages?.size != 0) {
            recyclerView?.scrollToPosition(messages?.size!! - 1)
        }

        //send message btn clicked
        sendMessageButton?.setOnClickListener {
            onSendClick()
        }
    }

    //Register listener
    fun listener() = myRoomRef?.addSnapshotListener { snapshot, exception ->
        if (exception != null) {
            Toast.makeText(this.applicationContext, "Please, check your Internet connection", Toast.LENGTH_SHORT).show()
            return@addSnapshotListener
        }

        if (snapshot != null && snapshot.documentChanges.last()?.document?.get("from")?.toString() == uidLF) {
            val from = snapshot.documentChanges.last()?.document?.get("from")?.toString()
            val message = snapshot.documentChanges.last()?.document?.get("message")?.toString()
            val receivedQuery = ChatModel(from!!, message!!, currentTime)
            messages?.add(receivedQuery)
            Log.d("ChatActivity***** ", "From: $from, Message: $message")
        }
    }


    fun onSendClick() {
        if (messageInputText?.length() != 0) {
            //User's uid,name etc
            //Add data to model
            val myMessage = ChatModel(uidMy!!, messageInputText?.text.toString(), currentTime)
            messages?.add(myMessage)
            if(messages?.size != 0) {
                recyclerView?.scrollToPosition(messages?.size!! - 1)
            }
            messageSent?.text = messageInputText?.text.toString()

            //Add data to Firestore
            foundUserRef?.add(myMessage)
            myRoomRef?.add(myMessage)
            //Clear input
            messageInputText?.text = null
            Log.d("Chat Activity***** ", "Message sent ")
        }
    }

    override fun onStart() {
        super.onStart()
        listener()
    }

    override fun onStop() {
        super.onStop()
        listener()?.remove()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        listener()?.remove()
        startActivity(Intent(this@ChatActivity, MainActivity::class.java))
        finish()
    }

}





