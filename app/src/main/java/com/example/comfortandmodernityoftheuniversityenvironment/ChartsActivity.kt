package com.example.comfortandmodernityoftheuniversityenvironment

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class ChartsActivity : AppCompatActivity() {

    private lateinit var dbHelper: DatabaseHelper
    private lateinit var listView: ListView
    private lateinit var tvEmpty: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_charts_simple)

        dbHelper = DatabaseHelper(this)
        listView = findViewById(R.id.lvChartData)
        tvEmpty = findViewById(R.id.tvEmpty)

        loadChartData()
    }

    private fun loadChartData() {
        val userId = 1
        val readings = dbHelper.getMeterReadings(userId)

        if (readings.isEmpty()) {
            tvEmpty.text = "Нет данных для анализа\nСначала введите показания счетчиков"
            listView.visibility = android.view.View.GONE
            tvEmpty.visibility = android.view.View.VISIBLE
        } else {
            // Сортируем по дате для анализа трендов
            val sortedReadings = readings.sortedBy { it.date }

            val adapter = ArrayAdapter(
                this,
                android.R.layout.simple_list_item_1,
                sortedReadings.mapIndexed { index, reading ->
                    "📅 ${reading.date}\n" +
                            "💧 Вода: ${reading.water} куб.м\n" +
                            "🔥 Газ: ${reading.gas} куб.м\n" +
                            "⚡ Электричество: ${reading.electricity} кВт/ч\n" +
                            "📊 Тренд: ${getTrend(index, sortedReadings)}"
                }
            )

            listView.adapter = adapter
            listView.visibility = android.view.View.VISIBLE
            tvEmpty.visibility = android.view.View.GONE
        }
    }

    private fun getTrend(index: Int, readings: List<DatabaseHelper.MeterReading>): String {
        if (index == 0) return "первая запись"

        val current = readings[index]
        val previous = readings[index - 1]

        val waterDiff = current.water - previous.water
        val gasDiff = current.gas - previous.gas
        val electricityDiff = current.electricity - previous.electricity

        return when {
            waterDiff > 10 || gasDiff > 10 || electricityDiff > 10 -> "📈 рост потребления"
            waterDiff < 0 || gasDiff < 0 || electricityDiff < 0 -> "⚠️ проверьте данные"
            else -> "➡️ стабильное потребление"
        }
    }
}