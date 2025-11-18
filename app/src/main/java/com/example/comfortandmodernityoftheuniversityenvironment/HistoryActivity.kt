package com.example.comfortandmodernityoftheuniversityenvironment

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class HistoryActivity : AppCompatActivity() {

    private lateinit var dbHelper: DatabaseHelper
    private lateinit var listView: ListView
    private lateinit var tvEmpty: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)

        dbHelper = DatabaseHelper(this)
        listView = findViewById(R.id.lvHistory)
        // ИСПРАВЛЕНИЕ: Используем правильный ID из макета
        tvEmpty = findViewById(R.id.tvEmptyHistory)

        loadHistory()
    }

    private fun loadHistory() {
        val userId = 1 // В реальном приложении берется из сессии/настроек
        val readings = dbHelper.getMeterReadings(userId)

        if (readings.isEmpty()) {
            tvEmpty.text = "История показаний пуста"
            listView.visibility = android.view.View.GONE
            tvEmpty.visibility = android.view.View.VISIBLE
        } else {
            val adapter = ArrayAdapter(
                this,
                android.R.layout.simple_list_item_1,
                readings.map { reading ->
                    "📅 ${reading.date}\n" +
                            "💧 Вода: ${reading.water} куб.м\n" +
                            "🔥 Газ: ${reading.gas} куб.м\n" +
                            "⚡ Электричество: ${reading.electricity} кВт/ч"
                }
            )

            listView.adapter = adapter
            listView.visibility = android.view.View.VISIBLE
            tvEmpty.visibility = android.view.View.GONE
        }
    }
}