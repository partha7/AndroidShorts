package com.example.androidshorts.model

import com.example.androidshorts.utils.smartTruncate


//Model objects are plain Kotlin data classes that represent the things that will be used by the app.


//Represents one video
data class Video(val title: String,
                 val description: String,
                 val url: String,
                 val updated: String,
                 val thumbnail: String) {

    //Description of the video
    val shortDescription: String
        get() = description.smartTruncate(200)
}
