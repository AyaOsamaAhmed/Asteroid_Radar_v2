package com.udacity.asteroidradar

import androidx.room.PrimaryKey
import com.squareup.moshi.Json

data class PictureOfDay(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    @Json(name = "media_type")
    val mediaType: String,
    val title: String,
    val url: String)