package com.example.tiptime.viewmodels

import android.app.Application
import androidx.activity.result.launch
import androidx.compose.runtime.rememberCoroutineScope
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.RoomDatabase
import com.example.tiptime.data.TipRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.text.NumberFormat
import kotlin.math.ceil
import com.example.tiptime.data.Tip
import com.example.tiptime.data.TipDatabase
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

/**
 * ViewModel for the Tip Time app.
 */
class TipTimeViewModel(application: Application): AndroidViewModel(application) {
    //UI state
    private val _uiState = MutableStateFlow(TipTimeState())
    /**
     * The UI state for the Tip Time app.
     */
    val uiState : StateFlow<TipTimeState> = _uiState.asStateFlow()

    // Variable to hold the ID of the tip being edited
    private var currentTipId: Int? = null


    private val tipRepository: TipRepository = TipRepository(
        TipDatabase.getDatabase(application).tipDao()
    )

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
    private fun calculateTip() {
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

    fun loadTipById(tipId: Int) {
        currentTipId = tipId
        viewModelScope.launch {
            // Fetch the tip from the repository
            val tip = tipRepository.getTipById(tipId).firstOrNull()
            tip?.let {
                // Update the UI state with the fetched tip data
                _uiState.update { currentState ->
                    currentState.copy(
                        id = it.id,
                        amountInput = it.billAmount.toString(),
                        tipInput = it.tipPercentage.toString(),
                        roundUp = it.roundUp
                    )
                }
                // Recalculate tip and total for the loaded values
                calculateTip()
            }
        }
    }

    fun saveTipCalculation() {
        // Launch a coroutine in the ViewModel's scope
        viewModelScope.launch {
            val tip = Tip(
                id = currentTipId ?: 0, // Use currentTipId if it exists, otherwise 0 for a new entry
                billAmount = _uiState.value.amountInput.toDoubleOrNull() ?: 0.0,
                tipPercentage = _uiState.value.tipInput.toIntOrNull() ?: 15,
                roundUp = _uiState.value.roundUp
            )
            // Use the repository to insert the tip
            if (currentTipId == null || currentTipId == 0) {
                tipRepository.insertTip(tip)
            } else {
                tipRepository.updateTip(tip)
            }
        }
    }

    // Function to reset the state for a new tip entry
    fun resetTip() {
        currentTipId = null
        _uiState.value = TipTimeState() // Reset to default state
    }


}
