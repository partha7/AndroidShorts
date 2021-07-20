package com.example.androidshorts.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.androidshorts.model.Video

@Entity
data class VideoDatabase constructor(
    @PrimaryKey
    val url : String,
    val updated : String,
    val title : String,
    val description : String,
    val thumbnail: String
)

fun List<VideoDatabase>.convertToModelObjects(): List<Video>{
    return map{
        Video(
            url = it.url,
            title = it.title,
            description = it.description,
            thumbnail = it.thumbnail,
            updated = it.updated

        )

    }
}
