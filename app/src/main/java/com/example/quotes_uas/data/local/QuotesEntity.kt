package com.example.quotes_uas.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "quotes")
data class QuotesEntity(
    @PrimaryKey(autoGenerate = true)
    @SerializedName("_id") // ID akan digunakan untuk parsing dari API
    val _id: Long = 0, // ID auto-generated oleh Room untuk penyimpanan lokal

    @SerializedName("quote")
    val kutipan: String, // Nama properti di API adalah "quote"

    @SerializedName("author")
    val penulis: String, // Nama properti di API adalah "author"

    val isFavorite: Boolean = false // Properti tambahan untuk fitur lokal
)