package com.jagl.playground

class Controler {
    data class Product(val id: String, val name: String)

    private val products = listOf(
        Product("7501099362282", "notebook"),
        Product("123", "pencil"),
        Product("4567", "tablet"),
        Product("890", "calculator"),
        Product("101112", "usb"),
        Product("134567890", "laptop")
    )

    fun findProduct(idProduct: String) = products.find { it.id == idProduct }

}