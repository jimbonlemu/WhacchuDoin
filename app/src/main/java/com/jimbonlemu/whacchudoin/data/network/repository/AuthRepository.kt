package com.jimbonlemu.whacchudoin.data.network.repository

import com.jimbonlemu.whacchudoin.data.network.auth.LoginRequest
import com.jimbonlemu.whacchudoin.data.network.auth.LoginResponse
import com.jimbonlemu.whacchudoin.data.network.auth.RegisterRequest
import com.jimbonlemu.whacchudoin.data.network.auth.RegisterResponse
import com.jimbonlemu.whacchudoin.data.network.response.ResponseState
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    fun login(loginDTO: LoginRequest): Flow<ResponseState<LoginResponse>>
    fun register(registerDTO: RegisterRequest): Flow<ResponseState<RegisterResponse>>
    fun logout(): Boolean
}