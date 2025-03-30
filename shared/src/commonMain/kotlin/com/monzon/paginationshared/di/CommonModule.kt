package com.monzon.paginationshared.di

import com.monzon.paginationshared.PopularsMoviesRepository
import com.monzon.paginationshared.PopularsViewModel
import com.monzon.paginationshared.data.PopularMoviesAPI
import org.koin.core.context.startKoin
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module


val appModule = module {
    single<PopularMoviesAPI> { PopularMoviesAPI() }
    single<PopularsMoviesRepository> { PopularsMoviesRepository(api = get()) }
    viewModelOf(::PopularsViewModel)
}


fun initKoin() =
    startKoin {
        modules(
            appModule
        )
    }