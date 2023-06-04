package com.dicoding.storyapp.ui

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.dicoding.storyapp.R
import com.dicoding.storyapp.view.liststory.ListStoryActivity
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4ClassRunner::class)
class ListStoryActivityTest {

    @Before
    fun setup() {
        ActivityScenario.launch(ListStoryActivity::class.java)
    }

    @Test
    fun testView() {
        Espresso.onView(ViewMatchers.withId(R.id.rv_liststory))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(R.id.fb_add))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(R.id.menu_account))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(R.id.menu_maps))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(R.id.menu_refresh))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun testPerform() {
        Espresso.onView(ViewMatchers.withId(R.id.rv_liststory)).perform(ViewActions.click())
        setup()
        Espresso.onView(ViewMatchers.withId(R.id.fb_add)).perform(ViewActions.click())
        setup()
        Espresso.onView(ViewMatchers.withId(R.id.menu_refresh)).perform(ViewActions.click())
        setup()
        Espresso.onView(ViewMatchers.withId(R.id.menu_maps)).perform(ViewActions.click())
        setup()
        Espresso.onView(ViewMatchers.withId(R.id.menu_account)).perform(ViewActions.click())
    }

}