package com.example.androidshorts.viewmodels

import android.app.Application
import androidx.lifecycle.*
import com.example.androidshorts.database.getDBInstance
import com.example.androidshorts.model.Video
import com.example.androidshorts.network.Network
import com.example.androidshorts.network.convertToModelObjects
import com.example.androidshorts.repository.AndroidShortsRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import java.io.IOException

class AndroidShortsViewModel(application: Application) : AndroidViewModel(application) {

    private val viewModelJob = SupervisorJob()

    private val viewModelScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    private val database = getDBInstance(application)

    private val videosRepository = AndroidShortsRepository(database)

    init {
        viewModelScope.launch {
            videosRepository.refreshVideos()
        }
    }

    val playlist = videosRepository.videos

//  Factory for constructing AndroidShortsViewModel with parameter
    class Factory(val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(AndroidShortsViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return AndroidShortsViewModel(app) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}
