package com.project.coolz.smack.ui

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.*
import android.view.View
import android.widget.TextView
import com.project.coolz.smack.R
import com.project.coolz.smack.adapters.SavedChatsAdapter
import com.project.coolz.smack.utils.RealmUtil
import io.realm.Realm
import kotlinx.android.synthetic.main.activity_saved_chats.*

open class SavedChats : AppCompatActivity() {

    var toolbar: Toolbar? = null
    var recycler: RecyclerView? = null
    var realm: Realm? = null
    var adapter: SavedChatsAdapter? = null
    var emptyTextView: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_saved_chats)

        emptyTextView = findViewById(R.id.emptyChatsText)


//        window?.setBackgroundDrawableResource(R.drawable.savedchat_blue)
        styleChangerAndChecker()

        realm = Realm.getDefaultInstance()
        toolbar = findViewById(R.id.savedChatToolbar)
        toolbar?.setNavigationIcon(R.drawable.abc_ic_ab_back_material)
        setSupportActionBar(toolbar)
        toolbar?.setNavigationOnClickListener {
            startActivity(Intent(this@SavedChats, MainActivity::class.java))
            overridePendingTransition(R.anim.slide_left_to_right, R.anim.slide_right_to_left)
            finish()
        }

//        val fake: Button? = find(R.id.fake)

        //                                   FAKE BUTTON!!!
//        fake?.onClick {
//            Log.d("SavedChats******** ", "start size: ${StartMessagesSize().queryAll().size}    " + "end size: ${EndMessagesSize().queryAll().size}")
//            Log.d("SavedChats******** ", "start 0: ${realm?.where(StartMessagesSize::class.java)
//                    ?.sort("id", Sort.ASCENDING)?.findAll()!![0]?.startMessagesSize}     " +
//                    "end 0: ${realm?.where(EndMessagesSize::class.java)?.sort("id", Sort.ASCENDING)?.findAll()!![0]?.endMessagesSize}")
//            try {
//                Log.d("SavedChats******** ", "start 1: ${realm?.where(StartMessagesSize::class.java)
//                        ?.sort("id", Sort.ASCENDING)?.findAll()!![1]?.startMessagesSize}     "
//                        + "end 1: ${realm?.where(EndMessagesSize::class.java)?.sort("id", Sort.ASCENDING)?.findAll()!![1]?.endMessagesSize}")
//            } catch (e: ArrayIndexOutOfBoundsException){
//                return@onClick
//            }
//            try{
//                Log.d("SavedChats******** ", "start 2: ${realm?.where(StartMessagesSize::class.java)
//                        ?.sort("id", Sort.ASCENDING)?.findAll()!![2]?.startMessagesSize}     "
//                        + "end 2: ${realm?.where(EndMessagesSize::class.java)?.sort("id", Sort.ASCENDING)?.findAll()!![2]?.endMessagesSize}")
//            } catch (e: ArrayIndexOutOfBoundsException){
//                return@onClick
//            }
//            Log.d("SavedChats******** ", "chatModelSize: ${ChatModel().queryAll().count()}")
//        }

        recycler = findViewById(R.id.savedChatList)
        val decor: DividerItemDecoration? = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        decor?.setDrawable(ContextCompat.getDrawable(this, R.drawable.saved_chat_divider)!!)
        val manager = object : LinearLayoutManager(this) {}
        manager.orientation = LinearLayoutManager.VERTICAL
        adapter = SavedChatsAdapter(recycler!!, this, this, RealmUtil().getSavedChatTime()!!, true)

        recycler?.setHasFixedSize(true)
        recycler?.itemAnimator = DefaultItemAnimator()
        recycler?.addItemDecoration(decor)
        recycler?.adapter = adapter
        recycler?.layoutManager = manager

        if (RealmUtil().retrieveMessages()?.size!! >= 1){
            emptyTextView?.visibility = View.GONE
        } else {
            emptyTextView?.visibility = View.VISIBLE
        }

    }


    fun styleChangerAndChecker() {
        when (RealmUtil().getStyle()) {
            1 -> {
                savedChatList?.setBackgroundResource(R.drawable.saved_chat_purple)
                if (Build.VERSION.SDK_INT > 21) {
                    window.statusBarColor = ContextCompat.getColor(this@SavedChats, R.color.purpleStatusBar)
                }
                savedChatToolbar?.setBackgroundColor(ContextCompat.getColor(this@SavedChats, R.color.purpleToolBar))
            }
            2 -> {
                savedChatList?.setBackgroundResource(R.drawable.savedchat_blue)
                savedChatToolbar?.setBackgroundColor(ContextCompat.getColor(this@SavedChats, R.color.lightBlueToolbar))
                if (Build.VERSION.SDK_INT > 21) {
                    window.statusBarColor = ContextCompat.getColor(this@SavedChats, R.color.blueStatusBar)
                }
            }
            else -> {
                savedChatList?.setBackgroundResource(R.drawable.savedchat_green)
                savedChatToolbar?.setBackgroundColor(ContextCompat.getColor(this@SavedChats, R.color.greenToolBar))
                if (Build.VERSION.SDK_INT > 21) {
                    window.statusBarColor = ContextCompat.getColor(this@SavedChats, R.color.greenStatusBar)
                }
            }
        }
    }




    override fun onBackPressed() {
        startActivity(Intent(this@SavedChats, MainActivity::class.java))
        overridePendingTransition(R.anim.slide_left_to_right, R.anim.slide_right_to_left)
        finish()
    }
}
