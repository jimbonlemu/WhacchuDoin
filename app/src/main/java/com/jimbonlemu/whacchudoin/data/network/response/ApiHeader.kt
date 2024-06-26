package com.jimbonlemu.whacchudoin.data.network.response

import com.jimbonlemu.whacchudoin.utils.PreferenceManager
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import java.io.IOException

class ApiHeader(
    private val requestHeaders: HashMap<String, String>,
    private val preferenceManager: PreferenceManager
) : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        mapRequestHeaders()

        val request = mapHeaders(chain)

        return chain.proceed(request)
    }

    private fun mapRequestHeaders() {
        val token = preferenceManager.getToken
        requestHeaders["Authorization"] = "Bearer $token"
    }

    private fun mapHeaders(chain: Interceptor.Chain): Request {

        val chainingBuilder = chain.request().newBuilder()

        for ((key, value) in requestHeaders) {
            chainingBuilder.addHeader(key, value)
        }
        return chainingBuilder.build()
    }

}