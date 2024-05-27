package ua.kpi.its.lab.security.ui.login

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import io.ktor.client.*
import io.ktor.client.plugins.*
import io.ktor.client.request.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ua.kpi.its.lab.security.LocalHttpClient

@Composable
fun LoginScreen(
    updateToken: (String) -> Unit
) {
    val scope = rememberCoroutineScope()
    val client = LocalHttpClient.current
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        Text("TODO: Replace with your code with login form here")
        Button(onClick = {
            scope.launch {
                val token = withContext(Dispatchers.IO) {
                    // TODO: do the real post with data to token endpoint
                    // TODO: add try...catch to handle io.ktor.client.plugins.ClientRequestException
                    //       thrown by 403 Forbidden status code if wrong username/password are sent
                    // client.post("http://localhost:8080/token") {
                    //     // translate http errors to exceptions
                    //     expectSuccess = true
                    //     basicAuth("username", "password")
                    // }
                    "dummy"
                }
                updateToken(token)
            }
        }) {
            Text("Login")
        }
    }
}

@Preview
@Composable
fun LoginPreview() {
    CompositionLocalProvider(
        LocalHttpClient provides HttpClient()
    ) {
        MaterialTheme {
            LoginScreen {  }
        }
    }
}