package com.example.mobilemandatoryassignment

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.mobilemandatoryassignment.view.MainActivity

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import org.junit.Rule
import java.util.regex.Pattern.matches

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    @get:Rule
    var activityRule: ActivityScenarioRule<MainActivity> =
        ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.example.mobilemandatoryassignment", appContext.packageName)

        onView(withText("Login authentication")).check(matches(isDisplayed()))

        onView(withId(R.id.emailInputField))
            .perform(clearText())
            .perform(typeText("123@gmail.com"))

        onView(withId(R.id.passwordInputField))
            .perform(clearText())
            .perform(typeText("123123"))
            .perform(closeSoftKeyboard())

        onView(withId(R.id.button_sign_in)).perform(click())
        pause(1000)

        onView(withText("What would you like to do?")).check(matches(isDisplayed()))

        onView(withId(R.id.button_view_feed)).perform(click())
        pause(1000)

        onView(withText("Recent messages:")).check(matches(isDisplayed()))
        pause(1000)

        openActionBarOverflowOrOptionsMenu(ApplicationProvider.getApplicationContext<Context>())
        onView(withText("Home")).perform(click())
        pause(1000)

        onView(withText("What would you like to do?")).check(matches(isDisplayed()))
        pause(1000)

        onView(withId(R.id.add_message_input))
            .perform(clearText())
            .perform(typeText("UI Espresso test message"))
            .perform(closeSoftKeyboard())

        onView(withId(R.id.button_add_message)).perform(click())
        pause(1000)

        openActionBarOverflowOrOptionsMenu(ApplicationProvider.getApplicationContext<Context>())
        onView(withText("Home")).perform(click())
        pause(1000)

        onView(withId(R.id.button_view_feed)).perform(click())
        pause(5000)

    }

    private fun pause(millis: Long) {
        try {
            Thread.sleep(millis)
            // https://www.repeato.app/android-espresso-why-to-avoid-thread-sleep/
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
    }


}