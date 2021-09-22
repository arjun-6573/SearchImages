package com.example.searchimages.domain.datasources

import com.example.searchimages.domain.entity.ImageInfoEntity
import com.example.searchimages.domain.entity.PageDataEntity
import com.example.searchimages.domain.entity.PageInfoEntity
import kotlinx.coroutines.flow.Flow

interface RemoteDataSource {
    fun searchImage(searchKey: String, pageInfo: PageInfoEntity): Flow<PageDataEntity<ImageInfoEntity>>
}