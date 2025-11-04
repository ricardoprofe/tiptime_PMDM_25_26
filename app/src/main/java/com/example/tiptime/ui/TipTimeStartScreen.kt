package com.example.tiptime.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tiptime.data.Tip
import com.example.tiptime.R
import com.example.tiptime.viewmodels.StartViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TipTimeStartScreen(
    onItemClick: (Int) -> Unit,
    modifier: Modifier = Modifier,
    startViewModel: StartViewModel = viewModel(),
    contentPadding: PaddingValues = PaddingValues(0.dp)
) {
    val uiState by startViewModel.uiState.collectAsState()
    val tipList = uiState.itemList

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.padding(top = 0.dp),
    ) {
        if (tipList.isEmpty()) {
            Text(
                text = stringResource(R.string.no_tips_description),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(contentPadding),
            )
        } else {
            TipList(
                tipList = tipList,
                onItemClick = { onItemClick(it.id) },
                contentPadding = contentPadding,
                modifier = Modifier.padding(horizontal = 6.dp)
            )
        }
    }
}

@Composable
fun TipList(
    tipList: List<Tip>,
    onItemClick: (Tip) -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues,
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = contentPadding
    ) {
        items(items = tipList, key = { it.id }) { tip ->
            TipItem(tip = tip,
                modifier = Modifier
                    .padding(6.dp)
                    .clickable { onItemClick(tip) })
        }
    }
}

@Composable
fun TipItem(
    tip: Tip,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Text(
                text = stringResource(R.string.bill_amount)  + ": " + tip.billAmount,
                style = MaterialTheme.typography.bodyLarge
            )
            Spacer(Modifier.weight(1f))
            Text(
                text = stringResource(R.string.tip_amount, tip.tipPercentage),
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.End,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}