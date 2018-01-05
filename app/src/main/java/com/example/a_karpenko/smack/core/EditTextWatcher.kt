package com.example.a_karpenko.smack.core

import android.os.AsyncTask
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.TextView
import com.example.a_karpenko.smack.R
import com.example.a_karpenko.smack.models.firestore.InputModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*

class EditTextWatcher(private var editText: EditText?, foundUser: String?, private var typeView: TextView?) {

    val inputMy = FirebaseFirestore.getInstance()
            .collection("Users")
            .document(FirebaseAuth.getInstance().currentUser?.uid!!)

    val inputLF = FirebaseFirestore.getInstance()
            .collection("Users")
            .document("$foundUser")

    private val currentDate = Calendar.getInstance().time

    //Check if user LF is typing
    fun checkInputLF() = inputLF.addSnapshotListener { snapshot, exception ->
        if (exception != null){
            Log.d("EditTextWatcher**** ", "Failed to get LF input")
        }
        if (snapshot.exists() && snapshot["input"] == true){
            typeView?.text = "Someone is typing..."
            typeView?.visibility = View.VISIBLE
        } else if(snapshot.exists() && snapshot["input"] == false){
            typeView?.visibility = View.GONE
        }
    }

    fun checkInputMy() = editText?.addTextChangedListener(object: TextWatcher{
        override fun afterTextChanged(s: Editable?) {
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            if(s?.isBlank() == false){
                AsyncTask.execute {
                    inputMy.set(InputModel(true, currentDate))
                    }
            } else {
                AsyncTask.execute {
                    inputMy.set(InputModel(false, currentDate))
                }
            }
        }
    })
}