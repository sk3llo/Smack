package com.example.a_karpenko.smack.utils

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import com.example.a_karpenko.smack.models.firestore.PresenceModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
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
            val presence = FirebaseFirestore.getInstance()
                    .collection("Users")
                    .document(FirebaseAuth.getInstance().currentUser?.uid!!)
                    .collection("presence")
                    .document("my")
            presence.set(PresenceModel("0"))
            Log.e("ClearFromRecentService", "END")
            stopSelf()
        }
    }
}