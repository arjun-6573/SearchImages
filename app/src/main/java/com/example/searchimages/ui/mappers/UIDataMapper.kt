package com.example.searchimages.ui.mappers

import com.example.searchimages.domain.entity.ImageInfoEntity
import com.example.searchimages.domain.entity.PageInfoEntity
import com.example.searchimages.ui.models.ImageItemUIModel
import com.example.searchimages.utils.AppConstants
import com.example.searchimages.utils.toKFormat
import com.example.searchimages.utils.toTagFormat

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
                toKFormat(it.likes),
                toKFormat(it.comments),
                toKFormat(it.views),
                toKFormat(it.downloads),
                it.tags.map { tagName -> toTagFormat(tagName) })
        }
    }
}