package com.mparticle.example.higgsshopsampleapp

import android.content.Intent
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.activityScenarioRule
import androidx.test.filters.LargeTest
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.platform.app.InstrumentationRegistry
import com.mparticle.example.higgsshopsampleapp.activities.LandingActivity
import com.mparticle.example.higgsshopsampleapp.activities.MainActivity
import com.mparticle.example.higgsshopsampleapp.repositories.ProductsRepository
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.io.InputStream


@RunWith(AndroidJUnit4ClassRunner::class)
@LargeTest
class ExampleInstrumentedTests {

    val intent = Intent(ApplicationProvider.getApplicationContext(), MainActivity::class.java)

    @get:Rule
    var landingActivityScenarioRule = activityScenarioRule<LandingActivity>()

    @get:Rule
    var mainActivityScenarioRule = activityScenarioRule<MainActivity>(intent)

    @Test
    fun testUseAppContext() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.mparticle.example.higgsshopsampleapp", appContext.packageName)
    }

    @Test
    fun testShowLandingCTA() {
        onView(withId(R.id.landing_cta)).check(matches(withText(R.string.landing_cta)))
    }

    @Test
    fun testClickLandingCTA() {
        onView(withId(R.id.landing_cta)).perform(click())
        onView(withId(R.id.tv_shop_header)).check(matches(withText(R.string.shop_title)))
    }

    @Test
    fun testProductJsonFileExists() {
        val file: InputStream =
            InstrumentationRegistry.getInstrumentation().targetContext.assets.open("products.json")
        assertNotNull(file)
    }

    @Test
    fun testProductCount() {
        val repository = ProductsRepository()
        val products = repository.getProducts(InstrumentationRegistry.getInstrumentation().targetContext)
        assertEquals(products.size, 13)
    }
}