package com.example.searchimages.data.mappers

import com.example.searchimages.domain.entity.ImageInfoEntity
import com.example.searchimages.domain.entity.PageDataEntity
import com.example.searchimages.domain.entity.PageInfoEntity
import com.example.searchimages.domain.entity.UserEntity
import com.example.searchimages.external.remote.reponse.SearchAPIResponse

class DataMapper {
    fun toSearchImagePageData(
        responseModel: SearchAPIResponse,
        pageInfo: PageInfoEntity
    ): PageDataEntity<ImageInfoEntity> {
        return responseModel.run {
            PageDataEntity(hits.map {
                toImageInfoEntity(it)
            }, pageInfo)
        }
    }

    fun toImageInfoEntity(response: SearchAPIResponse.Hit): ImageInfoEntity {
        return response.run {
            ImageInfoEntity(
                id = "$id",
                user = toUserEntity(this),
                thumbnail = previewURL,
                image = largeImageURL,
                likes = likes.toLong(),
                comments = comments.toLong(),
                views = views.toLong(),
                downloads = downloads.toLong(),
                tags = toTags(tags)
            )
        }
    }

    fun toTags(tagString: String): List<String> {
        return tagString.split(",")
    }


    fun toUserEntity(response: SearchAPIResponse.Hit): UserEntity {
        return response.run { UserEntity("$userId", user, userImageURL) }
    }
}