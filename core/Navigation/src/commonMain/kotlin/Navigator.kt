import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSerializable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.navigation3.runtime.NavKey
import androidx.savedstate.compose.serialization.serializers.SnapshotStateListSerializer
import androidx.savedstate.serialization.SavedStateConfiguration
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.PolymorphicSerializer
import kotlinx.serialization.SerializationException
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.descriptors.element
import kotlinx.serialization.encoding.CompositeDecoder
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import routes.AuthRoutes
import routes.ClipboardRoutes

class Navigator(
    initialIsLoggedIn: Boolean,
    initialStack: List<NavKey>
) {
    var isLoggedIn by mutableStateOf(initialIsLoggedIn)
        private set

    val topLevelRoute by derivedStateOf {
        backStack.findLast { it is TopLevelRoute }
    }

    val showBottomBar by derivedStateOf {
        val currentKey = backStack.lastOrNull()
        isLoggedIn && (currentKey !is DisabledNavBar)
    }

    val backStack: SnapshotStateList<NavKey> = mutableStateListOf(*initialStack.toTypedArray())

    fun goTo(route: NavKey) {
        if (backStack.lastOrNull() == route) return
        addWithAuthCheck(route)
    }

    fun clearAndGoTo(route: NavKey) {
        backStack.clear()
        addWithAuthCheck(route)
    }

    fun login(performer: AuthPerformer) {
        performer.login()
        isLoggedIn = true
    }

    fun logout(performer: AuthPerformer) {
        performer.logout()
        isLoggedIn = false
        backStack.removeAll { it is NeedAuth }
        if (backStack.isEmpty()) {
            backStack.add(AuthRoutes.SelectMethod)
        }
    }


    private fun addWithAuthCheck(route: NavKey) {
        if (route is NeedAuth && !isLoggedIn) {
            backStack.add(AuthRoutes.SelectMethod)
        } else {
            backStack.add(route)
        }
    }

    fun goBack() {
        backStack.removeLastOrNull()
    }

    interface AuthPerformer {
        fun logout()
        fun login()
    }
}

@Composable
fun rememberNavigator(
    configuration: SavedStateConfiguration,
    isLoggedIn: Boolean,
    initialLoggedScreen: TopLevelRoute = ClipboardRoutes.Clipboard,
): Navigator {
    val initialRoute: NavKey = remember(isLoggedIn) {
        if (isLoggedIn) initialLoggedScreen else AuthRoutes.SelectMethod
    }

    val navigator = rememberNavigator(
        configuration,
        isLoggedIn,
        initialRoute
    )

    return navigator
}


@Composable
fun rememberNavigator(
    configuration: SavedStateConfiguration,
    isLoggedIn: Boolean,
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
        Navigator(
            isLoggedIn,
            initialStack.toList()
        )
    }
    return navigator
}

class NavigatorSerializer<T : NavKey>(
    elementSerializer: KSerializer<T>
) : KSerializer<Navigator> {

    private val delegate = SnapshotStateListSerializer(elementSerializer)

    @OptIn(ExperimentalSerializationApi::class)
    override val descriptor: SerialDescriptor =
        buildClassSerialDescriptor("com.example.Navigator") {
            element<Boolean>("isLoggedIn")
            element(
                "backStack",
                delegate.descriptor
            )
        }

    override fun serialize(
        encoder: Encoder,
        value: Navigator
    ) {
        val composite = encoder.beginStructure(descriptor)
        composite.encodeBooleanElement(
            descriptor,
            0,
            value.isLoggedIn
        )
        composite.encodeSerializableElement(
            descriptor,
            1,
            delegate,
            value.backStack as SnapshotStateList<T>
        )
        composite.endStructure(descriptor)
    }

    override fun deserialize(decoder: Decoder): Navigator {
        val composite = decoder.beginStructure(descriptor)
        var isLoggedIn = false
        var backStack: SnapshotStateList<T>? = null

        loop@ while (true) {
            when (val index = composite.decodeElementIndex(descriptor)) {
                CompositeDecoder.DECODE_DONE -> break@loop
                0 -> isLoggedIn = composite.decodeBooleanElement(
                    descriptor,
                    0
                )

                1 -> backStack = composite.decodeSerializableElement(
                    descriptor,
                    1,
                    delegate
                )

                else -> throw SerializationException("Unknown index $index")
            }
        }
        composite.endStructure(descriptor)

        val finalStack = backStack ?: throw SerializationException("BackStack is missing")
        return Navigator(
            isLoggedIn,
            finalStack
        )
    }
}