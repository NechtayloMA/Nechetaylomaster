package com.example.comfortandmodernityoftheuniversityenvironment

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.MaterialAutoCompleteTextView

class ApplicationsActivity : AppCompatActivity() {
    private lateinit var dbHelper: DatabaseHelper
    private lateinit var etSubject: EditText
    private lateinit var etDescription: EditText
    private lateinit var actvPriority: MaterialAutoCompleteTextView
    private lateinit var btnSubmit: Button
    private lateinit var btnViewApplications: Button

    private val priorityOptions = arrayOf("Низкий", "Средний", "Высокий", "Критический")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_application)

        // Инициализация UI элементов
        initViews()

        // Инициализация DatabaseHelper
        dbHelper = DatabaseHelper(this)

        // Упрощенная проверка базы данных
        checkDatabaseStatus()

        // Настройка выпадающего списка приоритетов
        setupPriorityDropdown()

        // Настройка обработчиков кнопок
        setupButtonListeners()
    }

    private fun initViews() {
        etSubject = findViewById(R.id.etSubject)
        etDescription = findViewById(R.id.etDescription)
        actvPriority = findViewById(R.id.actvPriority)
        btnSubmit = findViewById(R.id.btnSubmit)
        btnViewApplications = findViewById(R.id.btnViewApplications)
    }

    private fun checkDatabaseStatus() {
        try {
            // Простая проверка - пытаемся получить список заявок
            val applications = dbHelper.getAllApplications()
            android.util.Log.d("ApplicationsActivity", "Database check: Found ${applications.size} applications")

            // Создаем тестового пользователя если нужно
            createTestUserIfNeeded()

        } catch (e: Exception) {
            android.util.Log.e("ApplicationsActivity", "Database check failed", e)
            showError("Ошибка базы данных: ${e.message}")
        }
    }

    private fun createTestUserIfNeeded() {
        try {
            // Пытаемся добавить тестового пользователя
            val testUserId = dbHelper.addUser("test", "P@ss25")
            if (testUserId != -1L) {
                android.util.Log.d("ApplicationsActivity", "Test user added with ID: $testUserId")
            } else {
                android.util.Log.d("ApplicationsActivity", "Test user already exists")
            }
        } catch (e: Exception) {
            android.util.Log.e("ApplicationsActivity", "Error creating test user", e)
        }
    }

    private fun setupPriorityDropdown() {
        val adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, priorityOptions)
        actvPriority.setAdapter(adapter)

        actvPriority.setOnItemClickListener { parent, view, position, id ->
            val selectedPriority = priorityOptions[position]
            android.util.Log.d("ApplicationsActivity", "Selected priority: $selectedPriority")
        }
    }

    private fun setupButtonListeners() {
        btnSubmit.setOnClickListener {
            submitApplication()
        }

        btnViewApplications.setOnClickListener {
            openApplicationsList()
        }
    }

    private fun openApplicationsList() {
        try {
            val intent = android.content.Intent(this, ApplicationsListActivity::class.java)
            startActivity(intent)
        } catch (e: Exception) {
            showError("Ошибка открытия списка заявок: ${e.message}")
            android.util.Log.e("ApplicationsActivity", "Error opening applications list", e)
        }
    }

    private fun submitApplication() {
        val subject = etSubject.text.toString().trim()
        val description = etDescription.text.toString().trim()
        val priority = actvPriority.text.toString().trim()

        android.util.Log.d("ApplicationsActivity", "Submit attempt - Subject: '$subject', Description: '${description.take(30)}...', Priority: '$priority'")

        if (!validateInput(subject, description, priority)) {
            return
        }

        val userId = getCurrentUserId()
        android.util.Log.d("ApplicationsActivity", "Using User ID: $userId")

        try {
            val result = dbHelper.addApplication(userId, subject, description, priority)
            android.util.Log.d("ApplicationsActivity", "Database insert result: $result")

            if (result != -1L) {
                showSuccess("Заявка успешно отправлена!")
                clearForm()
            } else {
                showError("Ошибка базы данных при отправке заявки. Проверьте логи.")
            }
        } catch (e: Exception) {
            android.util.Log.e("ApplicationsActivity", "Exception during submission", e)
            showError("Исключение при отправке: ${e.message}")
        }
    }

    private fun validateInput(subject: String, description: String, priority: String): Boolean {
        var isValid = true

        if (subject.isEmpty()) {
            etSubject.error = "Введите тему заявки"
            isValid = false
        } else {
            etSubject.error = null
        }

        if (description.isEmpty()) {
            etDescription.error = "Введите описание проблемы"
            isValid = false
        } else {
            etDescription.error = null
        }

        if (priority.isEmpty() || !priorityOptions.contains(priority)) {
            actvPriority.error = "Выберите приоритет из списка"
            isValid = false
        } else {
            actvPriority.error = null
        }

        return isValid
    }

    private fun showError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
        android.util.Log.e("ApplicationsActivity", "Error: $message")
    }

    private fun showSuccess(message: String) {
        Toast.makeText(this, "Заявка успешно отправлена!", Toast.LENGTH_SHORT).show()
        android.util.Log.d("ApplicationsActivity", "Success: $message")
    }

    private fun clearForm() {
        etSubject.text.clear()
        etDescription.text.clear()
        actvPriority.text.clear()
    }

    private fun getCurrentUserId(): Int {
        // Временное решение - возвращаем ID первого пользователя
        return 1
    }

    override fun onDestroy() {
        super.onDestroy()
        dbHelper.close()
    }
}