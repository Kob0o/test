package com.worldline.quiz.data.dataclass

import com.worldline.quiz.utils.LocalDateTimeIsoSerializer
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

@Serializable
data class Bet(
    val id: Int,
    val sport: String,
    val event: String,
    val cote: Double,
    val mise: Double,
    val gainPotentiel: Double,
    val statut: BetStatus,
    @Serializable(with = LocalDateTimeIsoSerializer::class)
    val date: LocalDateTime,
    val user_id: String? = null
)

@Serializable
enum class BetStatus {
    EN_COURS,
    GAGNE,
    PERDU
}