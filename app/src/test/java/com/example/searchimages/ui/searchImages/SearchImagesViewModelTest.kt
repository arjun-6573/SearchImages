package com.example.searchimages.ui.searchImages

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.searchimages.domain.entity.ImageInfoEntity
import com.example.searchimages.domain.entity.PageDataEntity
import com.example.searchimages.domain.entity.PageInfoEntity
import com.example.searchimages.domain.entity.UserEntity
import com.example.searchimages.domain.repositories.SearchRepository
import com.example.searchimages.domain.usecases.SearchImageUseCase
import com.example.searchimages.ui.base.Result
import com.example.searchimages.ui.mappers.UIDataMapper
import com.example.searchimages.ui.models.ImageItemUIModel
import com.example.searchimages.utils.TestDispatchersImpl
import com.example.searchimages.utils.captureValues
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class SearchImagesViewModelTest {
    var instantExecutorRule = InstantTaskExecutorRule()
        @Rule get

    private val mainThreadSurrogate = newSingleThreadContext("UI thread")

    @MockK
    private lateinit var repository: SearchRepository
    private lateinit var searchViewModel: SearchImagesViewModel
    private val pageInfo = PageInfoEntity(1, 10)
    private val imageList = listOf(
        ImageInfoEntity(
            "1",
            UserEntity("1", "A", "P1"),
            "T", "I", 1000, 10000,
            100000, 1000000, listOf("tag")
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
    fun setUp() {
        Dispatchers.setMain(mainThreadSurrogate)
        MockKAnnotations.init(this)
        every { repository.searchImage(any(), any()) } returns flow {
            emit(
                PageDataEntity(
                    imageList,
                    pageInfo
                )
            )
        }
        searchViewModel = SearchImagesViewModel(
            TestDispatchersImpl(),
            UIDataMapper(),
            SearchImageUseCase(repository)
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun testDefaultSearch() {
        assertEquals("fruits", searchViewModel.searchStream.value)
        searchViewModel.searchList.observeForever { assertEquals(2, it.size) }
        searchViewModel.isSearchListEmpty.observeForever { assertEquals(false, it) }
    }

    @Test
    fun testSearchImagesWithSuccess() {
        val loadingValues = searchViewModel.isLoading().captureValues()
        searchViewModel.searchImages("data")
        assertEquals(false, loadingValues[0])
        assertEquals(true, loadingValues[1])
        assertEquals(false, loadingValues[2])
        searchViewModel.searchList.observeForever {
            assertEquals(2, it.size)
            assertEquals("1.0 k", it[0].likesCount)
            assertEquals("10.0 k", it[0].commentsCount)
            assertEquals("100.0 k", it[0].viewsCount)
            assertEquals("1.0 M", it[0].downloadsCount)
            assertEquals("#Tag", it[0].tags.first())
        }
        searchViewModel.isSearchListEmpty.observeForever {
            assertEquals(false, it)
        }
    }

    @Test
    fun testSearchImagesWithError() {
        every { repository.searchImage(any(), any()) } returns flow {
            throw Exception(
                errorMessage
            )
        }
        val loadingValues = searchViewModel.isLoading().captureValues()
        val eventValues = searchViewModel.getEvents().captureValues()
        searchViewModel.searchImages("data")

        assertEquals(false, loadingValues[0])
        assertEquals(true, loadingValues[1])
        assertEquals(false, loadingValues[2])
        assertEquals(errorMessage, (eventValues[0] as Result.Error).error)

        searchViewModel.searchList.observeForever {
            assertEquals(0, it.size)
        }

        searchViewModel.isSearchListEmpty.observeForever {
            assertEquals(true, it)
        }
    }

    @Test
    fun testPageReset() {
        searchViewModel.searchImages("fruits")
        every { repository.searchImage(any(), any()) } returns flow {
            emit(
                PageDataEntity(
                    emptyList<ImageInfoEntity>(),
                    pageInfo
                )
            )
        }
        assertEquals(2, searchViewModel.searchList.value?.size)
        searchViewModel.searchImages("data")
        assertEquals(0, searchViewModel.searchList.value?.size)
    }

    @Test
    fun testNextPageLoaded() {
        every { repository.searchImage(any(), any()) } returns flow {
            emit(
                PageDataEntity(
                    imageList,
                    pageInfo.copy(pageSize = 2)
                )
            )
        }
        searchViewModel.searchImages("fruits")
        assertEquals(2, searchViewModel.searchList.value?.size)
        searchViewModel.searchImages("fruits")
        assertEquals(4, searchViewModel.searchList.value?.size)
        searchViewModel.searchImages()
        assertEquals(6, searchViewModel.searchList.value?.size)
    }

    @Test
    fun testPaginationEnded() {
        searchViewModel.searchImages("fruits")
        assertEquals(2, searchViewModel.searchList.value?.size)
        searchViewModel.searchImages("fruits")
        assertEquals(2, searchViewModel.searchList.value?.size)
        searchViewModel.searchImages("fruits")
        assertEquals(2, searchViewModel.searchList.value?.size)
    }

    @Test
    fun testOnImageItemClick() {
        val item = ImageItemUIModel(
            "1",
            "username",
            "thumb",
            "image",
            "1.0 k ",
            "1.0 k ",
            "1.0 k ",
            "1.0 k ",
            emptyList()
        )
        searchViewModel.onImageItemClick(item)
        assertEquals(item, searchViewModel.selectedItem.value)
        searchViewModel.onDismissClick()
        assertEquals(null, searchViewModel.selectedItem.value)
    }
}