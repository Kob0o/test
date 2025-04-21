package com.worldline.quiz.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.sun.net.httpserver.HttpServer
import com.worldline.quiz.data.models.LoginViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.awt.Desktop
import java.net.InetSocketAddress
import java.net.URI

@Composable
fun DesktopLoginScreen(loginViewModel: LoginViewModel, onLoginSuccess: () -> Unit) {
    val coroutineScope = rememberCoroutineScope()
    var isLoading by remember { mutableStateOf(false) }

    Column(
        Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("Connexion", style = MaterialTheme.typography.h4)
        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                coroutineScope.launch(Dispatchers.IO) {
                    isLoading = true
                    startDesktopLoginFlow(loginViewModel, onLoginSuccess)
                }
            },
            enabled = !isLoading
        ) {
            Text("Connexion avec Google")
        }
    }
}

fun startDesktopLoginFlow(
    loginViewModel: LoginViewModel,
    onLoginSuccess: () -> Unit
) {
    val redirectUri = "http://localhost:4280/callback"
    val authUrl =
        "https://kxtdhjouiuyjatcamfut.supabase.co/auth/v1/authorize?provider=google&redirect_to=$redirectUri"

    // Création du serveur local
    val server = HttpServer.create(InetSocketAddress(4280), 0)

    // Ce endpoint reçoit la redirection initiale avec fragment
    server.createContext("/callback") { exchange ->
        println("📩 Redirection reçue sur /callback")

        val html = """
            <html>
              <body>
                <script>
                  const fragment = window.location.hash;
                  if (fragment) {
                    window.location.href = "/token?" + fragment.substring(1);
                  } else {
                    document.body.innerText = "Pas de token trouvé.";
                  }
                </script>
                <p>Connexion en cours...</p>
              </body>
            </html>
        """.trimIndent()

        exchange.sendResponseHeaders(200, html.toByteArray().size.toLong())
        exchange.responseBody.use { it.write(html.toByteArray()) }
    }

    // Ce endpoint reçoit les vrais tokens en query string
    server.createContext("/token") { exchange ->
        val query = exchange.requestURI.rawQuery
        println("✅ Données reçues sur /token → $query")

        val accessToken = query?.split("&")
            ?.find { it.startsWith("access_token=") }
            ?.substringAfter("=")

        val responseText = if (accessToken != null) {
            loginViewModel.fetchUser(accessToken)
            onLoginSuccess()
            "Connexion réussie. Vous pouvez fermer cette fenêtre."
        } else {
            "Erreur : aucun token reçu."
        }

        exchange.sendResponseHeaders(200, responseText.toByteArray().size.toLong())
        exchange.responseBody.use { it.write(responseText.toByteArray()) }

        server.stop(0)
    }

    server.executor = null
    server.start()
    println("🚀 Serveur local en écoute sur http://localhost:4280")

    // Ouvre l'URL d'auth dans le navigateur
    if (Desktop.isDesktopSupported()) {
        Desktop.getDesktop().browse(URI(authUrl))
    }
}

