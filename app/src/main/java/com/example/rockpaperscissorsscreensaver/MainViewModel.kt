package com.example.rockpaperscissorsscreensaver

import android.os.CountDownTimer
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rockpaperscissorsscreensaver.CirclesUtility.Circle
import com.example.rockpaperscissorsscreensaver.RockPaperScissorsUtility.GameObject
import com.example.rockpaperscissorsscreensaver.RockPaperScissorsUtility.GameObjectType
import kotlinx.coroutines.launch
import kotlin.math.sqrt
import kotlin.random.Random

enum class GameState {
    BASIC,
    RACE,
    FINISHED
}

class MainViewModel: ViewModel() {

    var mode = mutableStateOf("RPS")
    var restartTrigger = mutableStateOf(false)
    var winnerType = mutableStateOf<GameObjectType>(GameObjectType.ROCK)

    // this state is for handling reset animation snappiness
    var isReset = mutableStateOf(false)

    private var initializedMode: String? = null

    fun initializeModeIfNeeded(mode: String) {
        if (initializedMode != mode) {
            when (mode) {
                "RPS" -> {
                    Log.d("MainViewModel", "RPS initialized")
                    startRockPaperScissors()
                }
                "BSS" -> {
                    Log.d("MainViewModel", "BSS initialized")
                    startCircles()
                }
            }
            initializedMode = mode
        }
    }

    fun reinitializeCurrentMode() {
        finalHeight = baseHeight
        finalWidth = baseWidth
        when (initializedMode) {
            "RPS" -> startRockPaperScissors()
            "BSS" -> startCircles()
        }
    }

    fun restartGameMode() {
        Log.d("MainViewModel", "restartTriggerBefore: ${restartTrigger.value}")
        cancelTimer()
        isReset.value = true
        restartTrigger.value = !restartTrigger.value
        Log.d("MainViewModel", "restartTriggerAfter: ${restartTrigger.value}")
    }

    fun shrink() {
        finalHeight -= 50
    }

    val screenHeightCompensation = 50.dp
    /* this is the real screen size (rather base box size) and it wont change
     this is used to calculate real time screen shrink to be used in bounce function. */
    private var baseHeight by  mutableIntStateOf(0)
    private var baseWidth by  mutableIntStateOf(0)

    /* this is the expected screen size that is updated to the final expected
     size and it is used in animated states in our screen composable. */
    var finalHeight by mutableIntStateOf(0)
    var finalWidth by mutableIntStateOf(0)

    /* this is our real time size and it is used to calculate the realtime shrink
     of the screen, it is used in bounce function. */
    var realHeight by mutableIntStateOf(0)
    var realWidth by mutableIntStateOf(0)

    var shrinkAmount = 0

    var gameState by mutableStateOf<GameState>(GameState.BASIC)

    @Composable
    fun ScreenSize() {
        Log.d("MainViewModel.screenSize", "MainViewModel.screenSize")
        if (gameState != GameState.BASIC) return
        Log.d("MainViewModel.screenSize", "init/height/width:$finalHeight,$finalWidth")
        val density = LocalDensity.current
        val conf = LocalConfiguration.current
        val height = with(density) {conf.screenHeightDp}
        val width = with(density) {conf.screenWidthDp}
        // set temporary screen height
        finalHeight = height
        finalWidth = width
        // set base screen size
        baseHeight = height
        baseWidth = width
    }

    fun changeMode() {
        if (mode.value == "RPS") mode.value = "BSS"
        else if (mode.value == "BSS") mode.value = "RPS"
    }

    val circles = mutableStateListOf<Circle>()

    val rockPaperScissorsList = mutableStateListOf<GameObject>()
    var pauseState by mutableStateOf(false)

