package com.example.recipesapp.ui

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.findNavController
import com.example.recipesapp.R
import com.example.recipesapp.data.COUNT_OF_THREADS
import com.example.recipesapp.data.URL_CATEGORY
import com.example.recipesapp.databinding.ActivityMainBinding
import com.example.recipesapp.model.Category
import com.example.recipesapp.model.Recipe
import kotlinx.serialization.json.Json
import java.net.HttpURLConnection
import java.net.URL
import java.util.concurrent.Executors

class MainActivity : AppCompatActivity() {

    private var _mainActivityBinding: ActivityMainBinding? = null
    private val mainActivityBinding
        get() = _mainActivityBinding ?: throw IllegalStateException(
            "Binding for MainActivityBinding mustn't be null"
        )

    private val threadPool = Executors.newFixedThreadPool(COUNT_OF_THREADS)

    private val json = Json {
        ignoreUnknownKeys = true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        _mainActivityBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainActivityBinding.root)
        ViewCompat.setOnApplyWindowInsetsListener(mainActivityBinding.layoutMain) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        threadPool.execute {
            val basicThreadConnection = URL(URL_CATEGORY).openConnection() as HttpURLConnection

            try {
                val jsonInput = basicThreadConnection.inputStream.bufferedReader().readText()
                val decodedData = json.decodeFromString<List<Category>>(jsonInput)
                val categoryIds = decodedData.map { it.id }

                categoryIds.forEach { categoryId ->
                    threadPool.execute {

                        val threadPoolConnection =
                            URL("$URL_CATEGORY/$categoryId/recipes").openConnection() as HttpURLConnection

                        try {
                            val jsonRecipe =
                                threadPoolConnection.inputStream.bufferedReader().readText()
                            val decodedRecipe = json.decodeFromString<List<Recipe>>(jsonRecipe)

                            Log.i("!!!", "Recipes by CategoryId $categoryId: $decodedRecipe")
                        } catch (e: Exception) {
                            Log.e(
                                "network",
                                "status code = ${threadPoolConnection.responseCode}, error = ${e.message}"
                            )
                        } finally {
                            threadPoolConnection.disconnect()
                        }
                    }
                }
            } catch (e: Exception) {
                Log.e(
                    "network",
                    "status code = ${basicThreadConnection.responseCode}, error = ${e.message}"
                )
            } finally {
                basicThreadConnection.disconnect()
            }
        }

        mainActivityBinding.btnFavorites.setOnClickListener {
            findNavController(R.id.navHostFragment).navigate(R.id.favoritesFragment)
        }

        mainActivityBinding.btnCategories.setOnClickListener {
            findNavController(R.id.navHostFragment).navigate(R.id.categoriesListFragment)
        }
    }

    override fun onDestroy() {
        threadPool.shutdown()
        super.onDestroy()
    }
}