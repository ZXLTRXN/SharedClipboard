package routes

import NeedAuth
import TopLevelRoute
import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable
import org.jetbrains.compose.resources.DrawableResource
import sharedclipboard.core.navigation.generated.resources.Res
import sharedclipboard.core.navigation.generated.resources.login_ic

@Serializable
sealed interface AuthRoutes: NavKey {
    @Serializable
    data object SelectMethod: AuthRoutes, TopLevelRoute {
        override val icon: DrawableResource
            get() = Res.drawable.login_ic
    }

    @Serializable
    data object ShowJoinCode: AuthRoutes, NeedAuth

    @Serializable
    data object JoinExistingRoom: AuthRoutes
}