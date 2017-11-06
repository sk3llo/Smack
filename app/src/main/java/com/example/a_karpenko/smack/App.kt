package com.example.a_karpenko.smack

import android.app.Activity
import android.app.Application
import android.content.Intent
import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.support.design.widget.CoordinatorLayout
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.Button
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.FacebookSdk
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.firebase.ui.auth.ErrorCodes
import com.firebase.ui.auth.IdpResponse
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.auth.api.signin.GoogleSignInResult
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.ResultCallback
import java.util.*


class App : AppCompatActivity() {


    val user = FirebaseAuth.getInstance().currentUser
    val RC_SIGN_IN = 321
    var showSnackbar : Snackbar? = null
    var loginSignInButton : Button? = null
    val TAG = "App"

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
                            .build(),
                    RC_SIGN_IN)
        } else {
            startActivity(Intent(this@App, MainActivity::class.java))
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
                startActivity(Intent(this@App, MainActivity::class.java))
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

