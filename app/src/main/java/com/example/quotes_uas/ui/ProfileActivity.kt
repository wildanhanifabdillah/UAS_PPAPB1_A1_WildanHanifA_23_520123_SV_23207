package com.example.quotes_uas.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.quotes_uas.R
import com.example.quotes_uas.ui.login.LoginActivity
import com.example.quotes_uas.utils.SharedPref

class ProfileActivity : AppCompatActivity() {

    private lateinit var sharedPref: SharedPref

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        // Inisialisasi SharedPref
        sharedPref = SharedPref.getInstance(this)

        // Tampilkan informasi pengguna
        displayUserInfo()

        // Tombol logout
        val logoutButton = findViewById<Button>(R.id.logoutButton)
        logoutButton.setOnClickListener {
            logout()
        }
    }

    private fun logout() {
        // Hapus data user dari SharedPreferences
        sharedPref.clear()

        // Arahkan pengguna ke halaman login
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish() // Menghentikan aktivitas ini agar tidak kembali ke ProfileActivity
    }

    private fun displayUserInfo() {
        // Ambil username dan role dari SharedPref
        val username = sharedPref.getUsername() ?: "Guest"
        val role = sharedPref.getRole() ?: "User"

        // Hubungkan dengan TextView
        val usernameTextView = findViewById<TextView>(R.id.textViewUsername)
        val roleTextView = findViewById<TextView>(R.id.textViewRole)

        // Set data ke TextView
        usernameTextView.text = "Username: $username"
        roleTextView.text = "Role: $role"
    }
}
