package com.example.a_karpenko.smack.ui

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.widget.LinearLayout
import android.widget.Toast
import com.example.a_karpenko.smack.R
import com.example.a_karpenko.smack.adapters.SavedMessagesAdapter
import com.example.a_karpenko.smack.models.firestore.ChatModel
import com.example.a_karpenko.smack.models.saved_chats.SavedMessagesModel
import com.vicpin.krealmextensions.queryAll
import io.realm.Realm
import io.realm.RealmList
import org.jetbrains.anko.toast

class SavedMessages : AppCompatActivity() {

    var toolbar: Toolbar? = null
    var recycler: RecyclerView? = null
    var messages: MutableList<SavedMessagesModel>? = null
    var realm: Realm? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_saved_messages)
        window?.setBackgroundDrawableResource(R.drawable.img_chat_background)

        realm = Realm.getDefaultInstance()
        messages?.add(SavedMessagesModel())

        toolbar = findViewById(R.id.savedMessagesToolbar)
        setSupportActionBar(toolbar)
        toolbar?.setNavigationIcon(R.drawable.abc_ic_ab_back_material)
        toolbar?.setNavigationOnClickListener {
            startActivity(Intent(this@SavedMessages, SavedChats::class.java))
            overridePendingTransition(R.anim.slide_left_to_right, R.anim.slide_right_to_left)
            finish()
        }

        messages = ArrayList()
        recycler = findViewById(R.id.savedMessagesList)
        recycler?.setHasFixedSize(true)

        realm?.beginTransaction()
        var x  = realm?.where(SavedMessagesModel::class.java)?.findAll()
        toast("${x?.last()?.messageMy}")
        realm?.commitTransaction()
        realm?.close()

        val adapter = SavedMessagesAdapter(x!!)
        val manager = LinearLayoutManager(this)
        manager.stackFromEnd = true
        manager.orientation = LinearLayoutManager.VERTICAL
        recycler?.layoutManager = manager
        recycler?.adapter = adapter

        val intent = intent?.getIntExtra("id", -1)

    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this@SavedMessages, SavedChats::class.java))
        overridePendingTransition(R.anim.slide_left_to_right, R.anim.slide_right_to_left)
        finish()
    }
}
