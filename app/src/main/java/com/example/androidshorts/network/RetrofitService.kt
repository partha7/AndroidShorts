package com.example.androidshorts.network


import kotlinx.coroutines.Deferred
import retrofit2.http.GET


interface VideoPlaylistService {
    @GET("videoplaylist.json")
    fun getPlaylist(): Deferred<NetworkVideoContainer>
}

