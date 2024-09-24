package com.example.prosthetictools

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Prosthetic(
    val id: String,
    val name: String,
    val description: String,
    val photo: Int
): Parcelable