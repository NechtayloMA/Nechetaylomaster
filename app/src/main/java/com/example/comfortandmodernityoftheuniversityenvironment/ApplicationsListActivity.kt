package com.example.comfortandmodernityoftheuniversityenvironment

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ApplicationsListActivity : AppCompatActivity() {

    private lateinit var dbHelper: DatabaseHelper
    private lateinit var recyclerView: RecyclerView
    private lateinit var applicationsAdapter: ApplicationsAdapter
    private var currentUserId: Long = -1 // Получите ID из системы аутентификации

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_applications_list)

        initViews()
        setupRecyclerView()
        loadApplications()
    }

    private fun initViews() {
        dbHelper = DatabaseHelper(this)
        recyclerView = findViewById(R.id.recyclerViewApplications)

        // TODO: Замените на реальное получение ID пользователя
        currentUserId = getCurrentUserId()
    }

    private fun setupRecyclerView() {
        applicationsAdapter = ApplicationsAdapter()
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = applicationsAdapter

        recyclerView.addItemDecoration(
            androidx.recyclerview.widget.DividerItemDecoration(
                this,
                LinearLayoutManager.VERTICAL
            )
        )
    }

    private fun loadApplications() {
        try {
            if (currentUserId == -1L) {
                Toast.makeText(this, "Пользователь не идентифицирован", Toast.LENGTH_SHORT).show()
                return
            }

            // Используем правильный метод с параметром userId
            val applications = dbHelper.getApplicationsByUserId(currentUserId.toInt())

            val applicationsWithStatus = applications.map { application ->
                // ИСПРАВЛЕНИЕ: Передаем application.id в метод getApplicationStatus
                val status = dbHelper.getApplicationStatus(application.id)
                DatabaseHelper.Application(
                    id = application.id,
                    userId = application.userId,
                    title = application.title,
                    content = application.content,
                    priority = application.priority,
                    date = application.date,
                    status = status
                )
            }

            applicationsAdapter.submitList(applicationsWithStatus)

            if (applications.isEmpty()) {
                Toast.makeText(this, "Заявок пока нет", Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            Toast.makeText(this, "Ошибка загрузки заявок", Toast.LENGTH_SHORT).show()
            android.util.Log.e("ApplicationsListActivity", "Error loading applications", e)
        }
    }

    private fun getCurrentUserId(): Long {
        // TODO: Реализуйте получение ID текущего пользователя
        // Примеры:
        // 1. Из SharedPreferences
        // 2. Из интента
        // 3. Из системы аутентификации
        return 1L // Заглушка
    }

    override fun onResume() {
        super.onResume()
        loadApplications()
    }

    override fun onDestroy() {
        super.onDestroy()
        dbHelper.close()
    }
}