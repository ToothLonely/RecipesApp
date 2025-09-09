package com.example.recipesapp.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
data class Category(
    val id: Int,
    val title: String,
    val description: String,
    val imageUrl: String
): Parcelable