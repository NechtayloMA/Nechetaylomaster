package com.example.comfortandmodernityoftheuniversityenvironment

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class EditProfileActivity : AppCompatActivity() {

    private lateinit var etUsername: EditText
    private lateinit var etPassword: EditText
    private lateinit var etConfirmPassword: EditText
    private lateinit var btnSave: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)

        supportActionBar?.title = "Редактирование профиля"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        etUsername = findViewById(R.id.etUsername)
        etPassword = findViewById(R.id.etPassword)
        etConfirmPassword = findViewById(R.id.etConfirmPassword)
        btnSave = findViewById(R.id.btnSave)

        // Загрузка текущих данных пользователя (заглушка)
        etUsername.setText("testuser")

        btnSave.setOnClickListener {
            saveProfile()
        }
    }

    private fun saveProfile() {
        val username = etUsername.text.toString().trim()
        val password = etPassword.text.toString()
        val confirmPassword = etConfirmPassword.text.toString()

        if (username.isEmpty()) {
            etUsername.error = "Введите имя пользователя"
            return
        }

        if (password.isNotEmpty() && password != confirmPassword) {
            etConfirmPassword.error = "Пароли не совпадают"
            return
        }

        // Здесь должна быть логика сохранения данных пользователя в БД
        Toast.makeText(this, "Профиль обновлен", Toast.LENGTH_SHORT).show()
        finish()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}