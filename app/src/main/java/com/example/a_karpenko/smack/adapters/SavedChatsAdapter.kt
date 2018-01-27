package com.example.a_karpenko.smack.adapters

import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.example.a_karpenko.smack.R
import com.example.a_karpenko.smack.models.chat.EndMessagesSize
import com.example.a_karpenko.smack.models.chat.SavedChatsTime
import com.example.a_karpenko.smack.models.chat.StartMessagesSize
import com.example.a_karpenko.smack.models.firestore.ChatModel
import com.example.a_karpenko.smack.ui.SavedChats
import com.example.a_karpenko.smack.ui.SavedMessages
import com.example.a_karpenko.smack.utils.RealmUtil
import io.realm.*
import org.jetbrains.anko.toast

open class SavedChatsAdapter(var recyclerView: RecyclerView,
                             var activity: SavedChats,
                             var context: Context,
                             var coll: OrderedRealmCollection<SavedChatsTime>,
                             animResults: Boolean): RealmRecyclerViewAdapter<SavedChatsTime, SavedChatsAdapter.ViewHolder>(coll, animResults){

    var realm = Realm.getDefaultInstance()


    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        val savedChats: SavedChatsTime? = coll[position]
        holder?.date?.text = savedChats?.time.toString()

        holder?.trash?.setOnClickListener(ClickListener(position))
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        val v: View = LayoutInflater.from(context).inflate(R.layout.saved_chat_row, parent, false)
        v.setOnClickListener(ClickListener(recyclerView.indexOfChild(v)))
        return ViewHolder(v)
    }



    open inner  class ViewHolder(private var container: View) : RecyclerView.ViewHolder(container) {
        var date: TextView? = container.findViewById(R.id.savedChatDate)
        var trash: Button? = container.findViewById(R.id.trash)
    }

    // On Saved chat recycler item clicked or trash icon clicked
    open inner class ClickListener(private var pos: Int): View.OnClickListener{
        override fun onClick(v: View?) {

            if (v?.id == R.id.trash && !realm?.isInTransaction!!) {

                //Ordered Realm Collection
                var orc = coll.sort("id", Sort.ASCENDING)

                realm?.executeTransaction {

                val list: RealmResults<ChatModel>? = realm?.where(ChatModel::class.java)
                        ?.findAllSorted("id", Sort.ASCENDING)

                try {

                    // Query pos startMessagesSize and endMessagesSize
                    val endSize = realm?.where(EndMessagesSize::class.java)
                            ?.findAllSorted("id", Sort.ASCENDING)!![pos]?.endMessagesSize!!

                    val startSize = realm?.where(StartMessagesSize::class.java)
                            ?.findAllSorted("id", Sort.ASCENDING)!![pos]?.startMessagesSize!!

                    // Delete messages between startMessagesSize and endMessagesSize
                    if (pos >= 1) {
                        for (i in endSize.minus(1)
                                downTo startSize) {
                            Log.d("SavedChatAdapter*** ", "i >= 1: ${i}")
                            list?.deleteFromRealm(i)
                        }
                    } else if (pos == 0 && realm?.where(StartMessagesSize::class.java)?.findAll()?.size!! > 1
                            && realm?.where(EndMessagesSize::class.java)?.findAll()?.size!! > 1) {
                        for (i in endSize.minus(1) downTo 0) {
                            Log.d("SavedChatAdapter*** ", "pos = 0: $i")
                            list?.deleteFromRealm(i)
                        }
                    } else if (pos == 0 && realm?.where(StartMessagesSize::class.java)?.findAll()?.size!! == 1
                            && realm?.where(EndMessagesSize::class.java)?.findAll()?.size!! == 1) {
                        list?.deleteAllFromRealm()
                    }


                    // Query all results that are greater than pos to change them
                    val endMessage = realm?.where(EndMessagesSize::class.java)
                            ?.greaterThan("endMessagesSize", RealmUtil().getEndMessagesSize()!![pos]?.endMessagesSize!!)
                            ?.findAllSorted("id", Sort.ASCENDING)
                    val startMessage = realm?.where(StartMessagesSize::class.java)
                            ?.greaterThan("startMessagesSize", RealmUtil().getStartMessagesSize()!![pos]?.startMessagesSize!!)
                            ?.findAllSorted("id", Sort.ASCENDING)

                    // Change startMessagesSize and endMessagesSize
                    if (endMessage?.isNotEmpty()!! && startMessage?.isNotEmpty()!!) {

                        if (pos == 0 && startMessage.size > 1) {
                            startMessage.forEach { start ->
                                start.startMessagesSize = start.startMessagesSize!!.minus(endSize)
                                Log.d("SavedChatsAdapter**** ", "start sizeeeeeeeeeeeeeeeeeeeEEEEEEE: ${start.startMessagesSize}")
                            }
                        } else if (pos == 0 && startMessage.size == 1){

                        } else if (pos >= 1) {
                            startMessage.forEach { start ->
                                start.startMessagesSize = start.startMessagesSize!!.minus(endSize - startSize)
                                Log.d("SavedChatsAdapter**** ", "start sizeeeeeeeeeeeeeeeeeeeEEEEEEE: ${start.startMessagesSize}")
                            }
                        }

                        endMessage.forEach {
                            it?.endMessagesSize = it?.endMessagesSize?.minus(endSize - startSize)
                            Log.d("SavedChatsAdapter**** ", "end sizeeeeeeeeeeeeeeeeeeeEEEEEEE: ${it.endMessagesSize}")
                        }
                    }


                } catch (e: ArrayIndexOutOfBoundsException){
                    activity.toast("zaebis")
                }

                // Delete startMessagesSize and endMessagesSize at given position
                realm?.where(StartMessagesSize::class.java)?.findAllSorted("id", Sort.ASCENDING)?.deleteFromRealm(pos)
                realm?.where(EndMessagesSize::class.java)?.findAllSorted("id", Sort.ASCENDING)?.deleteFromRealm(pos)


                notifyItemRangeRemoved(pos, itemCount)
                    // At last delete the recycler item
                orc?.deleteFromRealm(pos)

              }

            } else {
                val intent: Intent? = Intent(context, SavedMessages::class.java)
                intent?.putExtra("id", recyclerView.indexOfChild(v))
                activity.startActivity(intent)
                activity.finish()
            }
        }
    }


    override fun getItemCount(): Int {
        return coll.size
    }





}