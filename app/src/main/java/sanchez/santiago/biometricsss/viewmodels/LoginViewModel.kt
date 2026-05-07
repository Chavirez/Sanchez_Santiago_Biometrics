package sanchez.santiago.biometricsss.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import sanchez.santiago.biometricsss.datastore.DataStoreManager

class LoginViewModel(private val dataStoreManager: DataStoreManager): ViewModel() {

    private val emailCredential = "correo@mail.com"
    private val passwordCredential = "abc1234"

    val isLoggedIn: StateFlow<Boolean> = dataStoreManager.isLoggeedinFlow.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = false
    )

    val username: StateFlow<String> = dataStoreManager.usernameFlow.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = ""
    )

    val biometricsActive: StateFlow<Boolean> = dataStoreManager.biometricsFlow.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = false
    )

    fun login(email: String, password: String, onSuccess: () -> Unit, onError: (String) -> Unit) {
        if (email.lowercase() == emailCredential && password == passwordCredential) {
            viewModelScope.launch {
                dataStoreManager.saveSession(email)
                onSuccess()
            }
        } else {
            onError("Credenciales incorrectas")
        }
    }

    fun loginBiometric(onSuccess: () -> Unit) {
        viewModelScope.launch {
            dataStoreManager.loginWithBiometrics()
            onSuccess()
        }
    }

    fun toggleBiometrics(active: Boolean) {
        viewModelScope.launch {
            dataStoreManager.activeBiometrics(active)
        }
    }

    fun logout() {
        viewModelScope.launch {
            if (biometricsActive.value) {
                dataStoreManager.logoutKeepUser()
            } else {
                dataStoreManager.logout()
            }
        }
    }
}
