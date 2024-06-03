package com.mfitrahrmd.story.data.repository.cache.room.dao.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.mfitrahrmd.story.data.entity.db.DBStory
import com.mfitrahrmd.story.data.repository.cache.room.dao.StoryDao

@TypeConverters(Converters::class)
@Database(
    entities = [DBStory::class],
    version = 1
)
abstract class StoryDatabase : RoomDatabase() {
    abstract fun storyDao(): StoryDao

    companion object {
        @Volatile
        private var INSTANCE: StoryDatabase? = null

        @JvmStatic
        fun getInstance(context: Context): StoryDatabase {
            if (INSTANCE == null) {
                synchronized(StoryDatabase::class.java) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        StoryDatabase::class.java,
                        "story_database"
                    ).build()
                }
            }

            return INSTANCE!!
        }
    }

}