package com.example.androidshorts.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.example.androidshorts.database.VideosDatabase
import com.example.androidshorts.database.convertToModelObjects
import com.example.androidshorts.model.Video
import com.example.androidshorts.network.Network
import com.example.androidshorts.network.toDatabaseModels
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AndroidShortsRepository(private val database: VideosDatabase){

    val videos: LiveData<List<Video>> =
        Transformations.map(database.videoDao.getAllVideos()){
            it.convertToModelObjects()
        }

    suspend fun refreshVideos(){
        withContext(Dispatchers.IO){
            val playlist = Network.videoPlaylist.getPlaylist().await()
            database.videoDao.insertAllVideos(*playlist.toDatabaseModels())
        }
    }
}