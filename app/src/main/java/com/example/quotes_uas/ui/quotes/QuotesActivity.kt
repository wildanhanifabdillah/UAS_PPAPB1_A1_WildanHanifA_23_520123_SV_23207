package com.example.quotes_uas.ui.quotes

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.quotes_uas.R
import com.example.quotes_uas.data.api.ApiClient
import com.example.quotes_uas.data.api.ApiService
import com.example.quotes_uas.data.local.QuotesDatabase
import com.example.quotes_uas.data.local.QuotesEntity
import com.example.quotes_uas.ui.ProfileActivity
import com.example.quotes_uas.utils.SharedPref
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class QuotesActivity : AppCompatActivity() {

    private lateinit var quotesAdapter: QuoteAdapter
    private lateinit var sharedPref: SharedPref
    private val database by lazy { QuotesDatabase.getDatabase(this) }
    private val quotesDao by lazy { database.quotesDao() }

    private var isShowingFavorites = false

    private val apiService = ApiClient.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quotes)

        sharedPref = SharedPref.getInstance(this)
        val role = sharedPref.getRole() ?: "user"

        val recyclerView = findViewById<RecyclerView>(R.id.quotesRecyclerView)
        quotesAdapter = QuoteAdapter(
            role = role,
            onFavoriteClick = { quote -> toggleFavorite(quote) },
            onDeleteClick = { quote -> deleteQuote(quote) }
        )
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = quotesAdapter

        val addQuoteButton = findViewById<Button>(R.id.addQuoteButton)
        addQuoteButton.visibility = if (role == "admin") View.VISIBLE else View.GONE
        addQuoteButton.setOnClickListener {
            val intent = Intent(this, AddQuoteActivity::class.java)
            startActivity(intent)
        }

        val bottomNavigation = findViewById<com.google.android.material.bottomnavigation.BottomNavigationView>(R.id.bottomNavigationView)

        bottomNavigation.menu.clear()
        if (role == "admin") {
            bottomNavigation.inflateMenu(R.menu.bottom_nav_admin)
        } else {
            bottomNavigation.inflateMenu(R.menu.bottom_nav_user)
        }

        bottomNavigation.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_home -> {
                    isShowingFavorites = false
                    fetchQuotes() // Muat ulang semua data
                    Toast.makeText(this, "Home", Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.nav_favorite -> {
                    if (role != "admin") {
                        isShowingFavorites = true
                        showFavoriteQuotes() // Tampilkan data favorit
                    }
                    true
                }
                R.id.nav_profile -> {
                    val intent = Intent(this, ProfileActivity::class.java)
                    startActivity(intent)
                    true
                }
                else -> false
            }
        }

        // Muat data awal (default: semua quotes)
        fetchQuotes()
    }

    private fun fetchQuotes() {
        quotesDao.getAllQuotes().observe(this, Observer { quotes ->
            if (!isShowingFavorites) { // Periksa apakah mode saat ini adalah "all quotes"
                quotesAdapter.submitList(quotes)
            }
        })
    }

    private fun showFavoriteQuotes() {
        quotesDao.getFavoriteQuotes().observe(this, Observer { favoriteQuotes ->
            if (isShowingFavorites) { // Periksa apakah mode saat ini adalah "favorite quotes"
                quotesAdapter.submitList(favoriteQuotes)
            }
        })
    }

    private fun toggleFavorite(quote: QuotesEntity) {
        lifecycleScope.launch {
            quotesDao.updateFavoriteStatus(quote._id, !quote.isFavorite)
        }
    }

    private fun deleteQuote(quote: QuotesEntity) {
        lifecycleScope.launch {
            try {
                // Hapus data dari Room
                quotesDao.deleteQuote(quote)

                // Hapus data dari API
                val response = apiService.deleteQuote(quote._id.toString())
                if (response.isSuccessful) {
                    runOnUiThread {
                        Toast.makeText(this@QuotesActivity, "Quote deleted", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    val errorBody = response.errorBody()?.string()
                    Log.e("DeleteQuote", "Gagal menghapus dari API: $errorBody")
                    runOnUiThread {
//                        Toast.makeText(this@QuotesActivity, "Failed to delete quote from API", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                Log.e("DeleteQuote", "Error: ${e.message}", e)
                runOnUiThread {
                    Toast.makeText(this@QuotesActivity, "Kesalahan: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}

