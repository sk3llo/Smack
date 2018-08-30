package com.project.coolz.smack.utils

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.project.coolz.smack.App
import com.project.coolz.smack.models.firestore.PresenceModel
import org.jetbrains.anko.doAsync


//DO STUFF HERE WHEN APP IS KILLED
class IfAppIsKilledService : Service() {

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        Log.d("ClearFromRecentService", "Service Started")
        return START_NOT_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("ClearFromRecentService", "Service Destroyed")
    }

    override fun onTaskRemoved(rootIntent: Intent) {
        //Set presence to 0 if app is killed
        doAsync {
            try {
                val presence = FirebaseFirestore.getInstance()
                        .collection("Users")
                        .document(RealmUtil().retrieveMyId()!!)
                        .collection("presence")
                        .document("my")
                presence.set(PresenceModel("0"))
                //Delete me from online
                FirebaseFirestore.getInstance().collection("Online").document(RealmUtil().retrieveMyId()!!).delete()
                Log.e("ClearFromRecentService", "END")
            } finally {
                stopSelf()
            }
        }
    }
}