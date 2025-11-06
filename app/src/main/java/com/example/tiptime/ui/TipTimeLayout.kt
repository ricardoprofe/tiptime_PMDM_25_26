package com.example.tiptime.ui

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.tiptime.R
import com.example.tiptime.viewmodels.StartViewModel
import com.example.tiptime.viewmodels.TipTimeViewModel

@Composable
fun TipTimeLayout(
    tipTimeViewModel: TipTimeViewModel = viewModel(),
    startViewModel: StartViewModel = viewModel(),
    navController: NavHostController = rememberNavController()
) {
    // Get current back stack entry
    val backStackEntry by navController.currentBackStackEntryAsState()
    // Resolve a route string for the current entry (may include concrete id like "EditTip/3")
    val currentRoute = backStackEntry?.destination?.route ?: Routes.Start.route
    // Map the route string to our Routes enum, handling both plain and "withArg" patterns
    val currentScreen = Routes.entries.firstOrNull { r ->
        currentRoute == r.route ||
            currentRoute.startsWith(r.route + "/") ||
            currentRoute == r.withArg()
    } ?: Routes.Start

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
        },
        floatingActionButton = {
            // Add a FAB only on the Start screen
            if (currentScreen == Routes.Start) {
                androidx.compose.material3.FloatingActionButton(
                    onClick = { navController.navigate(Routes.EditTip.createRouteFor(0)) }
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = stringResource(R.string.add_tip)
                    )
                }
            }
        }

    ) { innerPadding ->

        NavHost(
            navController = navController,
            startDestination = Routes.Start.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(route = Routes.Start.route) {
                TipTimeStartScreen(
                    startViewModel = startViewModel,
                    // Navigate to EditTip with the selected tip id using the helper
                    onItemClick = { id -> navController.navigate(Routes.EditTip.createRouteFor(id)) },
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(10.dp)
                )
            }
            // EditTip route now accepts a path parameter using the enum helper
            composable(
                route = Routes.EditTip.withArg(),
                arguments = listOf(navArgument(Routes.TIP_ID_ARG) { type = NavType.IntType })
            ) { backStackEntry ->
                val tipId = backStackEntry.arguments?.getInt(Routes.TIP_ID_ARG) ?: 0
                TipTimeEditScreen(
                    tipTimeViewModel = tipTimeViewModel,
                    onNextButtonClicked = {
                        tipTimeViewModel.saveTipCalculation()
                        navController.navigate(Routes.TipResult.route)
                    },
                    backNavigation = { navController.navigateUp() },
                    tipId = tipId,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(10.dp)
                )
            }
            composable(route = Routes.TipResult.route) {
                TipTimeResultScreen(
                    tipTimeViewModel = tipTimeViewModel,
                    onBackButtonClicked = { navController.navigate(Routes.Start.route) {popUpTo(0)} },
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(10.dp)
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
                        contentDescription = stringResource(R.string.share)
                    )
                }
            }
        }
    )
}

/**
 * Create an intent to share the tip and total amount
 */
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

