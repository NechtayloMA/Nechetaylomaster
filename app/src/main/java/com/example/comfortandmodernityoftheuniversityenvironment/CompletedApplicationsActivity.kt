package com.example.comfortandmodernityoftheuniversityenvironment

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity

class CompletedApplicationsActivity : AppCompatActivity() {

    private lateinit var dbHelper: DatabaseHelper
    private lateinit var listView: ListView
    private lateinit var tvEmpty: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_completed_applications)

        supportActionBar?.title = "Выполненные заявки"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        dbHelper = DatabaseHelper(this)
        listView = findViewById(R.id.lvCompletedApplications)
        tvEmpty = findViewById(R.id.tvEmpty)

        // Устанавливаем пустое view для ListView
        listView.emptyView = tvEmpty

        loadCompletedApplications()

        // Замена устаревшего onBackPressed на современный подход
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                finish()
            }
        })
    }

    private fun loadCompletedApplications() {
        try {
            val allApplications = dbHelper.getAllApplications()
            val completedApplications = allApplications.filter {
                dbHelper.getApplicationStatus(it.id) == "Выполнено"
            }

            if (completedApplications.isNotEmpty()) {
                val adapter = ArrayAdapter(
                    this,
                    android.R.layout.simple_list_item_1,
                    completedApplications.map { application ->
                        "✅ ${application.title}\n" +
                                "📝 ${application.content}\n" +
                                "⚡ Приоритет: ${application.priority}\n" +
                                "📅 ${application.date}"
                    }
                )

                listView.adapter = adapter
            }
            // Если список пустой, tvEmpty будет показан автоматически благодаря emptyView
        } catch (e: Exception) {
            Toast.makeText(this, "Ошибка загрузки заявок", Toast.LENGTH_SHORT).show()
            android.util.Log.e("CompletedApplications", "Error loading applications", e)
        }
    }

    override fun onResume() {
        super.onResume()
        loadCompletedApplications()
    }

    override fun onSupportNavigateUp(): Boolean {
        // Используем finish() вместо устаревшего onBackPressed()
        finish()
        return true
    }
}