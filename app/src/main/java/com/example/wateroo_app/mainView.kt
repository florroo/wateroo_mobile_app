package com.example.wateroo_app

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.wateroo_app.databinding.ActivityMainViewBinding

class mainView : AppCompatActivity() {

    private lateinit var binding: ActivityMainViewBinding
    private var userLogin: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainViewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userLogin = intent.getStringExtra("USER_LOGIN")

        binding.greetingsLogin.text = userLogin ?: "Nieznany użytkownik"

        loadFragment(homeFragment())

        binding.bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.home -> loadFragment(homeFragment())
                R.id.charts -> loadFragment(chartsFragment())
                R.id.settings -> loadFragment(settingsFragment())
                else -> false
            }
            true
        }
    }

    private fun loadFragment(fragment: Fragment): Boolean {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
        return true
    }
}
