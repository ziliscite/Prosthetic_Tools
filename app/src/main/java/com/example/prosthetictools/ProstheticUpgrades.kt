package com.example.prosthetictools

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ProstheticUpgrades(
    val name: String,
    val description: String,
    var photo: Int
): Parcelable