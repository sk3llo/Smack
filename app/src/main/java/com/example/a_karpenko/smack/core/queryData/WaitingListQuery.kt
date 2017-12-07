package com.example.a_karpenko.smack.core.queryData

import android.content.Context
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*

class WaitingListQuery {

    private val currentDate = Calendar.getInstance().time
    private val uidMy = FirebaseAuth.getInstance().currentUser?.uid
    private val wl = FirebaseFirestore.getInstance()
    .collection("Users").document("$uidMy")
    .collection("waiting_list").document("enter")

    private val optionsMy = FirebaseFirestore.getInstance()
    .collection("Users").document("$uidMy")
    .collection("options").document("optionsMy")

    private val optionsLF = FirebaseFirestore.getInstance()
    .collection("Users").document("$uidMy")
    .collection("options").document("optionsLF")

    //Check if user is on waiting list
    fun checkWaitingList(context: Context?) {
        wl.addSnapshotListener { querySnapshot, exception ->
            val query = querySnapshot.data["waitingListOn"]
            if (query != null && query == true) {
                Toast.makeText(context, "Zaebis: ${query}", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "Hueva: ${query}", Toast.LENGTH_SHORT).show()
            }
        }
    }


}