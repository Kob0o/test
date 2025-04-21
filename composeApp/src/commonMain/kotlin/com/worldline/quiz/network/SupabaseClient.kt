package com.worldline.quiz.network

import com.worldline.quiz.data.dataclass.Bet
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.client.request.parameter
import io.ktor.client.request.patch
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpHeaders
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

class SupabaseClient {

    private val supabaseUrl = "https://kxtdhjouiuyjatcamfut.supabase.co"
    private val supabaseKey =
        "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6Imt4dGRoam91aXV5amF0Y2FtZnV0Iiwicm9sZSI6InNlcnZpY2Vfcm9sZSIsImlhdCI6MTc0NDMwNjA3MywiZXhwIjoyMDU5ODgyMDczfQ.eOTQXd9fZMKhYk0dayuBIYKTrKPzzLYeeWftq-PAsFQ"

    private val client = HttpClient {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                prettyPrint = true
                isLenient = true
            })
        }
    }

    suspend fun getAllBets(token: String, userId: String): List<Bet> {
        val response: HttpResponse = client.get("$supabaseUrl/rest/v1/bets") {
            headers {
                append("apikey", supabaseKey)
                append("Authorization", "Bearer $token") // Utiliser le token utilisateur
            }
            parameter("select", "*")
            parameter("user_id", "eq.$userId")
        }
        return response.body()
    }

    suspend fun addBet(bet: Bet, token: String, userId: String) {
        val betWithUser = bet.copy(user_id = userId)
        val response = client.post("$supabaseUrl/rest/v1/bets") {
            headers {
                append("Authorization", "Bearer $token")
                append("apikey", supabaseKey)
                append(HttpHeaders.ContentType, "application/json")
            }
            setBody(listOf(betWithUser))
        }

        println("Supabase POST response: ${response.status}")
        println("Supabase POST body: ${response.bodyAsText()}")
    }


    suspend fun deleteBet(id: String, token: String) {
        val response = client.delete("$supabaseUrl/rest/v1/bets") {
            headers {
                append("apikey", supabaseKey)
                append("Authorization", "Bearer $token")
                append("Prefer", "return=representation") // utile pour forcer un retour
            }
            parameter("id", "eq.$id")
        }

        println("📬 Supabase DELETE status: ${response.status}")
        println("📬 Supabase DELETE body: ${response.bodyAsText()}")
    }


    suspend fun updateBet(bet: Bet, token: String, userId: String) {
        val response = client.patch("$supabaseUrl/rest/v1/bets") {
            headers {
                append("Authorization", "Bearer $token")
                append("apikey", supabaseKey)
                append(HttpHeaders.ContentType, "application/json")
            }
            parameter("id", "eq.${bet.id}") // Filtre par ID
            setBody(bet) // Envoie TOUT l'objet Bet (y compris user_id)
        }

        println("Supabase PATCH response: ${response.status}")
        println("Supabase PATCH body: ${response.bodyAsText()}")
    }


}
