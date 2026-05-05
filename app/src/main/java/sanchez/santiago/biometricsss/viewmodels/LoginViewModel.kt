package sanchez.santiago.biometricsss.viewmodels

import androidx.lifecycle.ViewModel
import sanchez.santiago.biometricsss.datastore.DataStoreManager

class LoginViewModel(private val dataStoreManager: DataStoreManager): ViewModel() {

    private val emailCredential = "correo@mail.com"
    private val passwordCredential = "abc1234"

    fun login(email: String, password: String){
        if(email.lowercase() == emailCredential && password == passwordCredential){
        }

    }

}