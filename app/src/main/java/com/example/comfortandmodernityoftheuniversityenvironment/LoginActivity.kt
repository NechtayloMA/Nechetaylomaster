package com.example.comfortandmodernityoftheuniversityenvironment

// Импорт класса Intent для перехода между активностями
import android.content.Intent
// Импорт класса Bundle для сохранения состояния
import android.os.Bundle
// Импорт виджета Button
import android.widget.Button
// Импорт виджета EditText для ввода текста
import android.widget.EditText
// Импорт Toast для показа всплывающих сообщений
import android.widget.Toast
// Импорт базового класса Activity
import androidx.appcompat.app.AppCompatActivity
//
// Класс активности для авторизации пользователей
class LoginActivity : AppCompatActivity() {

    // Поле для ввода логина (инициализация отложена)
    private lateinit var etUsername: EditText
    // Поле для ввода пароля
    private lateinit var etPassword: EditText
    // Кнопка входа
    private lateinit var btnLogin: Button
    // Кнопка регистрации
    private lateinit var btnRegister: Button
    // Помощник для работы с базой данных
    private lateinit var dbHelper: DatabaseHelper

    // Основной метод создания активности
    override fun onCreate(savedInstanceState: Bundle?) {
        // Вызов родительского метода
        super.onCreate(savedInstanceState)
        // Установка layout-файла для этой активности
        setContentView(R.layout.activity_login)

        // Инициализация помощника базы данных
        dbHelper = DatabaseHelper(this)

        // Привязка элементов интерфейса к переменным
        etUsername = findViewById(R.id.etUsername)
        etPassword = findViewById(R.id.etPassword)
        btnLogin = findViewById(R.id.btnLogin)
        btnRegister = findViewById(R.id.btnRegister)

        // Установка обработчика нажатия на кнопку входа
        btnLogin.setOnClickListener {
            // Получение введенного логина (с удалением пробелов)
            val username = etUsername.text.toString().trim()
            // Получение введенного пароля (с удалением пробелов)
            val password = etPassword.text.toString().trim()

            // Проверка на пустые поля
            if (username.isEmpty() || password.isEmpty()) {
                // Показ сообщения о необходимости заполнить все поля
                Toast.makeText(this, "Заполните все поля", Toast.LENGTH_SHORT).show()
            } else {
                // Проверка пользователя в базе данных
                if (dbHelper.checkUser(username, password)) {
                    // Сообщение об успешном входе
                    Toast.makeText(this, "Вход выполнен успешно", Toast.LENGTH_SHORT).show()
                    // Переход на главный экран
                    startActivity(Intent(this, MainMenuActivity::class.java))
                    // Завершение текущей активности
                    finish()
                } else {
                    // Сообщение об ошибке авторизации
                    Toast.makeText(this, "Неверный логин или пароль", Toast.LENGTH_SHORT).show()
                }
            }
        }

        // Установка обработчика нажатия на кнопку регистрации
        btnRegister.setOnClickListener {
            // Получение введенного логина (с удалением пробелов)
            val username = etUsername.text.toString().trim()
            // Получение введенного пароля (с удалением пробелов)
            val password = etPassword.text.toString().trim()

            // Проверка на пустые поля
            if (username.isEmpty() || password.isEmpty()) {
                // Показ сообщения о необходимости заполнить все поля
                Toast.makeText(this, "Заполните все поля", Toast.LENGTH_SHORT).show()
            } else {
                // Попытка добавить нового пользователя
                val result = dbHelper.addUser(username, password)
                // Проверка результата (-1 означает ошибку)
                if (result != -1L) {
                    // Сообщение об успешной регистрации
                    Toast.makeText(this, "Регистрация успешна", Toast.LENGTH_SHORT).show()
                } else {
                    // Сообщение об ошибке регистрации
                    Toast.makeText(this, "Ошибка регистрации", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}