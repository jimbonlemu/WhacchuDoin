package com.jimbonlemu.whacchudoin.data.local.story

import androidx.room.Database
import androidx.room.RoomDatabase
import com.jimbonlemu.whacchudoin.data.local.remote_keys.RemoteKeys
import com.jimbonlemu.whacchudoin.data.local.remote_keys.RemoteKeysDao
import com.jimbonlemu.whacchudoin.data.network.dto.Story

@Database(entities = [Story::class, RemoteKeys::class], version = 2, exportSchema = false)
abstract class StoryDatabase : RoomDatabase() {

    abstract fun storyDao(): StoryDao
    abstract fun remoteKeysDao(): RemoteKeysDao

}