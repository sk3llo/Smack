package com.example.a_karpenko.smack.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import com.example.a_karpenko.smack.R
import com.example.a_karpenko.smack.models.saved_chats.SavedChatsModel
import io.realm.*
import org.jetbrains.anko.find

open class SavedChatsAdapter(var recyclerView: RecyclerView, var context: Context, data: OrderedRealmCollection<SavedChatsModel>, animResults: Boolean):
        RealmRecyclerViewAdapter<SavedChatsModel, SavedChatsAdapter.ViewHolder>(data, animResults){

    override fun getItemId(position: Int): Long {
        return super.getItemId(position)
    }

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        val savedChats: SavedChatsModel? = data!![position]
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

    open inner class ClickListener(): View.OnClickListener{
        override fun onClick(v: View?) {
            val pos = data?.get(itemCount)

//            Toast.makeText(context, recyclerView.indexOfChild(v).toString(), Toast.LENGTH_SHORT)?.show()
        }

    }

    override fun getItemCount(): Int {
        return data?.size!!
    }



}