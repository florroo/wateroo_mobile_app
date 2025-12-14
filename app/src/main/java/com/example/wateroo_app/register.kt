package com.example.wateroo_app

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.lifecycle.lifecycleScope
import com.example.wateroo_app.db.UserDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class register : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.register)

        val emailField = findViewById<EditText>(R.id.newEmail)
        val loginField = findViewById<EditText>(R.id.newLogin)
        val passwordField = findViewById<EditText>(R.id.newPass)
        val rePasswordField = findViewById<EditText>(R.id.newRePass)
        val backButton = findViewById<Button>(R.id.backButton)
        val createButton = findViewById<Button>(R.id.createButton)


        backButton.setOnClickListener{
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        createButton.setOnClickListener {
            val email = emailField.text.toString().trim()
            val login = loginField.text.toString().trim()
            val password = passwordField.text.toString().trim()
            val rePassword = rePasswordField.text.toString().trim()


            if (email.isEmpty() || login.isEmpty() || password.isEmpty() || rePassword.isEmpty()) {
                Toast.makeText(this, "Wszystkie pola są wymagane!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (password != rePassword) {
                Toast.makeText(this, "Hasła muszą się zgadzać!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (password.length < 8) {
                Toast.makeText(this, "Hasło musi mieć co najmniej 8 znaków!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            lifecycleScope.launch(Dispatchers.IO) {
                val userDao = UserDatabase.getInstance(this@register).getUserDao()

                val existingUser = userDao.getUserByEmail(email)

                if (existingUser != null) {
                    runOnUiThread {
                        Toast.makeText(this@register, "Użytkownik z tym e-mailem już istnieje!", Toast.LENGTH_SHORT).show()
                    }
                    return@launch
                }

                val user = User(
                    login = login,
                    password = password,
                    email = email,
                    drink_amount = 0
                )

                userDao.insertUser(user)

                runOnUiThread {
                    Toast.makeText(this@register, "Konto zostało utworzone!", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this@register, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }
        }
    }
}