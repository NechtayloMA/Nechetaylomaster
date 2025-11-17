package com.example.comfortandmodernityoftheuniversityenvironment

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class InventoryActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inventory)

        supportActionBar?.title = "Инвентаризация"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        findViewById<Button>(R.id.btnBarcode).setOnClickListener {
            startActivity(Intent(this, BarcodeActivity::class.java))
        }

        findViewById<Button>(R.id.btnCatalog).setOnClickListener {
            startActivity(Intent(this, InventoryCatalogActivity::class.java))
        }

        findViewById<Button>(R.id.btnAddItem).setOnClickListener {
            startActivity(Intent(this, AddInventoryItemActivity::class.java))
        }

        findViewById<Button>(R.id.btnSearch).setOnClickListener {
            startActivity(Intent(this, SearchInventoryActivity::class.java))
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}