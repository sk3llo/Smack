package com.example.a_karpenko.smack.utils

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.widget.Toast

//Start the Broadcast to check if user is connected to the Internet
class ConnectionChangeUtil: BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val cm: ConnectivityManager? = context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val ni = cm?.activeNetworkInfo
        val wifi = ni?.type == ConnectivityManager.TYPE_WIFI
        val mobile= ni?.type == ConnectivityManager.TYPE_MOBILE

        if (ni == null && !ni?.isConnectedOrConnecting!!) {
            Toast.makeText(context, "Please, check your Internet connection", Toast.LENGTH_SHORT).show()
        }
    }
}