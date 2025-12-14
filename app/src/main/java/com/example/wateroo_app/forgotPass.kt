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

class forgotPass : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_pass)

        val emailField = findViewById<EditText>(R.id.forgotEmail)
        val backButtonForgot = findViewById<Button>(R.id.backButtonForgot)
        val forgotPassSendButton = findViewById<Button>(R.id.forgotPassSendButton)

        val userDao = UserDatabase.getInstance(applicationContext).getUserDao()

        backButtonForgot.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        forgotPassSendButton.setOnClickListener {
            val email = emailField.text.toString().trim()

            if (email.isEmpty()) {
                Toast.makeText(this, "Proszę podać adres e-mail.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            lifecycleScope.launch(Dispatchers.IO) {
                val user = userDao.getUserByEmail(email)
                runOnUiThread {
                    if (user != null) {
                        Toast.makeText(this@forgotPass, "Twoje hasło to: ${user.password}", Toast.LENGTH_LONG).show()
                    } else {
                        Toast.makeText(this@forgotPass, "Nie znaleziono użytkownika o tym e-mailu.", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}