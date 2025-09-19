package com.rab.bit.road105.model.data

sealed class LoadingState {
    data object InitState : LoadingState()
    data object NoNetworkState : LoadingState()
    data class ContentState(val url: String) : LoadingState()
    data object HomeState : LoadingState()
}