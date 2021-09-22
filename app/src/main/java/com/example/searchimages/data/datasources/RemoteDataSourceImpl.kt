package com.example.searchimages.data.datasources

import com.example.searchimages.data.mappers.DataMapper
import com.example.searchimages.domain.datasources.RemoteDataSource
import com.example.searchimages.domain.entity.ImageInfoEntity
import com.example.searchimages.domain.entity.PageDataEntity
import com.example.searchimages.domain.entity.PageInfoEntity
import com.example.searchimages.external.remote.MyApi
import com.example.searchimages.external.remote.SafeApiRequest
import com.example.searchimages.utils.dispatcher.MyDispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map

class RemoteDataSourceImpl(
    private val api: MyApi,
    private val dispatchers: MyDispatchers,
    private val mapper: DataMapper
) : SafeApiRequest(), RemoteDataSource {
    override fun searchImage(
        searchKey: String,
        pageInfo: PageInfoEntity
    ): Flow<PageDataEntity<ImageInfoEntity>> {
        return flow {
            val response =
                apiRequest { api.searchImages(searchKey, pageInfo.pageNo, pageInfo.pageSize) }
            emit(response)
        }.flowOn(dispatchers.IO).map { mapper.toSearchImagePageData(it, pageInfo) }
            .flowOn(dispatchers.Computation)
    }
}