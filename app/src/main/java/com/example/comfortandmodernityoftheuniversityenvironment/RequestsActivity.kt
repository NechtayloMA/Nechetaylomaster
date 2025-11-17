package com.example.comfortandmodernityoftheuniversityenvironment

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity

class RequestsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_requests)

        supportActionBar?.title = "Управление заявками"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val btnCreateRequest: Button = findViewById(R.id.btnCreateRequest)
        val btnViewRequests: Button = findViewById(R.id.btnViewRequests)
        val btnCompletedRequests: Button = findViewById(R.id.btnCompletedRequests)
        val btnManageRequests: Button = findViewById(R.id.btnManageRequests)

        btnCreateRequest.setOnClickListener {
            startActivity(Intent(this, ApplicationsActivity::class.java))
        }

        btnViewRequests.setOnClickListener {
            startActivity(Intent(this, ApplicationsListActivity::class.java))
        }

        btnCompletedRequests.setOnClickListener {
            startActivity(Intent(this, CompletedApplicationsActivity::class.java))
        }

        btnManageRequests.setOnClickListener {
            startActivity(Intent(this, ManageApplicationsActivity::class.java))
        }

        // Замена устаревшего onBackPressed на современный подход
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                finish()
            }
        })
    }

    override fun onSupportNavigateUp(): Boolean {
        // Используем finish() вместо устаревшего onBackPressed()
        finish()
        return true
    }
}