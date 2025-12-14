package com.example.wateroo_app

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.wateroo_app.databinding.FragmentHomeBinding

class homeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private val drinksViewModel: DrinksViewModel by activityViewModels()

    private val availableDrinks = listOf(
        DrinkItem("Woda", 100, mapOf("Kalorie" to 0.0, "Tłuszcze" to 0.0, "Węglowodany" to 0.0, "Cukry" to 0.0, "Sól" to 0.0, "Białka" to 0.0)),
        DrinkItem("Sok pomarańczowy", 100, mapOf("Kalorie" to 45.0, "Tłuszcze" to 0.3, "Węglowodany" to 11.0, "Cukry" to 9.0, "Sól" to 0.01, "Białka" to 0.6)),
        DrinkItem("Kawa", 100, mapOf("Kalorie" to 2.0, "Tłuszcze" to 0.1, "Węglowodany" to 0.0, "Cukry" to 0.0, "Sól" to 0.0, "Białka" to 0.0)),
        DrinkItem("Herbata", 100, mapOf("Kalorie" to 2.0, "Tłuszcze" to 0.0, "Węglowodany" to 0.5, "Cukry" to 0.0, "Sól" to 0.0, "Białka" to 0.0)),
        DrinkItem("Coca-Cola", 100, mapOf("Kalorie" to 42.0, "Tłuszcze" to 0.0, "Węglowodany" to 11.0, "Cukry" to 10.0, "Sól" to 0.0, "Białka" to 0.0)),
        DrinkItem("Sok jabłkowy", 100, mapOf("Kalorie" to 46.0, "Tłuszcze" to 0.1, "Węglowodany" to 11.0, "Cukry" to 9.0, "Sól" to 0.0, "Białka" to 0.0)),
        DrinkItem("Mleko", 100, mapOf("Kalorie" to 50.0, "Tłuszcze" to 2.6, "Węglowodany" to 4.8, "Cukry" to 5.0, "Sól" to 0.1, "Białka" to 3.2)),
        DrinkItem("Red Bull", 100, mapOf("Kalorie" to 45.0, "Tłuszcze" to 0.0, "Węglowodany" to 11.0, "Cukry" to 11.0, "Sól" to 0.01, "Białka" to 0.0)),
        DrinkItem("Piwo", 100, mapOf("Kalorie" to 43.0, "Tłuszcze" to 0.0, "Węglowodany" to 3.0, "Cukry" to 1.0, "Sól" to 0.0, "Białka" to 1.0))
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        val recyclerView = binding.recyclerView

        val adapter = DrinksAdapter(drinksViewModel) {
            updateProgressBar()
        }

        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter

        drinksViewModel.drinksList.observe(viewLifecycleOwner) { drinksList ->
            adapter.updateList(drinksList)
            updateProgressBar()
        }

        binding.fabAdd.setOnClickListener {
            showDrinkSelectionDialog()
        }

        return binding.root
    }

    private fun showDrinkSelectionDialog() {
        val builder = android.app.AlertDialog.Builder(requireContext())
        builder.setTitle("Wybierz napój")
        builder.setItems(availableDrinks.map { it.name }.toTypedArray()) { _, which ->
            val selectedDrink = availableDrinks[which]
            drinksViewModel.addDrink(selectedDrink.copy(amount = 0))
            Toast.makeText(requireContext(), "${selectedDrink.name} dodany do listy", Toast.LENGTH_SHORT).show()
        }
        builder.show()
    }


    @SuppressLint("SetTextI18n")
    private fun updateProgressBar() {
        val totalAmount = drinksViewModel.drinksList.value?.sumOf { it.amount } ?: 0
        val progressBar = binding.amount
        progressBar.progress = totalAmount
        binding.amountText.text = "$totalAmount ml"
    }
}
