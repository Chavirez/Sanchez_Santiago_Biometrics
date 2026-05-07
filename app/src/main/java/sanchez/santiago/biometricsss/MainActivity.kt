package sanchez.santiago.biometricsss

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import sanchez.santiago.biometricsss.datastore.DataStoreManager
import sanchez.santiago.biometricsss.screens.HomeScreen
import sanchez.santiago.biometricsss.screens.LoginScreen
import sanchez.santiago.biometricsss.ui.theme.BiometricsSSTheme
import sanchez.santiago.biometricsss.viewmodels.LoginViewModel

class MainActivity : AppCompatActivity() {
    
    private lateinit var loginViewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        val dataStoreManager = DataStoreManager(applicationContext)
        loginViewModel = ViewModelProvider(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return LoginViewModel(dataStoreManager) as T
            }
        })[LoginViewModel::class.java]

        enableEdgeToEdge()
        setContent {
            BiometricsSSTheme {
                val isLoggedIn by loginViewModel.isLoggedIn.collectAsState()
                
                Scaffold { innerPadding ->
                    if (isLoggedIn) {
                        HomeScreen(innerPadding, loginViewModel)
                    } else {
                        LoginScreen(innerPadding, this, loginViewModel)
                    }
                }
            }
        }
    }
}
