package com.example.rockpaperscissorsscreensaver.CirclesUtility

import android.util.Log
import androidx.compose.ui.geometry.Offset
import kotlin.math.sqrt

data class Circle(
    var position: Offset,
    var direction: Offset,
    var color: Long
) {

    val radius: Float = 40.0F

    fun bounce(screenHeight: Float, screenWidth: Float) {
        if(position.x > screenWidth || position.x < 0) {
            if (position.y > screenHeight + 80 || position.y < 0) {
                // bounce off corner
                Log.d("Circle.BOUNCED", "bounce off corner")
//                direction.copy(x = -(direction.x), y = -(direction.y))
                direction = Offset(-direction.x, -direction.y)
            } else {
                // bounce off walls
                Log.d("Circle.BOUNCED", "bounce off walls")
//                direction.copy(x = -(direction.x))
                direction = Offset(-direction.x, direction.y)
            }
        } else if(position.y > screenHeight + 80 || position.y < 0) {
            // bounce off ceiling or bottom
            Log.d("Circle.BOUNCED", "bounce off ceiling or bottom")
//            direction.copy(y = (-direction.y))
            direction = Offset(direction.x, -direction.y)
        } else {
            Log.d("NO BOUNCE", "NO BOUNCE")
        }
    }

    fun crash(other: Circle): Boolean {

        val distance = sqrt(
            ((position.x - other.position.x) * (position.x - other.position.x)) +
                    ((position.y - other.position.y) * (position.y - other.position.y))
        )

        if (distance <= (radius * 2)+5){
            // crash has happened
            return true
        }
        return false
    }

    fun move() {
        Log.d("Circle.Move.fun", "X1:${position.x}, Y1:${position.y}")
        position.copy(
            x = position.x + direction.x,
            y = position.y + direction.y
        )
        Log.d("Move.fun", "X2:${position.x}, Y2:${position.y}")
    }
}