package com.worldline.quiz.network

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.client.statement.HttpResponse
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

class AuthClient {
    private val supabaseUrl = "https://kxtdhjouiuyjatcamfut.supabase.co"
    private val supabaseKey =
        "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6Imt4dGRoam91aXV5amF0Y2FtZnV0Iiwicm9sZSI6ImFub24iLCJpYXQiOjE3NDQzMDYwNzMsImV4cCI6MjA1OTg4MjA3M30.lSYfrEJlwUUBsn0KaCiX4MbgCcL-OTIZxkgE586JsNU" // ta anon key

    private val client = HttpClient {
        install(ContentNegotiation) {
            json(Json { ignoreUnknownKeys = true })
        }
    }

    suspend fun fetchUser(token: String): UserResponse? {
        return try {
            val response: HttpResponse = client.get("$supabaseUrl/auth/v1/user") {
                headers {
                    append("Authorization", "Bearer $token")
                    append("apikey", supabaseKey)
                }
            }
            response.body()
        } catch (e: Exception) {
            println("Erreur Auth : ${e.message}")
            null
        }
    }

    @Serializable
    data class UserResponse(val id: String, val email: String)
}
