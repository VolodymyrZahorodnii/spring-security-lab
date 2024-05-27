package ua.kpi.its.lab.security.ui.main

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.*
import ua.kpi.its.lab.security.dto.ProcessorRequest
import ua.kpi.its.lab.security.dto.SatelliteRequest
import ua.kpi.its.lab.security.dto.SatelliteResponse

@Composable
fun SatelliteScreen(
    token: String,
    scope: CoroutineScope,
    client: HttpClient,
    snackbarHostState: SnackbarHostState
) {
    var satellites by remember { mutableStateOf<List<SatelliteResponse>>(listOf()) }
    var loading by remember { mutableStateOf(false) }
    var openDialog by remember { mutableStateOf(false) }
    var selectedSatellite by remember { mutableStateOf<SatelliteResponse?>(null) }

    LaunchedEffect(token) {
        loading = true
        delay(1000)
        satellites = withContext(Dispatchers.IO) {
            try {
                val response = client.get("http://localhost:8080/satellites") {
                    bearerAuth(token)
                }
                loading = false
                response.body()
            }
            catch (e: Exception) {
                val msg = e.toString()
                snackbarHostState.showSnackbar(msg, withDismissAction = true, duration = SnackbarDuration.Indefinite)
                satellites
            }
        }
    }

    if (loading) {
        LinearProgressIndicator(
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.fillMaxWidth()
        )
    }
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    selectedSatellite = null
                    openDialog = true
                },
                content = {
                    Icon(Icons.Filled.Add, "add satellite")
                }
            )
        }
    ) {
        if (satellites.isEmpty()) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center
            ) {
                Text("No satellites to show", modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center)
            }
        }
        else {
            LazyColumn(
                modifier = Modifier.background(MaterialTheme.colorScheme.surfaceVariant).fillMaxSize().padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(satellites) { satellite ->
                    SatelliteItem(
                        satellite = satellite,
                        onEdit = {
                            selectedSatellite = satellite
                            openDialog = true
                        },
                        onRemove = {
                            scope.launch {
                                withContext(Dispatchers.IO) {
                                    try {
                                        val response = client.delete("http://localhost:8080/satellites/${satellite.id}") {
                                            bearerAuth(token)
                                        }
                                        require(response.status.isSuccess())
                                    }
                                    catch(e: Exception) {
                                        val msg = e.toString()
                                        snackbarHostState.showSnackbar(msg, withDismissAction = true, duration = SnackbarDuration.Indefinite)
                                    }
                                }

                                loading = true

                                satellites = withContext(Dispatchers.IO) {
                                    try {
                                        val response = client.get("http://localhost:8080/satellites") {
                                            bearerAuth(token)
                                        }
                                        loading = false
                                        response.body()
                                    }
                                    catch (e: Exception) {
                                        val msg = e.toString()
                                        snackbarHostState.showSnackbar(msg, withDismissAction = true, duration = SnackbarDuration.Indefinite)
                                        satellites
                                    }
                                }
                            }
                        }
                    )
                }
            }
        }

        if (openDialog) {
            SatelliteDialog(
                satellite = selectedSatellite,
                token = token,
                scope = scope,
                client = client,
                onDismiss = {
                    openDialog = false
                },
                onError = {
                    scope.launch {
                        snackbarHostState.showSnackbar(it, withDismissAction = true, duration = SnackbarDuration.Indefinite)
                    }
                },
                onConfirm = {
                    openDialog = false
                    loading = true
                    scope.launch {
                        satellites = withContext(Dispatchers.IO) {
                            try {
                                val response = client.get("http://localhost:8080/satellites") {
                                    bearerAuth(token)
                                }
                                loading = false
                                response.body()
                            }
                            catch (e: Exception) {
                                loading = false
                                satellites
                            }
                        }
                    }
                }
            )
        }
    }
}

