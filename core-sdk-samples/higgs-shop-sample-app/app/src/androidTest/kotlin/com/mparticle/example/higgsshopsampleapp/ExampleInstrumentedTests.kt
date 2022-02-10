package com.mparticle.example.higgsshopsampleapp

import androidx.test.ext.junit.rules.activityScenarioRule
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.filters.LargeTest
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.platform.app.InstrumentationRegistry
import com.mparticle.example.higgsshopsampleapp.activities.LandingActivity
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4ClassRunner::class)
@LargeTest
class ExampleInstrumentedTests {

    @get:Rule
    var activityScenarioRule = activityScenarioRule<LandingActivity>()

    @Test
    fun testUseAppContext() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.mparticle.example.higgsshopsampleapp", appContext.packageName)
    }

    @Test
    fun testShowLandingCTA() {
        onView(withId(R.id.landing_cta)).check(matches(withText(R.string.landing_cta)))
    }
}