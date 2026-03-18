package com.example.feature.auth.ui

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
sealed interface AuthRoutes: NavKey {
    @Serializable
    data object ShowJoinCode: AuthRoutes

    @Serializable
    data object JoinExistingRoom: AuthRoutes

    @Serializable
    data object SelectMethod: AuthRoutes
}