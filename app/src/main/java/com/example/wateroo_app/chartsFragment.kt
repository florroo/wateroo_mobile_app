package com.example.wateroo_app

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.data.Entry



class chartsFragment : Fragment() {

    private val drinksViewModel: DrinksViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_charts, container, false)

        val pieChart: PieChart = view.findViewById(R.id.pieChart)
        val emptyTextView: TextView = view.findViewById(R.id.emptyText)
        val barChart: BarChart = view.findViewById(R.id.barChart)

        drinksViewModel.drinksList.observe(viewLifecycleOwner, Observer { drinksList ->
            if (drinksList.isNullOrEmpty()) {
                pieChart.visibility = View.GONE
                emptyTextView.visibility = View.VISIBLE
                emptyTextView.text = "Nic jeszcze nie wypito"
                barChart.visibility = View.GONE
            } else {
                pieChart.visibility = View.VISIBLE
                emptyTextView.visibility = View.GONE
                barChart.visibility = View.VISIBLE

                val groupedDrinks = drinksList.groupBy { it.name }
                    .mapValues { entry -> entry.value.sumOf { it.amount } }

                val entries = groupedDrinks.map { (name, totalAmount) ->
                    PieEntry(totalAmount.toFloat(), name)
                }

                val dataSet = PieDataSet(entries, "Spożycie napojów")
                dataSet.colors = listOf(
                    ContextCompat.getColor(requireContext(), R.color.blue),
                    ContextCompat.getColor(requireContext(), R.color.orange),
                    ContextCompat.getColor(requireContext(), R.color.green),
                    ContextCompat.getColor(requireContext(), R.color.red),
                    ContextCompat.getColor(requireContext(), R.color.purple),
                    ContextCompat.getColor(requireContext(), R.color.yellow),
                    ContextCompat.getColor(requireContext(), R.color.ivory),
                    ContextCompat.getColor(requireContext(), R.color.amber),
                )
                dataSet.valueTextColor = ContextCompat.getColor(requireContext(), R.color.black)
                dataSet.valueTextSize = 12f

                val pieData = PieData(dataSet)
                pieChart.data = pieData
                pieChart.setUsePercentValues(false)
                pieChart.invalidate()
                pieChart.setOnChartValueSelectedListener(object : OnChartValueSelectedListener {
                    override fun onValueSelected(e: Entry?, h: Highlight?) {
                        if (e is PieEntry) {
                            val selectedDrinkName = e.label
                            val selectedTotalAmount = e.value.toFloat()
                            val selectedDrink = drinksList.firstOrNull { it.name == selectedDrinkName }
                            selectedDrink?.let {
                                val nutrientsDetails = it.composition.entries.joinToString("\n") { (macro, value) ->
                                    "$macro: ${(value * selectedTotalAmount)/100}"
                                }

                                AlertDialog.Builder(requireContext())
                                    .setTitle("Składniki odżywcze - $selectedDrinkName")
                                    .setMessage(nutrientsDetails)
                                    .setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
                                    .show()
                            }
                        }
                    }

                    override fun onNothingSelected() {
                    }
                })
                val macroNutrients = drinksList.flatMap { drink ->
                    drink.composition.map { (macro, percentage) ->
                        macro to (percentage * drink.amount /100)
                    }
                }
                    .groupBy({ it.first }, { it.second })
                    .mapValues { (_, values) -> values.sum() }
                    .filterKeys { it != "Woda" }

                val barEntries = macroNutrients.entries.mapIndexed { index, entry ->
                    BarEntry(index.toFloat(), entry.value.toFloat())
                }

                val barDataSet = BarDataSet(barEntries, "Składniki odżywcze")
                barDataSet.colors = listOf(
                    ContextCompat.getColor(requireContext(), R.color.blue),
                    ContextCompat.getColor(requireContext(), R.color.orange),
                    ContextCompat.getColor(requireContext(), R.color.green),
                    ContextCompat.getColor(requireContext(), R.color.red),
                    ContextCompat.getColor(requireContext(), R.color.purple)
                )
                barDataSet.valueTextSize = 12f

                val barData = BarData(barDataSet)
                barChart.data = barData

                barChart.xAxis.apply {
                    valueFormatter = IndexAxisValueFormatter(macroNutrients.keys.toList())
                    position = XAxis.XAxisPosition.BOTTOM
                    granularity = 1f
                    setDrawGridLines(false)
                }

                barChart.axisLeft.apply {
                    granularity = 1f
                    axisMinimum = 0f
                }
                barChart.axisRight.isEnabled = false
                barChart.description.isEnabled = false
                barChart.invalidate()
            }
        })

        return view
    }
}
