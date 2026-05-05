package sanchez.santiago.biometricsss.screens

import android.content.Context
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG
import androidx.biometric.BiometricPrompt
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import java.util.concurrent.Executor

@Composable
fun LoginScreen(innerPadding: PaddingValues, context: Context) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var authStatus by remember { mutableStateOf("Esperando autenticación") }
    var biometricAvailable by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        val biometricManager = BiometricManager.from(context)
        when (biometricManager.canAuthenticate(BIOMETRIC_STRONG)) {
            BiometricManager.BIOMETRIC_SUCCESS -> {
                authStatus = "Biométricos disponibles. Presiona el botón para iniciar"
                biometricAvailable = true
            }
            BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE -> {
                authStatus = "Biométricos no disponibles"
                biometricAvailable = false
            }
            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> {
                authStatus = "Biométricos no configurados"
                biometricAvailable = false
            }
            else -> {
                authStatus = "Error de biometría"
                biometricAvailable = false
            }
        }
    }

    val activity = context as FragmentActivity
    val executor: Executor = ContextCompat.getMainExecutor(context)

    val biometricPrompt = remember {
        BiometricPrompt(activity, executor, object : BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                super.onAuthenticationError(errorCode, errString)
                authStatus = "Error de autenticación: $errString"
            }

            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                super.onAuthenticationSucceeded(result)
                authStatus = "Autenticación exitosa"
            }

            override fun onAuthenticationFailed() {
                super.onAuthenticationFailed()
                authStatus = "Autenticación fallida"
            }
        })
    }

    val promptInfo = remember {
        BiometricPrompt.PromptInfo.Builder()
            .setTitle("Autenticación biométrica")
            .setSubtitle("Inicia sesión con tu huella o cara digital")
            .setDescription("Coloca tu dedo en el sensor, o mira tu cámara")
            .setNegativeButtonText("Cancelar")
            .build()
    }

    Column(modifier = Modifier.padding(innerPadding)) {
        Text("Iniciar sesión")
        TextField(email, onValueChange = { email = it }, label = { Text("Email") })
        TextField(
            password,
            onValueChange = { password = it },
            label = { Text("Contraseña") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
        )

        Button(
            onClick = {
                biometricPrompt.authenticate(promptInfo)
            },
            enabled = biometricAvailable
        ) {
            Text("Iniciar sesión con biometría")
        }

        Text(text = authStatus)
        Spacer(modifier = Modifier.height(32.dp))
    }
}
