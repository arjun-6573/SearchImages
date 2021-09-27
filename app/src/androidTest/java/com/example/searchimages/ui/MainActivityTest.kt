package com.example.searchimages.ui

import android.content.Context
import androidx.test.espresso.Espresso
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.platform.app.InstrumentationRegistry
import com.example.searchimages.ui.searchImages.ImagesAdapter
import com.example.searchimages.utils.Ids
import com.example.searchimages.utils.Strings
import com.example.searchimages.utils.espresso.EspressoIdleResources
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
@LargeTest
class MainActivityTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @get: Rule
    var activityRule = ActivityScenarioRule(MainActivity::class.java)

    @Inject
    lateinit var espressoIdleResourceImpl: EspressoIdleResources

    private var context: Context? = null

    @Before
    fun setUp() {
        hiltRule.inject()
        IdlingRegistry.getInstance().register(espressoIdleResourceImpl.countingIdlingResource)
        context = InstrumentationRegistry.getInstrumentation().targetContext
    }

    @After
    fun tearDown() {
        IdlingRegistry.getInstance().unregister(espressoIdleResourceImpl.countingIdlingResource)
        context = null
    }

    @Test
    fun testFruitsSearchDataVisible() {
        Espresso.onView(ViewMatchers.withId(Ids.txtNoData))
            .check(ViewAssertions.matches(ViewMatchers.withEffectiveVisibility(ViewMatchers.Visibility.GONE)))
    }

    @Test
    fun testNoDataFoundVisible() {
        Espresso.onView(ViewMatchers.withId(Ids.etSearchModel))
            .perform(ViewActions.clearText(),ViewActions.typeText("."))
        Espresso.onView(ViewMatchers.withId(Ids.txtNoData))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun testImageDetailsIsShowing() {
        Espresso.onView(ViewMatchers.withId(Ids.rvImages)).perform(
            RecyclerViewActions.actionOnItemAtPosition<ImagesAdapter.ItemImageViewHolder>(
                0,
                ViewActions.click()
            )
        )
        Espresso.onView(ViewMatchers.withText(context?.getString(Strings.do_you_want_to_view_details)))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        Espresso.onView(ViewMatchers.withId(android.R.id.button1))
            .perform(ViewActions.click())

        Espresso.onView(ViewMatchers.withId(Ids.layoutImageDetails))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        Espresso.pressBack()

        Espresso.onView(ViewMatchers.withId(Ids.layoutFragmentSearch))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }
}