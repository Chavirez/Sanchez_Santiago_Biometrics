package sanchez.santiago.biometricsss.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import sanchez.santiago.biometricsss.viewmodels.LoginViewModel

@Composable
fun HomeScreen(innerPadding: PaddingValues, viewModel: LoginViewModel) {
    val username by viewModel.username.collectAsState()
    val biometricsActive by viewModel.biometricsActive.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Hola $username", style = MaterialTheme.typography.headlineMedium)
        
        Spacer(modifier = Modifier.height(24.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text = "Habilitar autenticación biométrica")
            Spacer(modifier = Modifier.width(8.dp))
            Switch(
                checked = biometricsActive,
                onCheckedChange = { viewModel.toggleBiometrics(it) }
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(onClick = { viewModel.logout() }) {
            Text("Cerrar sesión")
        }
    }
}
