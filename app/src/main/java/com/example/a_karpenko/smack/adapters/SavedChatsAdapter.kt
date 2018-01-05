package com.example.a_karpenko.smack.adapters

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import com.example.a_karpenko.smack.R
import com.example.a_karpenko.smack.SavedChats
import com.example.a_karpenko.smack.models.saved_chats.SavedChatsModel
import io.realm.*

open class SavedChatsAdapter(activity: SavedChats,
                             realmResults: RealmResults<SavedChatsModel>,
                             automaticUpdate: Boolean,
                             animatedResults: Boolean): RealmBasedRecyclerViewAdapter<SavedChatsModel, SavedChatsAdapter.ViewHolder>(
        activity,
        realmResults,
        automaticUpdate,
        animatedResults) {

    override fun onBindRealmViewHolder(holder: ViewHolder?, pos: Int) {
        val savedChats: SavedChatsModel? = realmResults[pos]
        holder?.date?.text = savedChats?.time.toString()
    }

    override fun onCreateRealmViewHolder(vg: ViewGroup?, pos: Int): ViewHolder {
        val v: View? = inflater.inflate(R.layout.activity_saved_chats, vg, false)
        val vh: ViewHolder? = ViewHolder(v as FrameLayout)
        return vh!!
    }


    open inner class ViewHolder(container: FrameLayout): RealmViewHolder(container) {
        var date: TextView? = container.findViewById(R.id.savedChatDate)
    }
}