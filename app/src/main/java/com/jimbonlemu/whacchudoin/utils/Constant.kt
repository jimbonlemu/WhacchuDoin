package com.jimbonlemu.whacchudoin.utils

import com.jimbonlemu.whacchudoin.di.feature.authModule
import com.jimbonlemu.whacchudoin.di.feature.storyModule
import com.jimbonlemu.whacchudoin.di.modules.networkModule
import com.jimbonlemu.whacchudoin.di.modules.preferenceModule
import com.jimbonlemu.whacchudoin.di.modules.viewModelModule


const val PREFERENCE_NAME = "whaccudoin.pref"
const val KEY_TOKEN = "key.token"
const val KEY_NAME = "key.name"

val koinModules = listOf(
    networkModule,
    preferenceModule,
    viewModelModule,
    authModule,
    storyModule,
)

const val MAX_FILE_SIZE = 1000000