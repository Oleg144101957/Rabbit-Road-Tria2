package com.rab.bit.road104.model.data

sealed class LoadingState {
    data object InitState : LoadingState()
    data object NoNetworkState : LoadingState()
    data class ContentState(val url: String) : LoadingState()
    data object HomeState : LoadingState()
}