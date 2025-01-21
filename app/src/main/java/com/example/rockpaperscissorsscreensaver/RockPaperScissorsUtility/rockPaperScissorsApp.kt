package com.example.rockpaperscissorsscreensaver.RockPaperScissorsUtility

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import com.example.rockpaperscissorsscreensaver.MainViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun rockPaperScissorsApp(
    viewModel: MainViewModel = koinViewModel()
) {
    val localDensity = LocalDensity.current.density
    val screenWidthDp = LocalConfiguration.current.screenWidthDp.toFloat()
    val screenHeightDp = LocalConfiguration.current.screenHeightDp.toFloat()
    val screenWidthFloat = (screenWidthDp * localDensity)
    val screenHeightFloat = (screenHeightDp * localDensity)


    LaunchedEffect(key1 = Unit) {
        while (true) {
            kotlinx.coroutines.delay(16L) // ~60 frames
            if (viewModel.pauseState) continue
            viewModel.updateRockPaperScissors()
        }
    }

    // showGameObjects
    rockPaperScissorsScreen()
}