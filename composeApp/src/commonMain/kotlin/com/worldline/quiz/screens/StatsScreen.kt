package com.worldline.quiz.screens

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.worldline.quiz.data.dataclass.BetStatus
import com.worldline.quiz.data.models.BetViewModel
import com.worldline.quiz.utils.format

@Composable
fun StatsScreen(viewModel: BetViewModel, onBack: () -> Unit) {
    val bets by viewModel.bets.collectAsState()
    val totalBets = bets.size
    val totalGagne = bets.count { it.statut == BetStatus.GAGNE }
    val totalPerdu = bets.count { it.statut == BetStatus.PERDU }
    val totalEnCours = bets.count { it.statut == BetStatus.EN_COURS }
    val totalMise = bets.sumOf { it.mise }
    val totalGains = bets.filter { it.statut == BetStatus.GAGNE }.sumOf { it.gainPotentiel }
    val profit = totalGains - totalMise
    val tauxReussite = if (totalBets > 0) (totalGagne.toDouble() / totalBets * 100) else 0.0

    StyledScaffold(title = "Statistiques", showBack = true, onBack = onBack) {
        Text("Nombre total de paris : $totalBets", style = MaterialTheme.typography.body1)
        Text("Paris gagnés : $totalGagne", style = MaterialTheme.typography.body1)
        Text("Paris perdus : $totalPerdu", style = MaterialTheme.typography.body1)
        Text("Paris en cours : $totalEnCours", style = MaterialTheme.typography.body1)
        Text(
            "Taux de réussite : ${tauxReussite.format(1)} %",
            style = MaterialTheme.typography.body1
        )
        Text("Mise totale : ${totalMise.format(2)} €", style = MaterialTheme.typography.body1)
        Text("Gains totaux : ${totalGains.format(2)} €", style = MaterialTheme.typography.body1)
        Text("Profit / Perte : ${profit.format(2)} €", style = MaterialTheme.typography.body1)

        Spacer(modifier = Modifier.height(24.dp))
        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = onBack,
            colors = ButtonDefaults.buttonColors(
                backgroundColor = Color(0xFFE10014),
                contentColor = Color.White
            )
        ) {
            Text("Retour")
        }
    }
}
