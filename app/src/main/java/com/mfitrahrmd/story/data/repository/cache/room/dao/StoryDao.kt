package com.mfitrahrmd.story.data.repository.cache.room.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mfitrahrmd.story.data.entity.db.DBStory

@Dao
abstract class StoryDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract fun insertOne(story: DBStory)

    @Insert
    abstract suspend fun insertMany(items: List<DBStory>)

    @Query("SELECT * FROM stories ORDER BY dbId")
    abstract fun findAll(): PagingSource<Int, DBStory>

    @Delete
    abstract suspend fun deleteOne(story: DBStory)

    @Query("DELETE FROM stories")
    abstract suspend fun deleteAll()
}