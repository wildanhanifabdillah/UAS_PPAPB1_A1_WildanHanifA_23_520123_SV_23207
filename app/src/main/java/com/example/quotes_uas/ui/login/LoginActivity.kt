package com.example.quotes_uas.ui.login

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.quotes_uas.R
import com.example.quotes_uas.ui.quotes.QuotesActivity
import com.example.quotes_uas.utils.SharedPref

class LoginActivity : AppCompatActivity() {

    private lateinit var sharedPref: SharedPref

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        sharedPref = SharedPref.getInstance(this)

        val usernameInput = findViewById<EditText>(R.id.usernameInput)
        val passwordInput = findViewById<EditText>(R.id.passwordInput)
        val loginButton = findViewById<Button>(R.id.loginButton)

        loginButton.setOnClickListener {
            val username = usernameInput.text.toString().trim()
            val password = passwordInput.text.toString().trim()

            if (username.isNotEmpty() && password.isNotEmpty()) {
                when {
                    username == "admin" && password == "admin" -> {
                        sharedPref.saveLogin("admin")
                        sharedPref.saveUsername(username)
                        navigateToQuotes()
                    }
                    username == "user" && password == "user" -> {
                        sharedPref.saveLogin("user")
                        sharedPref.saveUsername(username)
                        navigateToQuotes()
                    }
                    else -> {
                        Toast.makeText(this, "Invalid credentials", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun navigateToQuotes() {
        val intent = Intent(this, QuotesActivity::class.java)
        startActivity(intent)
        finish()
    }
}
