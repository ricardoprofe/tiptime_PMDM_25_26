package com.example.tiptime.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tiptime.viewmodels.TipTimeState
import com.example.tiptime.viewmodels.TipTimeViewModel
import com.example.tiptime.R

@Composable
fun TipTimeResultScreen(
    tipTimeViewModel: TipTimeViewModel = viewModel(),
    onBackButtonClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    val tipTimeUiState by tipTimeViewModel.uiState.collectAsState()
    // PILLA el state de las 2 maneras

    Column(
        modifier = modifier
    ) {
        Text(text = stringResource(R.string.tip_amount, tipTimeUiState.tip))
        Text(text = stringResource(R.string.total, tipTimeUiState.total))
        Button(
            onClick = onBackButtonClicked,
        ) {
            Text(stringResource(R.string.back))
        }
    }
}

