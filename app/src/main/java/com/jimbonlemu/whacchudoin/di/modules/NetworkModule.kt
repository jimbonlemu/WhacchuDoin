package com.jimbonlemu.whacchudoin.di.modules

import com.jimbonlemu.whacchudoin.BuildConfig
import com.jimbonlemu.whacchudoin.data.libs.HeaderInterceptor
import com.jimbonlemu.whacchudoin.data.network.repository.AuthService
import com.jimbonlemu.whacchudoin.utils.PreferenceManager
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


val networkModule = module {
    single {
        OkHttpClient.Builder()
            .addInterceptor(getHeaderInterceptor(get()))
            .addInterceptor(if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
            } else {
                HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.NONE)
            })
            .build()
    }
    single {
        Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(get())
            .build()
    }
    single { provideAuthService(get()) }
//    single { provideStoryService(get()) }
}

private fun getHeaderInterceptor(preferenceManager: PreferenceManager): Interceptor {
    val headers = HashMap<String, String>()
    headers["Content-Type"] = "application/json"

    return HeaderInterceptor(headers, preferenceManager)
}

fun provideAuthService(retrofit: Retrofit): AuthService = retrofit.create(AuthService::class.java)
