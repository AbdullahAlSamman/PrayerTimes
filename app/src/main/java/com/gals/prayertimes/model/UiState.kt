package com.gals.prayertimes.model

sealed class UiState {

    object Loading : UiState()

    object Success : UiState()

    data class Error(val message: String) : UiState()
}