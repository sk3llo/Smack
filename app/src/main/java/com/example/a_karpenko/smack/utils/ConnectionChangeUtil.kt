package com.example.a_karpenko.smack.utils

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.view.View
import android.widget.Button
import android.widget.Toast
import com.example.a_karpenko.smack.R
import com.example.a_karpenko.smack.ui.MainActivity

//Start the Broadcast to check if user is connected to the Internet
class ConnectionChangeUtil: BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val cm: ConnectivityManager? = context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val ni: NetworkInfo? = cm?.activeNetworkInfo
        val wifi = ni?.type == ConnectivityManager.TYPE_WIFI
        val mobile= ni?.type == ConnectivityManager.TYPE_MOBILE

        if (ni == null && !wifi || !mobile) {
            Toast.makeText(context, "Please, check your Internet connection", Toast.LENGTH_SHORT).show()
        }
    }
}