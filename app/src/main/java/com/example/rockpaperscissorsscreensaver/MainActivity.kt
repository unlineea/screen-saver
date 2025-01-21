package com.example.rockpaperscissorsscreensaver

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.LaunchedEffect
import com.example.rockpaperscissorsscreensaver.CirclesUtility.CircleApp
import com.example.rockpaperscissorsscreensaver.RockPaperScissorsUtility.rockPaperScissorsApp
import com.example.rockpaperscissorsscreensaver.ui.theme.RockPaperScissorsScreenSaverTheme
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("MainActivity", "Main Activity created")
        // this flag for the app to know that this is the start of the
        // game and it is used to initiate screen size
        var startFlag = true
        super.onCreate(savedInstanceState)
        val viewModel: MainViewModel by viewModel()
//        if (viewModel.mode.value == "RPS") {
//            // rock paper scissors
////            viewModel.startRockPaperScissors()
//        } else if (viewModel.mode.value == "BSS") {
//            // ball screen saver
//            viewModel.startCircles()
//        }
        enableEdgeToEdge()
        setContent {
            Log.d("MainActivity", "main.setContent")
            RockPaperScissorsScreenSaverTheme {

//                LaunchedEffect(viewModel.mode.value, viewModel.restartTrigger.value) {
//                    Log.d("MainActivity", "MainActivity.launchedEffect")
//                    viewModel.initializeModeIfNeeded(viewModel.mode.value)
//                }
                // Separate LaunchedEffect to handle start
                LaunchedEffect(viewModel.mode.value) {
                    viewModel.initializeModeIfNeeded(viewModel.mode.value)
                    startFlag = true
                }
                // Separate LaunchedEffect to handle restarts
                LaunchedEffect(viewModel.restartTrigger.value) {
                    viewModel.reinitializeCurrentMode()
                    startFlag = true
                }

                when(viewModel.mode.value) {
                    "RPS" -> {
                        if (startFlag) {
                        viewModel.ScreenSize()
                        startFlag = !startFlag
                    }
                        rockPaperScissorsApp()
                    }
                    "BSS" -> CircleApp()
                }

//                Log.d("MainActivity", "main.appTheme")
//                if (viewModel.mode.value == "RPS") {
//
//                    // rock paper scissors app
//                    if (startFlag) {
//                        viewModel.screenSize()
//                        startFlag = !startFlag
//                    }
//                    rockPaperScissorsApp()
//                } else if (viewModel.mode.value == "BSS") {
//                    // ball screen saver
//                    CircleApp()
//                }
//                FAAFO()
            }
        }
    }
}

