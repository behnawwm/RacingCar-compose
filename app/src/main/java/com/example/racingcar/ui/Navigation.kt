package com.example.racingcar.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.racingcar.ui.game.RacingGameScreen
import com.example.racingcar.ui.settings.SettingsBottomSheet
import com.example.racingcar.ui.viewmodel.MainViewModel
import com.example.racingcar.utils.vibrateError
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
import com.google.accompanist.navigation.material.ModalBottomSheetLayout
import com.google.accompanist.navigation.material.bottomSheet
import com.google.accompanist.navigation.material.rememberBottomSheetNavigator

sealed class Destinations(val route: String) {
    data object Game : Destinations("game")
    data object Settings : Destinations("settings")
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
        val viewModel = hiltViewModel<MainViewModel>()
        NavHost(navController, Destinations.Game.route) {
            gameScreen(navController = navController, viewModel = viewModel)
            settingsScreen(viewModel = viewModel)
        }
    }
}

@OptIn(ExperimentalMaterialNavigationApi::class)
private fun NavGraphBuilder.settingsScreen(viewModel: MainViewModel) {
    bottomSheet(Destinations.Settings.route) {
        val movementInput by viewModel.movementInput.collectAsState()
        SettingsBottomSheet(
            movementInput = movementInput,
            onMovementInputChange = { viewModel.setMovementInput(it) }
        )
    }
}

private fun NavGraphBuilder.gameScreen(navController: NavHostController, viewModel: MainViewModel) {
    composable(Destinations.Game.route) {

        val context = LocalContext.current
        LaunchedEffect(context) {
            viewModel.vibrateSharedFlow.collect {
                context.vibrateError()
            }
        }

        val gameScore by viewModel.gameScore.collectAsState()
        val highscore by viewModel.highscore.collectAsState()
        val acceleration by viewModel.acceleration.collectAsState()
        val movementInput by viewModel.movementInput.collectAsState()

        RacingGameScreen(
            isDevMode = { true },
            onSettingsClick = {
                navController.navigate(Destinations.Settings.route)
            },
            gameScore = { gameScore },
            highscore = { highscore },
            acceleration = { acceleration },
            movementInput = { movementInput },
            onGameScoreIncrease = viewModel::increaseGameScore,
            onResetGameScore = viewModel::resetGameScore,
            onBlockerRectsDraw = viewModel::updateBlockerRects,
            onCarRectDraw = viewModel::updateCarRect,
            modifier = Modifier.fillMaxSize()
        )
    }
}
