package com.rab.bit.road104.ui.elements

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import com.rab.bit.road104.model.data.catcher.Puff
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun CollisionEffect(
    modifier: Modifier = Modifier,
    smokeColor: Color = Color.White,
    onAnimationEnd: () -> Unit
) {
    var visible by remember { mutableStateOf(true) }
    val offsetY by animateDpAsState(
        targetValue = if (visible) 0.dp else (-26).dp,
        animationSpec = tween(durationMillis = 700, easing = LinearOutSlowInEasing)
    )
    val alpha by animateFloatAsState(
        targetValue = if (visible) 1f else 0f,
        animationSpec = tween(durationMillis = 700)
    )

    val puffs = remember {
        listOf(
            Puff(dx = -14f, dy = 6f, maxR = 22f, delayMs = 0),
            Puff(dx = 0f, dy = 0f, maxR = 28f, delayMs = 40),
            Puff(dx = 16f, dy = 8f, maxR = 20f, delayMs = 80),
            Puff(dx = -8f, dy = -6f, maxR = 16f, delayMs = 120),
            Puff(dx = 10f, dy = -10f, maxR = 14f, delayMs = 160),
        )
    }

    val radii: List<Animatable<Float, AnimationVector1D>> = remember {
        puffs.map { Animatable(0f) }
    }

    LaunchedEffect(Unit) {
        delay(40)
        val scope = this
        puffs.forEachIndexed { i, puff ->
            scope.launch {
                delay(puff.delayMs.toLong())
                radii[i].animateTo(
                    targetValue = 1f,
                    animationSpec = tween(durationMillis = 380, easing = LinearOutSlowInEasing)
                )
            }
        }
        delay(420)
        visible = false
        delay(320)
        onAnimationEnd()
    }

    Box(modifier = modifier) {
        Canvas(
            modifier = Modifier
                .align(Alignment.Center)
                .offset(y = offsetY)
                .size(72.dp) // общий бокс эффекта
                .graphicsLayer { this.alpha = alpha }
        ) {
            // Рисуем клубы: увеличиваем радиус и чуть «разносим» вверх по мере раскрытия
            puffs.forEachIndexed { i, puff ->
                val progress = radii[i].value.coerceIn(0f, 1f)
                val rPx = (puff.maxR.dp.toPx()) * progress
                val lift = (6.dp.toPx()) * progress // небольшой подъём каждого пуха
                drawCircle(
                    color = smokeColor.copy(alpha = 0.9f - 0.6f * progress),
                    radius = rPx,
                    center = center + Offset(
                        x = puff.dx.dp.toPx(),
                        y = puff.dy.dp.toPx() - lift
                    )
                )
            }
        }
    }
}
