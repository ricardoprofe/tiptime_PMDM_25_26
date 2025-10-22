package com.example.tiptime.ui

import android.graphics.drawable.Icon
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
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

@Composable
fun TipTimeLayout(
    tipTimeViewModel: TipTimeViewModel = viewModel(),
    navController: NavHostController = rememberNavController()
) {
    Scaffold(
        topBar = {
            TipTimeTopBar(
                currentScreen = TODO(),
                canNavigateBack = TODO()
            )
        }
    ) { innerPadding ->
        //val tipTimeUiState by tipTimeViewModel.uiState.collectAsState()

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
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.back)
                    )
                }
            }
        }
    )
}