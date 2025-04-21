package com.worldline.quiz.utils

import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

object LocalDateTimeIsoSerializer : KSerializer<LocalDateTime> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("LocalDateTime", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: LocalDateTime) {
        val instant = value.toInstant(TimeZone.UTC)
        encoder.encodeString(instant.toString())
    }

    override fun deserialize(decoder: Decoder): LocalDateTime {
        val string = decoder.decodeString()
        val instant = Instant.parse(string)
        return instant.toLocalDateTime(TimeZone.UTC)
    }
}
