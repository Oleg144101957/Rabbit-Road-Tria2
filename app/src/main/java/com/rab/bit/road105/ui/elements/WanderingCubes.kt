package com.rab.bit.road105.ui.elements

import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.Easing
import androidx.compose.animation.core.InfiniteTransition
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.StartOffset
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp

@Composable
fun WanderingCubes(
    modifier: Modifier = Modifier,
    durationMillis: Int = 1800,
    size: DpSize = DpSize(60.dp, 60.dp),
    color: Color = Color.White,
) {
    val transition = rememberInfiniteTransition()

    val durationPerFraction = durationMillis / 4

    val x1Multiplier = transition.wanderingCubesTransition(
        initialValue = 1f,
        targetValue = 0f,
        durationMillisPerFraction = durationPerFraction
    )
    val y1Multiplier = transition.wanderingCubesTransition(
        initialValue = 1f,
        targetValue = 0f,
        durationMillisPerFraction = durationPerFraction,
        offsetMillis = durationPerFraction
    )
    val x2Multiplier = transition.wanderingCubesTransition(
        initialValue = 0f,
        targetValue = 1f,
        durationMillisPerFraction = durationPerFraction,
    )
    val y2Multiplier = transition.wanderingCubesTransition(
        initialValue = 0f,
        targetValue = 1f,
        durationMillisPerFraction = durationPerFraction,
        offsetMillis = durationPerFraction
    )
    val rectHeightMultiplier = transition.wanderingCubesTransition(
        initialValue = 2f,
        targetValue = 1f,
        durationMillisPerFraction = durationPerFraction / 2
    )
    val rectWidthMultiplier = transition.wanderingCubesTransition(
        initialValue = 2f,
        targetValue = 1f,
        durationMillisPerFraction = durationPerFraction / 2
    )

    val rectRotation = transition.wanderingCubesRotateTransition(
        initialValue = 0f,
        targetValue = -90f,
        durationMillisPerFraction = durationPerFraction
    )

    Canvas(modifier = modifier.size(size)) {
        val width = this.size.width
        val height = this.size.height

        val rectWidth = width / 6
        val rectHeight = height / 6

        val rectSize = Size(
            width = rectWidth * rectWidthMultiplier.value,
            height = rectHeight * rectHeightMultiplier.value
        )

        val effectiveWidth = width - rectSize.width
        val effectiveHeight = height - rectSize.height

        val x1 = x1Multiplier.value * effectiveWidth
        val y1 = y1Multiplier.value * effectiveHeight
        rotate(
            degrees = rectRotation.value,
            pivot = Offset(x1, y1) + Offset(rectSize.width / 2, rectSize.height / 2)
        ) {
            drawRect(
                color = color,
                size = rectSize,
                topLeft = Offset(x1, y1)
            )
        }

        val x2 = x2Multiplier.value * effectiveWidth
        val y2 = y2Multiplier.value * effectiveHeight
        rotate(
            degrees = rectRotation.value,
            pivot = Offset(x2, y2) + Offset(rectSize.width / 2, rectSize.height / 2)
        ) {
            drawRect(
                color = color,
                size = rectSize,
                topLeft = Offset(x2, y2)
            )
        }

    }
}

@Composable
internal fun InfiniteTransition.wanderingCubesTransition(
    initialValue: Float,
    targetValue: Float,
    durationMillisPerFraction: Int,
    offsetMillis: Int = 0,
    easing: Easing = EaseInOut
): State<Float> {
    return animateFloat(
        initialValue = initialValue,
        targetValue = targetValue,
        animationSpec = infiniteRepeatable(
            animation = keyframes {
                this.durationMillis = durationMillisPerFraction * 4
                initialValue at 0 using easing
                targetValue at durationMillisPerFraction using easing
                targetValue at durationMillisPerFraction * 2 using easing
                initialValue at durationMillisPerFraction * 3 using easing
                initialValue at durationMillisPerFraction * 4 using easing
            },
            RepeatMode.Restart,
            StartOffset(offsetMillis)
        )
    )
}

@Composable
internal fun InfiniteTransition.wanderingCubesRotateTransition(
    initialValue: Float,
    targetValue: Float,
    durationMillisPerFraction: Int,
    delayMillis: Int = 0,
    offsetMillis: Int = 0,
    easing: Easing = EaseInOut
): State<Float> {
    return animateFloat(
        initialValue = initialValue,
        targetValue = targetValue,
        animationSpec = infiniteRepeatable(
            animation = keyframes {
                this.durationMillis = durationMillisPerFraction * 4
                this.delayMillis = delayMillis
                initialValue at 0 using easing
                targetValue at durationMillisPerFraction using easing
                targetValue * 2 at durationMillisPerFraction * 2 using easing
                targetValue * 3 at durationMillisPerFraction * 3 using easing
                targetValue * 4 at durationMillisPerFraction * 4 using easing
            },
            RepeatMode.Restart,
            StartOffset(offsetMillis)
        )
    )
}

val EaseInOut = CubicBezierEasing(0.42f, 0f, 0.58f, 1f)
