package jp.mydns.kokoichi0206.fcm

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform