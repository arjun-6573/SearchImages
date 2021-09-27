package com.example.searchimages.utils.espresso

import androidx.test.espresso.idling.CountingIdlingResource


class FakeEspressoIdleResourceImpl : EspressoIdleResources {
    override val countingIdlingResource: CountingIdlingResource
        get() = TODO("Not yet implemented")

    override fun increment() {
    }

    override fun decrement() {
    }
}