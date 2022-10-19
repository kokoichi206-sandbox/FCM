package jp.mydns.kokoichi0206.fcm.entity

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RegisterRequestPayload(
    @SerialName("token")
    val token: String,
)
