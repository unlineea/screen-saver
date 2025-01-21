package com.example.rockpaperscissorsscreensaver.CirclesUtility

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import com.example.rockpaperscissorsscreensaver.MainViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun CircleApp(
    viewModel: MainViewModel = koinViewModel()
) {

    val localDensity = LocalDensity.current.density
    val screenWidthDp = LocalConfiguration.current.screenWidthDp.toFloat()
    val screenHeightDp = LocalConfiguration.current.screenHeightDp.toFloat()
    val screenWidthFloat = (screenWidthDp * localDensity)
    val screenHeightFloat = (screenHeightDp * localDensity)

    LaunchedEffect(key1 = Unit) {
        while (true) {
            viewModel.updateCircles(screenHeight = screenHeightFloat, screenWidth = screenWidthFloat)
            kotlinx.coroutines.delay(16L) // ~60 frames
            for (i in viewModel.circles) {
                Log.d(
                    "CircleApp",
                    "${i.position.x}," +
                            "${i.position.y}," +
                            "${i.direction.x}," +
                            "${i.direction.y}\n"
                )
            }
        }
    }
    // show circles
    circleScreen()
}