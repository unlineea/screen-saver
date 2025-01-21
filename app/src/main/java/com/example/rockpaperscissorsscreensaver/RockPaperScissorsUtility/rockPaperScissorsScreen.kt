package com.example.rockpaperscissorsscreensaver.RockPaperScissorsUtility

import android.util.Log
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.rockpaperscissorsscreensaver.GameState
import com.example.rockpaperscissorsscreensaver.MainViewModel
import com.example.rockpaperscissorsscreensaver.R
import com.example.rockpaperscissorsscreensaver.ui.CustomComponents.RockPaperScissorsButton
import com.example.rockpaperscissorsscreensaver.ui.theme.brunoAceFontFamily
import org.koin.androidx.compose.koinViewModel
import kotlin.math.log

@Composable
fun rockPaperScissorsScreen(
    viewModel: MainViewModel = koinViewModel()
) {

    val isReset = viewModel.isReset.value

    var heightState by remember {
//        mutableStateOf(viewModel.finalHeight.dp + viewModel.screenHeightCompensation)
        mutableStateOf(viewModel.finalHeight.dp)
    }
    LaunchedEffect(viewModel.finalHeight) {
        heightState = viewModel.finalHeight.dp
    }
    val height by animateDpAsState(
        targetValue = heightState,
        animationSpec = if (isReset) {
            tween(
                durationMillis = 0
            )
        } else {
            tween(
                durationMillis = 6000
            )
        }
    )

    var widthState by remember {
        mutableStateOf(viewModel.finalWidth.dp)
    }
    LaunchedEffect(viewModel.finalWidth) {
        widthState = viewModel.finalWidth.dp
    }
    val width by animateDpAsState(
        targetValue = widthState,
        animationSpec = if (isReset) {
            tween(
                durationMillis = 0
            )
        } else {
            tween(
                durationMillis = 6000
            )
        }
    )

    val time by viewModel.timeRemaining.observeAsState()

    Box(modifier = Modifier
        .fillMaxSize()
        .background(Color.Black)
        .clickable { viewModel.pauseState = !viewModel.pauseState }) {
        if (time != null) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 120.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "SHOW DOWN !",
                    color = Color(0xFFFFA500),
                    fontFamily = brunoAceFontFamily,
                    fontSize = 30.sp
                )
                if (time!! > 0 && viewModel.gameState == GameState.RACE) {
                    Text(
                        text = "- $time -",
                        color = Color(0xFFFFA500),
                        fontFamily = brunoAceFontFamily,
                        fontSize = 30.sp
                    )
                } else {
                    Text(
                        text = when(viewModel.winnerType.value) {
                            GameObjectType.PAPER -> "winner is PAPER"
                            GameObjectType.ROCK -> "winner is ROCK"
                            GameObjectType.SCISSORS -> "winner is SCISSORS"
                        }
                        ,
                        color = Color(0xFFFFA500),
                        fontFamily = brunoAceFontFamily,
                        fontSize = 25.sp
                    )
                }
            }
        }
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {

                Box(
                    modifier = Modifier
                        .padding(top = 5.dp)
                        .width(width)
                        .height(height)
                        .background(Color(0xFFFFA500))
                ) {
                    val density = LocalDensity.current
                    viewModel.realWidth = with(density) {width.toPx().toInt()}
                    viewModel.realHeight = with(density) {height.toPx().toInt()}
                    viewModel.rockPaperScissorsList.forEach { gameObj ->
                        ObjectComposable(gameObject = gameObj)
                    }
                }
            }
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
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color(0xFF2A3B5D)),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    RockPaperScissorsButton(
                        modifier = Modifier.fillMaxWidth(.7f),
                        onClick = { viewModel.pauseState = false },
                        text = "Continue"
                    )
                    RockPaperScissorsButton(
                        modifier = Modifier.fillMaxWidth(.7f),
                        onClick = {
                            viewModel.changeMode()
                            viewModel.pauseState = false },
                        text = "Switch Theme"
                    )
                    RockPaperScissorsButton(
                        modifier = Modifier.fillMaxWidth(.7f),
                        onClick = {
                            viewModel.restartGameMode()
                            viewModel.pauseState = false },
                        text = "Start Over"
                    )
                }
            }
        }
    }
}

@Composable
fun ObjectComposable(gameObject: GameObject) {
    val positionXDp = with(LocalDensity.current) {gameObject.position.x.toDp()}
    val positionYDp = with(LocalDensity.current) {gameObject.position.y.toDp()}
    
    when (gameObject.type) {
        GameObjectType.ROCK -> Icon(
            imageVector = ImageVector.vectorResource(id = R.drawable.rock),
            contentDescription = "",
            Modifier.offset(positionXDp, positionYDp))
        
        GameObjectType.PAPER -> Icon(
            imageVector = ImageVector.vectorResource(id = R.drawable.paper),
            contentDescription = "",
            Modifier.offset(positionXDp, positionYDp))
        
        GameObjectType.SCISSORS -> Icon(
            imageVector = ImageVector.vectorResource(id = R.drawable.scissors),
            contentDescription = "",
            Modifier.offset(positionXDp, positionYDp))
    }
}

