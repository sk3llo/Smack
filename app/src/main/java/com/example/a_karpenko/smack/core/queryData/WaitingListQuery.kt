package com.example.a_karpenko.smack.core.queryData

import android.content.Context
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*

//Main class for querying Firestore and find suitable person for chat
class WaitingListQuery {

    private val currentDate = Calendar.getInstance().time
    private val uidMy = FirebaseAuth.getInstance().currentUser?.uid

    //Find user to chat
    private val users = FirebaseFirestore.getInstance()
            .collection("Users")

    // My waiting list
    private val wl = FirebaseFirestore.getInstance()
    .collection("Users").document("$uidMy")
    .collection("waiting_list").document("enter")

    //My options
    private val optionsMy = FirebaseFirestore.getInstance()
    .collection("Users").document("$uidMy")
    .collection("options").document("optionsMy")

    //Options I'm looking for
    private val optionsLF = FirebaseFirestore.getInstance()
    .collection("Users").document("$uidMy")
    .collection("options").document("optionsLF")

    //Check if user is on waiting list
    fun checkWaitingList(context: Context?) {
        wl.addSnapshotListener { querySnapshot, exception ->
            val query = querySnapshot.data["waitingListOn"]
            if (query != null && query == true) {
                users.addSnapshotListener { userQuery, userException ->
                    //TODO: find user to chat with chosen options
                }
                Toast.makeText(context, "Zaebis: ${query}", Toast.LENGTH_SHORT).show()
            } else {
                //TODO: Show an error if it exists
                Toast.makeText(context, "Hueva: ${query}", Toast.LENGTH_SHORT).show()
            }
        }
    }


}