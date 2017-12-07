package com.example.a_karpenko.smack.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Button
import com.example.a_karpenko.smack.R
import com.example.a_karpenko.smack.utils.FirestoreUtil
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import com.firebase.ui.auth.ErrorCodes
import com.firebase.ui.auth.IdpResponse
import java.util.*


class Login : AppCompatActivity() {


    val user = FirebaseAuth.getInstance().currentUser
    val RC_SIGN_IN = 321
    var showSnackbar : Snackbar? = null
    var loginSignInButton : Button? = null
    val TAG = "Login"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        loginSignInButton = findViewById(R.id.loginSignInButton)
        loginSignInButton?.setOnClickListener { v ->
            startActivityForResult(
                    AuthUI.getInstance()
                            .createSignInIntentBuilder()
                            .setAvailableProviders(
                                    Arrays.asList(AuthUI.IdpConfig.Builder(AuthUI.EMAIL_PROVIDER).build(),
                                            AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build(),
                                            AuthUI.IdpConfig.Builder(AuthUI.FACEBOOK_PROVIDER).build()))
                            .setTheme(R.style.AppThemeFirebaseAuth)
                            .build(),
                    RC_SIGN_IN)
        }

        if (user == null) {
            startActivityForResult(
                    AuthUI.getInstance()
                            .createSignInIntentBuilder()
                            .setAvailableProviders(
                                    Arrays.asList(AuthUI.IdpConfig.Builder(AuthUI.EMAIL_PROVIDER).build(),
                                            AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build(),
                                            AuthUI.IdpConfig.Builder(AuthUI.FACEBOOK_PROVIDER).build()))
                            .setTheme(R.style.AppThemeFirebaseAuth)
                            .build(),
                    RC_SIGN_IN)
        } else {
            startActivity(Intent(this@Login, MainActivity::class.java))
            finish()
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        // RC_SIGN_IN is the request code you passed into startActivityForResult(...) when starting the sign in flow.

        if (data == null) {
            loginSignInButton?.visibility = View.VISIBLE
            return
        }

        if (requestCode == RC_SIGN_IN) {
            val response = IdpResponse.fromResultIntent(data)
            // Successfully signed in
            if (resultCode == Activity.RESULT_OK) {
                startActivity(Intent(this@Login, MainActivity::class.java))
                finish()
                return
            } else {
                // Sign in failed
                if (response == null) {
                    // User pressed back button
                    loginSignInButton?.visibility = View.VISIBLE
                    showSnackbar = Snackbar.make(findViewById(android.R.id.content), R.string.sign_in_cancelled, Snackbar.LENGTH_SHORT)
                    showSnackbar?.show()
                    return
                }

                if (response.errorCode == ErrorCodes.NO_NETWORK) {
                    loginSignInButton?.visibility = View.VISIBLE
                    showSnackbar = Snackbar.make(findViewById(android.R.id.content), R.string.no_internet_connection, Snackbar.LENGTH_LONG)
                    showSnackbar?.show()
                    return
                }

                if (response.errorCode == ErrorCodes.UNKNOWN_ERROR) {
                    loginSignInButton?.visibility = View.VISIBLE
                    showSnackbar = Snackbar.make(findViewById(android.R.id.content), R.string.unknown_error, Snackbar.LENGTH_SHORT)
                    showSnackbar?.show()
                    return
                }

            }

            loginSignInButton = findViewById(R.id.loginSignInButton)
            loginSignInButton?.visibility = View.VISIBLE
            showSnackbar = Snackbar.make(findViewById(android.R.id.content), R.string.unknown_sign_in_response, Snackbar.LENGTH_SHORT)
            showSnackbar?.show()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

}

