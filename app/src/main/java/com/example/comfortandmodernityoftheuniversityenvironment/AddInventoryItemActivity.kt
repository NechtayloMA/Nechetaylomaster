package com.example.comfortandmodernityoftheuniversityenvironment

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanOptions

class AddInventoryItemActivity : AppCompatActivity() {

    private lateinit var dbHelper: DatabaseHelper
    private lateinit var etName: EditText
    private lateinit var etInventoryNumber: EditText
    private lateinit var etLocation: EditText
    private lateinit var spCondition: Spinner
    private lateinit var etDescription: EditText
    private lateinit var etBarcode: EditText
    private lateinit var btnScanBarcode: Button
    private lateinit var btnSave: Button

    private val conditionOptions = arrayOf("Отличное", "Хорошее", "Удовлетворительное", "Требует ремонта", "Списано")

    // Новый способ обработки сканирования штрих-кода
    private val barcodeLauncher = registerForActivityResult(ScanContract()) { result ->
        if (result.contents == null) {
            Toast.makeText(this, "Сканирование отменено", Toast.LENGTH_SHORT).show()
        } else {
            val barcode = result.contents
            etBarcode.setText(barcode)

            // Проверяем, есть ли уже имущество с таким штрих-кодом
            val existingItem = dbHelper.getInventoryByBarcode(barcode)
            if (existingItem != null) {
                Toast.makeText(
                    this,
                    "Штрих-код уже используется для: ${existingItem.name}",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_inventory_item)

        supportActionBar?.title = "Добавить имущество"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        dbHelper = DatabaseHelper(this)
        initViews()
        setupSpinner()
        setupButtonListeners()

        // Замена устаревшего onBackPressed на современный подход
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                finish()
            }
        })
    }

    private fun initViews() {
        etName = findViewById(R.id.etName)
        etInventoryNumber = findViewById(R.id.etInventoryNumber)
        etLocation = findViewById(R.id.etLocation)
        spCondition = findViewById(R.id.spCondition)
        etDescription = findViewById(R.id.etDescription)
        etBarcode = findViewById(R.id.etBarcode)
        btnScanBarcode = findViewById(R.id.btnScanBarcode)
        btnSave = findViewById(R.id.btnSave)
    }

    private fun setupSpinner() {
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, conditionOptions)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spCondition.adapter = adapter
    }

    private fun setupButtonListeners() {
        btnScanBarcode.setOnClickListener {
            val options = ScanOptions().apply {
                setDesiredBarcodeFormats(ScanOptions.ALL_CODE_TYPES)
                setPrompt("Сканирование штрих-кода имущества")
                setCameraId(0)
                setBeepEnabled(false)
                setBarcodeImageEnabled(false)
            }
            barcodeLauncher.launch(options)
        }

        btnSave.setOnClickListener {
            saveInventoryItem()
        }
    }

    private fun saveInventoryItem() {
        val name = etName.text.toString().trim()
        val inventoryNumber = etInventoryNumber.text.toString().trim()
        val location = etLocation.text.toString().trim()
        val condition = spCondition.selectedItem.toString()
        val description = etDescription.text.toString().trim()
        val barcode = etBarcode.text.toString().trim()

        if (name.isEmpty() || inventoryNumber.isEmpty() || location.isEmpty()) {
            Toast.makeText(this, "Заполните обязательные поля", Toast.LENGTH_SHORT).show()
            return
        }

        try {
            val result = dbHelper.addInventoryItem(
                name,
                inventoryNumber,
                location,
                condition,
                description,
                barcode.ifEmpty { null } // Используем ifEmpty вместо проверки isEmpty
            )

            if (result != -1L) {
                Toast.makeText(this, "Имущество успешно добавлено", Toast.LENGTH_SHORT).show()
                clearForm()
            } else {
                Toast.makeText(this, "Ошибка при добавлении имущества", Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            Toast.makeText(this, "Ошибка: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun clearForm() {
        etName.text.clear()
        etInventoryNumber.text.clear()
        etLocation.text.clear()
        etDescription.text.clear()
        etBarcode.text.clear()
        spCondition.setSelection(0)
    }

    override fun onSupportNavigateUp(): Boolean {
        // Используем finish() вместо устаревшего onBackPressed()
        finish()
        return true
    }
}