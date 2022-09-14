package com.mparticle.example.higgsshopsampleapp

import android.content.Intent
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.rules.activityScenarioRule
import androidx.test.filters.LargeTest
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.platform.app.InstrumentationRegistry
import com.mparticle.example.higgsshopsampleapp.activities.LandingActivity
import com.mparticle.example.higgsshopsampleapp.activities.MainActivity
import com.mparticle.example.higgsshopsampleapp.repositories.ProductsRepository
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
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