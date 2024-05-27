package ua.kpi.its.lab.security.ui.main

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.Dialog
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ua.kpi.its.lab.security.dto.ExampleDto

@Composable
fun ExampleScreen(
    token: String,
    scope: CoroutineScope,
    client: HttpClient,
) {
    var data by remember { mutableStateOf("") }
    var isOpen by remember { mutableStateOf(false) }

    // Load data on first display
    LaunchedEffect(token) {
        data = withContext(Dispatchers.IO) {
            val response: ExampleDto =
                client.get("http://localhost:8080/example") {
                    header("Authorization", "Bearer $token")
                }.body()
            response.message
        }
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        if (data.isNotBlank()) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                Text(data)
                Button(onClick = {
                    isOpen = true
                }) {
                    Text("Add")
                }
            }
        } else {
            LinearProgressIndicator(
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.fillMaxWidth()
            )
        }
        if (isOpen) {
            ExampleDialog(scope, { isOpen = false }, {
                isOpen = false
                // TODO: Reload data to refresh list
            })
        }
    }
}

@Composable
fun ExampleDialog(
    scope: CoroutineScope,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(modifier = Modifier.wrapContentSize()) {
            Column {
                Text("your code with form here")
                Button(onClick = {
                    scope.launch {
                        // TODO: do the real post with data
                        // client.post()
                        onConfirm()
                    }
                }) {
                    Text("Add or update record")
                }
            }
        }
    }
}