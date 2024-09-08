package com.gals.prayertimes.model

sealed class UiState {
    data object Loading : UiState()
    data class Success(val uiPrayer: UiPrayer) : UiState()
    data class Error(val message: String) : UiState()
}

enum class UiPermissionState {
    PENDING,
    REQUESTED,
    GRANTED,
    DENIED,
    NOT_REQUESTED
}