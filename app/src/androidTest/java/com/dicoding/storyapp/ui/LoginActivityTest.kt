package com.dicoding.storyapp.ui

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.dicoding.storyapp.R
import com.dicoding.storyapp.view.login.LoginActivity
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4ClassRunner::class)
class LoginActivityTest {

    @Before
    fun setup() {
        ActivityScenario.launch(LoginActivity::class.java)
    }

    @Test
    fun testView() {
        Espresso.onView(ViewMatchers.withId(R.id.imageView))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(R.id.titleTextView))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(R.id.emailTextView))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(R.id.emailEditTextLayout))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(R.id.ed_login_email))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(R.id.passwordTextView))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(R.id.passwordEditTextLayout))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(R.id.ed_login_password))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(R.id.loginButton))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun testPerform() {
        Espresso.onView(ViewMatchers.withId(R.id.loginButton)).perform(ViewActions.click())
        setup()
        Espresso.onView(ViewMatchers.withId(R.id.createTextView)).perform(ViewActions.click())
    }

    @Test
    fun testLogin() {
        Espresso.onView(ViewMatchers.withId(R.id.ed_login_email))
            .perform(ViewActions.typeText("Sidiq@gmail.com"), ViewActions.closeSoftKeyboard())
        Espresso.onView(ViewMatchers.withId(R.id.ed_login_password))
            .perform(ViewActions.typeText("Sidiq010902"), ViewActions.closeSoftKeyboard())
        Espresso.onView(ViewMatchers.withId(R.id.loginButton)).perform(ViewActions.click())
    }

}