package com.example.searchimages.ui.mappers

import com.example.searchimages.domain.entity.ImageInfoEntity
import com.example.searchimages.domain.entity.PageInfoEntity
import com.example.searchimages.ui.models.ImageItemUIModel
import com.example.searchimages.utils.AppConstants
import com.example.searchimages.utils.toKFormat
import java.util.*

class UIDataMapper {
    fun toSearchImagePageInfo(pageNo: Int): PageInfoEntity {
        return PageInfoEntity(pageNo, AppConstants.PAGE_SIZE)
    }

    fun toImageItemUIModel(imageList: List<ImageInfoEntity>): List<ImageItemUIModel> {
        return imageList.map {
            ImageItemUIModel(
                it.id,
                it.user.name,
                it.thumbnail,
                it.image,
                it.likes.toKFormat(),
                it.comments.toKFormat(),
                it.views.toKFormat(),
                it.downloads.toKFormat(),
                it.tags.map { tagName -> toTag(tagName) })
        }
    }

    fun toTag(tagName: String): String {
        return "${AppConstants.HASH_TAG}${
            tagName.replaceFirstChar {
                if (it.isLowerCase()) it.titlecase(
                    Locale.getDefault()
                ) else it.toString()
            }
        }"
    }
}