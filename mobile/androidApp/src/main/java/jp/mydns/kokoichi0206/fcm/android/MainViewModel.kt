package jp.mydns.kokoichi0206.fcm.android

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging

class MainViewModel() : ViewModel() {

    var state by mutableStateOf(MainState())
        private set

    init {
        getCurrentFCMToken()
    }

    fun getCurrentFCMToken() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w("fcm.android", "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }

            // Get new FCM registration token
            val token = task.result
            Log.d("fcm.android", token)
            state = state.copy(token = token)
        })
    }
}
