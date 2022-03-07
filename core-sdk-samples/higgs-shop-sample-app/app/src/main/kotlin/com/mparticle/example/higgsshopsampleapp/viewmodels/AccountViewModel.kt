package com.mparticle.example.higgsshopsampleapp.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class AccountViewModel : ViewModel() {
    val loggedInResponseLiveData = MutableLiveData<Boolean>()
    private val TAG = "AccountViewModel"

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