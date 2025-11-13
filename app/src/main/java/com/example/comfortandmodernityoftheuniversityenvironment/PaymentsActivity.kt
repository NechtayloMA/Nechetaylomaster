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
    private lateinit var tvEmpty: TextView
    private lateinit var tvTotalPaid: TextView
    private lateinit var tvTotalPending: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payments)

        dbHelper = DatabaseHelper(this)
        listView = findViewById(R.id.lvPayments)
        tvEmpty = findViewById(R.id.tvEmpty)
        tvTotalPaid = findViewById(R.id.tvTotalPaid)
        tvTotalPending = findViewById(R.id.tvTotalPending)

        loadPayments()
        loadStatistics()
    }

    private fun loadPayments() {
        val userId = 1 // В реальном приложении берется из сессии/настроек
        val payments = dbHelper.getPayments(userId)

        if (payments.isEmpty()) {
            tvEmpty.text = "История платежей пуста"
            listView.visibility = android.view.View.GONE
            tvEmpty.visibility = android.view.View.VISIBLE
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
            listView.visibility = android.view.View.VISIBLE
            tvEmpty.visibility = android.view.View.GONE
        }
    }

    private fun loadStatistics() {
        val userId = 1
        val statistics = dbHelper.getPaymentStatistics(userId)
        val decimalFormat = DecimalFormat("#.##")

        tvTotalPaid.text = "Оплачено: ${decimalFormat.format(statistics.totalPaid)} руб."
        tvTotalPending.text = "К оплате: ${decimalFormat.format(statistics.totalPending)} руб."
    }
}