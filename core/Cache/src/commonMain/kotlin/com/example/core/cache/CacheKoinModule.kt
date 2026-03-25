package com.example.core.cache

import com.example.core.cache.db.ClipboardQueries
import com.example.core.cache.db.Database
import org.koin.dsl.module

val cacheModule = module {
    includes(platformCacheModule)
    single {
        val driverFactory: DatabaseDriverFactory = get()
        Database(driverFactory.createDriver())
    }

    single<ClipboardQueries> { get<Database>().clipboardQueries }
}