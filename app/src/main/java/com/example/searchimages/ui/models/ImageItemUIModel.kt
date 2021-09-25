package com.example.searchimages.ui.models

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
)