    // this functions calls move, bounce, crash functions
    fun updateCircles(screenHeight: Float, screenWidth: Float) {
        for (i in circles.indices) {
            // move
            val offset = circles[i].direction
            circles[i] = circles[i].copy(position = circles[i].position + offset)
        }

        circles.forEach {
            // Bounce
            it.bounce(screenHeight, screenWidth)
        }

        // crash implemented here not used because of balls glitching together
        // two balls that have the same general direction keep hitting each other

//        for(i in circles.indices) {
//            //
//            val index = i
//            for (j in (index+1)until circles.size) {
//                if (circles[i].crash(circles[j])) {
//                    // they crashed
//                    // switch directions
//                    var tempDirection = circles[i].direction
//                    if (((circles[i].direction.x * circles[j].direction.x) > 0) &&
//                        ((circles[i].direction.y * circles[j].direction.y) > 0)) {
//                        tempDirection = Offset(
//                            circles[i].direction.x.times(-1),
//                            circles[i].direction.y.times(-1))
//                    }
//
//                    circles[i].direction = circles[j].direction
//                    circles[j].direction = tempDirection
//                }
//            }
//        }
    }

    // circles code pile

    private fun startCircles(startingCount: Int? = 30) {

        clearCirclesList()
        clearRPSList()

        val directionsList = listOf(-5, 5)
        // this should initiate 5 circles
        for (i in 1..startingCount!!) {
            val randomPositionX = Random.nextInt(0,1050)
            val randomPositionY = Random.nextInt(0,2254)
            val randomDirectionX = directionsList[Random.nextInt(directionsList.size)]
            Log.d("MainViewModel", "directionX: $randomDirectionX")
            val randomDirectionY = directionsList[Random.nextInt(directionsList.size)]
            Log.d("MainViewModel", "directionY: $randomDirectionY")

            Log.d("MainViewModel", "px:$randomPositionX, py$randomPositionY, dx:$randomDirectionX, py:$randomDirectionY")
            circles.add(
                Circle(
                    position = Offset(randomPositionX.toFloat(),randomPositionY.toFloat()),
                    direction = Offset(randomDirectionX.toFloat(),randomDirectionY.toFloat()),
                    color = Random.nextLong()
                )
            )
        }
        Log.d("MainViewModel", "5 circles have been made start.fun")
        Log.d("MainViewModel", "$circles")
    }

    private fun clearCirclesList() {
        circles.clear()
    }

    // rock paper scissors code pile

    fun updateRockPaperScissors(screenHeight: Float = realHeight.toFloat(), screenWidth: Float = realWidth.toFloat()) {
        moveRPS()

        bounceRPS(
            screenHeight = screenHeight,
            screenWidth = screenWidth
        )

        detectCrashRPS()

        checkGameState()
    }

    private fun checkGameState() {

        Log.d("MainViewModel.checkGameState", "$$gameState")
        /* definition : race state
        * race state is a state of the game
        * in which there is only 2 types of
        * game objects remaining in this condition
        * the games winner is predetermined
        * (e.g. 5 rocks and 11 scissors are remaining
        * the games winner is rocks since theres
        * no way (except timerMode) for the scissors
        * to win)*/

        /* definition : timerMode
        * timerMode is a game mode that starts
        * when game goes to race state in this mode
        * game arena shrinks (height, width, padding maybe)
        * and a count down timer is started if in this
        * period the predetermined type wins well good for him
        * else the inferior type is the winner*/

        /* this val was set to 21(hardCoded) when the
        * the whole game size was 30(hardCoded) objects.
        * well the reason it is set to 21
        * (The Pigeonhole Principle) is that if out of all
        * 30 game objects (10 of each type) only 20 (n<21)
        * is remaining now there is a possibility that one of
        * 3 game objects type has disappeared all together
        * enabling race state*/

        val raceSizeLowerBound = 21

        var hasRock = false
        var hasPaper = false
        var hasScissors = false
        var objectVarietyCount: Int

        if (rockPaperScissorsList.size < raceSizeLowerBound) {
            rockPaperScissorsList.forEach { gameObject ->
//                if (hasRock && hasPaper && hasScissors) return // the game has all three game objects no race state

                    when (gameObject.type) {
                        GameObjectType.ROCK -> hasRock = true
                        GameObjectType.PAPER -> hasPaper = true
                        GameObjectType.SCISSORS -> hasScissors = true
                    }
            }
            // counts how many game object variation (type) is remaining
            objectVarietyCount = listOf(hasPaper, hasRock, hasScissors).count { it }
            when (objectVarietyCount) {
                3 -> { setRPSGameState(state = GameState.BASIC) } // the games has all three game objects no race state
                2 -> { setRPSGameState(state = GameState.RACE) } // race state has happened call race mode function
                1 -> {setRPSGameState(state = GameState.FINISHED)} // DingDingDing we have a winner call game end function
            }
        }
    }

