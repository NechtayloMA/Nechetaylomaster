package com.example.comfortandmodernityoftheuniversityenvironment

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.text.DecimalFormat

class PaymentsActivity : AppCompatActivity() {

    private lateinit var dbHelper: DatabaseHelper
    private lateinit var listView: ListView
    private lateinit var tvTotalPaid: TextView
    private lateinit var tvTotalPending: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payments)

        dbHelper = DatabaseHelper(this)
        listView = findViewById(R.id.lvPayments)
        tvTotalPaid = findViewById(R.id.tvTotalPaid)
        tvTotalPending = findViewById(R.id.tvTotalPending)

        loadPayments()
        loadStatistics()
    }

    private fun loadPayments() {
        val userId = 1 // В реальном приложении берется из сессии/настроек
        val payments = dbHelper.getPayments(userId)

        if (payments.isEmpty()) {
            // ИСПРАВЛЕНИЕ: Используем стандартный способ показа пустого списка
            val adapter = ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                listOf("История платежей пуста")
            )
            listView.adapter = adapter
        } else {
            val adapter = ArrayAdapter(
                this,
                android.R.layout.simple_list_item_1,
                payments.map { payment ->
                    val statusIcon = if (payment.status == "Оплачено") "✅" else "⏳"
                    val dateText = if (payment.date.isNotEmpty()) "\n📅 ${payment.date}" else ""
                    "$statusIcon ${payment.serviceType}\n" +
                            "💰 ${payment.amount} руб. за ${payment.period}$dateText\n" +
                            "📊 Статус: ${payment.status}"
                }
            )
            listView.adapter = adapter
        }
    }

    private fun loadStatistics() {
        val userId = 1
        val statistics = dbHelper.getPaymentStatistics(userId)
        val decimalFormat = DecimalFormat("#.##")

        // ИСПРАВЛЕНИЕ: Используем ресурсы строк вместо конкатенации
        tvTotalPaid.text = resources.getString(R.string.total_paid, decimalFormat.format(statistics.totalPaid))
        tvTotalPending.text = resources.getString(R.string.total_pending, decimalFormat.format(statistics.totalPending))
    }
}