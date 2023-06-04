package com.dicoding.storyapp.ui

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.dicoding.storyapp.R
import com.dicoding.storyapp.view.account.AccountActivity
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4ClassRunner::class)
class AccountActivityTest {

    @Before
    fun setup() {
        ActivityScenario.launch(AccountActivity::class.java)
    }

    @Test
    fun testView() {
        Espresso.onView(withId(R.id.hello_title))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(withId(R.id.name)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(withId(R.id.theme_title))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(withId(R.id.theme_dark))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(withId(R.id.switch_theme))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(withId(R.id.theme_light))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(withId(R.id.notif_title))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(withId(R.id.btn_set_repeating_alarm))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(withId(R.id.btn_cancel_repeating_alarm))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(withId(R.id.lang_title))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(withId(R.id.btn_set_language))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(withId(R.id.btn_logout))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun testPerform() {
        Espresso.onView(withId(R.id.switch_theme)).perform(ViewActions.click())
        Espresso.onView(withId(R.id.btn_set_repeating_alarm)).perform(ViewActions.click())
        Espresso.onView(withId(R.id.btn_cancel_repeating_alarm)).perform(ViewActions.click())
        Espresso.onView(withId(R.id.btn_set_language)).perform(ViewActions.click())
        setup()
        Espresso.onView(withId(R.id.btn_logout)).perform(ViewActions.click())
    }

}