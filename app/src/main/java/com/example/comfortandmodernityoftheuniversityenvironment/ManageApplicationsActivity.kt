package com.example.comfortandmodernityoftheuniversityenvironment

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity

class ManageApplicationsActivity : AppCompatActivity() {

    private lateinit var dbHelper: DatabaseHelper
    private lateinit var listView: ListView
    private lateinit var tvEmpty: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manage_applications)

        supportActionBar?.title = "Управление заявками (исполнитель)"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        dbHelper = DatabaseHelper(this)
        listView = findViewById(R.id.lvApplications)
        tvEmpty = findViewById(R.id.tvEmpty)

        // Устанавливаем пустое view для ListView
        listView.emptyView = tvEmpty

        loadApplications()

        // Замена устаревшего onBackPressed на современный подход
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                finish()
            }
        })
    }

    private fun loadApplications() {
        try {
            val applications = dbHelper.getAllApplications()

            if (applications.isNotEmpty()) {
                val adapter = ArrayAdapter(
                    this,
                    android.R.layout.simple_list_item_1,
                    applications.map { application ->
                        val status = dbHelper.getApplicationStatus(application.id)
                        "📋 ${application.title}\n" +
                                "📝 ${application.content}\n" +
                                "⚡ Приоритет: ${application.priority}\n" +
                                "📅 ${application.date}\n" +
                                "🔧 Статус: $status"
                    }
                )

                listView.adapter = adapter

                // Добавляем обработчик клика для изменения статуса
                listView.setOnItemClickListener { parent, view, position, id ->
                    val selectedApplication = applications[position]
                    showStatusDialog(selectedApplication)
                }
            }
            // Если список пустой, tvEmpty будет показан автоматически благодаря emptyView
        } catch (e: Exception) {
            Toast.makeText(this, "Ошибка загрузки заявок", Toast.LENGTH_SHORT).show()
            android.util.Log.e("ManageApplications", "Error loading applications", e)
        }
    }

    private fun showStatusDialog(application: DatabaseHelper.Application) {
        val statuses = arrayOf("В работе", "Выполнено", "Отложено")
        val builder = android.app.AlertDialog.Builder(this)
        builder.setTitle("Изменить статус заявки")
        builder.setItems(statuses) { dialog, which ->
            val newStatus = statuses[which]
            updateApplicationStatus(application.id, newStatus)
        }
        builder.show()
    }

    private fun updateApplicationStatus(applicationId: Int, status: String) {
        val success = dbHelper.updateApplicationStatus(applicationId, status, 1) // 1 - ID исполнителя
        if (success) {
            Toast.makeText(this, "Статус обновлен: $status", Toast.LENGTH_SHORT).show()
            loadApplications()
        } else {
            Toast.makeText(this, "Ошибка обновления статуса", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onResume() {
        super.onResume()
        loadApplications()
    }

    override fun onSupportNavigateUp(): Boolean {
        // Используем finish() вместо устаревшего onBackPressed()
        finish()
        return true
    }
}