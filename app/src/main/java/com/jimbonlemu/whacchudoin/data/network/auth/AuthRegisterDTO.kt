package com.jimbonlemu.whacchudoin.data.network.auth

import com.jimbonlemu.whacchudoin.data.network.response.ApiResponse

data class RegisterRequest(
    val name: String,
    val email: String,
    val password: String
)

class RegisterResponse : ApiResponse()