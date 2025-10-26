package com.example.comfortandmodernityoftheuniversityenvironment

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

//class MainMenuActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_menu)

        // Настройка кнопки "Показания счетчиков" и обработчик нажатия
        findViewById<Button>(R.id.btnMeters).setOnClickListener {
            startActivity(Intent(this, MetersActivity::class.java))
        }

        // Настройка кнопки "Сканер штрих-кодов" и обработчик нажатия
        findViewById<Button>(R.id.btnBarcode).setOnClickListener {
            startActivity(Intent(this, BarcodeActivity::class.java))
        }

        // Настройка кнопки "Создать заявку" и обработчик нажатия
        findViewById<Button>(R.id.btnCreateRequest).setOnClickListener {
            startActivity(Intent(this, ApplicationsActivity::class.java))
        }

        // Настройка кнопки "Мои заявки" и обработчик нажатия
        findViewById<Button>(R.id.btnRequests).setOnClickListener {
            startActivity(Intent(this, ApplicationsListActivity::class.java))
        }

        // Настройка кнопки "О программе" и обработчик нажатия
        findViewById<Button>(R.id.btnAbout).setOnClickListener {
            startActivity(Intent(this, AboutActivity::class.java))
        }
    }
}