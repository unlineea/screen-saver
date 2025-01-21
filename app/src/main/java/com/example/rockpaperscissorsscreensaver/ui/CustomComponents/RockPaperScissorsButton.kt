package com.example.rockpaperscissorsscreensaver.ui.CustomComponents

import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.example.rockpaperscissorsscreensaver.R
import com.example.rockpaperscissorsscreensaver.ui.theme.RockPaperScissorsScreenSaverTheme
import com.example.rockpaperscissorsscreensaver.ui.theme.brunoAceFontFamily

@Composable
fun RockPaperScissorsButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    containerColor: Long = 0xFFFFA500,
    textColor: Long = 0xFF2A3B5D,
    text: String = "not set yet"
) {
    TextButton(
        onClick = { onClick() },
        colors = ButtonDefaults.buttonColors().copy(containerColor = Color(containerColor))
        , modifier = modifier
    ) {
        Text(
            text = text,
            fontFamily = brunoAceFontFamily,
            fontWeight = FontWeight.ExtraBold,
            color = Color(textColor)
        )
    }
}

@Preview
@Composable
private fun RockPaperScissorsButtonPreview() {
    RockPaperScissorsScreenSaverTheme {
        RockPaperScissorsButton(onClick = {  })
    }
}