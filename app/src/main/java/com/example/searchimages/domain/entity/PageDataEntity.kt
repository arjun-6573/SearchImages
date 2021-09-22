package com.example.searchimages.domain.entity

data class PageDataEntity<DATA_TYPE>(val data: List<DATA_TYPE>, val pageInfo: PageInfoEntity)
