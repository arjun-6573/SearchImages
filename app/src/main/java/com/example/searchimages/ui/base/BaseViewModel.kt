package com.example.searchimages.ui.base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.searchimages.utils.SingleLiveEvent
import com.example.searchimages.utils.dispatcher.MyDispatchers
import kotlinx.coroutines.*

open class BaseViewModel(myDispatchers: MyDispatchers) : ViewModel() {
    private var loadingState = MutableLiveData(false)
    private val job = SupervisorJob()
    protected val uiScope = CoroutineScope(myDispatchers.Main + job)
    protected val event = SingleLiveEvent<Result<String>>()

    fun isLoading() = loadingState
    fun getEvents() = event
    fun setLoading(loading: Boolean) {
        loadingState.postValue(loading)
    }

    override fun onCleared() {
        super.onCleared()
        uiScope.cancel()
    }
}