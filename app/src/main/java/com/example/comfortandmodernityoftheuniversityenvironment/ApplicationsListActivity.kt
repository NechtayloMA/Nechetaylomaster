package com.example.comfortandmodernityoftheuniversityenvironment

import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ApplicationsListActivity : AppCompatActivity() {

    private lateinit var dbHelper: DatabaseHelper
    private lateinit var recyclerView: RecyclerView
    private lateinit var emptyStateText: TextView
    private lateinit var applicationsAdapter: ApplicationsAdapter
    private var currentUserId: Long = -1
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_applications_list)

        Log.d("ApplicationsList", "Activity created")

        initViews()
        setupRecyclerView()
        loadApplications()
    }

    private fun initViews() {
        dbHelper = DatabaseHelper(this)
        recyclerView = findViewById(R.id.recyclerViewApplications)
        emptyStateText = findViewById(R.id.tvEmpty) // Исправлено: tVEmptyState на tvEmpty

        sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE)
        currentUserId = getCurrentUserId()

        Log.d("ApplicationsList", "Current user ID: $currentUserId")
    }

    private fun setupRecyclerView() {
        applicationsAdapter = ApplicationsAdapter()
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = applicationsAdapter

        // Добавляем разделитель между элементами
        recyclerView.addItemDecoration(
            androidx.recyclerview.widget.DividerItemDecoration(
                this,
                LinearLayoutManager.VERTICAL
            )
        )
    }

    private fun loadApplications() {
        try {
            Log.d("ApplicationsList", "Loading applications for user: $currentUserId")

            if (currentUserId == -1L) {
                showEmptyState("Пользователь не идентифицирован")
                Log.e("ApplicationsList", "User not identified")
                return
            }

            val userApplications = dbHelper.getApplicationsByUserId(currentUserId.toInt())
            Log.d("ApplicationsList", "Raw applications found: ${userApplications.size}")

            if (userApplications.isEmpty()) {
                showEmptyState("Заявок пока нет")
                Log.d("ApplicationsList", "No applications found for user $currentUserId")
                return
            }

            // Получаем статус для каждой заявки
            val applicationsWithStatus = userApplications.map { application ->
                val status = dbHelper.getApplicationStatus(application.id) // Убрал оператор Элвиса
                Log.d("ApplicationsList", "App ID ${application.id} status: $status")

                DatabaseHelper.Application(
                    id = application.id,
                    userId = application.userId,
                    title = application.title ?: "Без названия",
                    content = application.content ?: "Описание отсутствует",
                    priority = application.priority ?: "Средний",
                    date = formatDate(application.date),
                    status = status
                )
            }

            applicationsAdapter.submitList(applicationsWithStatus)
            showApplicationsList()

            Toast.makeText(this, "Загружено ${applicationsWithStatus.size} заявок", Toast.LENGTH_SHORT).show()
            Log.d("ApplicationsList", "Successfully loaded ${applicationsWithStatus.size} applications")

        } catch (e: Exception) {
            showEmptyState("Ошибка загрузки заявок")
            Toast.makeText(this, "Ошибка загрузки заявок: ${e.message}", Toast.LENGTH_LONG).show()
            Log.e("ApplicationsList", "Error loading applications", e)
        }
    }

    private fun showEmptyState(message: String) {
        emptyStateText.text = message
        emptyStateText.visibility = View.VISIBLE
        recyclerView.visibility = View.GONE
    }

    private fun showApplicationsList() {
        emptyStateText.visibility = View.GONE
        recyclerView.visibility = View.VISIBLE
    }

    private fun getCurrentUserId(): Long {
        return try {
            var userId = sharedPreferences.getLong("user_id", -1L)
            Log.d("UserID", "1. User ID from SharedPreferences: $userId")

            if (userId == -1L) {
                userId = intent.getLongExtra("user_id", -1L)
                Log.d("UserID", "2. User ID from intent: $userId")
            }

            if (userId == -1L) {
                userId = getFirstUserIdFromDatabase()
                Log.d("UserID", "3. User ID from database: $userId")
            }

            Log.d("UserID", "Final user ID: $userId")
            userId
        } catch (e: Exception) {
            Log.e("UserID", "Error getting user ID", e)
            1L
        }
    }

    private fun getFirstUserIdFromDatabase(): Long {
        return try {
            val db = dbHelper.readableDatabase
            val cursor = db.rawQuery("SELECT id FROM users LIMIT 1", null)

            var userId = -1L
            if (cursor.moveToFirst()) {
                val idIndex = cursor.getColumnIndex("id")
                if (idIndex != -1) {
                    userId = cursor.getLong(idIndex)
                }
            }
            cursor.close()

            if (userId == -1L) {
                val newUserId = dbHelper.addUser("testuser", "password123")
                Log.d("UserID", "Created test user with ID: $newUserId")
                newUserId
            } else {
                userId
            }
        } catch (e: Exception) {
            Log.e("UserID", "Error getting first user ID", e)
            1L
        }
    }

    private fun formatDate(dateString: String?): String {
        if (dateString.isNullOrEmpty()) return "Дата не указана"

        return try {
            val inputFormat = java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss", java.util.Locale.getDefault())
            val outputFormat = java.text.SimpleDateFormat("dd.MM.yyyy HH:mm", java.util.Locale.getDefault())
            val date = inputFormat.parse(dateString)
            date?.let { outputFormat.format(it) } ?: dateString
        } catch (e: Exception) {
            dateString
        }
    }

    override fun onResume() {
        super.onResume()
        Log.d("ApplicationsList", "onResume - reloading applications")
        loadApplications()
    }

    override fun onDestroy() {
        super.onDestroy()
        dbHelper.close()
    }
}