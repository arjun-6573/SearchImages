package com.example.searchimages.domain.entity

data class ImageInfoEntity(
    val user: UserEntity,
    val thumbnail: String,
    val image: String,
    val likes: Long,
    val comments: Long,
    val views: Long,
    val downloads: Long,
    val tags: List<String>,
)
