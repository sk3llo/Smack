package com.example.a_karpenko.smack

import android.app.Application
import android.drm.DrmStore
import android.util.Log
import com.example.a_karpenko.smack.utils.FirestoreUtil
import com.google.firebase.firestore.FirebaseFirestoreException
import io.realm.Realm
import io.realm.RealmConfiguration

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        Realm.init(this)

        val realmConfig = RealmConfiguration.Builder()
                .name("Choose.realm")
                .schemaVersion(3)
                .deleteRealmIfMigrationNeeded()
                .build()
        Realm.setDefaultConfiguration(realmConfig)

        FirestoreUtil().logIn()
    }
}