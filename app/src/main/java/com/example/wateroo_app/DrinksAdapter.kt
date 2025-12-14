package com.example.wateroo_app

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.RecyclerView

class DrinksAdapter(private val drinksViewModel: DrinksViewModel, private val onAmountChanged: () -> Unit) :
    RecyclerView.Adapter<DrinksAdapter.DrinkViewHolder>() {
    private var drinksList: MutableList<DrinkItem> = drinksViewModel.drinksList.value ?: mutableListOf()

    fun updateList(newList: List<DrinkItem>) {
        drinksList = newList.toMutableList()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DrinkViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_drink, parent, false)
        return DrinkViewHolder(view)
    }

    override fun onBindViewHolder(holder: DrinkViewHolder, position: Int) {
        holder.bind(drinksList[position])
    }

    override fun getItemCount(): Int = drinksList.size

    inner class DrinkViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val drinkNameTextView: TextView = itemView.findViewById(R.id.drinkName)
        private val amountEditText: EditText = itemView.findViewById(R.id.amountEditText)
        private val deleteButton: TextView = itemView.findViewById(R.id.deleteButton)

        fun bind(drinkItem: DrinkItem) {
            drinkNameTextView.text = drinkItem.name
            amountEditText.setText(drinkItem.amount.toString())

            amountEditText.addTextChangedListener {
                val newAmount = it.toString().toIntOrNull()
                if (newAmount != null) {
                    drinkItem.amount = newAmount
                    onAmountChanged()
                }
            }
            deleteButton.setOnClickListener {
                removeDrink(drinkItem)
            }
        }

        fun removeDrink(drink: DrinkItem) {
            drinksViewModel.removeDrink(drink)  // Correct ViewModel usage
            notifyItemRemoved(adapterPosition)
            notifyItemRangeChanged(adapterPosition, drinksList.size)
        }


    }
}
