package com.example.a_karpenko.smack.ui

import android.content.Intent
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.*
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.example.a_karpenko.smack.R
import com.example.a_karpenko.smack.adapters.SavedChatsAdapter
import com.example.a_karpenko.smack.models.chat.EndMessagesSize
import com.example.a_karpenko.smack.models.chat.StartMessagesSize
import com.example.a_karpenko.smack.models.firestore.ChatModel
import com.example.a_karpenko.smack.utils.RealmUtil
import com.vicpin.krealmextensions.queryAll
import io.realm.Realm
import io.realm.Sort
import org.jetbrains.anko.find
import org.jetbrains.anko.sdk25.coroutines.onClick
import javax.annotation.Nullable

open class SavedChats : AppCompatActivity() {

    var toolbar: Toolbar? = null
    var recycler: RecyclerView? = null
//    var list: ArrayList<SavedChatsTime>? = null
    var realm: Realm? = null
    var adapter: SavedChatsAdapter? = null
    var emptyTextView: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_saved_chats)

        emptyTextView = findViewById(R.id.emptyChatsText)


        window?.setBackgroundDrawableResource(R.drawable.chat_back)

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
//            Log.d("SavedChats******** ", "chatModelSize: ${ChatModel().queryAll().size}")
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




    override fun onBackPressed() {
        startActivity(Intent(this@SavedChats, MainActivity::class.java))
        overridePendingTransition(R.anim.slide_left_to_right, R.anim.slide_right_to_left)
        finish()
    }
}
