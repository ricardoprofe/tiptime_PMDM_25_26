package com.example.tiptime.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tiptime.R
import com.example.tiptime.viewmodels.TipTimeState
import com.example.tiptime.viewmodels.TipTimeViewModel

@Composable
fun TipTimeStartScreen(
    tipTimeViewModel: TipTimeViewModel = viewModel(),
    onNextButtonClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    val tipTimeUiState by tipTimeViewModel.uiState.collectAsState()

    Column(
        modifier = modifier
            .statusBarsPadding()
            .verticalScroll(rememberScrollState())
            .safeDrawingPadding(),
        horizontalAlignment = Alignment.Companion.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(R.string.calculate_tip),
            modifier = Modifier.Companion
                .padding(bottom = 16.dp, top = 40.dp)
                .align(alignment = Alignment.Companion.Start)
        )
        EditNumberField(
            label = R.string.bill_amount,
            leadingIcon = R.drawable.money,
            keyboardOptions = KeyboardOptions.Companion.Default.copy(
                keyboardType = KeyboardType.Companion.Number,
                imeAction = ImeAction.Companion.Next
            ),
            value = tipTimeUiState.amountInput,
            onValueChanged = { tipTimeViewModel.updateAmountInput(it) },
            modifier = Modifier.Companion
                .padding(bottom = 32.dp)
                .fillMaxWidth(),
        )
        EditNumberField(
            label = R.string.how_was_the_service,
            leadingIcon = R.drawable.percent,
            keyboardOptions = KeyboardOptions.Companion.Default.copy(
                keyboardType = KeyboardType.Companion.Number,
                imeAction = ImeAction.Companion.Done
            ),
            value = tipTimeUiState.tipInput, //TODO
            onValueChanged = { tipTimeViewModel.updateTipInput(it) },
            modifier = Modifier.Companion
                .padding(bottom = 32.dp)
                .fillMaxWidth(),
        )
        RoundTheTipRow(
            roundUp = tipTimeUiState.roundUp, //TODO
            onRoundUpChanged = { tipTimeViewModel.updateRoundUp(it) },
            modifier = Modifier.Companion.padding(bottom = 32.dp)
        )
        Text(
            text = stringResource(R.string.tip_amount, tipTimeUiState.tip),
            style = MaterialTheme.typography.displaySmall
        )
        Button(
            onClick = onNextButtonClicked,
        ) {
            Text(stringResource(R.string.next))
        }
        Spacer(modifier = Modifier.Companion.height(150.dp))
    }
}