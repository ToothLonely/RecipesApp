package com.example.recipesapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.recipesapp.databinding.FavoritesFragmentBinding

class FavoritesFragment : Fragment() {
    private var _favoritesFragmentBinding: FavoritesFragmentBinding? = null
    private val favoritesFragmentBinding
        get() = _favoritesFragmentBinding ?: throw IllegalStateException(
            "Binding for FavoritesFragmentBinding mustn't be null"
        )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _favoritesFragmentBinding =
            FavoritesFragmentBinding.inflate(inflater, container, false)
        return favoritesFragmentBinding.root
    }
}