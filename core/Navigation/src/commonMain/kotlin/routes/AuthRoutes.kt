package routes

import NeedAuth
import TopLevelRoute
import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
sealed interface AuthRoutes: NavKey {
    @Serializable
    data object SelectMethod: AuthRoutes, TopLevelRoute

    @Serializable
    data object ShowJoinCode: AuthRoutes, NeedAuth

    @Serializable
    data object JoinExistingRoom: AuthRoutes
}