package com.worldline.quiz.data.models

import com.worldline.quiz.data.dataclass.Bet
import com.worldline.quiz.data.dataclass.BetStatus
import com.worldline.quiz.network.SupabaseClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class BetViewModel : CoroutineScope {
    private val job = SupervisorJob()
    override val coroutineContext: CoroutineContext = Dispatchers.Default + job

    private val client = SupabaseClient()

    private val _bets = MutableStateFlow<List<Bet>>(emptyList())
    val bets: StateFlow<List<Bet>> = _bets

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private var token: String? = null
    var userId: String? = null


    init {
        syncWithSupabase()
    }

    fun syncWithSupabase() {
        launch {
            _isLoading.value = true
            try {
                val remoteBets = client.getAllBets(token!!, userId!!)
                _bets.value = remoteBets
            } catch (e: Exception) {
                println("Erreur de synchro Supabase : ${e.message}")
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun addBet(bet: Bet) {
        val betWithUser = bet.copy(user_id = userId) // Inclure user_id localement
        _bets.update { it + betWithUser }
        launch {
            try {
                client.addBet(betWithUser, token!!, userId!!)
            } catch (e: Exception) {
                println("Erreur lors de l'ajout Supabase : ${e.message}")
            }
        }
    }


    fun updateBetStatus(id: Int, newStatus: BetStatus) {
        _bets.update { bets ->
            bets.map { bet ->
                if (bet.id == id) {
                    val updatedBet = bet.copy(statut = newStatus)
                    // Appel IMMÉDIAT à Supabase
                    launch {
                        try {
                            client.updateBet(updatedBet, token!!, userId!!)
                        } catch (e: Exception) {
                            println("Erreur update statut Supabase : ${e.message}")
                        }
                    }
                    updatedBet
                } else bet
            }
        }
    }


    fun updateBet(id: Int, mise: Double, cote: Double, statut: BetStatus) {
        _bets.update { bets ->
            bets.map {
                if (it.id == id) {
                    val updated = it.copy(
                        mise = mise,
                        cote = cote,
                        gainPotentiel = mise * cote,
                        statut = statut
                    )
                    launch {
                        try {
                            client.updateBet(updated, token!!, userId!!)
                        } catch (e: Exception) {
                            println("Erreur update Supabase : ${e.message}")
                        } finally {
                            _isLoading.value = false
                        }
                    }
                    updated
                } else it
            }
        }
    }


    fun deleteBet(id: Int) {
        _bets.value = _bets.value.filter { it.id != id }
        launch {
            try {
                client.deleteBet(id.toString(), token!!) // Passer le token
            } catch (e: Exception) {
                println("Erreur lors de la suppression Supabase : ${e.message}")
            }
        }
    }


    fun setSession(token: String, userId: String) {
        this.token = token
        this.userId = userId
        syncWithSupabase()
    }

    fun clearSession() {
        token = null
        userId = null
        _bets.value = emptyList()
    }

}
