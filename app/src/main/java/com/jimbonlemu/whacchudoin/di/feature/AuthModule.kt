package com.jimbonlemu.whacchudoin.di.feature

import com.jimbonlemu.whacchudoin.data.network.repository.AuthRepository
import org.koin.dsl.module

val authModule = module {
    single<AuthRepository> { AuthRepository(get(), get()) }
}