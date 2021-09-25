package com.example.searchimages.ui.searchImages

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.searchimages.domain.usecases.SearchImageUseCase
import com.example.searchimages.ui.base.BaseViewModel
import com.example.searchimages.ui.base.Result
import com.example.searchimages.ui.mappers.UIDataMapper
import com.example.searchimages.ui.models.ImageItemUIModel
import com.example.searchimages.utils.dispatcher.MyDispatchers
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject


@HiltViewModel
class SearchImagesViewModel @Inject constructor(
    private val myDispatchers: MyDispatchers,
    private val uiDataMapper: UIDataMapper,
    private val searchImageUseCase: SearchImageUseCase
) :
    BaseViewModel(myDispatchers) {

    val searchStream = MutableLiveData("fruits")

    private val _searchList = MutableLiveData<ArrayList<ImageItemUIModel>>()
    val searchList: LiveData<ArrayList<ImageItemUIModel>> = _searchList
    private var pageNo: Int = 0
    private var searchedKey: String? = null
    private var pagingEnd: Boolean = false

    init {
        searchStream.observeForever { searchKey ->
            searchKey?.let { searchImages(it) }
        }
    }


    fun searchImages(key: String = searchedKey.orEmpty()) = uiScope.launch {
        if (searchedKey != key) {
            resetPage()
            setLoading(true)
        }
        if (pagingEnd) {
            return@launch
        }
        searchedKey = key
        searchImageUseCase.invoke(key, uiDataMapper.toSearchImagePageInfo(++pageNo))
            .onCompletion { setLoading(false) }
            .catch {
                event.postValue(Result.Error(it.message.orEmpty()))
                Timber.e(it)
            }
            .map {
                pagingEnd = it.data.size < it.pageInfo.pageSize
                pageNo = it.pageInfo.pageNo
                uiDataMapper.toImageItemUIModel(it.data)
            }
            .flowOn(myDispatchers.Computation)
            .collect {
                updateSearchData(it)
            }
    }

    private fun updateSearchData(dataList: List<ImageItemUIModel>) {
        if (_searchList.value.isNullOrEmpty()) {
            _searchList.value = ArrayList(dataList)
        } else {
            _searchList.value?.addAll(dataList)
            _searchList.value = _searchList.value
        }
    }

    private fun resetPage() {
        _searchList.value?.clear()
        pagingEnd = false
        pageNo = 0
    }

}