@Composable
fun SatelliteDialog(
    satellite: SatelliteResponse?,
    token: String,
    scope: CoroutineScope,
    client: HttpClient,
    onDismiss: () -> Unit,
    onError: (String) -> Unit,
    onConfirm: () -> Unit,
) {
    val processor = satellite?.processor

    var name by remember { mutableStateOf(satellite?.name ?: "") }
    var country by remember { mutableStateOf(satellite?.country ?: "") }
    var launchDate by remember { mutableStateOf(satellite?.launchDate ?: "") }
    var purpose by remember { mutableStateOf(satellite?.purpose ?: "") }
    var weight by remember { mutableStateOf(satellite?.weight?.toString() ?: "") }
    var height by remember { mutableStateOf(satellite?.height?.toString() ?: "") }
    var isGeostationary by remember { mutableStateOf(satellite?.isGeostationary ?: false) }
    var processorName by remember { mutableStateOf(processor?.name ?: "") }
    var processorManufacturer by remember { mutableStateOf(processor?.manufacturer ?: "") }
    var processorCores by remember { mutableStateOf(processor?.cores?.toString() ?: "") }
    var processorFrequency by remember { mutableStateOf(processor?.frequency?.toString() ?: "") }
    var processorSocket by remember { mutableStateOf(processor?.socket ?: "") }
    var processorProductionDate by remember { mutableStateOf(processor?.productionDate ?: "") }
    var processorMmxSupport by remember { mutableStateOf(processor?.mmxSupport ?: false) }

    Dialog(onDismissRequest = onDismiss) {
        Card(modifier = Modifier.padding(16.dp).wrapContentSize()) {
            Column(
                modifier = Modifier.padding(16.dp, 8.dp).width(IntrinsicSize.Max).verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (satellite == null) {
                    Text("Create satellite")
                } else {
                    Text("Update satellite")
                }

                HorizontalDivider()
                Text("Satellite info")
                TextField(name, { name = it }, label = { Text("Name") })
                TextField(country, { country = it }, label = { Text("Country") })
                TextField(launchDate, { launchDate = it }, label = { Text("Launch date") })
                TextField(purpose, { purpose = it }, label = { Text("Purpose") })
                TextField(weight, { weight = it }, label = { Text("Weight") })
                TextField(height, { height = it }, label = { Text("Height") })
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Switch(isGeostationary, { isGeostationary = it })
                    Spacer(modifier = Modifier.width(16.dp))
                    Text("Geostationary")
                }

                HorizontalDivider()
                Text("Processor info")
                TextField(processorName, { processorName = it }, label = { Text("Name") })
                TextField(processorManufacturer, { processorManufacturer = it }, label = { Text("Manufacturer") })
                TextField(processorCores, { processorCores = it }, label = { Text("Cores") })
                TextField(processorFrequency, { processorFrequency = it }, label = { Text("Frequency") })
                TextField(processorSocket, { processorSocket = it }, label = { Text("Socket") })
                TextField(processorProductionDate, { processorProductionDate = it }, label = { Text("Production date") })
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Switch(processorMmxSupport, { processorMmxSupport = it })
                    Spacer(modifier = Modifier.width(16.dp))
                    Text("MMX Support")
                }

                HorizontalDivider()
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Button(
                        modifier = Modifier.weight(1f),
                        onClick = onDismiss,
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                    ) {
                        Text("Cancel")
                    }
                    Spacer(modifier = Modifier.fillMaxWidth(0.1f))
                    Button(
                        modifier = Modifier.weight(1f),
                        onClick = {
                            scope.launch {
                                try {
                                    val request = SatelliteRequest(
                                        name, country, launchDate, purpose, weight.toDouble(), height.toDouble(), isGeostationary,
                                        ProcessorRequest(
                                            processorName, processorManufacturer, processorCores.toInt(), processorFrequency.toDouble(),
                                            processorSocket, processorProductionDate, processorMmxSupport
                                        )
                                    )
                                    val response = if (satellite == null) {
                                        client.post("http://localhost:8080/satellites") {
                                            bearerAuth(token)
                                            setBody(request)
                                            contentType(ContentType.Application.Json)
                                        }
                                    } else {
                                        client.put("http://localhost:8080/satellites/${satellite.id}") {
                                            bearerAuth(token)
                                            setBody(request)
                                            contentType(ContentType.Application.Json)
                                        }
                                    }
                                    require(response.status.isSuccess())
                                    onConfirm()
                                } catch (e: Exception) {
                                    val msg = e.toString()
                                    onError(msg)
                                }
                            }
                        }
                    ) {
                        if (satellite == null) {
                            Text("Create")
                        } else {
                            Text("Update")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SatelliteItem(satellite: SatelliteResponse, onEdit: () -> Unit, onRemove: () -> Unit) {
    Card(shape = CardDefaults.elevatedShape, elevation = CardDefaults.elevatedCardElevation()) {
        ListItem(
            overlineContent = {
                Text(satellite.name)
            },
            headlineContent = {
                Text(satellite.country)
            },
            supportingContent = {
                Text("${satellite.weight} kg")
            },
            trailingContent = {
                Row(modifier = Modifier.padding(0.dp, 20.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.Edit,
                        contentDescription = "Edit",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.clip(CircleShape).clickable(onClick = onEdit)
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Icon(
                        Icons.Default.Delete,
                        contentDescription = "Remove",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.clip(CircleShape).clickable(onClick = onRemove)
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                }
            }
        )
    }
}
