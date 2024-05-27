package com.jimbonlemu.whacchudoin.di.feature

import com.jimbonlemu.whacchudoin.data.network.repository.StoryRepository
import org.koin.dsl.module

val storyModule = module {
    single<StoryRepository> { StoryRepository(get(), get()) }
}