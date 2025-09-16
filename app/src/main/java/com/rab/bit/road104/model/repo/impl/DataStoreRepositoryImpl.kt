package com.rab.bit.road104.model.repo.impl

import android.content.Context
import androidx.core.content.edit
import com.rab.bit.road104.model.data.catcher.GameRecord
import com.rab.bit.road104.model.repo.DataStoreRepository
import org.json.JSONArray
import org.json.JSONObject
import javax.inject.Inject

class DataStoreRepositoryImpl @Inject constructor(private val context: Context) :
    DataStoreRepository {

    private val pref = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    override fun getUrl(): String {
        return pref.getString(
            GOAL,
            EMPTY
        ) ?: EMPTY
    }

    override fun saveUrl(newGoalToSave: String) {
        pref.edit { putString(GOAL, newGoalToSave) }
    }

    override fun saveAdb(adb: Boolean) {
        pref.edit { putBoolean(ADB, adb) }
    }

    override fun getAdb(): Boolean? {
        return if (pref.contains(ADB)) {
            pref.getBoolean(ADB, false)
        } else {
            null
        }
    }

    override fun setSpeed(newSpeed: Float) {
        pref.edit { putFloat(SPEED, newSpeed) }
    }


    override fun getSpeed(): Float {
        return pref.getFloat(SPEED, 5f)
    }

    override fun saveGameResult(score: Int, timestamp: Long) {
        val current = pref.getString(KEY_BEST_GAMES, "[]") ?: "[]"
        val arr = JSONArray(current)

        // Добавляем новую запись
        val obj = JSONObject().apply {
            put("score", score)
            put("timestamp", timestamp)
        }
        arr.put(obj)

        // Сортируем по score убыв. и ограничиваем 5
        val sorted = (0 until arr.length())
            .map { arr.getJSONObject(it) }
            .sortedByDescending { it.getInt("score") }
            .take(5)

        val newArr = JSONArray()
        sorted.forEach { newArr.put(it) }

        pref.edit {
            putString(KEY_BEST_GAMES, newArr.toString())
        }
    }

    override fun getBestGames(limit: Int): List<GameRecord> {
        val current = pref.getString(KEY_BEST_GAMES, "[]") ?: "[]"
        val arr = JSONArray(current)
        return (0 until arr.length())
            .map { arr.getJSONObject(it) }
            .map { obj ->
                GameRecord(
                    score = obj.optInt("score", 0),
                    timestamp = obj.optLong("timestamp", 0L)
                )
            }
            .sortedByDescending { it.score }
            .take(limit)
    }

    companion object {
        const val PREFS_NAME = "prefs"
        private const val GOAL = "GOAL"
        private const val EMPTY = "EMPTY"
        private const val ADB = "ADB"
        private const val SPEED = "speed"
        private const val KEY_BEST_GAMES = "best_games"
    }
}


