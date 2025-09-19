package com.rab.bit.road105.model.repo

import android.content.Context

interface NetworkCheckerRepository {
    fun isConnectionAvailable(): Boolean
    fun isAdbEnabled(context: Context): Boolean
}