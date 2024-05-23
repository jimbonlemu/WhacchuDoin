package com.jimbonlemu.whacchudoin.di.feature

import com.jimbonlemu.whacchudoin.data.network.repository.AuthRepository
import com.jimbonlemu.whacchudoin.data.network.repository.AuthRepositoryImplementation
import org.koin.dsl.module

val authModule = module {
    single<AuthRepository> { AuthRepositoryImplementation(get(), get()) }
}