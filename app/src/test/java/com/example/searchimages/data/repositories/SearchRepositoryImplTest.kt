package com.example.searchimages.data.repositories

import com.example.searchimages.domain.datasources.RemoteDataSource
import com.example.searchimages.domain.entity.ImageInfoEntity
import com.example.searchimages.domain.entity.PageDataEntity
import com.example.searchimages.domain.entity.PageInfoEntity
import com.example.searchimages.domain.entity.UserEntity
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class SearchRepositoryImplTest {
    @MockK
    private lateinit var remoteDataSource: RemoteDataSource
    private lateinit var repositoryImpl: SearchRepositoryImpl
    private val pageInfo = PageInfoEntity(1, 10)
    private val imageList = listOf(
        ImageInfoEntity(
            "1",
            UserEntity("1", "A", "P1"),
            "T", "I", 0, 0,
            0, 0, listOf("tag")
        ),
        ImageInfoEntity(
            "2",
            UserEntity("2", "B", "P2"),
            "T2", "I2", 1, 1,
            1, 1, listOf("tag")
        )
    )
    private val errorMessage = "No internet"

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        repositoryImpl = SearchRepositoryImpl(remoteDataSource)
    }


    @Test
    fun testSearchImage() = runBlocking {
        every { remoteDataSource.searchImage(any(), any()) } returns flow {
            emit(
                PageDataEntity(
                    imageList,
                    pageInfo
                )
            )
        }
        repositoryImpl
            .searchImage("", pageInfo)
            .last()
            .let {
                assertEquals(imageList, it.data)
                assertEquals(pageInfo, it.pageInfo)
            }
    }

    @Test
    fun testSearchImageWithError() = runBlocking {
        every { remoteDataSource.searchImage(any(), any()) } returns flow {
            throw Exception(
                errorMessage
            )
        }
        repositoryImpl
            .searchImage("", pageInfo)
            .catch { assertEquals(errorMessage, it.message) }
            .collect { }
    }
}