package com.worldline.quiz

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform