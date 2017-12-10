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

    var currentTime: Date? = null

    var uidMy: String? = ""
    var uidLF: String? = ""

    var foundUserRef: CollectionReference? = null
    var myRoomRef: CollectionReference? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_messages)

        currentTime = Calendar.getInstance().time

        //Uid's
        uidMy = FirebaseAuth.getInstance().currentUser!!.uid
        uidLF = intent.getStringExtra("foundUser")


        //Found user uid
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
        adapter = MessagesAdapter()


        //init recyclerview adapter
        val manager = LinearLayoutManager(this)
        manager.stackFromEnd = true

        recyclerView?.setHasFixedSize(true)
        recyclerView?.smoothScrollToPosition(adapter?.itemCount!!)
        recyclerView?.layoutManager = manager
        recyclerView?.adapter = adapter


        foundUserRef?.addSnapshotListener { snapshot, exception ->
            //Huinya, nada '== $uidLF'
            if (snapshot?.isEmpty == false && snapshot.documents.last()?.get("from").toString()  == "$uidMy"){
                messageReceived?.text = snapshot.documents.last()?.get("from").toString()
            }
        }

        //send message btn clicked
        sendMessageButton?.setOnClickListener {
            onSendClick()
        }
    }


    fun onSendClick() {
        if (messageInputText?.length() != 0) {
            recyclerView?.smoothScrollToPosition(adapter?.itemCount!!)

            //User's uid,name etc

            val chat = ChatModel(uidMy!!, uidLF!!, messageInputText?.text.toString(), currentTime)
            messageSent?.text = messageInputText?.text.toString()


            foundUserRef?.add(chat)
            myRoomRef?.add(chat)

            messageInputText?.text = null

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
        startActivity(Intent(this@ChatActivity, MainActivity::class.java))
        finish()
    }

    override fun onAuthStateChanged(p0: FirebaseAuth) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}





