package com.example.a_karpenko.smack.ui

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.util.Log
import android.widget.Button
import com.example.a_karpenko.smack.R
import com.example.a_karpenko.smack.adapters.SavedChatsAdapter
import com.example.a_karpenko.smack.models.chat.EndMessagesSize
import com.example.a_karpenko.smack.models.chat.StartMessagesSize
import com.example.a_karpenko.smack.models.firestore.ChatModel
import com.example.a_karpenko.smack.utils.RealmUtil
import com.vicpin.krealmextensions.count
import com.vicpin.krealmextensions.queryAll
import io.realm.Realm
import org.jetbrains.anko.find
import org.jetbrains.anko.sdk25.coroutines.onClick

open class SavedChats : AppCompatActivity() {

    var toolbar: Toolbar? = null
    var recycler: RecyclerView? = null
//    var list: ArrayList<SavedChatsTime>? = null
    var realm: Realm? = null
    var adapter: SavedChatsAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_saved_chats)

        realm = Realm.getDefaultInstance()
        toolbar = findViewById(R.id.savedChatToolbar)
        toolbar?.setNavigationIcon(R.drawable.abc_ic_ab_back_material)
        setSupportActionBar(toolbar)
        toolbar?.setNavigationOnClickListener {
            startActivity(Intent(this@SavedChats, MainActivity::class.java))
            overridePendingTransition(R.anim.slide_left_to_right, R.anim.slide_right_to_left)
            finish()
        }

        var fake: Button? = find(R.id.fake)

        fake?.onClick {
            Log.d("SavedChats******** ", "end size: ${EndMessagesSize().queryAll().size}")
            Log.d("SavedChats******** ", "start size: ${StartMessagesSize().queryAll().size}")
            Log.d("SavedChats******** ", "end 0: ${EndMessagesSize().queryAll()[0].endMessagesSize}")
            Log.d("SavedChats******** ", "end 1: ${EndMessagesSize().queryAll()[1].endMessagesSize}")
            Log.d("SavedChats******** ", "start 0: ${StartMessagesSize().queryAll()[0].startMessagesSize}")
            Log.d("SavedChats******** ", "start 1: ${StartMessagesSize().queryAll()[1].startMessagesSize}")
            Log.d("SavedChats******** ", "chatModelSize: ${ChatModel().queryAll().size}")
        }

        recycler = findViewById(R.id.savedChatList)
        val manager = object : LinearLayoutManager(this) {}
        manager.orientation = LinearLayoutManager.VERTICAL
        adapter = SavedChatsAdapter(recycler!!, this, this, RealmUtil().getSavedChatTime()!!, true)

        recycler?.adapter = adapter
        recycler?.layoutManager = manager

    }




    override fun onBackPressed() {
        startActivity(Intent(this@SavedChats, MainActivity::class.java))
        overridePendingTransition(R.anim.slide_left_to_right, R.anim.slide_right_to_left)
        finish()
    }
}
