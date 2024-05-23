package com.jimbonlemu.whacchudoin.data.network.repository

import com.jimbonlemu.whacchudoin.data.network.auth.LoginRequest
import com.jimbonlemu.whacchudoin.data.network.auth.LoginResponse
import com.jimbonlemu.whacchudoin.data.network.auth.RegisterRequest
import com.jimbonlemu.whacchudoin.data.network.auth.RegisterResponse
import com.jimbonlemu.whacchudoin.data.network.response.ResponseState
import com.jimbonlemu.whacchudoin.utils.PreferenceManager
import com.jimbonlemu.whacchudoin.utils.koinModules
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.koin.core.context.GlobalContext.loadKoinModules
import org.koin.core.context.unloadKoinModules

class AuthRepositoryImplementation(
    private val authService: AuthService,
    private val preferenceManager: PreferenceManager
) : AuthRepository {
    override fun login(dto: LoginRequest): Flow<ResponseState<LoginResponse>> = flow {
        try {
            emit(ResponseState.Loading)
            val response = authService.login(dto.email, dto.password)
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

    override fun register(dto: RegisterRequest): Flow<ResponseState<RegisterResponse>> = flow {
        try {
            emit(ResponseState.Loading)
            val response = authService.register(dto.name, dto.email, dto.password)
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

    override fun logout(): Boolean {
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