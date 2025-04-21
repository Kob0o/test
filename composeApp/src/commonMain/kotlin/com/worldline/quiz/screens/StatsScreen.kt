package com.worldline.quiz.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.worldline.quiz.data.dataclass.BetStatus
import com.worldline.quiz.data.models.BetViewModel
import com.worldline.quiz.utils.format  // Import de ton extension

@Composable
fun StatsScreen(viewModel: BetViewModel, onBack: () -> Unit) {
    // Récupération de la liste des paris depuis le ViewModel
    val bets by viewModel.bets.collectAsState()

    // Calcul des statistiques
    val totalBets = bets.size
    val totalGagne = bets.count { it.statut == BetStatus.GAGNE }
    val totalPerdu = bets.count { it.statut == BetStatus.PERDU }
    val totalEnCours = bets.count { it.statut == BetStatus.EN_COURS }
    val totalMise = bets.sumOf { it.mise }
    val totalGains = bets.filter { it.statut == BetStatus.GAGNE }.sumOf { it.gainPotentiel }
    val profit = totalGains - totalMise
    val tauxReussite = if (totalBets > 0) (totalGagne.toDouble() / totalBets * 100) else 0.0

    // Disposition de l'affichage
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("Statistiques", style = MaterialTheme.typography.h5)
        Spacer(modifier = Modifier.height(16.dp))
        Text("Nombre total de paris : $totalBets")
        Text("Paris gagnés : $totalGagne")
        Text("Paris perdus : $totalPerdu")
        Text("Paris en cours : $totalEnCours")
        Text("Taux de réussite : ${tauxReussite.format(1)} %")
        Text("Mise totale : ${totalMise.format(2)} €")
        Text("Gains totaux : ${totalGains.format(2)} €")
        Text("Profit / Perte : ${profit.format(2)} €")
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = onBack
        ) {
            Text("Retour")
        }
    }
}
