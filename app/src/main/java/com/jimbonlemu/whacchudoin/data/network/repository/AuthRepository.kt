package com.jimbonlemu.whacchudoin.data.network.repository

import com.jimbonlemu.whacchudoin.data.network.dto.LoginRequest
import com.jimbonlemu.whacchudoin.data.network.dto.LoginResponse
import com.jimbonlemu.whacchudoin.data.network.dto.RegisterRequest
import com.jimbonlemu.whacchudoin.data.network.dto.RegisterResponse
import com.jimbonlemu.whacchudoin.data.network.response.ResponseState
import com.jimbonlemu.whacchudoin.data.network.services.AuthService
import com.jimbonlemu.whacchudoin.utils.PreferenceManager
import com.jimbonlemu.whacchudoin.utils.koinModules
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.koin.core.context.GlobalContext.loadKoinModules
import org.koin.core.context.unloadKoinModules

class AuthRepository(
    private val authService: AuthService,
    private val preferenceManager: PreferenceManager
)  {
     fun login(loginDto: LoginRequest): Flow<ResponseState<LoginResponse>> = flow {
        try {
            emit(ResponseState.Loading)
            val response = authService.login(loginDto.email, loginDto.password)
            if (response.error) {
                emit(ResponseState.Error(response.message))
            } else {
                val loginResult = response.loginResult
                preferenceManager.setLoginPrefs(loginResult)
                reloadKoinModules()
                emit(ResponseState.Success(response))
            }
        } catch (e: Exception) {
            e.printStackTrace()
            emit(ResponseState.Error(e.message.toString()))
        }
    }

     fun register(registerDTO: RegisterRequest): Flow<ResponseState<RegisterResponse>> = flow {
        try {
            emit(ResponseState.Loading)
            val response = authService.register(registerDTO.name, registerDTO.email, registerDTO.password)
            if (response.error) {
                emit(ResponseState.Error(response.message))
            } else {
                emit(ResponseState.Success(response))
            }
        } catch (e: Exception) {
            e.printStackTrace()
            emit(ResponseState.Error(e.message.toString()))
        }
    }

     fun logout(): Boolean {
        return try {
            preferenceManager.clearAllPreferences()
            reloadKoinModules()
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    private fun reloadKoinModules() {
        unloadKoinModules(koinModules)
        loadKoinModules(koinModules)
    }
}