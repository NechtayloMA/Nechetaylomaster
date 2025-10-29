package com.example.comfortandmodernityoftheuniversityenvironment

// Импорт класса Bundle для сохранения состояния активности
import android.os.Bundle
// Импорт класса Button для работы с кнопками
import android.widget.Button
// Импорт класса EditText для работы с полями ввода
import android.widget.EditText
// Импорт класса Toast для показа всплывающих сообщений
import android.widget.Toast
// Импорт базового класса Activity с поддержкой Material Design
import androidx.appcompat.app.AppCompatActivity
// Импорт класса для форматирования даты
import java.text.SimpleDateFormat
// Импорт класса для работы с датой и временем
import java.util.*

//

// Класс активности для ввода показаний счетчиков
class MetersActivity : AppCompatActivity() {

    // Поле для ввода показаний воды (инициализация отложена)
    private lateinit var etWater: EditText
    // Поле для ввода показаний газа
    private lateinit var etGas: EditText
    // Поле для ввода показаний электроэнергии
    private lateinit var etElectricity: EditText
    // Кнопка отправки данных
    private lateinit var btnSubmit: Button
    // Помощник для работы с базой данных
    private lateinit var dbHelper: DatabaseHelper

    // Основной метод, вызываемый при создании активности
    override fun onCreate(savedInstanceState: Bundle?) {
        // Вызов метода родительского класса
        super.onCreate(savedInstanceState)
        // Установка layout-файла для этой активности
        setContentView(R.layout.activity_meters)

        // Инициализация помощника базы данных
        dbHelper = DatabaseHelper(this)

        // Привязка элементов интерфейса к переменным
        etWater = findViewById(R.id.etWater)
        etGas = findViewById(R.id.etGas)
        etElectricity = findViewById(R.id.etElectricity)
        btnSubmit = findViewById(R.id.btnSubmit)

        // Установка обработчика нажатия на кнопку отправки
        btnSubmit.setOnClickListener {
            // Получение введенных значений из полей ввода
            val waterStr = etWater.text.toString()
            val gasStr = etGas.text.toString()
            val electricityStr = etElectricity.text.toString()

            // Проверка на пустые поля
            if (waterStr.isEmpty() || gasStr.isEmpty() || electricityStr.isEmpty()) {
                // Показать сообщение об ошибке
                Toast.makeText(this, "Заполните все поля", Toast.LENGTH_SHORT).show()
                // Выход из обработчика
                return@setOnClickListener
            }

            // Блок try-catch для обработки ошибок преобразования типов
            try {
                // Преобразование строк в числа с плавающей точкой
                val water = waterStr.toDouble()
                val gas = gasStr.toDouble()
                val electricity = electricityStr.toDouble()

                // Временное значение ID пользователя (в реальном приложении берется из сессии)
                val userId = 1
                // Форматирование текущей даты в строку (год-месяц-день)
                val date = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

                // Добавление показаний в базу данных
                val result = dbHelper.addMeterReading(userId, water, gas, electricity, date)

                // Проверка результата операции (-1 означает ошибку)
                if (result != -1L) {
                    // Показать сообщение об успехе
                    Toast.makeText(this, "Данные сохранены", Toast.LENGTH_SHORT).show()
                    // Закрыть текущую активность
                    finish()
                } else {
                    // Показать сообщение об ошибке
                    Toast.makeText(this, "Ошибка сохранения", Toast.LENGTH_SHORT).show()
                }
            } catch (e: NumberFormatException) {
                // Обработка ошибки неверного формата числа
                Toast.makeText(this, "Введите корректные числа", Toast.LENGTH_SHORT).show()
            }
        }
    }
}