package com.example.a_karpenko.smack

import android.app.Application
import android.content.Context
import android.support.multidex.MultiDex
import com.vanniktech.emoji.EmojiManager
import com.vanniktech.emoji.ios.IosEmojiProvider
import io.realm.Realm
import io.realm.RealmConfiguration

class App : Application() {

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

    override fun onCreate() {
        super.onCreate()

        //Realm
        Realm.init(this)

        val realmConfig = RealmConfiguration.Builder()
                .name("Choose.realm")
                .schemaVersion(3)
                .deleteRealmIfMigrationNeeded()
                .build()
        Realm.setDefaultConfiguration(realmConfig)

        //Emojis
        EmojiManager.install(IosEmojiProvider())

    }
}