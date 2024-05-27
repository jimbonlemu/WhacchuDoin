package com.jimbonlemu.whacchudoin.di.modules

import androidx.room.Room
import com.jimbonlemu.whacchudoin.data.local.story.StoryDatabase
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val databaseModule = module {
    single {
        Room.databaseBuilder(
            androidApplication(),
            StoryDatabase::class.java,
            "story_db"
        ).build()
    }

    single { get<StoryDatabase>().storyDao() }
    single { get<StoryDatabase>().remoteKeysDao() }
}