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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.worldline.quiz.data.models.LoginViewModel
import java.io.InputStreamReader
import java.io.PrintWriter
import java.net.ServerSocket
import java.net.URI
import java.net.URLDecoder

@Composable
fun DesktopLoginScreen(
    loginViewModel: LoginViewModel,
    onLoginSuccess: () -> Unit
) {
    var isWaiting by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    Column(Modifier.fillMaxSize().padding(16.dp)) {
        Text("Connexion Desktop", style = MaterialTheme.typography.h4)
        Spacer(modifier = Modifier.height(24.dp))

        errorMessage?.let {
            Text("Erreur : $it", color = MaterialTheme.colors.error)
            Spacer(modifier = Modifier.height(8.dp))
        }

        Button(
            onClick = {
                isWaiting = true
                errorMessage = null

                Thread {
                    try {
                        val server = ServerSocket(8080).apply { soTimeout = 120000 }

                        val client = server.accept()
                        val input = client.getInputStream()
                        val output = client.getOutputStream()
                        val reader = input.bufferedReader()
                        val writer = PrintWriter(output, true)

                        // Lire la requête
                        val request = StringBuilder()
                        while (true) {
                            val line = reader.readLine() ?: break
                            if (line.isEmpty()) break
                            request.append(line).append("\n")
                        }

                        // Analyser la méthode et le chemin
                        val firstLine = request.toString().lines().first()
                        val (method, path) = firstLine.split(" ").let { it[0] to it[1] }

                        when {
                            path.startsWith("/callback") -> {
                                // Envoyer la page de capture du token
                                writer.println("HTTP/1.1 200 OK")
                                writer.println("Content-Type: text/html; charset=utf-8")
                                writer.println("Connection: close")
                                writer.println()
                                writer.println(
                                    """
                                    <html><body>
                                        <script>
                                            const hash = window.location.hash.substring(1);
                                            const params = new URLSearchParams(hash);
                                            const accessToken = params.get('access_token');
                                            
                                            fetch('/token', {
                                                method: 'POST',
                                                headers: {
                                                    'Content-Type': 'application/x-www-form-urlencoded'
                                                },
                                                body: 'access_token=' + encodeURIComponent(accessToken)
                                            }).then(() => {
                                                window.close();
                                            });
                                        </script>
                                    </body></html>
                                    """.trimIndent()
                                )
                            }

                            method == "POST" && path == "/token" -> {
                                // Lire le corps de la requête
                                val contentLength = request.toString()
                                    .lines()
                                    .find { it.startsWith("Content-Length:") }
                                    ?.substringAfter(":")?.trim()?.toInt() ?: 0

                                val body = InputStreamReader(input, "UTF-8")
                                    .use { it.readText().take(contentLength) }

                                val accessToken = URLDecoder.decode(
                                    body.substringAfter("access_token="),
                                    "UTF-8"
                                )

                                if (accessToken.isNotBlank()) {
                                    loginViewModel.fetchUser(accessToken)
                                    onLoginSuccess()

                                    // Réponse de confirmation
                                    writer.println("HTTP/1.1 200 OK")
                                    writer.println("Content-Type: text/plain")
                                    writer.println()
                                    writer.println("Authentification réussie")
                                }
                            }
                        }

                        client.close()
                        server.close()
                    } catch (e: Exception) {
                        errorMessage = "Erreur de connexion : ${e.localizedMessage}"
                        isWaiting = false
                    }
                }.start()

                // Ouvrir le navigateur
                try {
                    val authUrl = "https://kxtdhjouiuyjatcamfut.supabase.co/auth/v1/authorize" +
                            "?provider=google" +
                            "&redirect_to=http://localhost:8080/callback" +
                            "&response_type=token"

                    java.awt.Desktop.getDesktop().browse(URI(authUrl))
                } catch (e: Exception) {
                    errorMessage = "Impossible d'ouvrir le navigateur : ${e.message}"
                    isWaiting = false
                }
            },
            enabled = !isWaiting
        ) {
            Text(if (isWaiting) "Connexion en cours..." else "Se connecter avec Google")
        }
    }
}