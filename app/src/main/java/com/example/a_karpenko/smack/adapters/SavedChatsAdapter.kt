package com.example.a_karpenko.smack.adapters

import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.a_karpenko.smack.R
import com.example.a_karpenko.smack.models.chat.SavedChatsTime
import com.example.a_karpenko.smack.models.firestore.ChatModel
import com.example.a_karpenko.smack.ui.SavedChats
import com.example.a_karpenko.smack.ui.SavedMessages
import com.example.a_karpenko.smack.utils.RealmUtil
import io.realm.*

open class SavedChatsAdapter(var recyclerView: RecyclerView,
                             var activity: SavedChats,
                             var context: Context,
                             data: OrderedRealmCollection<SavedChatsTime>,
                             animResults: Boolean):
        RealmRecyclerViewAdapter<SavedChatsTime, SavedChatsAdapter.ViewHolder>(data, animResults){

    var realm = Realm.getDefaultInstance()



    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        val savedChats: SavedChatsTime? = data!![position]
        holder?.date?.text = savedChats?.time.toString()
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        val v: View = LayoutInflater.from(context).inflate(R.layout.saved_chat_row, parent, false)
        v.setOnClickListener(ClickListener())
        return ViewHolder(v)
    }



    open inner  class ViewHolder(var container: View) : RecyclerView.ViewHolder(container) {
        var date: TextView? = container.findViewById(R.id.savedChatDate)

    }

    open inner class ClickListener: View.OnClickListener{
        override fun onClick(v: View?) {

            var list = RealmUtil().retrieveMessages()
            list?.get(recyclerView.indexOfChild(v))

            val intent: Intent? = Intent(context, SavedMessages::class.java)
            intent?.putExtra("id", recyclerView.indexOfChild(v))
            activity.startActivity(intent)
            activity.finish()

//            Toast.makeText(context, recyclerView.indexOfChild(v).toString(), Toast.LENGTH_SHORT)?.show()
        }
    }


    override fun getItemCount(): Int {
        return data?.size!!
    }



}