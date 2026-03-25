package com.example.core.cache

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.native.NativeSqliteDriver
import com.example.core.cache.db.Database
import org.koin.core.module.Module
import org.koin.dsl.module



class NativeDatabaseDriver() : DatabaseDriverFactory {
    override fun createDriver(): SqlDriver =
        NativeSqliteDriver(
            Database.Schema,
            "database.db"
        )
}


actual val platformCacheModule: Module
    get() = module {
        single<DatabaseDriverFactory> { NativeDatabaseDriver() }
    }