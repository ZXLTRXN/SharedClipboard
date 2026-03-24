import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.saveable.rememberSerializable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.navigation3.runtime.NavKey
import androidx.savedstate.compose.serialization.serializers.SnapshotStateListSerializer
import androidx.savedstate.serialization.SavedStateConfiguration
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.PolymorphicSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

class Navigator(
    initialStack: List<NavKey>,
) {
    constructor(startDestination: NavKey) : this(listOf(startDestination))

    val backStack: SnapshotStateList<NavKey> = mutableStateListOf(*initialStack.toTypedArray())

    fun goTo(route: NavKey) {
        if (backStack.lastOrNull() == route) return
        backStack.add(route)
    }

    fun clearAndGoTo(route: NavKey) {
        backStack.removeLastOrNull()
        backStack.add(route)
    }

    fun goBack() {
        backStack.removeLastOrNull()
    }
}

//@Composable
//public fun rememberNavigator(
//    configuration: SavedStateConfiguration,
//    vararg elements: NavKey,
//): Navigator {
//
//    val stack = rememberSerializable(
//        configuration = configuration,
//        serializer = NavBackStackSerializer(PolymorphicSerializer(NavKey::class)),
//    ) {
//        NavBackStack(*elements)
//    }
//    return Navigator(stack)
//}

@Composable
public fun rememberNavigator(
    configuration: SavedStateConfiguration,
    vararg initialStack: NavKey,
): Navigator {
    // Проверка на наличие модуля сериализации, как в вашем примере
    require(configuration.serializersModule != SavedStateConfiguration.DEFAULT.serializersModule) {
        "You must pass a `SavedStateConfiguration.serializersModule` configured to handle " +
                "`NavKey` open polymorphism. Define it with: `polymorphic(NavKey::class) { ... }`"
    }

    return rememberSerializable(
        configuration = configuration,
        serializer = NavigatorSerializer(PolymorphicSerializer(NavKey::class)),
    ) {
        Navigator(initialStack.toList())
    }
}

public class NavigatorSerializer<T : NavKey>(
    private val elementSerializer: KSerializer<T>
) : KSerializer<Navigator> {

    // Используем тот же SnapshotStateListSerializer для управления списком NavKey
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
        // Сериализуем внутреннее состояние backStack (SnapshotStateList)
        encoder.encodeSerializableValue(
            delegate,
            value.backStack as SnapshotStateList<T>
        )
    }

    override fun deserialize(decoder: Decoder): Navigator {
        // Десериализуем список и передаем его в основной конструктор
        val restoredStack = decoder.decodeSerializableValue(delegate)
        return Navigator(restoredStack)
    }
}