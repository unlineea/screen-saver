package com.example.rockpaperscissorsscreensaver.di

import com.example.rockpaperscissorsscreensaver.MainViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val appModule = module {
    viewModelOf(::MainViewModel)
}