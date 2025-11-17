package com.example.comfortandmodernityoftheuniversityenvironment

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Switch
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        supportActionBar?.title = "Настройки"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val tvUsername = findViewById<TextView>(R.id.tvUsername)
        val btnEditProfile = findViewById<Button>(R.id.btnEditProfile)
        val switchNotifications = findViewById<Switch>(R.id.switchNotifications)
        val btnLogout = findViewById<Button>(R.id.btnLogout)

        tvUsername.text = "Пользователь: testuser"

        btnEditProfile.setOnClickListener {
            // Переход к редактированию профиля
            startActivity(Intent(this, EditProfileActivity::class.java))
        }

        btnLogout.setOnClickListener {
            // Выход из аккаунта
            val intent = Intent(this, LoginActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            finish()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}