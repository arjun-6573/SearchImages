package com.example.searchimages.utils

import com.example.searchimages.utils.dispatcher.MyDispatchers
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher

@ExperimentalCoroutinesApi
class TestDispatchersImpl : MyDispatchers {
    override val IO: CoroutineDispatcher = TestCoroutineDispatcher()
    override val Main: CoroutineDispatcher = TestCoroutineDispatcher()
    override val Computation: CoroutineDispatcher = TestCoroutineDispatcher()
}