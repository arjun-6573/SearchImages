package com.example.searchimages.data.repositories

import com.example.searchimages.domain.datasources.RemoteDataSource
import com.example.searchimages.domain.entity.ImageInfoEntity
import com.example.searchimages.domain.entity.PageDataEntity
import com.example.searchimages.domain.entity.PageInfoEntity
import com.example.searchimages.domain.repositories.SearchRepository
import kotlinx.coroutines.flow.Flow

class SearchRepositoryImpl(
    private val remoteDataSource: RemoteDataSource,
) : SearchRepository {

    override fun searchImage(
        searchKey: String,
        pageInfo: PageInfoEntity
    ): Flow<PageDataEntity<ImageInfoEntity>> {
        return remoteDataSource.searchImage(searchKey, pageInfo)
    }
}