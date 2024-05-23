package com.jimbonlemu.whacchudoin.data.network.response

sealed class ResponseState<out R> {
    data class Success<out T>(val data: T) : ResponseState<T>()
    data class Error(val errorMessage: String) : ResponseState<Nothing>()
    data object Loading : ResponseState<Nothing>()
}