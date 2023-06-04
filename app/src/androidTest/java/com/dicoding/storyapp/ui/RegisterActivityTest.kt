package com.dicoding.storyapp.ui

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.dicoding.storyapp.R
import com.dicoding.storyapp.view.register.RegisterActivity
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4ClassRunner::class)
class RegisterActivityTest {

    @Before
    fun setup() {
        ActivityScenario.launch(RegisterActivity::class.java)
    }

    @Test
    fun testView() {
        Espresso.onView(ViewMatchers.withId(R.id.imageView)).check(
            ViewAssertions.matches(
                ViewMatchers.isDisplayed()
            )
        )
        Espresso.onView(ViewMatchers.withId(R.id.titleTextView)).check(
            ViewAssertions.matches(
                ViewMatchers.isDisplayed()
            )
        )
        Espresso.onView(ViewMatchers.withId(R.id.nameTextView)).check(
            ViewAssertions.matches(
                ViewMatchers.isDisplayed()
            )
        )
        Espresso.onView(ViewMatchers.withId(R.id.nameEditTextLayout)).check(
            ViewAssertions.matches(
                ViewMatchers.isDisplayed()
            )
        )
        Espresso.onView(ViewMatchers.withId(R.id.ed_register_name)).check(
            ViewAssertions.matches(
                ViewMatchers.isDisplayed()
            )
        )
        Espresso.onView(ViewMatchers.withId(R.id.emailTextView)).check(
            ViewAssertions.matches(
                ViewMatchers.isDisplayed()
            )
        )
        Espresso.onView(ViewMatchers.withId(R.id.emailEditTextLayout)).check(
            ViewAssertions.matches(
                ViewMatchers.isDisplayed()
            )
        )
        Espresso.onView(ViewMatchers.withId(R.id.ed_register_email)).check(
            ViewAssertions.matches(
                ViewMatchers.isDisplayed()
            )
        )
        Espresso.onView(ViewMatchers.withId(R.id.passwordTextView)).check(
            ViewAssertions.matches(
                ViewMatchers.isDisplayed()
            )
        )
        Espresso.onView(ViewMatchers.withId(R.id.passwordEditTextLayout)).check(
            ViewAssertions.matches(
                ViewMatchers.isDisplayed()
            )
        )
        Espresso.onView(ViewMatchers.withId(R.id.ed_register_password)).check(
            ViewAssertions.matches(
                ViewMatchers.isDisplayed()
            )
        )
        Espresso.onView(ViewMatchers.withId(R.id.registerButton)).check(
            ViewAssertions.matches(
                ViewMatchers.isDisplayed()
            )
        )
    }

    @Test
    fun testPerform() {
        Espresso.onView(ViewMatchers.withId(R.id.registerButton)).perform(ViewActions.click())
    }

    @Test
    fun testRegister() {
        Espresso.onView(ViewMatchers.withId(R.id.ed_register_name))
            .perform(ViewActions.typeText("Reviewer Ganteng"), ViewActions.closeSoftKeyboard())
        Espresso.onView(ViewMatchers.withId(R.id.ed_register_email))
            .perform(
                ViewActions.typeText("ReviewerGanteng@gmail.com"),
                ViewActions.closeSoftKeyboard()
            )
        Espresso.onView(ViewMatchers.withId(R.id.ed_register_password))
            .perform(ViewActions.typeText("ReviewerGanteng010902"), ViewActions.closeSoftKeyboard())
        Espresso.onView(ViewMatchers.withId(R.id.registerButton)).perform(ViewActions.click())
    }

}