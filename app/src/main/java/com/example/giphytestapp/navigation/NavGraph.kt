package com.example.giphytestapp.navigation

import android.content.Context
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.giphytestapp.ui.SharedViewModel
import com.example.giphytestapp.ui.screens.master.MasterScreen

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SetupNavGraph(
    context: Context,
    sharedViewModel: SharedViewModel,
    navController: NavHostController
){
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route){
        composable(
            route = Screen.Home.route
        ) {
            MasterScreen(context = context, sharedViewModel = sharedViewModel, navController)
        }
        composable(
            route = Screen.Details.route
        ) {
        }
    }
}