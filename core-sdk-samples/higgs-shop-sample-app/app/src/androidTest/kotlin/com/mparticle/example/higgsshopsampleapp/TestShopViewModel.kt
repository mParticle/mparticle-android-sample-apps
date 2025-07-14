package com.mparticle.example.higgsshopsampleapp

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.mparticle.example.higgsshopsampleapp.repositories.CartRepository
import com.mparticle.example.higgsshopsampleapp.repositories.ProductsRepository
import com.mparticle.example.higgsshopsampleapp.viewmodels.ShopViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.*
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4ClassRunner::class)
class TestShopViewModel {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()
    private val dispatcher = StandardTestDispatcher()

    private val productsRepository = ProductsRepository()
    private val cartRepository = CartRepository()
    private val viewModel =  ShopViewModel()

    @Before
    fun setup() {
            Dispatchers.setMain(dispatcher)
    }

    @After
    fun tearDown() {
            Dispatchers.resetMain()
    }

    @Test
    fun `testGetProducts`() = runTest {
        val products = productsRepository.getProducts(ApplicationProvider.getApplicationContext())
        viewModel.getProducts(ApplicationProvider.getApplicationContext())
        Assert.assertEquals(products, viewModel.inventoryResponseLiveData.getValueFromLiveData())
    }

    @Test
    fun `testGetTotalCartItems`() = runTest {
        val cartItems = cartRepository.getCartItems(ApplicationProvider.getApplicationContext())
        viewModel.getTotalCartItems(ApplicationProvider.getApplicationContext())
        Assert.assertEquals(
            cartItems.sumOf { it.quantity },
            viewModel.cartTotalSizeResponseLiveData.getValueFromLiveData()
        )
    }

}