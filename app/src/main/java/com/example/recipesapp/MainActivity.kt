package com.example.recipesapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import com.example.recipesapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private var _mainActivityBinding: ActivityMainBinding? = null
    private val mainActivityBinding
        get() = _mainActivityBinding ?: throw IllegalStateException(
            "Binding for MainActivityBinding mustn't be null"
        )
    private val fragmentManager = supportFragmentManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _mainActivityBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainActivityBinding.root)

        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.add(R.id.mainContainer, CategoriesListFragment())
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()

        mainActivityBinding.btnFavorites.setOnClickListener {
            supportFragmentManager.commit {
                replace(R.id.mainContainer, FavoritesFragment())
                setReorderingAllowed(true)
            }
        }

        mainActivityBinding.btnCategories.setOnClickListener {
            supportFragmentManager.commit {
                replace(R.id.mainContainer, CategoriesListFragment())
                setReorderingAllowed(true)
            }
        }
    }
}