package com.example.firebaseimpl.data

import com.russhwolf.settings.Settings
import io.mockative.Mockable

@Mockable(RoomSettings::class, Settings::class)
internal class RoomSettings(private val settings: Settings) {
    var roomId: String?
        get() = settings.getStringOrNull(ROOM_ID_KEY)
        set(value) {
            if (value == null) {
                settings.remove(ROOM_ID_KEY)
            } else {
                settings.putString(
                    ROOM_ID_KEY,
                    value
                )
            }
        }

    companion object {
        private const val ROOM_ID_KEY = "room_id"
    }
}