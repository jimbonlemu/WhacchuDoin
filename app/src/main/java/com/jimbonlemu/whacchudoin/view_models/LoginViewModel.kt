package com.jimbonlemu.whacchudoin.view_models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jimbonlemu.whacchudoin.data.network.auth.LoginRequest
import com.jimbonlemu.whacchudoin.data.network.auth.LoginResponse
import com.jimbonlemu.whacchudoin.data.network.repository.AuthRepository
import com.jimbonlemu.whacchudoin.data.network.response.ResponseState
import kotlinx.coroutines.launch

class LoginViewModel (private val authRepository: AuthRepository) : ViewModel() {
    private val _loginResult = MutableLiveData<ResponseState<LoginResponse>>()
    val loginResult: LiveData<ResponseState<LoginResponse>> by lazy { _loginResult }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            authRepository.login(LoginRequest(email, password)).collect {
                _loginResult.value = it
            }
        }
    }
}