package jp.mydns.kokoichi0206.fcm.api

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import jp.mydns.kokoichi0206.fcm.entity.RegisterRequestPayload
import kotlinx.serialization.json.Json

class FCMApi {

    private val httpClient = HttpClient {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                useAlternativeNames = false
            })
        }
    }

    companion object {
        // FIXME: FCM 登録サーバーの立ってる IP を直で打っている
        private const val FCM_ENDPOINT = "http://192.168.0.7:8080/register"
    }

    suspend fun registerFCM(token: String): String {
        return httpClient.post(FCM_ENDPOINT) {
            // https://ktor.io/docs/request.html#body
            contentType(ContentType.Application.Json)
            setBody(RegisterRequestPayload(token = token))
        }.body()
    }
}
