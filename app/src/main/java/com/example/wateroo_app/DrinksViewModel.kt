package com.example.wateroo_app
import android.app.Application
import android.content.SharedPreferences
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class DrinksViewModel(application: Application) : AndroidViewModel(application) {
    private val sharedPreferences: SharedPreferences =
        application.getSharedPreferences("WaterooPrefs", Application.MODE_PRIVATE)

    val drinksList: MutableLiveData<MutableList<DrinkItem>> = MutableLiveData(mutableListOf())

    init {
        loadDrinksFromPreferences()
    }

    fun addDrink(drinkItem: DrinkItem) {
        val currentList = drinksList.value ?: mutableListOf()
        currentList.add(drinkItem)
        drinksList.value = currentList

        saveDrinksToPreferences()
    }

    fun removeDrink(drink: DrinkItem) {
        val currentList = drinksList.value ?: mutableListOf()
        currentList.remove(drink)
        drinksList.value = currentList

        saveDrinksToPreferences()
    }


    private fun saveDrinksToPreferences() {
        val editor = sharedPreferences.edit()
        val json = Gson().toJson(drinksList.value)
        editor.putString("drinks_list", json)
        editor.apply()
    }

    private fun loadDrinksFromPreferences() {
        val json = sharedPreferences.getString("drinks_list", null)
        if (!json.isNullOrEmpty()) {
            val type = object : TypeToken<MutableList<DrinkItem>>() {}.type
            val savedDrinks: MutableList<DrinkItem> = Gson().fromJson(json, type)
            drinksList.value = savedDrinks
        }
    }
}
