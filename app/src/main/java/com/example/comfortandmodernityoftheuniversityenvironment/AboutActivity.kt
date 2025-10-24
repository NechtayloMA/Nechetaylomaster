package com.example.comfortandmodernityoftheuniversityenvironment

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class AboutActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)

        // Дополнительная настройка, если нужна
        setupViews()
    }

    private fun setupViews() {
        // Инициализация элементов UI, если они есть
        // Например:
        // supportActionBar?.setDisplayHomeAsUpEnabled(true)
        // supportActionBar?.title = "О приложении"
    }

    // Опционально: обработка кнопки "Назад" в ActionBar
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}