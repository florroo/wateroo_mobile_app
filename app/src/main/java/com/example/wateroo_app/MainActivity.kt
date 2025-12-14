package com.example.wateroo_app

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import androidx.work.OneTimeWorkRequest
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import com.example.wateroo_app.db.UserDatabase
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {
    companion object{
        lateinit var userDatabase: UserDatabase
    }

    private val drinksViewModel: DrinksViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login)

        scheduleHydrationReminder()

        val loginButton = findViewById<Button>(R.id.loginButton)
        val createAccButton = findViewById<Button>(R.id.createAccButton)
        val forgotPassButton = findViewById<Button>(R.id.forgotPassButton)
        val emailField = findViewById<EditText>(R.id.email)
        val passwordField = findViewById<EditText>(R.id.password)

        userDatabase = Room.databaseBuilder(
            applicationContext,
            UserDatabase::class.java,
            UserDatabase.NAME
        ).allowMainThreadQueries().build()

        loginButton.setOnClickListener {
            val email = emailField.text.toString().trim()
            val password = passwordField.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Wszystkie pola są wymagane!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val userDao = userDatabase.getUserDao()
            val user = userDao.getUserEmail(email)

            if (user != null && user.password == password) {
                val intent = Intent(this, mainView::class.java)
                intent.putExtra("USER_LOGIN", user.login)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, "Błędny email lub hasło!", Toast.LENGTH_SHORT).show()
            }
        }

        createAccButton.setOnClickListener {
            val intent = Intent(this, register::class.java)
            startActivity(intent)
        }

        forgotPassButton.setOnClickListener {
            val intent = Intent(this, forgotPass::class.java)
            startActivity(intent)
        }

    }

    fun scheduleHydrationReminder() {
        val hydrationReminderRequest = PeriodicWorkRequest.Builder(HydrationReminderWorker::class.java, 15, TimeUnit.MINUTES)
            .build()

        WorkManager.getInstance(applicationContext).enqueue(hydrationReminderRequest)


        Toast.makeText(this, "Hydration reminder set!", Toast.LENGTH_SHORT).show()
    }
}

