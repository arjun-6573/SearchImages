package com.example.searchimages.ui.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ImageItemUIModel(
    val id: String,
    val username: String,
    val thumbnail: String,
    val image: String,
    val likesCount: String,
    val commentsCount: String,
    val viewsCount: String,
    val downloadsCount: String,
    val tags: List<String>,
) : Parcelable
