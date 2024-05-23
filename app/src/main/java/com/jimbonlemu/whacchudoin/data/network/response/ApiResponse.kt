package com.jimbonlemu.whacchudoin.data.network.response

import com.google.gson.annotations.SerializedName

abstract class ApiResponse(
    @field:SerializedName("error")
    val error: Boolean = false,

    @field:SerializedName("message")
    val message: String = "",
)