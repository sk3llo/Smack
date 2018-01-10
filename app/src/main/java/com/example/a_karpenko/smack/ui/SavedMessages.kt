package com.example.a_karpenko.smack.ui

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import com.example.a_karpenko.smack.R
import com.example.a_karpenko.smack.adapters.SavedMessagesAdapter
import com.example.a_karpenko.smack.models.chat.EndMessagesSize
import com.example.a_karpenko.smack.models.chat.StartMessagesSize
import com.example.a_karpenko.smack.models.firestore.ChatModel
import com.example.a_karpenko.smack.utils.RealmUtil
import com.vicpin.krealmextensions.queryAll
import io.realm.Realm
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.toast

class SavedMessages : AppCompatActivity() {

    var toolbar: Toolbar? = null
    var recycler: RecyclerView? = null
    var messages: MutableList<ChatModel>? = null
    var realm: Realm? = null
    var getIntent: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_saved_messages)
        window?.setBackgroundDrawableResource(R.drawable.img_chat_background)

        realm = Realm.getDefaultInstance()
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
        //Retrieve saved messages from Realm

        getIntent = intent.getIntExtra("id", 0)



        messages = RealmUtil().retrieveMessages()
        val showMessages = messages?.subList(startList()!!, RealmUtil().getEndMessagesSize()?.size!!)?.toMutableList()

        val fakeList: MutableList<ChatModel>? = showMessages


        val adapter = SavedMessagesAdapter(showMessages!!)
        val manager = LinearLayoutManager(this)
        manager.stackFromEnd = true
        manager.orientation = LinearLayoutManager.VERTICAL
        manager.stackFromEnd = true
        recycler?.layoutManager = manager
        recycler?.adapter = adapter


//        toast(getIntent.toString() +
//                " sms: " + "${StartMessagesSize().queryAll().size} " +
//                "ind: ${StartMessagesSize().queryAll()[getIntent!!].startMessagesSize!!} " +
//                "end: ${EndMessagesSize().queryAll().size} ")

    }

    fun startList(): Int? {
        return if (getIntent == 0) {
                return 0
            } else if (StartMessagesSize().queryAll().size == StartMessagesSize().queryAll()[getIntent!!].startMessagesSize!! && getIntent != 0) {
                return StartMessagesSize().queryAll()[getIntent!!].startMessagesSize!!
            } else if (StartMessagesSize().queryAll().size != StartMessagesSize().queryAll()[getIntent!!].startMessagesSize!! && getIntent != 0) {
                return StartMessagesSize().queryAll()[getIntent!!].startMessagesSize!!
            } else {
                0
            }
//        } catch (e: ArrayIndexOutOfBoundsException){
//            return 0
//        }
    }




    override fun onBackPressed() {
        startActivity(Intent(this@SavedMessages, SavedChats::class.java))
        overridePendingTransition(R.anim.slide_left_to_right, R.anim.slide_right_to_left)
        finish()
    }
}
