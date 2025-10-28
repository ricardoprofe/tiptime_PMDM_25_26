package com.example.tiptime.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tiptime.R
import com.example.tiptime.viewmodels.TipTimeViewModel

@Composable
fun TipTimeResultScreen(
    onBackButtonClicked: () -> Unit,
    modifier: Modifier = Modifier,
    tipTimeViewModel: TipTimeViewModel = viewModel(),
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
            text = stringResource(R.string.tip_amount, tipTimeUiState.tip),
            style = MaterialTheme.typography.titleLarge,
        )
        Text(
            text = stringResource(R.string.total, tipTimeUiState.total),
            style = MaterialTheme.typography.titleLarge,
        )
        Spacer(modifier = Modifier.height(30.dp))
        Button(
            onClick = onBackButtonClicked,
        ) {
            Text(stringResource(R.string.back))
        }
    }
}

@Preview
@Composable
fun ResultPreview(){
    TipTimeResultScreen(
        onBackButtonClicked = {  }
    )
}