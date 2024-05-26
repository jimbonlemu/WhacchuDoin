package com.jimbonlemu.whacchudoin.view_models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jimbonlemu.whacchudoin.data.network.dto.LoginRequest
import com.jimbonlemu.whacchudoin.data.network.dto.LoginResponse
import com.jimbonlemu.whacchudoin.data.network.dto.RegisterRequest
import com.jimbonlemu.whacchudoin.data.network.dto.RegisterResponse
import com.jimbonlemu.whacchudoin.data.network.repository.AuthRepository
import com.jimbonlemu.whacchudoin.data.network.response.ResponseState
import kotlinx.coroutines.launch

class AuthViewModel (private val authRepository: AuthRepository) : ViewModel() {
    private val _loginResult = MutableLiveData<ResponseState<LoginResponse>>()
    val loginResult: LiveData<ResponseState<LoginResponse>> by lazy { _loginResult }

    private val _registerResult = MutableLiveData<ResponseState<RegisterResponse>>()
    val registerResult: LiveData<ResponseState<RegisterResponse>> by lazy { _registerResult }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            authRepository.login(LoginRequest(email, password)).collect {
                _loginResult.value = it
            }
        }
    }

    fun register(name: String, email: String, password: String) {
        viewModelScope.launch {
            authRepository.register(RegisterRequest(name, email, password)).collect {
                _registerResult.value = it
            }
        }
    }
    fun logout()= authRepository.logout()
}