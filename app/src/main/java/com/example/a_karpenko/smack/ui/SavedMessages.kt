package com.example.a_karpenko.smack.ui

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import com.example.a_karpenko.smack.R
import com.example.a_karpenko.smack.adapters.SavedMessagesAdapter
import com.example.a_karpenko.smack.models.firestore.ChatModel
import io.realm.OrderedRealmCollection
import io.realm.Realm
import io.realm.Sort

open class SavedMessages : AppCompatActivity() {

    var toolbar: Toolbar? = null
    var recycler: RecyclerView? = null
    var messages: MutableList<ChatModel>? = null
    var realm: Realm? = null
    var getIntent: Int? = null
    var list: MutableList<ChatModel>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_saved_messages)
        window?.setBackgroundDrawableResource(R.drawable.img_chat_background)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window?.statusBarColor = R.color.chatStatusBar
        }


        realm = Realm.getDefaultInstance()
        toolbar = findViewById(R.id.savedMessagesToolbar)
        setSupportActionBar(toolbar)
        toolbar?.setNavigationIcon(R.drawable.abc_ic_ab_back_material)
        toolbar?.setNavigationOnClickListener {
            startActivity(Intent(this@SavedMessages, SavedChats::class.java))
            overridePendingTransition(R.anim.slide_left_to_right, R.anim.slide_right_to_left)
            finish()
        }

        list = ArrayList()
        recycler = findViewById(R.id.savedMessagesList)
        recycler?.setHasFixedSize(true)

        //Retrieve saved messages from Realm
        getIntent = intent.getIntExtra("id", 0)

//        messages = RealmUtil().retrieveMessages()?.subList(startList(getIntent)!!, RealmUtil().getEndMessagesSize()!![getIntent!!]?.endMessagesSize!!)
        realm?.beginTransaction()
        val orc: OrderedRealmCollection<ChatModel>? = realm?.where(ChatModel::class.java)?.sort("id", Sort.ASCENDING)?.findAll()
        realm?.commitTransaction()

        val adapter = SavedMessagesAdapter(orc!!, getIntent!!)
        val manager = LinearLayoutManager(this)
        manager.orientation = LinearLayoutManager.VERTICAL
        manager.stackFromEnd = true
        recycler?.layoutManager = manager
        recycler?.adapter = adapter
    }

    // Get first index based on clicked recyclerview item (intent)
//    open fun startList(intent: Int?): Int? {
//        return try {
//            if (intent != null && intent == 0) {
//                0
//            } else if (intent != null
//                    && RealmUtil().getStartMessagesSize()?.isNotEmpty()!!
//                    && RealmUtil().getStartMessagesSize()?.last()?.startMessagesSize == RealmUtil().getStartMessagesSize()!![intent]?.startMessagesSize!!
//                    && intent != 0) {
//                RealmUtil().getStartMessagesSize()?.last()?.startMessagesSize
//            } else if (intent != null
//                    && RealmUtil().getStartMessagesSize()?.isNotEmpty()!!
//                    && RealmUtil().getStartMessagesSize()?.last()?.startMessagesSize != RealmUtil().getStartMessagesSize()!![intent]?.startMessagesSize!!
//                    && intent != 0) {
//                RealmUtil().getStartMessagesSize()!![intent]?.startMessagesSize
//            } else {
//                RealmUtil().getStartMessagesSize()!![intent!!]?.startMessagesSize
//            }
//        } catch (e: java.lang.IndexOutOfBoundsException){
//            RealmUtil().getStartMessagesSize()!![intent!!]?.startMessagesSize
//        }
//    }

    override fun onBackPressed() {
        startActivity(Intent(this@SavedMessages, SavedChats::class.java))
        overridePendingTransition(R.anim.slide_left_to_right, R.anim.slide_right_to_left)
        finish()
    }
}
