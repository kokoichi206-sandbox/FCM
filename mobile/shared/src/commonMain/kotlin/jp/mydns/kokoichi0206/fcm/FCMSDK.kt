package jp.mydns.kokoichi0206.fcm

import jp.mydns.kokoichi0206.fcm.api.FCMApi

class FCMSDK {

    private val api = FCMApi()

    @Throws(Exception::class)
    suspend fun register(token: String) {
        api.registerFCM(token)
    }
}