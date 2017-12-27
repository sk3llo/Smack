package com.example.a_karpenko.smack.ui

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.inputmethodservice.Keyboard
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import android.support.design.R.drawable.abc_ic_ab_back_material
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.AppCompatImageButton
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.*
import com.afollestad.materialdialogs.MaterialDialog
import com.example.a_karpenko.smack.adapters.MessagesAdapter
import com.example.a_karpenko.smack.R
import com.example.a_karpenko.smack.core.EditTextWatcher
import com.example.a_karpenko.smack.core.queryData.PresenceChecker
import com.example.a_karpenko.smack.models.firestore.ChatModel
import com.example.a_karpenko.smack.models.firestore.InputModel
import com.example.a_karpenko.smack.utils.ConnectionChangeUtil
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*
import com.vanniktech.emoji.EmojiEditText
import com.vanniktech.emoji.EmojiPopup
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.sdk25.coroutines.onClick
import org.jetbrains.anko.sdk25.coroutines.onFocusChange
import java.util.*
import kotlin.collections.ArrayList

class ChatActivity : AppCompatActivity() {

    //Emojis
    lateinit var emojiPopup: EmojiPopup
    var emojiButton: Button? = null

    var messageSent: TextView? = null
    var messageReceived: TextView? = null
    var messageSentTime: TextView? = null
    var messageReceivedTime: TextView? = null
    lateinit var messageInputText: EmojiEditText
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

    var rooms: CollectionReference? = null
    var editTextLayout: LinearLayout? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.chat_activity)

        //Set image for chat background
        //And it has constant size even when keybord is opened
        window?.setBackgroundDrawableResource(R.drawable.img_chat_background)

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
                .collection("rooms").document("$uidMy")
                .collection("messages")
        //My chat room reference
        myRoomRef = FirebaseFirestore.getInstance()
                .collection("Users").document("$uidMy")
                .collection("rooms").document("$uidLF")
                .collection("messages")

        rooms = FirebaseFirestore.getInstance()
                .collection("Rooms")

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
        editTextLayout = findViewById(R.id.editTextLayout)

        //Toolbar
        setSupportActionBar(toolbar)
        toolbar?.setNavigationIcon(R.drawable.chat_back_arrow)
        toolbar?.setNavigationOnClickListener {
            alertDialog()
        }

        //Emojis
        emojiButton = findViewById(R.id.emojiButton)
        val rootView: View? = findViewById(R.id.chatRootView)
        emojiPopup = EmojiPopup.Builder.fromRootView(rootView).build(messageInputText)
        emojiButton?.setOnClickListener {
            displayEmojis()
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
            if (ni != null && ni?.isConnectedOrConnecting!! || isWifi!! || isMobile!!) {
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
                    doAsync {
                        startActivity(Intent(this@ChatActivity, MainActivity::class.java))
                        listener()?.remove()
                        input?.set(InputModel(false, currentDate))
                        PresenceChecker(uidLF, typingTextView, messageInputText).getOut()
                        PresenceChecker(uidLF, typingTextView, messageInputText).checkLfPresence().remove()
                        //Remove typing listener for user LF
                        EditTextWatcher(messageInputText, uidLF, typingTextView).checkInputLF().remove()
                        runOnUiThread {
                            //Remove typing indicator
                            typingTextView?.visibility = View.GONE
                            messageInputText.isEnabled = true
                            messageInputText.isFocusable = true
                        }
                        finish()
                    }
        }
                .onNegative { dialog, which -> dialog.dismiss() }.show()


    //Register listener for live messages
    fun listener() = foundUserRef?.addSnapshotListener { snapshot, exception ->
        if (exception != null) {
            Toast.makeText(this.applicationContext, "Please, check your Internet connection", Toast.LENGTH_SHORT).show()
            return@addSnapshotListener
        }

        if (snapshot != null && !snapshot.isEmpty && snapshot.documentChanges.last().document.exists()
                //Do not show preveious messages
                && snapshot.documents.size != snapshot.documentChanges.size
                //Do not show messages from me as received
                && snapshot.documentChanges.last().document["from"].toString() == uidLF) {
            val from = snapshot.documentChanges.last().document["from"].toString()
            val message = snapshot.documentChanges.last().document["message"].toString()
            val receivedQuery = ChatModel(from, message, currentDate)
            messages?.add(receivedQuery)
            adapter?.notifyDataSetChanged()
            recyclerView?.scrollToPosition(messages?.size!! - 1)
        }
    }

    fun displayEmojis() {

        if (emojiPopup.isShowing) {
            emojiPopup.dismiss()
        } else {
            emojiPopup.toggle()
        }
    }

    fun onSendClick() {

        if (messageInputText.length() != 0) {
            //User's uid,name etc
            //Add data to model
            val myMessage = ChatModel(uidMy!!, messageInputText.text.toString(), currentDate)
            messages?.add(myMessage)
            if (messages?.size != 0) {
                recyclerView?.scrollToPosition(messages?.size!! - 1)
            }
            messageSent?.text = messageInputText.text.toString()
//
            //Add data to Firestore
            foundUserRef?.get()?.addOnCompleteListener { fu ->
                myRoomRef?.get()?.addOnCompleteListener { me ->
//                    foundUserRef?.document("message" + (fu.result?.size()!! + 1))?.set(myMessage)
                    myRoomRef?.document("message" + (me.result?.size()!! + 1))?.set(myMessage)
                }
            }
            //Clear input
            messageInputText.text = null
    }
}

    override fun onStart() {
        super.onStart()
        //Broadcast network state
        this.applicationContext.registerReceiver(ConnectionChangeUtil(), IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"))
        doAsync {
            runOnUiThread {
                typingTextView?.visibility = View.GONE
                messageInputText.isEnabled = true
                messageInputText.isFocusable = true
                messageInputText.hint = "Message:"
            }
            //Start listening for messages
            listener()
            //Check if I'm typing
            EditTextWatcher(messageInputText, uidLF, typingTextView).checkInputMy()
            //Check if User's typing
            EditTextWatcher(messageInputText, uidLF, typingTextView).checkInputLF()
            //Presence == true
            PresenceChecker(uidLF, typingTextView, messageInputText).getIn().addOnCompleteListener {

                //Check if user LF is still in chat
                PresenceChecker(uidLF, typingTextView, messageInputText).checkLfPresence()
            }
        }
    }



    override fun onBackPressed() {
        alertDialog()
    }

    override fun onStop() {
        super.onStop()
//        listener()?.remove()
//        input?.set(InputModel(false, currentDate))
//        EditTextWatcher(messageInputText, uidLF, typingTextView).checkInputLF().remove()
//        PresenceChecker(uidLF, typingTextView, messageInputText).getOut()
//        PresenceChecker(uidLF, typingTextView, messageInputText).checkLfPresence().remove()
//        typingTextView?.visibility = View.GONE
//        messageInputText.isEnabled = true
//        messageInputText.isFocusable = true
    }

    override fun onDestroy() {
        super.onDestroy()
        listener()?.remove()
        input?.set(InputModel(false, currentDate))
        EditTextWatcher(messageInputText, uidLF, typingTextView).checkInputLF().remove()
        PresenceChecker(uidLF, typingTextView, messageInputText).getOut()
        PresenceChecker(uidLF, typingTextView, messageInputText).checkLfPresence().remove()
    }
}





