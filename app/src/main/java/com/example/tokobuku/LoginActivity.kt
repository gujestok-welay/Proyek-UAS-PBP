package com.example.tokobuku

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.tokobuku.databinding.ActivityLoginBinding
import com.example.tokobuku.model.UserRole

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.loginButton.setOnClickListener {
            val username = binding.usernameEditText.text.toString()
            val password = binding.passwordEditText.text.toString()

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Username and password cannot be empty", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val userRole = when {
                username == "admin" && password == "admin123" -> UserRole.Admin
                username == "user" && password == "user123" -> UserRole.User
                else -> null
            }

            if (userRole != null) {
                val intent = Intent(this, BookListActivity::class.java).apply {
                    putExtra(BookListActivity.EXTRA_USER_ROLE, userRole)
                }
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, "Invalid credentials", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
