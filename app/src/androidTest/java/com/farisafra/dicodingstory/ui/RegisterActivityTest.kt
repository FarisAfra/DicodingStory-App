package com.farisafra.dicodingstory.ui

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.farisafra.dicodingstory.R
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4ClassRunner::class)
class RegisterActivityTest {

    private fun generateRandomEmail(): String {
        val allowedChars = ('A'..'Z') + ('a'..'z') + ('0'..'9')
        return (1..10)
            .map { allowedChars.random() }
            .joinToString("") + "@testing.com"
    }
    private val randomEmail = generateRandomEmail()

    private val dummyName = "Testing"
    private val dummyEmail = randomEmail
    private val dummyPassword = "Testing123"

    private val dummyEmailReal = "testingui@dicoding.com"
    private val dummyPasswordReal = "Testing1"

    private val dummyRegsiterInvalid = " "

    @Before
    fun setup(){
        ActivityScenario.launch(RegisterActivity::class.java)
    }

    @Test
    fun assertGetRegisterSuccess() {
        Espresso.onView(ViewMatchers.withId(R.id.ed_register_name))
            .perform(ViewActions.typeText(dummyName), ViewActions.closeSoftKeyboard())
        Espresso.onView(ViewMatchers.withId(R.id.ed_register_email))
            .perform(ViewActions.typeText(dummyEmail), ViewActions.closeSoftKeyboard())
        Espresso.onView(ViewMatchers.withId(R.id.ed_register_password))
            .perform(ViewActions.typeText(dummyPassword), ViewActions.closeSoftKeyboard())

        Espresso.onView(ViewMatchers.withId(R.id.BtnRegister))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(R.id.BtnRegister)).perform(ViewActions.click())

        Thread.sleep(1000)
    }

    @Test
    fun assertGetRegisterFailed() {
        Espresso.onView(ViewMatchers.withId(R.id.ed_register_name))
            .perform(ViewActions.typeText(dummyName), ViewActions.closeSoftKeyboard())
        Espresso.onView(ViewMatchers.withId(R.id.ed_register_email))
            .perform(ViewActions.typeText(dummyEmailReal), ViewActions.closeSoftKeyboard())
        Espresso.onView(ViewMatchers.withId(R.id.ed_register_password))
            .perform(ViewActions.typeText(dummyPasswordReal), ViewActions.closeSoftKeyboard())

        Espresso.onView(ViewMatchers.withId(R.id.BtnRegister))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(R.id.BtnRegister)).perform(ViewActions.click())

        Thread.sleep(1000)
    }

    @Test
    fun assertGetRegisterInvalid() {
        Espresso.onView(ViewMatchers.withId(R.id.ed_register_name))
            .perform(ViewActions.typeText(dummyRegsiterInvalid), ViewActions.closeSoftKeyboard())
        Espresso.onView(ViewMatchers.withId(R.id.ed_register_email))
            .perform(ViewActions.typeText(dummyRegsiterInvalid), ViewActions.closeSoftKeyboard())
        Espresso.onView(ViewMatchers.withId(R.id.ed_register_password))
            .perform(ViewActions.typeText(dummyRegsiterInvalid), ViewActions.closeSoftKeyboard())

        Espresso.onView(ViewMatchers.withId(R.id.BtnRegister))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(R.id.BtnRegister)).perform(ViewActions.click())

        Thread.sleep(1000)
    }
}