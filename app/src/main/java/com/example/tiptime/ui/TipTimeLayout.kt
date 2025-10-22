package com.example.tiptime.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.tiptime.viewmodels.TipTimeViewModel

@Composable
fun TipTimeLayout(
    tipTimeViewModel: TipTimeViewModel = viewModel(),
    navController: NavHostController = rememberNavController()
) {
    Scaffold(

    ) { innerPadding ->
        val tipTimeUiState by tipTimeViewModel.uiState.collectAsState()

        NavHost(
            navController = navController,
            startDestination = Routes.Start.name,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable( route = Routes.Start.name) {
                TipTimeStartScreen(
                    tipTimeViewModel = tipTimeViewModel,
                    //tipTimeUiState = tipTimeUiState,
                    onNextButtonClicked = { navController.navigate(Routes.TipResult.name) },
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(40.dp)
                )

            }
            composable(route = Routes.TipResult.name) {
                TipTimeResultScreen(
                    tipTimeViewModel = tipTimeViewModel,
                    //tipTimeUiState = tipTimeUiState,
                    onBackButtonClicked = { navController.navigate(Routes.Start.name) },
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                )

            }
        }


    }
}