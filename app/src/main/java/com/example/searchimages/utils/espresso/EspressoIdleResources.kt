package com.example.searchimages.utils.espresso

import androidx.test.espresso.idling.CountingIdlingResource

interface EspressoIdleResources {
    val countingIdlingResource: CountingIdlingResource
    fun increment()
    fun decrement()
}