    private fun setRPSGameState(state: GameState) {
        // handle different game states
        // race : start timer mode or other modes if needed
        // comeBack : when inferior type wins in race mode
        // finish : declare the winner

        when(state) {
            GameState.BASIC -> {
                gameState = GameState.BASIC
            }
            GameState.RACE -> if (gameState == GameState.RACE) {
                // do nothing
            } else {
                startTimer()
                isReset.value = false
                shrinkScreen()
            }
            GameState.FINISHED -> {
                cancelTimer()
                // declare winner
                winnerType.value = checkUnderDog()
            }
        }
        gameState = state
    }

    private fun shrinkScreen() {

        // calculate how much you want to shrink the game arena
        val value: Int = 50
//        rockPaperScissorsList.size / 30 * 300
        Log.d("MainViewModel", "ShrinkAmountIs: $value")
        Log.d("MainViewModel", "ShrinkAmountIs: $value")
//        if (shrinkAmount == 0) {
//            shrinkAmount += value
//        }
        finalHeight -= 300
        finalWidth  -= 100
    }

    private fun bounceRPS(screenHeight: Float, screenWidth: Float) {
        rockPaperScissorsList.forEach {
            it.bounce(
                screenHeight = screenHeight,
                screenWidth = screenWidth,
                realHeight = realHeight,
                realWidth = realWidth
            )
        }
    }

    private fun moveRPS() {
        // this approach was causing recompositions with every individual ball move (inefficient)
//        for (i in rockPaperScissorss.indices) {
//            // move
//            val offset = rockPaperScissorss[i].direction
//            rockPaperScissorss[i] = rockPaperScissorss[i].copy(position = circles[i].position + offset)
//        }

        // now this new approach does batch update so after we move all objects a recomposition is done

        val updatedObjects = rockPaperScissorsList.map { obj ->
            obj.copy(position = obj.position + obj.direction)
        }
        rockPaperScissorsList.clear()
        rockPaperScissorsList.addAll(updatedObjects)
    }

    private fun startRockPaperScissors(
        rock: Int? = 10,
        paper: Int? = 10,
        scissors: Int? = 10
    ) {
        clearCirclesList()
        clearRPSList()

        // start initiating rocks and papers and scissors
        val directionsList = listOf(-5, 5)
        // this should initiate 5 circles
        for (i in 1..rock!!) {
            Log.d("MainViewModel", "all objects been added ${rockPaperScissorsList.size}")
            val randomPositionX = Random.nextInt(0,1050)
            val randomPositionY = Random.nextInt(0,2254)
            val randomDirectionX = directionsList[Random.nextInt(directionsList.size)]
            val randomDirectionY = directionsList[Random.nextInt(directionsList.size)]

            rockPaperScissorsList.add(
                GameObject(
                    position = Offset(randomPositionX.toFloat(),randomPositionY.toFloat()),
                    direction = Offset(randomDirectionX.toFloat(),randomDirectionY.toFloat()),
                    type = GameObjectType.ROCK
                )
            )
        }

        for (i in 1..paper!!) {
            Log.d("MainViewModel", "all objects been added ${rockPaperScissorsList.size}")
            val randomPositionX = Random.nextInt(0,1050)
            val randomPositionY = Random.nextInt(0,2254)
            val randomDirectionX = directionsList[Random.nextInt(directionsList.size)]
            val randomDirectionY = directionsList[Random.nextInt(directionsList.size)]

            rockPaperScissorsList.add(
                GameObject(
                    position = Offset(randomPositionX.toFloat(),randomPositionY.toFloat()),
                    direction = Offset(randomDirectionX.toFloat(),randomDirectionY.toFloat()),
                    type = GameObjectType.PAPER
                )
            )
        }

        for (i in 1..scissors!!) {
            Log.d("MainViewModel", "all objects been added ${rockPaperScissorsList.size}")
            val randomPositionX = Random.nextInt(0,1050)
            val randomPositionY = Random.nextInt(0,2254)
            val randomDirectionX = directionsList[Random.nextInt(directionsList.size)]
            val randomDirectionY = directionsList[Random.nextInt(directionsList.size)]

            rockPaperScissorsList.add(
                GameObject(
                    position = Offset(randomPositionX.toFloat(),randomPositionY.toFloat()),
                    direction = Offset(randomDirectionX.toFloat(),randomDirectionY.toFloat()),
                    type = GameObjectType.SCISSORS
                )
            )
        }

        Log.d("MainViewModel", "all objects been added ${rockPaperScissorsList.size}")
    }

