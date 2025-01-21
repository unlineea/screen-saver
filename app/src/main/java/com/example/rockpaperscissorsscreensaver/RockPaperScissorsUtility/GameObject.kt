package com.example.rockpaperscissorsscreensaver.RockPaperScissorsUtility

import androidx.compose.ui.geometry.Offset

enum class GameObjectType {
    ROCK, PAPER, SCISSORS
}

data class GameObject(
    var position: Offset,
    var direction: Offset,
    val type: GameObjectType
) {

    private val radius = 120f

    fun bounce(screenHeight: Float, screenWidth: Float, realHeight: Int, realWidth: Int) {

        var newDirection = direction

        val baseTop = 0
        val baseBottom = screenHeight
        val baseStart = 0
        val baseEnd = screenWidth

        val widthShrink = baseEnd - realWidth
        val heightShrink = baseBottom - realHeight

        val currentStart = baseStart + widthShrink
        val currentEnd = baseEnd - widthShrink
        val currentTop = baseTop + heightShrink
        val currentBottom = baseBottom - heightShrink

        if (position.x < currentStart) {
            if (newDirection.x > 0) else newDirection = newDirection.copy(x = -newDirection.x)
        }
        if (position.x > currentEnd - radius) {
            if (newDirection.x < 0) else newDirection = newDirection.copy(x = -newDirection.x)
        }

        if (position.y < currentTop) {
            if (newDirection.y > 0) else newDirection = newDirection.copy(y = -newDirection.y)
        }
        if (position.y > currentBottom - radius) {
            if (newDirection.y < 0) else newDirection = newDirection.copy(y = -newDirection.y)
        }

        direction = newDirection
    }

    // detect crash is done in the viewModel
    // this crash function only shows which object needs to be removed
    fun crash(other: GameObject): GameObject? {
        return when {
            this.type == GameObjectType.ROCK && other.type == GameObjectType.SCISSORS -> other
            this.type == GameObjectType.ROCK && other.type == GameObjectType.PAPER -> this
            this.type == GameObjectType.SCISSORS && other.type == GameObjectType.PAPER -> other
            this.type == GameObjectType.SCISSORS && other.type == GameObjectType.ROCK -> this
            this.type == GameObjectType.PAPER && other.type == GameObjectType.ROCK -> other
            this.type == GameObjectType.PAPER && other.type == GameObjectType.SCISSORS -> this
            else -> null // they are the same just bounce off
        }
    }
}
