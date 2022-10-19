package jp.mydns.kokoichi0206.fcm.android

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import jp.mydns.kokoichi0206.fcm.FCMSDK
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FCMService : FirebaseMessagingService() {

    /**
     * トークンが更新された時に呼ばれる。
     */
    override fun onNewToken(token: String) {

        Log.d("fcm.android", token)

        sendRegistrationToServer(token)
    }

    private fun sendRegistrationToServer(token: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val sdk = FCMSDK()
            sdk.register(token)
        }
    }
}