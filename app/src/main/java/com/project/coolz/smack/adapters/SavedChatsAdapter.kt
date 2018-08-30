package com.project.coolz.smack.adapters

import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.afollestad.materialdialogs.DialogAction
import com.afollestad.materialdialogs.GravityEnum
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.Theme
import com.project.coolz.smack.R
import com.project.coolz.smack.models.chat.EndMessagesSize
import com.project.coolz.smack.models.chat.SavedChatsTime
import com.project.coolz.smack.models.chat.StartMessagesSize
import com.project.coolz.smack.models.firestore.ChatModel
import com.project.coolz.smack.ui.SavedChats
import com.project.coolz.smack.ui.SavedMessages
import com.project.coolz.smack.utils.RealmUtil
import io.realm.*
import org.jetbrains.anko.toast

open class SavedChatsAdapter(var recyclerView: RecyclerView,
                             var activity: SavedChats,
                             var context: Context,
                             var coll: OrderedRealmCollection<SavedChatsTime>,
                             animResults: Boolean) : RealmRecyclerViewAdapter<SavedChatsTime, SavedChatsAdapter.ViewHolder>(coll, animResults) {

    var realm = Realm.getDefaultInstance()


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val savedChats: SavedChatsTime? = coll[position]
        val text = "   Chat from " + savedChats?.time?.drop(3)?.dropLast(9)
        holder.date?.text = text

        holder.trash?.setOnClickListener(ClickListener(position))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v: View = LayoutInflater.from(context).inflate(R.layout.saved_chat_row, parent, false)
        v.setOnClickListener(ClickListener(recyclerView.indexOfChild(v)))
        return ViewHolder(v)
    }


    open inner class ViewHolder(private var container: View) : RecyclerView.ViewHolder(container) {
        var date: TextView? = container.findViewById(R.id.savedChatDate)
        var trash: Button? = container.findViewById(R.id.trash)
    }

    // On Saved chat recycler item clicked or trash icon clicked
    open inner class ClickListener(private var pos: Int) : View.OnClickListener {
        override fun onClick(v: View?) {

            if (v?.id == R.id.trash && !realm?.isInTransaction!!) {
                // Open Alert dialog
                MaterialDialog.Builder(activity)
                        .title("You are deleting saved conversation").content("Are you sure?")
                        .positiveText("Yes").negativeText("No")
                        .theme(Theme.LIGHT)
                        .btnSelector(R.drawable.alert_positive_btn_selector, DialogAction.POSITIVE)
                        .btnSelector(R.drawable.alert_negative_btn_selector, DialogAction.NEGATIVE)
                        .positiveColorRes(R.color.white)
                        .negativeColorRes(R.color.white)
                        .backgroundColorRes(R.color.material_grey_300)
                        .contentGravity(GravityEnum.CENTER)
                        .titleGravity(GravityEnum.CENTER)
                        .buttonsGravity(GravityEnum.CENTER)

                        .onPositive { dialog, which ->

                            //Ordered Realm Collection
                            val orc = coll.sort("id", Sort.ASCENDING)
                            val emptyTextView: TextView? = activity.findViewById(R.id.emptyChatsText)

                            realm?.executeTransaction { _ ->

                                val list: RealmResults<ChatModel>? = realm?.where(ChatModel::class.java)
                                        ?.sort("id", Sort.ASCENDING)?.findAll()

                                try {

                                    // Query pos startMessagesSize and endMessagesSize
                                    val endSize = realm?.where(EndMessagesSize::class.java)
                                            ?.sort("id", Sort.ASCENDING)?.findAll()!![pos]?.endMessagesSize!!

                                    val startSize = realm?.where(StartMessagesSize::class.java)
                                            ?.sort("id", Sort.ASCENDING)?.findAll()!![pos]?.startMessagesSize!!

                                    // Delete messages between startMessagesSize and endMessagesSize
                                    if (pos >= 1) {
                                        for (i in endSize.minus(1) downTo startSize) {
                                            Log.d("SavedChatAdapter*** ", "i >= 1: ${i}")
                                            list?.deleteFromRealm(i)
                                    }
                                    } else if (pos == 0 && realm?.where(StartMessagesSize::class.java)?.findAll()?.count()!! > 1
                                            && realm?.where(EndMessagesSize::class.java)?.findAll()?.size!! > 1) {
                                        for (i in endSize.minus(1) downTo 0) {
                                            Log.d("SavedChatAdapter*** ", "pos = 0: $i")
                                            list?.deleteFromRealm(i)
                                        }
                                    } else if (pos == 0 && realm?.where(StartMessagesSize::class.java)?.findAll()?.count()!! == 1
                                            && realm?.where(EndMessagesSize::class.java)?.findAll()?.size!! == 1) {
                                        list?.deleteAllFromRealm()
                                    }


                                    // Query all results that are greater than pos to change them
                                    val endMessage = realm?.where(EndMessagesSize::class.java)
                                            ?.greaterThan("endMessagesSize", RealmUtil().getEndMessagesSize()!![pos]?.endMessagesSize!!)
                                            ?.sort("id", Sort.ASCENDING)?.findAll()
                                    val startMessage = realm?.where(StartMessagesSize::class.java)
                                            ?.greaterThan("startMessagesSize", RealmUtil().getStartMessagesSize()!![pos]?.startMessagesSize!!)
                                            ?.sort("id", Sort.ASCENDING)?.findAll()

                                    // Change startMessagesSize and endMessagesSize
                                    if (endMessage?.isNotEmpty()!! && startMessage?.isNotEmpty()!!) {

                                        if (pos == 0 && startMessage.count() > 1) {
                                            startMessage.forEach { start ->
                                                start.startMessagesSize = start.startMessagesSize!!.minus(endSize)
                                                Log.d("SavedChatsAdapter**** ", "start sizeeeeeeeeeeeeeeeeeeeEEEEEEE: ${start.startMessagesSize}")
                                            }
                                        } else if (pos >= 1) {
                                            startMessage.forEach { start ->
                                                start.startMessagesSize = start.startMessagesSize!!.minus(endSize - startSize)
                                                Log.d("SavedChatsAdapter**** ", "start sizeeeeeeeeeeeeeeeeeeeEEEEEEE: ${start.startMessagesSize}")
                                            }
                                        }

                                        endMessage.forEach { it ->
                                            it?.endMessagesSize = it?.endMessagesSize?.minus(endSize - startSize)
                                            Log.d("SavedChatsAdapter**** ", "end sizeeeeeeeeeeeeeeeeeeeEEEEEEE: ${it.endMessagesSize}")
                                        }
                                    }


                                } catch (e: ArrayIndexOutOfBoundsException) {
                                    activity.toast("zaebis")
                                }

                                // Delete startMessagesSize and endMessagesSize at given position
                                realm?.where(StartMessagesSize::class.java)?.sort("id", Sort.ASCENDING)?.findAll()?.deleteFromRealm(pos)
                                realm?.where(EndMessagesSize::class.java)?.sort("id", Sort.ASCENDING)?.findAll()?.deleteFromRealm(pos)

                                notifyItemRangeRemoved(pos, itemCount)
                                // At last delete the recycler item
                                orc?.deleteFromRealm(pos)

                                if (realm?.where(ChatModel::class.java)?.sort("id", Sort.ASCENDING)?.findAll()?.count()!! >= 1) {
                                    emptyTextView?.visibility = View.GONE
                                } else {
                                    emptyTextView?.visibility = View.VISIBLE
                                }
                            }

                        }
                        .onNegative { dialog, which -> dialog.dismiss() }.show()!!

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