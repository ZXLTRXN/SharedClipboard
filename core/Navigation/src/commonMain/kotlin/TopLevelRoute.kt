import androidx.navigation3.runtime.NavKey
import org.jetbrains.compose.resources.DrawableResource

interface TopLevelRoute: NavKey {
    val icon: DrawableResource
}