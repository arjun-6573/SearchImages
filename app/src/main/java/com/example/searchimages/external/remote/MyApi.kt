package com.example.searchimages.external.remote

import com.example.searchimages.external.remote.reponse.SearchAPIResponse
import retrofit2.Response
import retrofit2.http.*


interface MyApi {
    @GET("image_type=photo")
    suspend fun searchImages(
        @Query("q") searchKey: String,
        @Query("page") pageNo: Int,
        @Query("per_page") pageSize: Int
    ): Response<SearchAPIResponse>
}