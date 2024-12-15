package com.example.quotes_uas.ui.quotes

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.quotes_uas.R
import com.example.quotes_uas.data.api.ApiClient
import com.example.quotes_uas.data.api.ApiService
import com.example.quotes_uas.data.api.QuoteRequest
import com.example.quotes_uas.data.local.QuotesDao
import com.example.quotes_uas.data.local.QuotesDatabase
import com.example.quotes_uas.data.local.QuotesEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AddQuoteActivity : AppCompatActivity() {

    private lateinit var apiService: ApiService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_quote)

        val inputAuthor = findViewById<EditText>(R.id.inputAuthor)
        val inputQuote = findViewById<EditText>(R.id.inputQuote)
        val saveButton = findViewById<Button>(R.id.saveButton)

        val database = QuotesDatabase.getDatabase(this)
        val quotesDao = database.quotesDao()

        // Inisialisasi ApiService
        apiService = ApiClient.getInstance()

        saveButton.setOnClickListener {
            val author = inputAuthor.text.toString().trim()
            val quote = inputQuote.text.toString().trim()

            if (author.isNotEmpty() && quote.isNotEmpty()) {
                // Tambahkan data baru
                val newQuote = QuotesEntity(
                    penulis = author,
                    kutipan = quote
                )
                saveQuoteToDatabase(newQuote, quotesDao)
            } else {
                Toast.makeText(this, "Semua kolom harus diisi", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun saveQuoteToDatabase(quote: QuotesEntity, quotesDao: QuotesDao) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                // Simpan ke database lokal terlebih dahulu tanpa ID API
                val insertedId = quotesDao.insertQuote(quote)
                Log.d("SaveQuote", "Quote disimpan dengan ID lokal: $insertedId")

                // Kirim data ke API
                val requestBody = QuoteRequest(
                    quote = quote.kutipan,
                    author = quote.penulis,
                )

                val response = apiService.addQuote(requestBody)

                if (response.isSuccessful) {
                    // Ambil data yang dikembalikan dari API
                    val savedQuoteResponse = response.body()!!

                    // Salin objek lokal dan perbarui dengan ID dari API
                    val updatedQuote = quote.copy(_id = savedQuoteResponse._id)  // Salin ID dari API

                    // Perbarui database lokal dengan ID dari API
                    quotesDao.updateQuote(updatedQuote)

                    runOnUiThread {
                        Toast.makeText(this@AddQuoteActivity, "Quote berhasil ditambahkan", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Log.e("SaveQuote", "Gagal menambahkan ke server: ${response.errorBody()?.string()}")
                    throw Exception("Gagal menyimpan ke server")
                }
            } catch (e: Exception) {
                Log.e("SaveQuote", "Error: ${e.message}", e)
                runOnUiThread {
                    Toast.makeText(this@AddQuoteActivity, "Kesalahan: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            } finally {
                runOnUiThread { finish() }
            }
        }
    }


}