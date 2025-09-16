package com.rab.bit.road104.ui.screens.games

import android.content.pm.ActivityInfo
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import com.rab.bit.road104.MainActivity
import com.rab.bit.road104.R
import com.rab.bit.road104.model.data.catcher.FallingElement
import com.rab.bit.road104.model.data.catcher.HitFx
import com.rab.bit.road104.navigation.ScreenRoutes
import com.rab.bit.road104.ui.elements.Background
import com.rab.bit.road104.ui.elements.CollisionEffect
import com.rab.bit.road104.ui.elements.DefaultButton
import com.rab.bit.road104.ui.theme.DefFont
import com.rab.bit.road104.util.lockOrientation
import kotlinx.coroutines.delay
import kotlin.math.PI
import kotlin.math.sin
import kotlin.random.Random

@Composable
fun CatcherScreen(
    navController: NavController,
    innerPadding: PaddingValues,
    viewModel: GameViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val activity = context as? MainActivity
    activity?.lockOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)

    var gameOver by remember { mutableStateOf(false) }
    var scoreRes by remember { mutableIntStateOf(0) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
    ) {
        Background()
        if (gameOver) {
            GameOverScreen(navController, scoreRes)
        } else {
            MixedCatcherGame(
                navController,
                viewModel.getSpeed(),
                listOf(
                    painterResource(id = R.drawable.catch_1),
                    painterResource(id = R.drawable.catch_2)
                ),
                listOf(
                    painterResource(id = R.drawable.escape)
                ),
                painterResource(id = R.drawable.platforma),
                painterResource(R.drawable.btn)
            ) { finalScore ->
                gameOver = true
                scoreRes = finalScore
                viewModel.saveResult(finalScore)
            }
        }
    }
}

