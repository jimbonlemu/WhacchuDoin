package com.jimbonlemu.whacchudoin.data.network.dto

import com.google.gson.annotations.SerializedName
import com.jimbonlemu.whacchudoin.data.network.response.ApiResponse

data class LoginRequest(
    val email: String,
    val password: String
)

data class LoginResponse(
    @field:SerializedName("loginResult")
    val loginResult: LoginResult
) : ApiResponse()

data class LoginResult(

    @field:SerializedName("name")
    val name: String? = null,

    @field:SerializedName("userId")
    val userId: String? = null,

    @field:SerializedName("token")
    val token: String? = null
)

