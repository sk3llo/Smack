package com.example.a_karpenko.smack

import android.app.Application
import io.realm.Realm
import io.realm.RealmConfiguration

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        Realm.init(this)

        val realmConfig = RealmConfiguration.Builder()
                .name("Choose_main")
                .schemaVersion(2)
                .deleteRealmIfMigrationNeeded()
                .build()
        Realm.setDefaultConfiguration(realmConfig)
    }
}