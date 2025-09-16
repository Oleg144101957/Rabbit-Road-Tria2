package com.rab.bit.road104.model.repo

import com.rab.bit.road104.model.data.catcher.GameRecord

interface DataStoreRepository {

    fun saveUrl(newGoalToSave: String)
    fun getUrl(): String
    fun saveAdb(adb: Boolean)
    fun getAdb(): Boolean?
    fun setSpeed(newSpeed: Float)
    fun getSpeed(): Float
    fun saveGameResult(score: Int, timestamp: Long = System.currentTimeMillis())
    fun getBestGames(limit: Int = 5): List<GameRecord>

}