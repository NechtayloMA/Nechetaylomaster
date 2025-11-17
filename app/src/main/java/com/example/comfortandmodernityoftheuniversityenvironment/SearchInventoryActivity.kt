package com.example.comfortandmodernityoftheuniversityenvironment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class SearchInventoryActivity : AppCompatActivity() {

    private lateinit var dbHelper: DatabaseHelper
    private lateinit var etSearch: EditText
    private lateinit var listView: ListView
    private lateinit var tvEmpty: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_inventory)

        supportActionBar?.title = "Поиск имущества"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        dbHelper = DatabaseHelper(this)
        etSearch = findViewById(R.id.etSearch)
        listView = findViewById(R.id.lvSearchResults)
        tvEmpty = findViewById(R.id.tvEmpty)

        setupSearch()
    }

    private fun setupSearch() {
        etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                performSearch(s.toString())
            }
        })
    }

    private fun performSearch(query: String) {
        if (query.length < 2) {
            listView.visibility = android.view.View.GONE
            tvEmpty.visibility = android.view.View.VISIBLE
            tvEmpty.text = "Введите минимум 2 символа для поиска"
            return
        }

        val searchResults = dbHelper.searchInventory(query)

        if (searchResults.isEmpty()) {
            tvEmpty.text = "По запросу \"$query\" ничего не найдено"
            listView.visibility = android.view.View.GONE
            tvEmpty.visibility = android.view.View.VISIBLE
        } else {
            val adapter = ArrayAdapter(
                this,
                android.R.layout.simple_list_item_1,
                searchResults.map { item ->
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

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}