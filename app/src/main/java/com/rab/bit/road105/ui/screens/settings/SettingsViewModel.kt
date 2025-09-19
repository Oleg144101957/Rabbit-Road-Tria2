package com.rab.bit.road105.ui.screens.settings

import androidx.lifecycle.ViewModel
import com.rab.bit.road105.model.data.catcher.DifficultyOptions
import com.rab.bit.road105.model.repo.DataStoreRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val dataStore: DataStoreRepository
) : ViewModel() {
    fun setSpeed(speed: DifficultyOptions) {
        val newSpeed = when (speed) {
            DifficultyOptions.EASY -> 3f
            DifficultyOptions.MEDIUM -> 5f
            DifficultyOptions.HARD -> 10f
        }
        dataStore.setSpeed(newSpeed)
    }

    fun getSpeed(): DifficultyOptions {
        val speed = dataStore.getSpeed()
        return when (speed) {
            3f -> DifficultyOptions.EASY
            10f -> DifficultyOptions.HARD
            else -> DifficultyOptions.MEDIUM
        }
    }
}