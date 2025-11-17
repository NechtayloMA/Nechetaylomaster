package com.example.comfortandmodernityoftheuniversityenvironment

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class InventoryCatalogActivity : AppCompatActivity() {

    private lateinit var dbHelper: DatabaseHelper
    private lateinit var listView: ListView
    private lateinit var tvEmpty: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inventory_catalog)

        supportActionBar?.title = "Каталог имущества"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        dbHelper = DatabaseHelper(this)
        listView = findViewById(R.id.lvInventory)
        tvEmpty = findViewById(R.id.tvEmpty)

        loadInventory()
    }

    private fun loadInventory() {
        val inventoryItems = dbHelper.getAllInventoryItems()

        if (inventoryItems.isEmpty()) {
            tvEmpty.text = "Каталог имущества пуст"
            listView.visibility = android.view.View.GONE
            tvEmpty.visibility = android.view.View.VISIBLE
        } else {
            val adapter = ArrayAdapter(
                this,
                android.R.layout.simple_list_item_1,
                inventoryItems.map { item ->
                    "🏷️ ${item.name}\n" +
                            "📊 Инв. номер: ${item.inventoryNumber}\n" +
                            "📍 Местоположение: ${item.location}\n" +
                            "🔧 Состояние: ${item.condition}"
                }
            )

            listView.adapter = adapter
            listView.visibility = android.view.View.VISIBLE
            tvEmpty.visibility = android.view.View.GONE
        }
    }

    override fun onResume() {
        super.onResume()
        loadInventory()
    }

    override fun onSupportNavigateUp(): Boolean {
        // Заменяем устаревший onBackPressed на finish()
        finish()
        return true
    }
}