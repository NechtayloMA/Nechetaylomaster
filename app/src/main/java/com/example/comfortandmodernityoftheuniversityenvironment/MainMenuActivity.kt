package com.example.comfortandmodernityoftheuniversityenvironment

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainMenuActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_menu)

        // Настройка кнопки "Коммунальные услуги" и обработчик нажатия
        findViewById<Button>(R.id.btnUtilities).setOnClickListener {
            startActivity(Intent(this, UtilitiesActivity::class.java))
        }

        // Настройка кнопки "Инвентаризация" и обработчик нажатия
        findViewById<Button>(R.id.btnInventory).setOnClickListener {
            startActivity(Intent(this, InventoryActivity::class.java))
        }

        // Настройка кнопки "Заявки" и обработчик нажатия
        findViewById<Button>(R.id.btnRequests).setOnClickListener {
            startActivity(Intent(this, RequestsActivity::class.java))
        }

        // Настройка кнопки "Настройки" и обработчик нажатия
        findViewById<Button>(R.id.btnSettings).setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
        }

        // Настройка кнопки "О программе" и обработчик нажатия
        findViewById<Button>(R.id.btnAbout).setOnClickListener {
            startActivity(Intent(this, AboutActivity::class.java))
        }
    }
}