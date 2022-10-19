package jp.mydns.kokoichi0206.fcm.android

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService

class FCMService : FirebaseMessagingService() {

    /**
     * トークンが更新された時に呼ばれる。
     */
    override fun onNewToken(token: String) {

        Log.d("fcm.android", token)

        sendRegistrationToServer(token)
    }

    private fun sendRegistrationToServer(token: String) {
        // TODO: Send to server
    }
}