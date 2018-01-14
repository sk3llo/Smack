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
import com.example.a_karpenko.smack.utils.RealmUtil
import com.vicpin.krealmextensions.queryAll
import io.realm.Realm
import org.jetbrains.anko.sdk25.coroutines.onClick

open class SavedChats : AppCompatActivity() {

    var toolbar: Toolbar? = null
    var recycler: RecyclerView? = null
//    var list: ArrayList<SavedChatsTime>? = null
    var realm: Realm? = null
    var adapter: SavedChatsAdapter? = null


    var fakeButton: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_saved_chats)


        fakeButton = findViewById(R.id.huinya_button)

        realm = Realm.getDefaultInstance()
        toolbar = findViewById(R.id.savedChatToolbar)
        toolbar?.setNavigationIcon(R.drawable.abc_ic_ab_back_material)
        setSupportActionBar(toolbar)
        toolbar?.setNavigationOnClickListener {
            startActivity(Intent(this@SavedChats, MainActivity::class.java))
            overridePendingTransition(R.anim.slide_left_to_right, R.anim.slide_right_to_left)
            finish()
        }

        recycler = findViewById(R.id.savedChatList)
        val manager = object : LinearLayoutManager(this) {}
        manager.orientation = LinearLayoutManager.VERTICAL
        adapter = SavedChatsAdapter(recycler!!, this, this, RealmUtil().getSavedChatTime()!!, true)

        recycler?.adapter = adapter
        recycler?.layoutManager = manager


        fakeButton?.onClick {
            Log.d("MainActivity****** ", "endMessageSize*************: ${EndMessagesSize().queryAll().size}")
            Log.d("MainActivity****** ", "startMessageSize*************: ${StartMessagesSize().queryAll().size}")
            Log.d("MainActivity****** ", "endMessage00000000: ${EndMessagesSize().queryAll()[0].endMessagesSize}")
            Log.d("MainActivity****** ", "startMessage00000000: ${StartMessagesSize().queryAll()[0].startMessagesSize}")

        }


    }




    override fun onBackPressed() {
        startActivity(Intent(this@SavedChats, MainActivity::class.java))
        overridePendingTransition(R.anim.slide_left_to_right, R.anim.slide_right_to_left)
        finish()
    }
}
