package com.example.a_karpenko.smack.ui

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import android.support.design.R.drawable.abc_ic_ab_back_material
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.AppCompatImageButton
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.View
import android.widget.*
import com.afollestad.materialdialogs.MaterialDialog
import com.example.a_karpenko.smack.adapters.MessagesAdapter
import com.example.a_karpenko.smack.R
import com.example.a_karpenko.smack.core.EditTextWatcher
import com.example.a_karpenko.smack.core.queryData.PresenceChecker
import com.example.a_karpenko.smack.models.chat.IncrementValue
import com.example.a_karpenko.smack.models.firestore.ChatModel
import com.example.a_karpenko.smack.models.firestore.InputModel
import com.example.a_karpenko.smack.utils.ConnectionChangeUtil
import com.example.a_karpenko.smack.utils.RealmUtil
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*
import com.vicpin.krealmextensions.queryLast
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
    var toolbar: Toolbar? = null
    //Timestamp
    var currentDate: Date? = null
    //Uid's
    var uidMy: String? = ""
    var uidLF: String? = ""
    //Refs
    var foundUserRef: CollectionReference? = null
    var myRoomRef: CollectionReference? = null
    var messages: ArrayList<ChatModel>? = null
    //Input
    var input: DocumentReference? = null
    var typingTextView: TextView? = null
    //Network
    var cm : ConnectivityManager? = null
    var ni: NetworkInfo? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_messages)

        //Network info
        cm = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        ni = cm?.activeNetworkInfo

        //RecyclerView Array
        messages = ArrayList()
        currentDate = Calendar.getInstance().time

        //Uid's
        uidMy = FirebaseAuth.getInstance().currentUser!!.uid
        uidLF = intent.getStringExtra("foundUser")

        //Found user reference
        foundUserRef = FirebaseFirestore.getInstance()
                .collection("Users").document("$uidLF")
                .collection("rooms").document("room" + RealmUtil().incrementValue())
                .collection("$uidMy")
        //My chat room reference
        myRoomRef = FirebaseFirestore.getInstance()
                .collection("Users").document("$uidMy")
                .collection("rooms").document("room" + RealmUtil().incrementValue())
                .collection("$uidLF")
        //Input ref
        input = FirebaseFirestore.getInstance()
                .collection("Users").document(uidMy!!)

        typingTextView = findViewById(R.id.typingTextView)

        messageSent = findViewById(R.id.messageSentText)
        messageReceived = findViewById(R.id.messageReceivedText)
        messageSentTime = findViewById(R.id.messageSentTime)
        messageReceivedTime = findViewById(R.id.messageReceivedTime)

        messageInputText = findViewById(R.id.messageInputText)
        sendMessageButton = findViewById(R.id.sendMessagebutton)
        recyclerView = findViewById(R.id.messageList)
        toolbar = findViewById(R.id.chatToolbar)

        //Toolbar
        setSupportActionBar(toolbar)
        toolbar?.setNavigationIcon(abc_ic_ab_back_material)
        toolbar?.setNavigationOnClickListener {
            alertDialog()
        }


        //RecyclerView
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
            //Check Network connection
            val isWifi: Boolean? = ni?.type == ConnectivityManager.TYPE_WIFI
            val isMobile: Boolean? = ni?.type == ConnectivityManager.TYPE_MOBILE
            if (ni != null && ni?.isConnectedOrConnecting!! && isWifi!! || isMobile!!) {
                onSendClick()
            } else {
                Toast.makeText(this, "Please, check your Internet connection", Toast.LENGTH_SHORT).show()
            }
        }
    }


    fun alertDialog() = MaterialDialog.Builder(this)
                .title("You are leaving the conversation").content("Are you sure?")
                .positiveText("Yes").negativeText("No")

                .onPositive { dialog, which ->
                    startActivity(Intent(this@ChatActivity, MainActivity::class.java))
                    listener()?.remove()
                    input?.set(InputModel(false, currentDate))
                    PresenceChecker(uidLF, typingTextView, messageInputText).getOut()
                    PresenceChecker(uidLF, typingTextView, messageInputText).checkLfPresence().remove()
                    //Remove typing indicator
                    typingTextView?.visibility = View.GONE
                    //Remove typing listener for user LF
                    EditTextWatcher(messageInputText, uidLF, typingTextView).checkInputLF().remove()
                    messageInputText?.isEnabled = true
                    messageInputText?.isFocusable = true
                    finish()
        }
                .onNegative { dialog, which -> dialog.dismiss() }.show()

//    //Adds blank last message so onStart listener will not query it
//    fun lastMessage(last: Boolean?) {
//        myRoomRef?.add(ChatModel("blank", "blank", currentDate))
//        foundUserRef?.add(ChatModel("blank", "blank", currentDate))
//    }


    //Register listener for live messages
    fun listener() = myRoomRef?.addSnapshotListener { snapshot, exception ->
        if (exception != null) {
            Toast.makeText(this.applicationContext, "Please, check your Internet connection", Toast.LENGTH_SHORT).show()
            return@addSnapshotListener
        }

        if (snapshot != null && !snapshot.isEmpty
                && snapshot.documents.last()["from"].toString() == uidLF) {
            val from = snapshot.documents.last()["from"].toString()
            val message = snapshot.documents.last()["message"].toString()
            val receivedQuery = ChatModel(from, message, currentDate)
            messages?.add(receivedQuery)
            adapter?.notifyDataSetChanged()
            recyclerView?.scrollToPosition(messages?.size!! - 1)
//            Log.d("ChatActivity***** ", "From: $from, Message: $message")
        }
    }


    fun onSendClick() {
        if (messageInputText?.length() != 0) {
            //User's uid,name etc
            //Add data to model
            val myMessage = ChatModel(uidMy!!, messageInputText?.text.toString(), currentDate)
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
        //Broadcast network state
        this.applicationContext.registerReceiver(ConnectionChangeUtil(), IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"))
        //Start listening for messages
        listener()
        //Check if I'm typing
        EditTextWatcher(messageInputText, uidLF, typingTextView).checkInputMy()
        //Check if User's typing
        EditTextWatcher(messageInputText, uidLF, typingTextView).checkInputLF()
        //Presence == true
        PresenceChecker(uidLF, typingTextView, messageInputText).getIn()
        //Check if user LF is still in chat
        PresenceChecker(uidLF, typingTextView, messageInputText).checkLfPresence()
    }

    override fun onBackPressed() {
        alertDialog()
    }

    override fun onDestroy() {
        super.onDestroy()
        listener()?.remove()
        input?.set(InputModel(false, currentDate))
        EditTextWatcher(messageInputText, uidLF, typingTextView).checkInputLF().remove()
        PresenceChecker(uidLF, typingTextView, messageInputText).getOut()
        PresenceChecker(uidLF, typingTextView, messageInputText).checkLfPresence().remove()
        typingTextView?.visibility = View.GONE
        messageInputText?.isEnabled = true
        messageInputText?.isFocusable = true
    }
}





