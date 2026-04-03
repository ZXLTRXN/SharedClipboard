package routes

import NeedAuth
import TopLevelRoute
import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable
import org.jetbrains.compose.resources.DrawableResource
import sharedclipboard.core.navigation.generated.resources.Res
import sharedclipboard.core.navigation.generated.resources.home_ic

@Serializable
sealed interface ClipboardRoutes: NavKey {
    @Serializable
    data object Clipboard: ClipboardRoutes, TopLevelRoute, NeedAuth {
        override val icon: DrawableResource
            get() = Res.drawable.home_ic
    }
}