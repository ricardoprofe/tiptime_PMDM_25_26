package com.example.tiptime.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.tiptime.data.Tip
import com.example.tiptime.data.TipDatabase
import com.example.tiptime.data.TipRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.NumberFormat
import kotlin.math.ceil

/**
 * ViewModel for the Tip Time app.
 *
 * Now uses SavedStateHandle to persist UI inputs so they survive configuration
 * changes and process death (for simple primitives/strings).
 */
class TipTimeViewModel(
    application: Application,
    private val savedStateHandle: SavedStateHandle
) : AndroidViewModel(application) {

    private val TAG = "TipTimeViewModel"

    private companion object {
        private const val KEY_ID = "current_tip_id"
        private const val KEY_AMOUNT = "amount_input"
        private const val KEY_TIP = "tip_input"
        private const val KEY_ROUND_UP = "round_up"
    }

    // Restore currentTipId from saved state if present
    private var currentTipId: Int? = savedStateHandle.get<Int>(KEY_ID)


    //UI state initialized from SavedStateHandle when possible
    private val _uiState = MutableStateFlow(
        TipTimeState(
            id = savedStateHandle.get<Int>(KEY_ID) ?: 0,
            amountInput = savedStateHandle.get<String>(KEY_AMOUNT) ?: "",
            tipInput = savedStateHandle.get<String>(KEY_TIP) ?: "",
            roundUp = savedStateHandle.get<Boolean>(KEY_ROUND_UP) ?: true
        )
    )
    /**
     * The UI state for the Tip Time app.
     */
    val uiState : StateFlow<TipTimeState> = _uiState.asStateFlow()

    private val tipRepository: TipRepository = TipRepository(
        TipDatabase.getDatabase(application).tipDao()
    )

    init {
        // If an id was restored, try to load it from DB to refresh values
        currentTipId?.let { id ->
            if (id != 0) loadTipById(id)
        }
    }

    /**
     * Updates the amount input and recalculates the tip.
     */
    fun updateAmountInput(newAmount: String) {
        _uiState.update { currentState ->
            currentState.copy(amountInput = newAmount)
        }
        // persist
        savedStateHandle.set(KEY_AMOUNT, newAmount)
        calculateTip()
    }

    /**
     * Updates the tip percent input and recalculates the tip.
     */
    fun updateTipInput(newTipPercent: String) {
        _uiState.update { currentState ->
            currentState.copy(tipInput = newTipPercent)
        }
        savedStateHandle.set(KEY_TIP, newTipPercent)
        calculateTip()
    }

    /**
     * Updates the round up option and recalculates the tip.
     */
    fun updateRoundUp(shouldRoundUp: Boolean) {
        _uiState.update { currentState ->
            currentState.copy(roundUp = shouldRoundUp)
        }
        savedStateHandle.set(KEY_ROUND_UP, shouldRoundUp)
        calculateTip()
    }

    private fun calculateTip() {
        val amount = _uiState.value.amountInput.toDoubleOrNull() ?: 0.0
        val tipPercent = _uiState.value.tipInput.toDoubleOrNull() ?: 15.0
        val roundUp = _uiState.value.roundUp

        var tip = tipPercent / 100 * amount
        if (roundUp) {
            tip = ceil(tip)
        }
        val total = amount + tip

        val formattedTip = NumberFormat.getCurrencyInstance().format(tip)
        val formattedTotal = NumberFormat.getCurrencyInstance().format(total)

        _uiState.update { currentState ->
            currentState.copy(
                tip = formattedTip,
                total = formattedTotal
            )
        }
    }

    fun loadTipById(tipId: Int) {
        currentTipId = tipId
        savedStateHandle.set(KEY_ID, tipId)
        viewModelScope.launch {
            val tip = tipRepository.getTipById(tipId).firstOrNull()
            tip?.let {
                _uiState.update { currentState ->
                    currentState.copy(
                        id = it.id,
                        amountInput = it.billAmount.toString(),
                        tipInput = it.tipPercentage.toString(),
                        roundUp = it.roundUp
                    )
                }
                // persist loaded values
                savedStateHandle.set(KEY_AMOUNT, it.billAmount.toString())
                savedStateHandle.set(KEY_TIP, it.tipPercentage.toString())
                savedStateHandle.set(KEY_ROUND_UP, it.roundUp)
                calculateTip()
            }
        }
    }

    fun saveTipCalculation() {
        viewModelScope.launch {
            val tip = Tip(
                id = currentTipId ?: 0,
                billAmount = _uiState.value.amountInput.toDoubleOrNull() ?: 0.0,
                tipPercentage = _uiState.value.tipInput.toIntOrNull() ?: 15,
                roundUp = _uiState.value.roundUp
            )
            if (currentTipId == null || currentTipId == 0) {
                tipRepository.insertTip(tip)
            } else {
                tipRepository.updateTip(tip)
            }
        }
    }

    fun deleteTip() {
        val tipId = currentTipId ?: return
        val tip = Tip(
            id = tipId,
            billAmount = _uiState.value.amountInput.toDoubleOrNull() ?: 0.0,
            tipPercentage = _uiState.value.tipInput.toIntOrNull() ?: 15,
            roundUp = _uiState.value.roundUp
        )
        viewModelScope.launch {
            tipRepository.deleteTip(tip)
        }
    }

    fun resetTip() {
        currentTipId = null
        savedStateHandle.remove<Int>(KEY_ID)
        savedStateHandle.remove<String>(KEY_AMOUNT)
        savedStateHandle.remove<String>(KEY_TIP)
        savedStateHandle.remove<Boolean>(KEY_ROUND_UP)
        _uiState.value = TipTimeState()
    }
}
