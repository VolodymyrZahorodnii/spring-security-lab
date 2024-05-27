package ua.kpi.its.lab.security.ui.main

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import ua.kpi.its.lab.security.LocalHttpClient

@Composable
fun MainScreen(token: String) {
    val scope = rememberCoroutineScope()
    val client = LocalHttpClient.current

    Column(modifier = Modifier.fillMaxSize()) {
        Text("TODO: Replace with your code")
        ExampleScreen(token, scope, client)
    }
}
