package com.rab.bit.road105.ui.elements

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rab.bit.road105.R
import com.rab.bit.road105.ui.theme.DefFont

@Composable
fun <T> DropDownMenu(items: List<T>, initialSelectedIndex: Int = 0, onClick: (item: T) -> Unit) {
    var isDropDownExpanded by rememberSaveable { mutableStateOf(false) }
    var itemPosition by rememberSaveable { mutableIntStateOf(initialSelectedIndex) }
    LaunchedEffect(initialSelectedIndex) {
        itemPosition = initialSelectedIndex
    }
    Box(
        modifier = Modifier
            .padding(6.dp)
            .fillMaxWidth(0.7f),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painterResource(R.drawable.btn), R.drawable.btn.toString(),
            modifier = Modifier.fillMaxWidth(), contentScale = ContentScale.FillWidth
        )
        Row(
            modifier = Modifier
                .clickable {
                    isDropDownExpanded = true
                }
                .fillMaxWidth(0.7f)
                .align(Alignment.BottomCenter)
                .padding(bottom = 24.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = items[itemPosition].toString(),
                style = TextStyle(
                    color = Color.White,
                    fontFamily = DefFont,
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp
                )
            )
            Icon(
                painter = painterResource(id = R.drawable.ic_drop_down),
                contentDescription = R.drawable.ic_drop_down.toString(),
                tint = Color.White
            )
        }
        DropdownMenu(
            modifier = Modifier.heightIn(max = LocalConfiguration.current.screenHeightDp.dp / 2),
            expanded = isDropDownExpanded,
            onDismissRequest = {
                isDropDownExpanded = false
            }) {
            items.forEachIndexed { index, value ->
                DropdownMenuItem(
                    text = {
                        Text(
                            text = value.toString(),
                            style = TextStyle(
                                color = Color.Black,
                                fontFamily = DefFont,
                                fontWeight = FontWeight.Bold,
                                fontSize = 24.sp
                            )
                        )
                    },
                    onClick = {
                        isDropDownExpanded = false
                        itemPosition = index
                        onClick(value)
                    }
                )
            }
        }
    }
}
