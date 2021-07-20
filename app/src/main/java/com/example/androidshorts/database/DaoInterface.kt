package com.example.androidshorts.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface DaoInterface {

    @Query("SELECT * FROM videodatabase")
    fun getAllVideos(): LiveData<List<VideoDatabase>>


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllVideos(vararg videos: VideoDatabase)
}