    private fun clearRPSList() {
        rockPaperScissorsList.clear()
    }

    private fun detectCrashRPS() {
        val toRemove = arrayListOf<GameObject>()
        for (i in rockPaperScissorsList.indices) {
            for (j in i + 1 until rockPaperScissorsList.size) {

                val distance = sqrt(
                            (
                            (rockPaperScissorsList[i].position.x - rockPaperScissorsList[j].position.x) * (rockPaperScissorsList[i].position.x - rockPaperScissorsList[j].position.x) +
                            ((rockPaperScissorsList[i].position.y - rockPaperScissorsList[j].position.y) * (rockPaperScissorsList[i].position.y - rockPaperScissorsList[j].position.y))
                            )
                    )

                if (distance > 100f) continue

                val object1 = rockPaperScissorsList[i]
                val object2 = rockPaperScissorsList[j]
                val loser = object1.crash(object2)
                if (loser != null) {
                    toRemove.add(loser)
                }
            }
        }
        rockPaperScissorsList.removeAll(toRemove)
    }

    private fun checkUnderDog(): GameObjectType {
        var underDog: GameObjectType? = null

        // find under dog
        rockPaperScissorsList.forEach { gameObject ->
                when(gameObject.type) {
                    GameObjectType.ROCK -> if (underDog != GameObjectType.SCISSORS) underDog = gameObject.type
                    GameObjectType.PAPER -> if (underDog != GameObjectType.ROCK) underDog = gameObject.type
                    GameObjectType.SCISSORS -> if (underDog != GameObjectType.PAPER) underDog = gameObject.type
                    null -> underDog = gameObject.type
                }
        }
        return underDog!!
    }

    // timer code pile

    private val _timeRemaining = MutableLiveData<Long>()
    val timeRemaining: LiveData<Long> get() = _timeRemaining

    private val _timerFinished = MutableLiveData<Boolean>()
    val timerFinished: LiveData<Boolean> get() = _timerFinished

    private val timer = object : CountDownTimer(5000, 1000) {
        override fun onTick(millisUntilFinished: Long) {
            Log.d("TimerViewModel", "tick")
            _timeRemaining.value = millisUntilFinished / 1000
        }

        override fun onFinish() {
            Log.d("TimerViewModel", "finish")
            setRPSGameState(GameState.FINISHED)
            _timerFinished.value = true

            val underDog = checkUnderDog()
            winnerType.value = underDog
            viewModelScope.launch {
                deleteButUnderDog(underDog = underDog)
            }
        }
    }

    private fun deleteButUnderDog(underDog: GameObjectType) {
        rockPaperScissorsList.forEach{ gameObject ->
            if(gameObject.type != underDog) {
                rockPaperScissorsList.remove(gameObject)
            }
        }
    }
    private fun startTimer() {
        timer.start()
    }

    private fun cancelTimer() {
        timer.cancel()
    }

    override fun onCleared() {
        super.onCleared()
        timer.cancel() // Cancel timer to avoid memory leaks
    }
}