package com.example.comfortandmodernityoftheuniversityenvironment

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.zxing.integration.android.IntentIntegrator

class BarcodeActivity : AppCompatActivity() {

    private lateinit var btnScan: Button
    private lateinit var tvBarcode: TextView
    private lateinit var tvProductName: TextView
    private lateinit var tvDescription: TextView
    private lateinit var tvInventoryInfo: TextView
    private lateinit var dbHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_barcode)

        dbHelper = DatabaseHelper(this)

        btnScan = findViewById(R.id.btnScan)
        tvBarcode = findViewById(R.id.tvBarcode)
        tvProductName = findViewById(R.id.tvProductName)
        tvDescription = findViewById(R.id.tvDescription)
        tvInventoryInfo = findViewById(R.id.tvInventoryInfo)

        btnScan.setOnClickListener {
            val integrator = IntentIntegrator(this)
            integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES)
            integrator.setPrompt("Сканирование штрих-кода")
            integrator.setCameraId(0)
            integrator.setBeepEnabled(false)
            integrator.setBarcodeImageEnabled(false)
            integrator.initiateScan()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (result != null) {
            if (result.contents == null) {
                Toast.makeText(this, "Сканирование отменено", Toast.LENGTH_SHORT).show()
            } else {
                val barcode = result.contents
                tvBarcode.text = "Штрих-код: $barcode"

                // Сначала ищем в инвентаризации
                val inventoryItem = dbHelper.getInventoryByBarcode(barcode)
                if (inventoryItem != null) {
                    tvProductName.text = "Название: ${inventoryItem.name}"
                    tvDescription.text = "Описание: ${inventoryItem.description}"
                    tvInventoryInfo.text = "Инв. номер: ${inventoryItem.inventoryNumber}\n" +
                            "Местоположение: ${inventoryItem.location}\n" +
                            "Состояние: ${inventoryItem.condition}"
                } else {
                    // Если не найдено в инвентаризации, ищем в таблице штрих-кодов
                    val barcodeInfo = dbHelper.getBarcodeInfo(barcode)
                    if (barcodeInfo != null) {
                        tvProductName.text = "Название: ${barcodeInfo.productName}"
                        tvDescription.text = "Описание: ${barcodeInfo.description}"
                        tvInventoryInfo.text = "Не зарегистрировано в инвентаризации"
                    } else {
                        tvProductName.text = "Название: не найдено"
                        tvDescription.text = "Описание: нет данных"
                        tvInventoryInfo.text = "Не найдено в базе данных"
                        Toast.makeText(this, "Штрих-код не найден в базе", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }
}