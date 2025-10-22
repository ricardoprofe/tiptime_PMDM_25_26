package com.example.tiptime.viewmodels

import androidx.compose.animation.core.copy
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.text.NumberFormat
import kotlin.math.ceil

/**
 * ViewModel for the Tip Time app.
 */
class TipTimeViewModel : ViewModel() {
    //UI state
    private val _uiState = MutableStateFlow(TipTimeState())
    /**
     * The UI state for the Tip Time app.
     */
    val uiState : StateFlow<TipTimeState> = _uiState.asStateFlow()

    /**
     * Updates the amount input and recalculates the tip.
     *
     * @param newAmount The new amount input.
     */
    fun updateAmountInput(newAmount: String) {
        _uiState.update { currentState ->
            currentState.copy(amountInput = newAmount)
        }
        calculateTip() // Recalculate the tip each time the amount changes

    }

    /**
     * Updates the tip percent input and recalculates the tip.
     *
     * @param newTipPercent The new tip percent input.
     */
    fun updateTipInput(newTipPercent: String) {
        _uiState.update { currentState ->
            currentState.copy(tipInput = newTipPercent)
        }
        calculateTip() // Recalculate the tip each time the amount changes
    }

    /**
     * Updates the round up option and recalculates the tip.
     *
     * @param shouldRoundUp Whether the tip should be rounded up.
     */
    fun updateRoundUp(shouldRoundUp: Boolean) {
        _uiState.update { currentState ->
            currentState.copy(roundUp = shouldRoundUp)
        }
        calculateTip() // Recalculate the tip each time the amount changes
    }

    /**
     * Calculates the tip based on the user input and format the tip amount
     * according to the local currency.
     * Example would be "$10.00".
     */
    private fun calculateTip(): Unit {
        // Get the current values from the sate
        val amount = _uiState.value.amountInput.toDoubleOrNull() ?: 0.0
        val tipPercent = _uiState.value.tipInput.toDoubleOrNull() ?: 15.0
        val roundUp = _uiState.value.roundUp

        var tip = tipPercent / 100 * amount
        if (roundUp) {
            tip = ceil(tip)
        }
        val total = amount + tip //NEW

        val formattedTip = NumberFormat.getCurrencyInstance().format(tip)
        val formattedTotal = NumberFormat.getCurrencyInstance().format(total) //NEW

        // Update the state with the calculated tip
        _uiState.update { currentState ->
            currentState.copy(
                tip = formattedTip,
                total = formattedTotal //NEW
            )
        }
    }

}
