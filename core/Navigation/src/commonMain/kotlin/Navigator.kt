import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSerializable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.navigation3.runtime.NavKey
import androidx.savedstate.compose.serialization.serializers.SnapshotStateListSerializer
import androidx.savedstate.serialization.SavedStateConfiguration
import io.github.aakira.napier.Napier
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.PolymorphicSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import routes.AuthRoutes

class Navigator(
    initialStack: List<NavKey>
) {
//    var isLoggedIn: (() -> Boolean)? = null
//
//    val backStack: SnapshotStateList<NavKey> by lazy(LazyThreadSafetyMode.NONE) {
//        val isLoggedIn = isLoggedIn?.invoke()
//        if (isLoggedIn == null) {
//            Napier.e(
//                "isLoggedIn is null",
//                tag = this::class.simpleName
//            )
//        }
//
//        return@lazy if (initialStack.any { it is NeedAuth && isLoggedIn != true }) {
//            mutableStateListOf(AuthRoutes.SelectMethod)
//        } else {
//            mutableStateListOf(*initialStack.toTypedArray())
//        }
//    }

    val backStack: SnapshotStateList<NavKey> = mutableStateListOf(*initialStack.toTypedArray())

    fun goTo(route: NavKey) {
        if (backStack.lastOrNull() == route) return
        backStack.add(route)
//        addWithAuthCheck(route) fixme

    }

    fun clearAndGoTo(route: NavKey) {
        backStack.removeLastOrNull()
        backStack.add(route)
//        addWithAuthCheck(route) fixme
    }

//    private fun addWithAuthCheck(route: NavKey) {
//        if (route is NeedAuth && isLoggedIn?.invoke() != true) {
//            backStack.add(AuthRoutes.SelectMethod)
//        } else {
//            backStack.add(route)
//        }
//    }

    fun goBack() {
        backStack.removeLastOrNull()
    }
}

@Composable
fun rememberNavigator(
    configuration: SavedStateConfiguration,
    vararg initialStack: NavKey,
): Navigator {
    require(configuration.serializersModule != SavedStateConfiguration.DEFAULT.serializersModule) {
        "You must pass a `SavedStateConfiguration.serializersModule` configured to handle " +
                "`NavKey` open polymorphism. Define it with: `polymorphic(NavKey::class) { ... }`"
    }
    val navigator = rememberSerializable(
        configuration = configuration,
        serializer = NavigatorSerializer(PolymorphicSerializer(NavKey::class)),
    ) {
        Navigator(initialStack.toList())
    }
    return navigator
}

class NavigatorSerializer<T : NavKey>(
    elementSerializer: KSerializer<T>
) : KSerializer<Navigator> {

    private val delegate = SnapshotStateListSerializer(elementSerializer)

    @OptIn(ExperimentalSerializationApi::class)
    override val descriptor: SerialDescriptor =
        SerialDescriptor(
            "com.example.Navigator",
            delegate.descriptor
        )

    override fun serialize(
        encoder: Encoder,
        value: Navigator
    ) {
        encoder.encodeSerializableValue(
            delegate,
            value.backStack as SnapshotStateList<T>
        )
    }

    override fun deserialize(decoder: Decoder): Navigator {
        val restoredStack = decoder.decodeSerializableValue(delegate)
        return Navigator(restoredStack)
    }
}