package com.rab.bit.road104.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import com.rab.bit.road104.R
import com.rab.bit.road104.ui.elements.Background
import com.rab.bit.road104.ui.screens.games.GameViewModel
import com.rab.bit.road104.ui.theme.DefFont
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@Composable
fun TopGamesScreen(
    navController: NavController,
    paddingValues: PaddingValues,
    viewModel: GameViewModel = hiltViewModel(),
) {
    val records = viewModel.bestGames()

    val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")
        .withZone(ZoneId.systemDefault())

    Box(
        Modifier
            .fillMaxSize()
            .padding(paddingValues),
        contentAlignment = Alignment.Center
    ) {
        Background()
        Image(
            painterResource(R.drawable.ic_back),
            R.drawable.ic_back.toString(),
            modifier = Modifier
                .padding(8.dp)
                .size(64.dp)
                .align(Alignment.TopStart)
                .clickable {
                    navController.popBackStack()
                },
            contentScale = ContentScale.FillWidth
        )
        Box(
            Modifier
                .fillMaxWidth()
                .padding(8.dp),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painterResource(R.drawable.table),
                R.drawable.table.toString(),
                Modifier
                    .fillMaxWidth(),
                contentScale = ContentScale.FillWidth
            )
            if (records.isEmpty()) {
                Text(
                    "No data",
                    color = Color.White,
                    fontFamily = DefFont,
                    fontSize = 32.sp
                )
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxWidth(0.7f)
                        .offset(y = 64.dp)
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    HeaderRow()

                    Spacer(Modifier.height(8.dp))

                    LazyColumn(
                        modifier = Modifier.fillMaxWidth(),
                        contentPadding = PaddingValues(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        itemsIndexed(records) { index, item ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 8.dp, vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Cell(text = "${item.score}", weight = 0.4f, bold = true)
                                val dateStr = formatter.format(Instant.ofEpochMilli(item.timestamp))
                                Cell(text = dateStr, weight = 0.8f)
                            }
                        }
                    }
                }
            }

        }
    }
}

@Composable
private fun HeaderRow() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Cell("Score", 0.4f, header = true)
        Cell("Date", 0.8f, header = true)
    }
}

@Composable
private fun RowScope.Cell(
    text: String,
    weight: Float,
    header: Boolean = false,
    bold: Boolean = false
) {
    Text(
        text = text,
        modifier = Modifier
            .weight(weight),
        color = Color.White,
        fontSize = 18.sp,
        fontWeight = when {
            header -> FontWeight.SemiBold
            bold -> FontWeight.SemiBold
            else -> null
        }
    )
}
