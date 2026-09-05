package com.example.core.cache

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import com.example.core.cache.db.Database
import org.koin.dsl.module
import java.util.Properties
import java.nio.file.Path
import kotlin.io.path.createDirectories
import org.koin.core.module.Module


class JvmDatabaseDriver : DatabaseDriverFactory {
    override fun createDriver(): SqlDriver {
        val databaseDirectory = applicationDataDirectory().createDirectories()
        val databasePath = databaseDirectory.resolve("database.db")

        return JdbcSqliteDriver(
            "jdbc:sqlite:$databasePath",
            Properties(),
            Database.Schema
        )
    }

    private fun applicationDataDirectory(): Path {
        val userHome = Path.of(System.getProperty("user.home"))
        val osName = System.getProperty("os.name").lowercase()

        return when {
            osName.contains("mac") ->
                userHome.resolve("Library/Application Support/Shared Clipboard")

            osName.contains("win") ->
                System.getenv("APPDATA")
                    ?.takeIf(String::isNotBlank)
                    ?.let { Path.of(it) }
                    ?.resolve("Shared Clipboard")
                    ?: userHome.resolve("AppData/Roaming/Shared Clipboard")

            else ->
                System.getenv("XDG_DATA_HOME")
                    ?.takeIf(String::isNotBlank)
                    ?.let { Path.of(it) }
                    ?.resolve("shared-clipboard")
                    ?: userHome.resolve(".local/share/shared-clipboard")
        }
    }
}


actual val platformCacheModule: Module
    get() = module {
        single<DatabaseDriverFactory> { JvmDatabaseDriver() }
    }
