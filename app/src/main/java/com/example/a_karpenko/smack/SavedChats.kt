package com.example.a_karpenko.smack

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import com.example.a_karpenko.smack.adapters.SavedChatsAdapter
import com.example.a_karpenko.smack.models.saved_chats.SavedChatsModel
import com.example.a_karpenko.smack.ui.MainActivity
import com.vicpin.krealmextensions.queryAll
import io.realm.Realm

class SavedChats : AppCompatActivity() {

    var toolbar: Toolbar? = null
    var recycler: RecyclerView? = null
//    var list: ArrayList<SavedChatsModel>? = null
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
            finish()
        }

        val savedChats = realm?.where(SavedChatsModel::class.java)?.findAll()
        recycler = findViewById(R.id.savedChatList)
        recycler?.setHasFixedSize(true)
        val manager = object : LinearLayoutManager(this) {}
        manager.orientation = LinearLayoutManager.VERTICAL
        adapter = SavedChatsAdapter(this, savedChats!!,true, true)

        recycler?.layoutManager = manager


    }
}
