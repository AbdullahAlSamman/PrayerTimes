package com.gals.prayertimes.model

sealed class UiState {
    data object Loading : UiState()
    data class Success(val prayer: Prayer) : UiState()
    data class Error(val message: String) : UiState()
}