@Composable
fun MixedCatcherGame(
    navController: NavController,
    speed: Float,
    catchableDrawables: List<Painter>,
    avoidableDrawables: List<Painter>,
    platformDrawable: Painter,
    scoreBg: Painter,
    onGameOver: (finalScore: Int) -> Unit
) {
    var score by remember { mutableIntStateOf(0) }
    var platformPosition by remember { mutableFloatStateOf(0f) }
    var lives by remember { mutableIntStateOf(3) }

    val fallingElements = remember { mutableStateListOf<FallingElement>() }

    val screenWidth = with(LocalDensity.current) { LocalConfiguration.current.screenWidthDp.dp }
    val screenHeight = LocalConfiguration.current.screenHeightDp.dp
    val elementSize = 48.dp
    val platformHeight = 100.dp
    val platformWidth = 90.dp

    var timeSec by remember { mutableFloatStateOf(0f) }
    var nextFxId by remember { mutableLongStateOf(1L) }
    val hitFxList = remember { mutableStateListOf<HitFx>() }

    // Элемент появляется каждую секунду
    LaunchedEffect(Unit) {
        while (true) {
            delay(1000)

            val amp = Random.nextFloat() * 16f + 16f
            val speedRad = Random.nextFloat() * 2f + 2.5f
            val phase = Random.nextFloat() * (2f * PI.toFloat())

            val minX = amp
            val maxX = screenWidth.value - 100f - amp
            val randomX = (Random.nextFloat() * (maxX - minX)).coerceAtLeast(0f) + minX

            val all = catchableDrawables.map { it to true } + avoidableDrawables.map { it to false }
            val (drawable, isCatchable) = all.random()

            fallingElements.add(
                FallingElement(
                    x = randomX,
                    y = 0f,
                    drawable = drawable,
                    isCatchable = isCatchable,
                    swayAmp = amp,
                    swaySpeed = speedRad,
                    swayPhase = phase
                )
            )
        }
    }

    // игровой цикл
    LaunchedEffect(Unit) {
        while (true) {
            delay(16)
            timeSec += 0.016f

            val screenH = screenHeight.value
            val elemSize = elementSize.value
            val platW = platformWidth.value
            val platH = platformHeight.value

            val platformLeft = platformPosition
            val platformRight = platformPosition + platW
            val platformTop = screenH - platH
            val platformBottom = screenH

            for (i in (fallingElements.lastIndex) downTo 0) {
                val e = fallingElements[i]
                val newY = e.y + speed

                // текущее смещение по X (качание)
                val swayX = sin(timeSec * e.swaySpeed + e.swayPhase) * e.swayAmp

                val elemLeft = e.x + swayX
                val elemRight = elemLeft + elemSize
                val elemTop = newY
                val elemBottom = newY + elemSize

                // за экран
                if (elemTop > screenH) {
                    if (e.isCatchable) {
                        lives -= 1
                        if (lives <= 0) onGameOver(score)
                    } else {
                        score += 1
                    }
                    fallingElements.removeAt(i)
                    continue
                }

                // столкновение с платформой
                val intersectsX = elemRight >= platformLeft && elemLeft <= platformRight
                val intersectsY = elemBottom >= platformTop && elemTop <= platformBottom
                if (intersectsX && intersectsY) {
                    if (e.isCatchable) score += 1 else {
                        lives -= 1; if (lives <= 0) onGameOver(score)
                    }
                    val hitCenterX = (elemLeft + elemRight) / 2f
                    val hitY = platformTop

                    val isGood = e.isCatchable
                    if (isGood) score += 1 else {
                        lives -= 1
                        if (lives <= 0) onGameOver(score)
                    }

                    // создаём эффект
                    hitFxList.add(
                        HitFx(
                            id = nextFxId++,
                            x = hitCenterX,
                            y = hitY,
                            text = if (isGood) "+1" else "-1",
                            textColor = if (isGood) Color.Yellow else Color.Red
                        )
                    )

                    fallingElements.removeAt(i)
                    continue
                }

                // прошёл мимо платформы — удаляем сразу
                if (elemTop >= platformTop && !intersectsX) {
                    if (e.isCatchable) {
                        lives -= 1; if (lives <= 0) onGameOver(score)
                    } else score += 1
                    fallingElements.removeAt(i)
                    continue
                }

                // продолжаем падение
                fallingElements[i] = e.copy(y = newY)
            }
        }
    }

    // UI отрисовка
    Box(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectHorizontalDragGestures { _, dragAmount ->
                    platformPosition = (platformPosition + dragAmount).coerceIn(
                        0f,
                        screenWidth.value - platformWidth.value
                    )
                }
            }
    ) {
        fallingElements.forEach { element ->
            val swayX = sin(timeSec * element.swaySpeed + element.swayPhase) * element.swayAmp
            Image(
                painter = element.drawable,
                contentDescription = null,
                modifier = Modifier
                    .offset(x = (element.x + swayX).dp, y = element.y.dp)
                    .size(elementSize)
            )
        }

        Image(
            painter = platformDrawable,
            contentDescription = "Platform",
            contentScale = ContentScale.FillBounds,
            modifier = Modifier
                .align(Alignment.BottomStart)
                .size(platformWidth, platformHeight)
                .offset(x = platformPosition.dp)
        )

        Row(
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Image(
                painterResource(R.drawable.ic_back),
                R.drawable.ic_back.toString(),
                modifier = Modifier
                    .width(60.dp)
                    .clickable {
                        navController.popBackStack()
                    },
                contentScale = ContentScale.FillWidth
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
                (1..lives).forEach { i ->
                    Image(
                        painterResource(R.drawable.live),
                        R.drawable.live.toString(),
                        Modifier.size(32.dp)
                    )
                    Spacer(Modifier.width(4.dp))
                }
            }
            Box(
                Modifier.width(100.dp),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    scoreBg,
                    null,
                    modifier = Modifier.fillMaxWidth(),
                    contentScale = ContentScale.FillWidth
                )
                Text(
                    text = "$score",
                    color = Color.White,
                    fontSize = 24.sp,
                    fontFamily = DefFont
                )
            }
        }

        hitFxList.forEach { fx ->
            CollisionEffect(
                modifier = Modifier
                    .offset(x = fx.x.dp, y = fx.y.dp),
                smokeColor = Color.White
            ) {
                // по завершении анимации удаляем эффект
                hitFxList.removeAll { it.id == fx.id }
            }
        }
    }
}

@Composable
fun GameOverScreen(navController: NavController, scoreRes: Int) {
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painterResource(R.drawable.you_win),
                R.drawable.you_win.toString(),
                Modifier
                    .padding(horizontal = 8.dp)
                    .fillMaxWidth(0.7f),
                contentScale = ContentScale.FillWidth
            )

            Text(
                text = scoreRes.toString(),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 32.dp),
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 32.sp,
                textAlign = TextAlign.Center,
                fontFamily = DefFont
            )

            DefaultButton(modifier = Modifier.padding(8.dp), "Home") {
                navController.navigate(ScreenRoutes.HomeScreen.route)
            }
        }
    }
}
