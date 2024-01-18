package com.example.mylibrary.ui

import androidx.lifecycle.ViewModel
import com.example.mylibrary.data.Product
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class MainViewModel : ViewModel() {

    val random = (0..10)

    val products = listOf(
        Product(11234123, "Mouse", 59.99),
        Product(1896234, "Laptop", 150.0),
        Product(983485, "Pencil", 0.5),
    )

    fun getProducts(): Flow<List<Product>> {
        val succesRequest = random.random()
        return if (succesRequest % 2 == 0) flowOf(products)
        else throw RuntimeException("Error in the request")
    }
}