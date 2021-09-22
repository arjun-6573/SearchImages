package com.example.searchimages.domain.usecases

import com.example.searchimages.domain.entity.ImageInfoEntity
import com.example.searchimages.domain.entity.PageDataEntity
import com.example.searchimages.domain.entity.PageInfoEntity
import com.example.searchimages.domain.repositories.SearchRepository
import kotlinx.coroutines.flow.Flow

class SearchImageUseCase(private val repository: SearchRepository) {
    fun invoke(searchKey: String, pageInfo: PageInfoEntity): Flow<PageDataEntity<ImageInfoEntity>> {
        return repository.searchImage(searchKey, pageInfo)
    }
}