package com.example.comfortandmodernityoftheuniversityenvironment

// Импорт класса Intent для работы с межактивностными сообщениями
import android.content.Intent
// Импорт класса Bundle для сохранения состояния
import android.os.Bundle
// Импорт виджета Button
import android.widget.Button
// Импорт виджета TextView для отображения текста
import android.widget.TextView
// Импорт Toast для показа всплывающих уведомлений
import android.widget.Toast
// Импорт базового класса Activity
import androidx.appcompat.app.AppCompatActivity
// Импорт библиотеки ZXing для сканирования штрих-кодов
import com.google.zxing.integration.android.IntentIntegrator

// Определение класса активности для работы со штрих-кодами
class BarcodeActivity : AppCompatActivity() {

    // Объявление кнопки для запуска сканирования (инициализация отложена)
    private lateinit var btnScan: Button
    // Поле для отображения штрих-кода
    private lateinit var tvBarcode: TextView
    // Поле для отображения названия продукта
    private lateinit var tvProductName: TextView
    // Поле для отображения описания продукта
    private lateinit var tvDescription: TextView
    // Помощник для работы с базой данных
    private lateinit var dbHelper: DatabaseHelper

    // Основной метод создания активности
    override fun onCreate(savedInstanceState: Bundle?) {
        // Вызов родительского метода
        super.onCreate(savedInstanceState)
        // Установка layout-файла для этой активности
        setContentView(R.layout.activity_barcode)

        // Инициализация помощника базы данных
        dbHelper = DatabaseHelper(this)

        // Привязка виджетов из layout к переменным
        btnScan = findViewById(R.id.btnScan)
        tvBarcode = findViewById(R.id.tvBarcode)
        tvProductName = findViewById(R.id.tvProductName)
        tvDescription = findViewById(R.id.tvDescription)

        // Установка обработчика клика на кнопку сканирования
        btnScan.setOnClickListener {
            // Создание интегратора ZXing
            val integrator = IntentIntegrator(this)
            // Указание всех поддерживаемых типов штрих-кодов
            integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES)
            // Установка текста подсказки при сканировании
            integrator.setPrompt("Сканирование штрих-кода")
            // Выбор основной камеры (0 - задняя)
            integrator.setCameraId(0)
            // Отключение звукового сигнала при сканировании
            integrator.setBeepEnabled(false)
            // Запрет сохранения изображения штрих-кода
            integrator.setBarcodeImageEnabled(false)
            // Запуск процесса сканирования
            integrator.initiateScan()
        }
    }

    // Обработка результата от сканера (и других активностей)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        // Парсинг результата с помощью ZXing
        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        // Проверка, что результат относится к ZXing
        if (result != null) {
            // Если результат пуст (сканирование отменено)
            if (result.contents == null) {
                // Показ сообщения об отмене
                Toast.makeText(this, "Сканирование отменено", Toast.LENGTH_SHORT).show()
            } else {
                // Получение значения штрих-кода
                val barcode = result.contents
                // Отображение штрих-кода в TextView
                tvBarcode.text = "Штрих-код: $barcode"

                // Поиск информации о штрих-коде в базе данных
                val barcodeInfo = dbHelper.getBarcodeInfo(barcode)
                // Если информация найдена
                if (barcodeInfo != null) {
                    // Отображение названия продукта
                    tvProductName.text = "Название: ${barcodeInfo.productName}"
                    // Отображение описания продукта
                    tvDescription.text = "Описание: ${barcodeInfo.description}"
                } else {
                    // Сообщение, если продукт не найден
                    tvProductName.text = "Название: не найдено"
                    tvDescription.text = "Описание: нет данных"
                    // Всплывающее уведомление о ненайденном штрих-коде
                    Toast.makeText(this, "Штрих-код не найден в базе", Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            // Если результат не относится к ZXing, передача обработки родительскому классу
            super.onActivityResult(requestCode, resultCode, data)
        }
    }
}