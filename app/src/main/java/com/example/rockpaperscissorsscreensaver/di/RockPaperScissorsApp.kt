package com.example.rockpaperscissorsscreensaver.di

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class RockPaperScissorsApp: Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@RockPaperScissorsApp)
            modules(
                appModule
            )
        }
    }
}