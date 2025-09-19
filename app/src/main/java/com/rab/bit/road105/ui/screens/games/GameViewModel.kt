package com.rab.bit.road105.ui.screens.games

import androidx.lifecycle.ViewModel
import com.rab.bit.road105.model.data.catcher.GameRecord
import com.rab.bit.road105.model.repo.DataStoreRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class GameViewModel @Inject constructor(
    private val dataStore: DataStoreRepository
) : ViewModel() {
    fun getSpeed(): Float {
        return dataStore.getSpeed()
    }

    fun saveResult(score: Int) {
        dataStore.saveGameResult(score)
    }

    fun bestGames(): List<GameRecord> = dataStore.getBestGames(5)
}