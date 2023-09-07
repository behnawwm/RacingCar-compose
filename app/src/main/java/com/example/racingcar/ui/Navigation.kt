package com.example.racingcar.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.racingcar.ui.game.RacingCar
import com.example.racingcar.ui.settings.settings
import com.example.racingcar.ui.viewmodel.MainViewModel
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
import com.google.accompanist.navigation.material.ModalBottomSheetLayout
import com.google.accompanist.navigation.material.bottomSheet
import com.google.accompanist.navigation.material.rememberBottomSheetNavigator

sealed class Destinations(val route: String) {
    object Game : Destinations("game")
    object Settings : Destinations("settings")
}


@Composable
@OptIn(ExperimentalMaterialNavigationApi::class)
fun RacingCarGameNavHost() {
    val bottomSheetNavigator = rememberBottomSheetNavigator()
    val navController = rememberNavController(bottomSheetNavigator)
    ModalBottomSheetLayout(
        bottomSheetNavigator = bottomSheetNavigator,
        sheetShape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
    ) {
        NavHost(navController, Destinations.Game.route) {
            gameScreen(navController)
            settingsScreen()
        }
    }
}

@OptIn(ExperimentalMaterialNavigationApi::class)
private fun NavGraphBuilder.settingsScreen() {
    bottomSheet(Destinations.Settings.route) {
        val viewModel = hiltViewModel<MainViewModel>()
        val movementInput by viewModel.movementInput.collectAsState()
        settings(
            movementInput = movementInput,
            onMovementInputChange = { viewModel.setMovementInput(it) }
        )
    }
}

private fun NavGraphBuilder.gameScreen(navController: NavHostController) {
    composable(Destinations.Game.route) {
        val viewModel = hiltViewModel<MainViewModel>()
        RacingCar(
            viewModel = viewModel,
            isDevMode = true,
            onSettingsClick = {
                navController.navigate(Destinations.Settings.route)
            },
            modifier = Modifier.fillMaxSize()
        )
    }
}
