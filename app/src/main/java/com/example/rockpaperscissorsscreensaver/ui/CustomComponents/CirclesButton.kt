package com.example.rockpaperscissorsscreensaver.ui.CustomComponents

import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.example.rockpaperscissorsscreensaver.ui.theme.RockPaperScissorsScreenSaverTheme
import com.example.rockpaperscissorsscreensaver.ui.theme.bangersFontFamily

@Composable
fun CirclesButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    containerColor: Long = 0xFFB8B8B8,
    textColor: Long = 0xFF333333,
    text: String = "not set yet"
) {
    TextButton(
        onClick = { onClick() },
        colors = ButtonDefaults.buttonColors().copy(containerColor = Color(containerColor))
        , modifier = modifier
    ) {
        Text(
            text = text,
            fontFamily = bangersFontFamily,
            fontWeight = FontWeight.Medium,
            fontSize = 25.sp,
            color = Color(textColor)
        )
    }
}

@Preview
@Composable
private fun CirclesButtonPreview() {
    RockPaperScissorsScreenSaverTheme {
        CirclesButton(onClick = {  })
    }
}