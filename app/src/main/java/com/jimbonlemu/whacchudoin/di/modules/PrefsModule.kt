package com.jimbonlemu.whacchudoin.di.modules

import com.jimbonlemu.whacchudoin.utils.PreferenceManager
import org.koin.dsl.module

val preferenceModule = module {
    single { PreferenceManager(get()) }
}