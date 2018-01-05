package com.example.a_karpenko.smack.ui

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.support.constraint.ConstraintLayout
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.AppCompatImageButton
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.*
import com.afollestad.materialdialogs.*
import com.example.a_karpenko.smack.adapters.MessagesAdapter
import com.example.a_karpenko.smack.R
import com.example.a_karpenko.smack.core.EditTextWatcher
import com.example.a_karpenko.smack.core.queryData.PresenceChecker
import com.example.a_karpenko.smack.models.firestore.ChatModel
import com.example.a_karpenko.smack.models.firestore.InputModel
import com.example.a_karpenko.smack.models.firestore.LoginCheckerModel
import com.example.a_karpenko.smack.utils.ConnectionChangeUtil
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*
import com.vanniktech.emoji.EmojiEditText
import com.vanniktech.emoji.EmojiPopup
import com.vanniktech.emoji.EmojiTextView
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.find
import org.jetbrains.anko.sdk25.coroutines.onClick
import org.jetbrains.anko.toast
import java.util.*
import kotlin.collections.ArrayList

class ChatActivity : AppCompatActivity() {

    lateinit var emojiPopup: EmojiPopup
    var emojiButton: Button? = null
    var messageSent: EmojiTextView? = null
    var messageReceived: EmojiTextView? = null
    var messageSentTime: TextView? = null
    var messageReceivedTime: TextView? = null
    var spinner: Button? = null
    var startOver: Button? = null
    var goMain: Button? = null
    var hideLayout: ConstraintLayout? = null
    var textLayout: LinearLayout? = null
    var leftChatLayout: ConstraintLayout? = null
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.chat_activity)

        //Set image for chat background
        //And it has constant size even when keybord is opened
        window?.setBackgroundDrawableResource(R.drawable.img_chat_background)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window?.statusBarColor = R.color.chatStatusBar
        }

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
        textLayout = find(R.id.editTextLayout)
        messageSent = findViewById(R.id.messageSentText)
        messageReceived = findViewById(R.id.messageReceivedText)
        messageSentTime = findViewById(R.id.messageSentTime)
        messageReceivedTime = findViewById(R.id.messageReceivedTime)

        leftChatLayout = findViewById(R.id.leftChatLayout)
        spinner = findViewById(R.id.spinner)
        hideLayout = findViewById(R.id.hideLayout)
        startOver = findViewById(R.id.startOver)
        goMain = findViewById(R.id.goMain)

        messageInputText = findViewById(R.id.messageInputText)
        sendMessageButton = findViewById(R.id.sendMessagebutton)
        recyclerView = findViewById(R.id.messageList)
        toolbar = findViewById(R.id.chatToolbar)

        //Toolbar
        toolbar?.setNavigationIcon(R.drawable.abc_ic_ab_back_material)
        setSupportActionBar(toolbar)
        toolbar?.setNavigationOnClickListener {
            when (leftChatLayout?.visibility){
                View.VISIBLE -> closeActivity()
                View.GONE -> firstDialog()
            }
        }

        //Hide and open widget when spinner clicker
        spinner?.onClick {
            when (hideLayout?.visibility){
                View.VISIBLE -> {
                    hideLayout?.visibility = View.GONE
                    spinner?.setBackgroundResource(R.drawable.up_arrow)
                }
                View.GONE -> {
                    hideLayout?.visibility = View.VISIBLE
                    spinner?.setBackgroundResource(R.drawable.down_arrow)
                }
            }
        }

        startOver?.onClick{
            if (ni != null && ni?.isConnectedOrConnecting!!) {
                //Add doc to Firestore with "Online" Action
                val db: FirebaseFirestore? = FirebaseFirestore.getInstance()
                val user = FirebaseAuth.getInstance().currentUser
                db?.collection("WL")?.document("${user?.uid}")?.set(LoginCheckerModel(true, currentDate))?.addOnCompleteListener {
                    Log.d("Main Activity***** ", "ADDED TRUE TO WL, USER: ${user?.uid}")
                }
                //Start Waiting Activity
                startActivity(Intent(this@ChatActivity, WaitingActivity::class.java))
                finish()
            } else {
                toast("Please, check your Internet connection")
            }
        }

        //Go Main button clicked
        goMain?.onClick {
            closeActivity()
        }

        //Emojis
        emojiButton = findViewById(R.id.emojiButton)
        var rootView: View? = findViewById(R.id.chatRootView)
        emojiPopup = EmojiPopup.Builder.fromRootView(rootView).build(messageInputText)
        emojiButton?.setOnClickListener {
            displayEmojis()
        }

        //Open keyboard on message input click and change icon to emoji icon
        val imm: InputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        messageInputText.onClick {
            it?.requestFocus()
            if (emojiPopup.isShowing) {
                emojiPopup.dismiss()
                imm.showSoftInput(messageInputText, InputMethodManager.SHOW_FORCED)
                emojiButton?.setBackgroundResource(R.drawable.emoji_ic_smile)
            }
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

    private fun closeActivity() {
        doAsync {
            startActivity(Intent(this@ChatActivity, MainActivity::class.java))
            runOnUiThread {
                //Remove typing indicator
                typingTextView?.visibility = View.GONE
                messageInputText.isEnabled = true
                messageInputText.isFocusable = true
            }
            finish()
        }
    }

    private fun firstDialog() = MaterialDialog.Builder(this)
            .title("You are leaving the conversation").content("Are you sure?")
            .positiveText("Yes").negativeText("No")
            .theme(Theme.LIGHT)
            .btnSelector(R.drawable.alert_positive_btn_selector, DialogAction.POSITIVE)
            .btnSelector(R.drawable.alert_negative_btn_selector, DialogAction.NEGATIVE)
            .positiveColorRes(R.color.white)
            .negativeColorRes(R.color.white)
            .backgroundColorRes(R.color.material_grey_300)
            .contentGravity(GravityEnum.CENTER)
            .titleGravity(GravityEnum.CENTER)
            .buttonsGravity(GravityEnum.CENTER)

            .onPositive { dialog, which ->
                emojiButton?.visibility = View.GONE
                textLayout?.visibility = View.GONE
                sendMessageButton?.visibility = View.GONE
                leftChatLayout?.visibility = View.VISIBLE
                listener()?.remove()
                input?.set(InputModel(false, currentDate))
                PresenceChecker(uidLF, typingTextView, messageInputText, emojiButton).getOut()
                PresenceChecker(uidLF, typingTextView, messageInputText, emojiButton).checkLfPresence().remove()
                //Remove typing listener for user LF
                EditTextWatcher(messageInputText, uidLF, typingTextView).checkInputLF().remove()
            }
            .onNegative { dialog, which -> dialog.dismiss() }.show()!!



    //Register listener for live messages
    fun listener() = foundUserRef?.addSnapshotListener { snapshot, exception ->

        if (exception != null) {
            Toast.makeText(this.applicationContext, "Please, check your Internet connection", Toast.LENGTH_SHORT).show()
            return@addSnapshotListener
        }
        //Add empty message if the snapshot is empty (to show first message)
        if (snapshot == null || snapshot.isEmpty){
            foundUserRef?.add(ChatModel(uidMy.toString(), "empyMessage", currentDate))
        }

        if (snapshot != null
                && !snapshot.isEmpty && snapshot.documentChanges.last().document.exists()
                //Do not show preveious messages
                && snapshot.size() != snapshot.documentChanges.size
                && snapshot.documentChanges.last().document["from"].toString() == uidLF) {
//            snapshot.documents.dropLastWhile { snapshot.size() == snapshot.documentChanges.size }
            val from = snapshot.documentChanges.last().document["from"].toString()
            val message = snapshot.documentChanges.last().document["message"].toString()
            val receivedQuery = ChatModel(from, message, currentDate)
            messages?.add(receivedQuery)
            adapter?.notifyDataSetChanged()
            recyclerView?.scrollToPosition(messages?.size!! - 1)
        }
    }

    private fun displayEmojis() {

        if (emojiPopup.isShowing) {
            emojiPopup.dismiss()
            emojiButton?.setBackgroundResource(R.drawable.emoji_ic_smile)
        } else {
            emojiPopup.toggle()
            emojiButton?.setBackgroundResource(R.drawable.emoji_ic_keyboard)
        }
    }

    private fun onSendClick() {
        val text = messageInputText.text?.toString()?.trim()

        if (text?.length != 0) {
            //User's uid,name etc
            //Add data to model
            val myMessage = ChatModel(uidMy!!, text!!, currentDate)
            messages?.add(myMessage)
            if (messages?.size != 0) {
                recyclerView?.scrollToPosition(messages?.size!! - 1)
            }
            messageSent?.text = text
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
            //Get in (presence == true)
            PresenceChecker(uidLF, typingTextView, messageInputText, emojiButton).getIn()
        }
        //Check LF presence after 1.5 sec
        Handler().postDelayed({
            PresenceChecker(uidLF, typingTextView, messageInputText, emojiButton).checkLfPresence()
        }, 1500)
    }

    override fun onBackPressed() {
        when (leftChatLayout?.visibility){
            View.VISIBLE -> closeActivity()
            View.GONE -> firstDialog()
        }
    }

    //TODO: check if onPause calls before onDestroy method
    override fun onDestroy() {
        super.onDestroy()
        listener()?.remove()
        input?.set(InputModel(false, currentDate))
        EditTextWatcher(messageInputText, uidLF, typingTextView).checkInputLF().remove()
        PresenceChecker(uidLF, typingTextView, messageInputText, emojiButton).getOut()
        PresenceChecker(uidLF, typingTextView, messageInputText, emojiButton).checkLfPresence().remove()
    }
}





