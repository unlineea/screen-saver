package com.example.rockpaperscissorsscreensaver.CirclesUtility

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.rockpaperscissorsscreensaver.MainViewModel
import com.example.rockpaperscissorsscreensaver.ui.CustomComponents.CirclesButton
import org.koin.androidx.compose.koinViewModel

@Composable
fun circleScreen(
    viewModel: MainViewModel = koinViewModel()
) {

    Log.d("MainComposable", "main composable is running")

    Box(
        modifier = Modifier
            .background(Color(0xFFFFF9E6))
            .fillMaxSize()
            .clickable { viewModel.pauseState = !viewModel.pauseState }
    ) {
        viewModel.circles.forEach { circle ->
            CircleComposable(circle = circle)
        }
    }
    if(viewModel.pauseState) {
        Dialog(onDismissRequest = { viewModel.pauseState = false }) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp)
                    .padding(46.dp),
                shape = RoundedCornerShape(16.dp),
            ) {
                Column(
                    modifier = Modifier.fillMaxSize()
                        .background(Color(0xFFDCE4F2)),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CirclesButton(
                        modifier = Modifier.fillMaxWidth(.7f),
                        onClick = { viewModel.pauseState = false },
                        text = "Continue"
                    )
                    CirclesButton(
                        modifier = Modifier.fillMaxWidth(.7f),
                        onClick = {
                        viewModel.changeMode()
                        viewModel.pauseState = false
                    },
                        text = "Switch Theme"
                    )
                    CirclesButton(
                        modifier = Modifier.fillMaxWidth(.7f)
                        , onClick = {
                        viewModel.restartGameMode()
                        viewModel.pauseState = false
                    },
                        text = "Start Over"
                    )
                }
            }
        }
    }
}

@Composable
fun CircleComposable(circle: Circle) {
    val radiusDp = with(LocalDensity.current) {circle.radius.toDp()}
    val positionXDp = with(LocalDensity.current) {circle.position.x.toDp()}
    val positionYDp = with(LocalDensity.current) {circle.position.y.toDp()}

    Box(
        modifier = Modifier
            .size(radiusDp * 2) // Diameter of the circle
            .offset(
                x = positionXDp - radiusDp,
                y = positionYDp - radiusDp
            ) // Center the circle at its position
            .background(
                color = Color(circle.color),
                shape = CircleShape
            )
    )
}