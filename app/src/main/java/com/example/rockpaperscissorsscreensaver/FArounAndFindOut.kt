package com.example.rockpaperscissorsscreensaver

import android.util.Log
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun FAAFO() {
    val localDensity = LocalDensity.current.density
    val localWidth = LocalConfiguration.current.screenWidthDp
    val localHeight = LocalConfiguration.current.screenHeightDp
    val realWidth = with(LocalDensity.current) {(localWidth * localDensity).toDp()}
    val realHeight = with(LocalDensity.current) {(localHeight * localDensity).toDp()}


//    val localWidthDp = with(LocalDensity.current) {localWidth.toDp()}
//    val localHeightDp = with(LocalDensity.current) {localHeight.toDp()}

    var heightState by remember {
        mutableStateOf(realHeight)
    }
    val height by animateDpAsState(
        targetValue = heightState,
        tween(
            durationMillis = 3000
        )
    )

    var widthState by remember {
        mutableStateOf(realWidth)
    }
    val width by animateDpAsState(
        targetValue = widthState,
        tween(
            durationMillis = 3000
        )
    )

    var paddingState by remember {
        mutableStateOf(0.dp)
    }
    val padding by animateDpAsState(
        targetValue = paddingState,
        tween(
            durationMillis = 3000
        )
    )

    Box(modifier = Modifier.fillMaxSize()) {
        Log.d("MainBox", "recomposing $width")
        Column(modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center) {
            Row(modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center) {
                Box(
                    modifier = Modifier
                        .padding(top = 0.dp)
                        .padding(padding)
                        .height(height)
                        .width(width)
                        .background(Color.Red),
                ) {

                    Log.d("InnerBox", "recomposing $width")
                    Column(
                        modifier = Modifier
                            .fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Bottom
                    ) {
                        Row {
                            Button(
                                onClick = { widthState -= 50.dp },
                                modifier = Modifier.weight(1f)
                            ) {
                                Text(text = "width")
                            }
                            Button(
                                onClick = { heightState -= 50.dp },
                                modifier = Modifier.weight(1f)
                            ) {
                                Text(text = "height")
                            }
                        }
                    }
                }

            }
        }
    }
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun FAAFOPreview() {
    FAAFO()
}
