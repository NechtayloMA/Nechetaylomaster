package com.example.comfortandmodernityoftheuniversityenvironment

// Импорт классов для работы с базой данных SQLite
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import java.text.SimpleDateFormat
import java.util.*

// Основной класс для работы с базой данных
class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    // Объект-компаньон для хранения констант
    companion object {
        // Имя файла базы данных
        private const val DATABASE_NAME = "AppDatabase.db"

        // Версия базы данных (при изменении сработает onUpgrade)
        private const val DATABASE_VERSION = 1

        // Константы для таблицы пользователей
        const val TABLE_USERS = "users"             // Имя таблицы
        const val COLUMN_USER_ID = "user_id"         // ID пользователя
        const val COLUMN_USERNAME = "username"       // Логин
        const val COLUMN_PASSWORD = "password"       // Пароль

        // Константы для таблицы счетчиков
        const val TABLE_METERS = "meters"            // Имя таблицы
        const val COLUMN_METER_ID = "meter_id"      // ID показания
        const val COLUMN_USER_ID_FK = "user_id"      // Внешний ключ на пользователя
        const val COLUMN_WATER = "water"             // Показания воды
        const val COLUMN_GAS = "gas"                 // Показания газа
        const val COLUMN_ELECTRICITY = "electricity" // Показания электричества
        const val COLUMN_DATE = "date"               // Дата показаний

        // Константы для таблицы заявок
        const val TABLE_APPLICATION = "application"
        const val COLUMN_APPLICATION_ID = "application_id"
        const val COLUMN_USER_ID_APP = "user_id"
        const val COLUMN_TITLE = "title"
        const val COLUMN_CONTENT = "content"
        const val COLUMN_PRIORITY = "priority"
        const val COLUMN_DATE_APP = "date"

        // Константы для таблицы штрих-кодов
        const val TABLE_BARCODES = "barcodes"        // Имя таблицы
        const val COLUMN_BARCODE_ID = "barcode_id"   // ID штрих-кода
        const val COLUMN_BARCODE_VALUE = "barcode_value" // Значение штрих-кода
        const val COLUMN_PRODUCT_NAME = "product_name"   // Название продукта
        const val COLUMN_DESCRIPTION = "description"     // Описание продукта
    }

    // Метод создания базы данных (вызывается при первом запуске)
    override fun onCreate(db: SQLiteDatabase) {
        // SQL-запрос для создания таблицы пользователей
        val createUserTable = """
            CREATE TABLE $TABLE_USERS (
                $COLUMN_USER_ID INTEGER PRIMARY KEY AUTOINCREMENT,  
                $COLUMN_USERNAME TEXT UNIQUE NOT NULL,             
                $COLUMN_PASSWORD TEXT NOT NULL                      
            )
        """.trimIndent()



        // SQL-запрос для создания таблицы показаний счетчиков
        val createMetersTable = """
            CREATE TABLE $TABLE_METERS (
                $COLUMN_METER_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_USER_ID_FK INTEGER NOT NULL,             
                $COLUMN_WATER REAL NOT NULL,                        
                $COLUMN_GAS REAL NOT NULL,                          
                $COLUMN_ELECTRICITY REAL NOT NULL,                  
                $COLUMN_DATE TEXT NOT NULL,                      
                FOREIGN KEY ($COLUMN_USER_ID_FK) REFERENCES $TABLE_USERS($COLUMN_USER_ID)  
            )
        """.trimIndent()

        // SQL-запрос для создания таблицы заявок (без FOREIGN KEY для упрощения)
        val createApplicationTable = """
            CREATE TABLE $TABLE_APPLICATION (
                $COLUMN_APPLICATION_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_USER_ID_APP INTEGER NOT NULL,             
                $COLUMN_TITLE TEXT NOT NULL,                        
                $COLUMN_CONTENT TEXT NOT NULL,                          
                $COLUMN_PRIORITY TEXT NOT NULL,                  
                $COLUMN_DATE_APP TEXT NOT NULL,
                FOREIGN KEY ($COLUMN_USER_ID_APP) REFERENCES $TABLE_USERS($COLUMN_USER_ID)  
            )
        """.trimIndent()


        // SQL-запрос для создания таблицы штрих-кодов
        val createBarcodesTable = """
            CREATE TABLE $TABLE_BARCODES (
                $COLUMN_BARCODE_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_BARCODE_VALUE TEXT UNIQUE NOT NULL,        
                $COLUMN_PRODUCT_NAME TEXT NOT NULL,                 
                $COLUMN_DESCRIPTION TEXT                            
            )
        """.trimIndent()

        // Выполнение SQL-запросов
        db.execSQL(createUserTable)
        db.execSQL(createMetersTable)
        db.execSQL(createBarcodesTable)
        db.execSQL(createApplicationTable)

        // Добавление тестовых данных в таблицу штрих-кодов
        insertSampleBarcodes(db)
    }

    // Приватный метод для добавления тестовых штрих-кодов
    private fun insertSampleBarcodes(db: SQLiteDatabase) {
        // Список тестовых штрих-кодов
        val barcodes = listOf(
            ContentValues().apply {  // Первый продукт
                put(COLUMN_BARCODE_VALUE, "123456789012")          // Штрих-код
                put(COLUMN_PRODUCT_NAME, "Минеральная вода")      // Название
                put(COLUMN_DESCRIPTION, "Газированная минеральная вода, 1л")  // Описание
            },
            ContentValues().apply {  // Второй продукт
                put(COLUMN_BARCODE_VALUE, "234567890123")
                put(COLUMN_PRODUCT_NAME, "Хлеб")
                put(COLUMN_DESCRIPTION, "Белый хлеб, 500г")
            },
            ContentValues().apply {  // Третий продукт
                put(COLUMN_BARCODE_VALUE, "345678901234")
                put(COLUMN_PRODUCT_NAME, "Молоко")
                put(COLUMN_DESCRIPTION, "Пастеризованное молоко, 2.5% жирности, 1л")
            }
        )

        // Вставка всех тестовых данных в таблицу
        barcodes.forEach { db.insert(TABLE_BARCODES, null, it) }
    }

    // Метод обновления базы данных при изменении версии
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // Удаление всех таблиц
        db.execSQL("DROP TABLE IF EXISTS $TABLE_USERS")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_METERS")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_BARCODES")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_APPLICATION")
        // Создание новой версии базы//
        onCreate(db)
    }

    // Метод добавления нового пользователя
    fun addUser(username: String, password: String): Long {
        val db = this.writableDatabase  // Получение базы для записи
        val values = ContentValues().apply {  // Создание значений
            put(COLUMN_USERNAME, username)    // Добавление логина
            put(COLUMN_PASSWORD, password)    // Добавление пароля
        }
        // Вставка записи и возврат ID новой строки
        return db.insert(TABLE_USERS, null, values)
    }

    // Метод проверки пользователя (авторизации)
    fun checkUser(username: String, password: String): Boolean {
        val db = this.readableDatabase  // Получение базы для чтения
        // Условие выборки: логин и пароль совпадают
        val selection = "$COLUMN_USERNAME = ? AND $COLUMN_PASSWORD = ?"
        val selectionArgs = arrayOf(username, password)  // Параметры для выборки
        // Выполнение запроса
        val cursor = db.query(TABLE_USERS, null, selection, selectionArgs, null, null, null)
        val count = cursor.count  // Получение количества найденных записей
        cursor.close()  // Закрытие курсора
        return count > 0  // Возврат true если пользователь найден
    }

    // Метод добавления показаний счетчиков
    fun addMeterReading(
        userId: Int,
        water: Double,
        gas: Double,
        electricity: Double,
        date: String
    ): Long {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_USER_ID_FK, userId)        // ID пользователя
            put(COLUMN_WATER, water)              // Показания воды
            put(COLUMN_GAS, gas)                  // Показания газа
            put(COLUMN_ELECTRICITY, electricity)  // Показания электричества
            put(COLUMN_DATE, date)                // Дата показаний
        }
        // Вставка записи и возврат ID
        return db.insert(TABLE_METERS, null, values)
    }

    // Метод получения информации о штрих-коде
    fun getBarcodeInfo(barcodeValue: String): BarcodeInfo? {
        val db = this.readableDatabase
        // Условие выборки по значению штрих-кода
        val selection = "$COLUMN_BARCODE_VALUE = ?"
        val selectionArgs = arrayOf(barcodeValue)
        // Выполнение запроса
        val cursor = db.query(TABLE_BARCODES, null, selection, selectionArgs, null, null, null)

        // Обработка результата
        return if (cursor.moveToFirst()) {  // Если есть результаты
            // Получение названия продукта
            val productName = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PRODUCT_NAME))
            // Получение описания
            val description = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRIPTION))
            // Создание объекта с информацией
            BarcodeInfo(barcodeValue, productName, description)
        } else {
            null  // Если штрих-код не найден
        }.also { cursor.close() }  // Закрытие курсора в любом случае
    }

    // Класс данных для хранения информации о штрих-коде
    data class BarcodeInfo(
        val barcodeValue: String,  // Значение штрих-кода
        val productName: String,   // Название продукта
        val description: String   // Описание продукта
    )

    // Метод проверки существования таблицы
    fun checkDatabase(): Boolean {
        val db = this.readableDatabase
        return try {
            db.rawQuery("SELECT name FROM sqlite_master WHERE type='table' AND name='$TABLE_APPLICATION'", null).use { cursor ->
                cursor != null && cursor.count > 0
            }
        } catch (e: Exception) {
            android.util.Log.e("DatabaseHelper", "Error checking database", e)
            false
        } finally {
            db.close()
        }
    }

    // Метод проверки структуры таблицы
    fun checkTableStructure(): Boolean {
        val db = this.readableDatabase
        return try {
            db.rawQuery("PRAGMA table_info($TABLE_APPLICATION)", null).use { cursor ->
                if (cursor != null) {
                    android.util.Log.d("DatabaseHelper", "Table structure of $TABLE_APPLICATION:")
                    while (cursor.moveToNext()) {
                        val name = cursor.getString(cursor.getColumnIndexOrThrow("name"))
                        val type = cursor.getString(cursor.getColumnIndexOrThrow("type"))
                        android.util.Log.d("DatabaseHelper", "Column: $name, Type: $type")
                    }
                    true
                } else {
                    false
                }
            }
        } catch (e: Exception) {
            android.util.Log.e("DatabaseHelper", "Error checking table structure", e)
            false
        } finally {
            db.close()
        }
    }

    // Метод добавления новой заявки с улучшенной обработкой ошибок
    fun addApplication(userId: Int, title: String, content: String, priority: String): Long {
        val db = this.writableDatabase
        var result: Long = -1

        try {
            // Сначала логируем все данные
            android.util.Log.d("DatabaseHelper", "Adding application: userId=$userId, title='$title', content='${content.take(50)}...', priority='$priority'")

            val values = ContentValues().apply {
                put(COLUMN_USER_ID_APP, userId)
                put(COLUMN_TITLE, title)
                put(COLUMN_CONTENT, content)
                put(COLUMN_PRIORITY, priority)
                put(COLUMN_DATE_APP, SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date()))
            }

            // Пытаемся вставить данные
            result = db.insert(TABLE_APPLICATION, null, values)

            if (result == -1L) {
                android.util.Log.e("DatabaseHelper", "Failed to insert application")
                // Пробуем получить более подробную ошибку
                try {
                    db.execSQL("INSERT INTO $TABLE_APPLICATION ($COLUMN_USER_ID_APP, $COLUMN_TITLE, $COLUMN_CONTENT, $COLUMN_PRIORITY, $COLUMN_DATE_APP) VALUES (?, ?, ?, ?, ?)",
                        arrayOf(userId.toString(), title, content, priority, SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())))
                    result = 1 // Если успешно
                    android.util.Log.d("DatabaseHelper", "Application inserted via execSQL")
                } catch (e: Exception) {
                    android.util.Log.e("DatabaseHelper", "Detailed error: ${e.message}")
                }
            } else {
                android.util.Log.d("DatabaseHelper", "Application inserted successfully with ID: $result")
            }

        } catch (e: Exception) {
            android.util.Log.e("DatabaseHelper", "Exception in addApplication: ${e.message}", e)
        } finally {
            db.close()
        }

        return result
    }

    // Метод для добавления тестового пользователя (для отладки)
    fun addTestUser(): Long {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put("name", "Test User")
            put("email", "test@university.edu")
        }
        return db.insert(TABLE_USERS, null, values)
    }

    // Метод для получения всех заявок
    fun getAllApplications(): List<Application> {
        val applications = mutableListOf<Application>()
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $TABLE_APPLICATION ORDER BY $COLUMN_APPLICATION_ID DESC", null)

        cursor?.use {
            while (it.moveToNext()) {
                val application = Application(
                    id = it.getInt(it.getColumnIndexOrThrow(COLUMN_APPLICATION_ID)),
                    userId = it.getInt(it.getColumnIndexOrThrow(COLUMN_USER_ID_APP)),
                    title = it.getString(it.getColumnIndexOrThrow(COLUMN_TITLE)),
                    content = it.getString(it.getColumnIndexOrThrow(COLUMN_CONTENT)),
                    priority = it.getString(it.getColumnIndexOrThrow(COLUMN_PRIORITY)),
                    date = it.getString(it.getColumnIndexOrThrow(COLUMN_DATE_APP))
                )
                applications.add(application)
            }
        }
        db.close()
        return applications
    }

    // Метод для получения заявок по ID пользователя
    fun getApplicationsByUserId(userId: Int): List<Application> {
        val applications = mutableListOf<Application>()
        val db = this.readableDatabase
        val cursor = db.rawQuery(
            "SELECT * FROM $TABLE_APPLICATION WHERE $COLUMN_USER_ID_APP = ? ORDER BY $COLUMN_APPLICATION_ID DESC",
            arrayOf(userId.toString())
        )

        cursor?.use {
            while (it.moveToNext()) {
                val application = Application(
                    id = it.getInt(it.getColumnIndexOrThrow(COLUMN_APPLICATION_ID)),
                    userId = it.getInt(it.getColumnIndexOrThrow(COLUMN_USER_ID_APP)),
                    title = it.getString(it.getColumnIndexOrThrow(COLUMN_TITLE)),
                    content = it.getString(it.getColumnIndexOrThrow(COLUMN_CONTENT)),
                    priority = it.getString(it.getColumnIndexOrThrow(COLUMN_PRIORITY)),
                    date = it.getString(it.getColumnIndexOrThrow(COLUMN_DATE_APP))
                )
                applications.add(application)
            }
        }
        db.close()
        return applications
    }

    // Модель данных для заявки
    data class Application(
        val id: Int,
        val userId: Int,
        val title: String,
        val content: String,
        val priority: String,
        val date: String
    )
}


