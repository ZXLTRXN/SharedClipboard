package com.example.core.cache

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import com.example.core.cache.db.Database
import org.koin.dsl.module
import java.util.Properties
import org.koin.core.module.Module


class JvmDatabaseDriver() : DatabaseDriverFactory {
    override fun createDriver(): SqlDriver =
        JdbcSqliteDriver("jdbc:sqlite:database.db",
            Properties(), Database.Schema)
}


actual val platformCacheModule: Module
    get() = module {
        single<DatabaseDriverFactory> { JvmDatabaseDriver() }
    }