package com.mparticle.example.higgsshopsampleapp.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mparticle.MParticle
import kotlinx.coroutines.launch

class AccountViewModel : ViewModel() {
    val loggedInResponseLiveData = MutableLiveData<Boolean>()
    private val TAG = "AccountViewModel"

    init {
        MParticle.getInstance()?.Identity()?.currentUser.let {
            if(it?.isLoggedIn == true) {
                loggedInResponseLiveData.value = true
            } else {
                loggedInResponseLiveData.value = false
            }
        }
    }
    fun login() {
        viewModelScope.launch {
            //do some more stuff
            loggedInResponseLiveData.value = true
        }
    }

    fun logout() {
        viewModelScope.launch {
            //do some more stuff
            loggedInResponseLiveData.value = false
        }
    }
}