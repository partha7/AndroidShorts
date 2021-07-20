package com.example.androidshorts.network

import com.example.androidshorts.database.VideoDatabase
import com.example.androidshorts.model.Video
import com.squareup.moshi.JsonClass

// Here the responses from the network requests are parsed into objects to send to the server

// Contains a list of videos
@JsonClass(generateAdapter = true)
data class NetworkVideoContainer(val videos: List<NetworkVideo>)

// Represents one video that can played
@JsonClass(generateAdapter = true)
data class NetworkVideo(
    val title: String,
    val description: String,
    val url: String,
    val updated: String,
    val thumbnail: String,
    val closedCaptions: String?)

// Conversion into model objects
fun NetworkVideoContainer.convertToModelObjects(): List<Video> {
    return videos.map {
        Video(
            title = it.title,
            description = it.description,
            url = it.url,
            updated = it.updated,
            thumbnail = it.thumbnail
        )
    }
}

fun NetworkVideoContainer.toDatabaseModels(): Array<VideoDatabase>{
    return videos.map {
        VideoDatabase(
            title = it.title,
            description = it.description,
            url = it.url,
            updated = it.updated,
            thumbnail = it.thumbnail
        )
    }.toTypedArray()
}
