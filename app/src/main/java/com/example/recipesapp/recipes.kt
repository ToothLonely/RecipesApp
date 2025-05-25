package com.example.recipesapp

object STUB {
    private val burgers = Category(
        0, "Бургеры", "Рецепты всех популярных видов бургеров", "burger.png"
    )
    private val dessert = Category(
        1, "Десерты", "Самые вкусные рецепты десертов специально для вас", "dessert.png"
    )
    private val pizza = Category(
        2, "Пицца", "Пицца на любой вкус и цвет. Лучшая подборка для тебя", "pizza.png"
    )
    private val fish = Category(
        3, "Рыба", "Печеная, жареная, сушеная, любая рыба на твой вкус", "fish.png"
    )
    private val soup = Category(
        4, "Супы", "От классики до экзотики: мир в одной тарелке", "soup.png"
    )
    private val salad = Category(
        5, "Салаты", "Хрустящий калейдоскоп под соусом вдохновения", "salad.png"
    )

    private val categories: List<Category> = listOf(burgers, dessert, pizza, fish, soup, salad)

    fun getCategories(): List<Category> {
        return categories
    }
}