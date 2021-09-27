package com.example.searchimages.ui.searchImages

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.asFlow
import com.example.searchimages.domain.usecases.SearchImageUseCase
import com.example.searchimages.ui.base.BaseViewModel
import com.example.searchimages.ui.base.Result
import com.example.searchimages.ui.mappers.UIDataMapper
import com.example.searchimages.ui.models.ImageItemUIModel
import com.example.searchimages.utils.AppConstants
import com.example.searchimages.utils.dispatcher.MyDispatchers
import com.example.searchimages.utils.espresso.EspressoIdleResources
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject


@OptIn(FlowPreview::class)
@HiltViewModel
class SearchImagesViewModel @Inject constructor(
    private val myDispatchers: MyDispatchers,
    private val uiDataMapper: UIDataMapper,
    private val espressoIdleResources: EspressoIdleResources,
    private val searchImageUseCase: SearchImageUseCase
) :
    BaseViewModel(myDispatchers) {

    val searchStream = MutableLiveData("fruits")

    private val _searchList = MutableLiveData<ArrayList<ImageItemUIModel>>()
    val searchList: LiveData<ArrayList<ImageItemUIModel>> = _searchList
    private var pageNo: Int = 0
    private var searchedKey: String? = null
    private var pagingEnd: Boolean = false

    val isSearchListEmpty = Transformations.map(_searchList) {
        it.isNullOrEmpty()
    }

    private val _selectedItem = MutableLiveData<ImageItemUIModel?>()
    val selectedItem: LiveData<ImageItemUIModel?> = _selectedItem

    private var testingCounter = 0

    init {
        uiScope.launch {
            searchStream
                .asFlow()
                .filter {
                    espressoIdleResources.increment()
                    testingCounter++
                    true
                }
                .debounce(AppConstants.SEARCH_DELAY)
                .filter {
                    while (testingCounter > 0) {
                        testingCounter--
                        espressoIdleResources.decrement()
                    }
                    true
                }
                .distinctUntilChanged()
                .collectLatest { searchKey ->
                    searchKey?.let { searchImages(it) }
                }
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

        if (key.isEmpty()) {
            updateSearchData(emptyList())
            setLoading(false)
            return@launch
        }

        searchedKey = key
        espressoIdleResources.increment()
        searchImageUseCase.invoke(key, uiDataMapper.toSearchImagePageInfo(++pageNo))
            .onCompletion {
                setLoading(false)
                espressoIdleResources.decrement()
            }
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

    fun onImageItemClick(item: ImageItemUIModel) {
        _selectedItem.value = item
    }

    fun onDismissClick() {
        _selectedItem.value = null
    }
}