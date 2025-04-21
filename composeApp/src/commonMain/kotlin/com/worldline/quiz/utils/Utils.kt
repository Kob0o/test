package com.worldline.quiz.utils

import kotlin.math.pow
import kotlin.math.round

fun generateRandomId(): Int {
    return (100000..999999).random()
}


fun Double.format(decimals: Int): String {
    val multiplier = 10.0.pow(decimals)
    val value = round(this * multiplier) / multiplier
    return value.toString()
}
