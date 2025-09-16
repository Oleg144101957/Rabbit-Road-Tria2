package com.rab.bit.road104.model.data.catcher

import androidx.compose.ui.graphics.painter.Painter

data class FallingElement(
    val x: Float,
    val y: Float,
    val drawable: Painter,
    val isCatchable: Boolean,
    val swayAmp: Float,
    val swaySpeed: Float,
    val swayPhase: Float
)
