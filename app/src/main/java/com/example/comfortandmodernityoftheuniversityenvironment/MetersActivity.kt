package com.example.comfortandmodernityoftheuniversityenvironment

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.*

class MetersActivity : AppCompatActivity() {

    private lateinit var etWater: EditText
    private lateinit var etGas: EditText
    private lateinit var etElectricity: EditText
    private lateinit var btnSubmit: Button
    private lateinit var btnHistory: Button
    private lateinit var btnCharts: Button
    private lateinit var btnPayments: Button
    private lateinit var dbHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_meters)

        dbHelper = DatabaseHelper(this)

        // Инициализация элементов
        etWater = findViewById(R.id.etWater)
        etGas = findViewById(R.id.etGas)
        etElectricity = findViewById(R.id.etElectricity)
        btnSubmit = findViewById(R.id.btnSubmit)
        btnHistory = findViewById(R.id.btnHistory)
        btnCharts = findViewById(R.id.btnCharts)
        btnPayments = findViewById(R.id.btnPayments)

        // Обработчик кнопки сохранения
        btnSubmit.setOnClickListener {
            val waterStr = etWater.text.toString()
            val gasStr = etGas.text.toString()
            val electricityStr = etElectricity.text.toString()

            if (waterStr.isEmpty() || gasStr.isEmpty() || electricityStr.isEmpty()) {
                Toast.makeText(this, "Заполните все поля", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            try {
                val water = waterStr.toDouble()
                val gas = gasStr.toDouble()
                val electricity = electricityStr.toDouble()

                val userId = 1
                val date = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

                val result = dbHelper.addMeterReading(userId, water, gas, electricity, date)

                if (result != -1L) {
                    Toast.makeText(this, "Данные сохранены", Toast.LENGTH_SHORT).show()
                    // Очищаем поля после успешного сохранения
                    etWater.text.clear()
                    etGas.text.clear()
                    etElectricity.text.clear()
                } else {
                    Toast.makeText(this, "Ошибка сохранения", Toast.LENGTH_SHORT).show()
                }
            } catch (e: NumberFormatException) {
                Toast.makeText(this, "Введите корректные числа", Toast.LENGTH_SHORT).show()
            }
        }

        // НОВЫЕ ОБРАБОТЧИКИ КНОПОК НАВИГАЦИИ
        btnHistory.setOnClickListener {
            val intent = Intent(this, HistoryActivity::class.java)
            startActivity(intent)
        }

        btnCharts.setOnClickListener {
            val intent = Intent(this, ChartsActivity::class.java)
            startActivity(intent)
        }

        btnPayments.setOnClickListener {
            val intent = Intent(this, PaymentsActivity::class.java)
            startActivity(intent)
        }
    }
}