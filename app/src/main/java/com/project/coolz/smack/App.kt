package com.project.coolz.smack

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.provider.Settings
import android.support.multidex.MultiDex
import android.telephony.TelephonyManager
import com.vanniktech.emoji.EmojiManager
import com.vanniktech.emoji.ios.IosEmojiProvider
import io.realm.Realm
import io.realm.RealmConfiguration
import android.os.Build



open class App : Application() {

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

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }
}