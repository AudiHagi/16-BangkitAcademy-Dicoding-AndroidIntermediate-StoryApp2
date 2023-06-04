package com.dicoding.storyapp.ui

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.dicoding.storyapp.R
import com.dicoding.storyapp.view.insertstory.InsertStoryActivity
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4ClassRunner::class)
class InsertStoryActivityTest {

    @Before
    fun setup() {
        ActivityScenario.launch(InsertStoryActivity::class.java)
    }

    @Test
    fun testView() {
        Espresso.onView(ViewMatchers.withId(R.id.previewImageView))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(R.id.cameraButton))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(R.id.galleryButton))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(R.id.checkBox_Location))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(R.id.descEditTextLayout))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(R.id.ed_add_description))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(R.id.uploadButton))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun testPerform() {
        Espresso.onView(ViewMatchers.withId(R.id.cameraButton)).perform(ViewActions.click())
        setup()
        Espresso.onView(ViewMatchers.withId(R.id.galleryButton)).perform(ViewActions.click())
        setup()
        Espresso.onView(ViewMatchers.withId(R.id.uploadButton)).perform(ViewActions.click())
    }

}