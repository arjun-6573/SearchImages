package com.example.searchimages.utils.espresso

import androidx.test.espresso.idling.CountingIdlingResource

class EspressoIdleResourceImpl : EspressoIdleResources {
    override val countingIdlingResource = CountingIdlingResource("EspressoIdleResource")
    override fun increment() {
        countingIdlingResource.increment()
    }
    override fun decrement() {
        if (countingIdlingResource.isIdleNow.not()) {
            countingIdlingResource.decrement()
        }
    }
}