package com.project.coolz.smack.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import com.project.coolz.smack.R
import com.project.coolz.smack.adapters.SavedMessagesAdapter
import com.project.coolz.smack.models.firestore.ChatModel
import com.project.coolz.smack.utils.RealmUtil
import io.realm.OrderedRealmCollection
import io.realm.Realm
import io.realm.Sort
import kotlinx.android.synthetic.main.activity_saved_chats.*

open class SavedMessages : AppCompatActivity() {

    var toolbar: Toolbar? = null
    var recycler: RecyclerView? = null
    var messages: MutableList<ChatModel>? = null
    var realm: Realm? = null
    var getIntent: Int? = null
    var list: MutableList<ChatModel>? = null

    @SuppressLint("ResourceAsColor")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_saved_messages)
//        window?.setBackgroundDrawableResource(R.drawable.chat_blue)

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            window?.statusBarColor = R.color.chatStatusBar
//        }


        realm = Realm.getDefaultInstance()
        toolbar = findViewById(R.id.savedMessagesToolbar)
        setSupportActionBar(toolbar)
        toolbar?.setNavigationIcon(R.drawable.abc_ic_ab_back_material)
        toolbar?.setNavigationOnClickListener {
            startActivity(Intent(this@SavedMessages, SavedChats::class.java))
            overridePendingTransition(R.anim.slide_left_to_right, R.anim.slide_right_to_left)
            finish()
        }

        styleChangerAndChecker()

        list = ArrayList()
        recycler = findViewById(R.id.savedMessagesList)
        recycler?.setHasFixedSize(true)

        //Retrieve saved messages from Realm
        getIntent = intent.getIntExtra("id", 0)

//        messages = RealmUtil().retrieveMessages()?.subList(startList(getIntent)!!, RealmUtil().getEndMessagesSize()!![getIntent!!]?.endMessagesSize!!)
        realm?.beginTransaction()
//        val orc: OrderedRealmCollection<ChatModel>? = realm?.where(ChatModel::class.java)?.sort("id", Sort.ASCENDING)?.findAll()
        val orc: OrderedRealmCollection<ChatModel>? = realm?.where(ChatModel::class.java)?.sort("id", Sort.ASCENDING)?.findAll()
        realm?.commitTransaction()

        val adapter = SavedMessagesAdapter(orc!!, getIntent!!)
        val manager = LinearLayoutManager(this)
        manager.orientation = LinearLayoutManager.VERTICAL
        manager.stackFromEnd = true
        recycler?.layoutManager = manager
        recycler?.adapter = adapter
    }

    fun styleChangerAndChecker() {
        when (RealmUtil().getStyle()) {
            1 -> {
                window?.setBackgroundDrawableResource(R.drawable.chat_purple)
                if (Build.VERSION.SDK_INT > 21) {
                    window.statusBarColor = ContextCompat.getColor(this@SavedMessages, R.color.purpleStatusBar)
                }
                toolbar?.setBackgroundColor(ContextCompat.getColor(this@SavedMessages, R.color.purpleToolBar))
            }
            2 -> {
                window?.setBackgroundDrawableResource(R.drawable.chat_blue)
                if (Build.VERSION.SDK_INT > 21) {
                    window.statusBarColor = ContextCompat.getColor(this@SavedMessages, R.color.blueStatusBar)
                }
                toolbar?.setBackgroundColor(ContextCompat.getColor(this@SavedMessages, R.color.blueToolbar))
            }
            else -> {
                window?.setBackgroundDrawableResource(R.drawable.chat_green)
                if (Build.VERSION.SDK_INT > 21) {
                    window.statusBarColor = ContextCompat.getColor(this@SavedMessages, R.color.greenStatusBar)
                }
                toolbar?.setBackgroundColor(ContextCompat.getColor(this@SavedMessages, R.color.greenToolBar))
            }
        }
    }

    override fun onBackPressed() {
        startActivity(Intent(this@SavedMessages, SavedChats::class.java))
        overridePendingTransition(R.anim.slide_left_to_right, R.anim.slide_right_to_left)
        finish()
    }
}
