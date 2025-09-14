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
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
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

    private val interceptor = HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }

    private val client = OkHttpClient().newBuilder()
        .addInterceptor(interceptor)
        .build()

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
            val getCategoriesRequest = Request.Builder()
                .url(URL_CATEGORY)
                .build()

            try {
                client.newCall(getCategoriesRequest).execute().use { response ->

                    val jsonResponse = response.body.string()
                    val decodedResponse = json.decodeFromString<List<Category>>(jsonResponse)
                    val categoryIds = decodedResponse.map { it.id }

                    categoryIds.forEach { categoryId ->
                        threadPool.execute {

                            val getRecipesRequest = Request.Builder()
                                .url("$URL_CATEGORY/$categoryId/recipes")
                                .build()

                            try {
                                client.newCall(getRecipesRequest).execute().use { response ->
                                    val jsonResponse = response.body.string()
                                    val decodedResponse =
                                        json.decodeFromString<List<Recipe>>(jsonResponse)

                                    Log.i(
                                        "!!!",
                                        "Recipes by CategoryId $categoryId: $decodedResponse"
                                    )

                                }
                            } catch (e: Exception) {
                                Log.e("!!!", "failure in getting recipes by category ids", e)
                                e.printStackTrace()
                            }
                        }
                    }
                }
            } catch (e: Exception) {
                Log.e("!!!", "failure in getting categories", e)
                e.printStackTrace()
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