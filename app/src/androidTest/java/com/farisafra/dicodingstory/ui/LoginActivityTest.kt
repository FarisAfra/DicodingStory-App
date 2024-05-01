package com.farisafra.dicodingstory.ui

import androidx.test.core.app.ActivityScenario
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import com.farisafra.dicodingstory.R

@RunWith(AndroidJUnit4ClassRunner::class)
class LoginActivityTest {
    private val dummyEmail = "testingui@dicoding.com"
    private val dummyPassword = "Testing1"

    private val dummyEmailFake = "asdwafawcw@gmail.com"
    private val dummyPasswordFake = "asdwadacd1"

    private val dummyLoginInvalid = " "

    @Before
    fun setup(){
        ActivityScenario.launch(LoginActivity::class.java)
    }

    @Test
    fun assertGetLoginSuccess() {
        onView(withId(R.id.ed_login_email)).perform(typeText(dummyEmail), closeSoftKeyboard())
        onView(withId(R.id.ed_login_password)).perform(typeText(dummyPassword), closeSoftKeyboard())

        onView(withId(R.id.BtnLogin)).check(matches(isDisplayed()))
        onView(withId(R.id.BtnLogin)).perform(click())

        ActivityScenario.launch(MainActivity::class.java)
        onView(withId(R.id.btnProfil)).check(matches(isDisplayed()))
        onView(withId(R.id.btnProfil)).perform(click())

        ActivityScenario.launch(ProfileActivity::class.java)
        onView(withId(R.id.action_logout)).check(matches(isDisplayed()))
        onView(withId(R.id.action_logout)).perform(click())
    }

    @Test
    fun assertGetLoginFailed() {
        onView(withId(R.id.ed_login_email)).perform(typeText(dummyEmailFake), closeSoftKeyboard())
        onView(withId(R.id.ed_login_password)).perform(typeText(dummyPasswordFake), closeSoftKeyboard())

        onView(withId(R.id.BtnLogin)).check(matches(isDisplayed()))
        onView(withId(R.id.BtnLogin)).perform(click())

        Thread.sleep(1000)
    }

    @Test
    fun assertGetLoginInvalid() {
        onView(withId(R.id.ed_login_email)).perform(typeText(dummyLoginInvalid), closeSoftKeyboard())
        onView(withId(R.id.ed_login_password)).perform(typeText(dummyLoginInvalid), closeSoftKeyboard())

        onView(withId(R.id.BtnLogin)).check(matches(isDisplayed()))
        onView(withId(R.id.BtnLogin)).perform(click())

        Thread.sleep(1000)
    }
}