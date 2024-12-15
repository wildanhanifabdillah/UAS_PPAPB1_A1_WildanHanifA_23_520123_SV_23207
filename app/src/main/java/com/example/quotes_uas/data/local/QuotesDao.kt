package com.example.quotes_uas.data.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface QuotesDao {
    // Mengambil semua quotes secara real-time
    @Query("SELECT * FROM quotes")
    fun getAllQuotes(): LiveData<List<QuotesEntity>>

    // Menampilkan quote favorit
    @Query("SELECT * FROM quotes WHERE isFavorite = 1")
    fun getFavoriteQuotes(): LiveData<List<QuotesEntity>>

    // Memasukkan atau mengganti quote baru
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuote(quote: QuotesEntity): Long

    // Memperbarui data quote secara keseluruhan
    @Update
    suspend fun updateQuote(quote: QuotesEntity)

    // Memperbarui status favorit berdasarkan ID lokal
    @Query("UPDATE quotes SET isFavorite = :isFavorite WHERE _id = :quoteId")
    suspend fun updateFavoriteStatus(quoteId: Long, isFavorite: Boolean)

    // Menghapus quote dari database
    @Delete
    suspend fun deleteQuote(quote: QuotesEntity)
}
