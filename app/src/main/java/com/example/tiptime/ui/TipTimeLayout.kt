package com.example.tiptime.ui

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.tiptime.viewmodels.TipTimeViewModel
import com.example.tiptime.R
import androidx.compose.material3.Icon
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.currentBackStackEntryAsState

@Composable
fun TipTimeLayout(
    tipTimeViewModel: TipTimeViewModel = viewModel(),
    navController: NavHostController = rememberNavController()
) {
    // Get current back stack entry
    val backStackEntry by navController.currentBackStackEntryAsState()
    // Get the name of the current screen
    val currentScreen = Routes.valueOf(
        backStackEntry?.destination?.route ?: Routes.Start.name
    )

    val context = LocalContext.current
    val uiState by tipTimeViewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TipTimeTopBar(
                currentScreen = currentScreen,
                canNavigateBack = navController.previousBackStackEntry != null,
                navigateUp = { navController.navigateUp() },
                showShare = currentScreen == Routes.TipResult,
                onShareClicked = { createShareIntent(context, tip = uiState.tip, total = uiState.total) }
            )
        }
    ) { innerPadding ->

        NavHost(
            navController = navController,
            startDestination = Routes.Start.name,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable( route = Routes.Start.name) {
                TipTimeStartScreen(
                    tipTimeViewModel = tipTimeViewModel,
                    onNextButtonClicked = { navController.navigate(Routes.TipResult.name) },
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(40.dp)
                )

            }
            composable(route = Routes.TipResult.name) {
                TipTimeResultScreen(
                    tipTimeViewModel = tipTimeViewModel,
                    onBackButtonClicked = { navController.navigate(Routes.Start.name) },
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(40.dp)
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TipTimeTopBar(
    currentScreen: Routes,
    canNavigateBack: Boolean,
    modifier: Modifier = Modifier,
    navigateUp: () -> Unit = {},
    showShare: Boolean = false,
    onShareClicked: () -> Unit = {},
) {
    TopAppBar(
        title = { Text(stringResource(currentScreen.title)) },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
        ),
        modifier = modifier,
        navigationIcon = {
            if (canNavigateBack) {
                IconButton(onClick = navigateUp) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.back)
                    )
                }
            }
        },
        actions = {
            if (showShare) {
                IconButton(onClick = onShareClicked) {
                    Icon(
                        imageVector = Icons.Filled.Share,
                        contentDescription = "Share"
                    )
                }
            }
        }
    )
}

private fun createShareIntent(context: Context, tip: String, total: String) {
    val shareText = context.getString(R.string.tip_amount_total_bill, tip, total)
    // Create an ACTION_SEND implicit intent with order details in the intent extras
    val intent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_SUBJECT, context.getString(R.string.your_tip))
        putExtra(Intent.EXTRA_TEXT, shareText)
    }
    context.startActivity(
        Intent.createChooser(
            intent,
            context.getString(R.string.your_tip)
        )
    )

}