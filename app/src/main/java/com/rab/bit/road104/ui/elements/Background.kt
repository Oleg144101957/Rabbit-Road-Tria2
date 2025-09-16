package com.rab.bit.road104.ui.elements

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.rab.bit.road104.R

@Composable
fun Background(imgRes: Int = R.drawable.bg) {
    Image(
        painter = painterResource(imgRes),
        contentDescription = imgRes.toString(),
        modifier = Modifier.fillMaxSize(),
        contentScale = ContentScale.FillBounds
    )
}