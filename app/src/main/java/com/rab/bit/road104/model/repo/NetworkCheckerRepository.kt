package com.rab.bit.road104.model.repo

import android.content.Context

interface NetworkCheckerRepository {
    fun isConnectionAvailable(): Boolean
    fun isAdbEnabled(context: Context): Boolean
}