package com.example.a_karpenko.smack.models.chat

import io.realm.RealmObject
import io.realm.annotations.Index

open class EndMessagesSize: RealmObject() {

    //Save list size at the beginning and the end
    //and when user save the chat afterward i use this values to query it
    @Index
    var endMessagesSize: Int? = 0
}