package com.example.tiptime.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.tiptime.data.Tip
import com.example.tiptime.data.TipDatabase
import com.example.tiptime.data.TipRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

/**
 * ViewModel for the Start screen.
 */
class StartViewModel(application: Application) : AndroidViewModel(application) {

    private val tipRepository: TipRepository = TipRepository(
        TipDatabase.getDatabase(application).tipDao()
    )

    val uiState: StateFlow<StartUiState> = tipRepository.getAllTips()
        .map { StartUiState(it) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = StartUiState()
        )

    companion object {
        // Timeout for keeping the flow active while there are no subscribers.
        private const val TIMEOUT_MILLIS = 5_000L
    }
}

/**
 * The UI state for the Start screen.
 */
data class StartUiState(
    val itemList :List<Tip> = listOf()
)
