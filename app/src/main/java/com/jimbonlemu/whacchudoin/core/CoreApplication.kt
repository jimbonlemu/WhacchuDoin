package com.jimbonlemu.whacchudoin.core

import android.app.Application
import com.jimbonlemu.whacchudoin.utils.koinModules
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.core.logger.Level

class CoreApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger(Level.NONE)
            androidContext(this@CoreApplication)
            modules(koinModules)
        }
    }

    override fun onTerminate() {
        super.onTerminate()
        stopKoin()
    }
}
