package com.example.comfortandmodernityoftheuniversityenvironment

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import java.text.SimpleDateFormat
import java.util.*

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "AppDatabase.db"
        private const val DATABASE_VERSION = 2 // Увеличиваем версию для миграции

        // Таблица пользователей
        const val TABLE_USERS = "users"
        const val COLUMN_USER_ID = "user_id"
        const val COLUMN_USERNAME = "username"
        const val COLUMN_PASSWORD = "password"

        // Таблица счетчиков
        const val TABLE_METERS = "meters"
        const val COLUMN_METER_ID = "meter_id"
        const val COLUMN_USER_ID_FK = "user_id"
        const val COLUMN_WATER = "water"
        const val COLUMN_GAS = "gas"
        const val COLUMN_ELECTRICITY = "electricity"
        const val COLUMN_DATE = "date"

        // Таблица заявок
        const val TABLE_APPLICATION = "application"
        const val COLUMN_APPLICATION_ID = "application_id"
        const val COLUMN_USER_ID_APP = "user_id"
        const val COLUMN_TITLE = "title"
        const val COLUMN_CONTENT = "content"
        const val COLUMN_PRIORITY = "priority"
        const val COLUMN_DATE_APP = "date"

        // Таблица штрих-кодов
        const val TABLE_BARCODES = "barcodes"
        const val COLUMN_BARCODE_ID = "barcode_id"
        const val COLUMN_BARCODE_VALUE = "barcode_value"
        const val COLUMN_PRODUCT_NAME = "product_name"
        const val COLUMN_DESCRIPTION = "description"

        // НОВАЯ: Таблица платежей
        const val TABLE_PAYMENTS = "payments"
        const val COLUMN_PAYMENT_ID = "payment_id"
        const val COLUMN_USER_ID_PAYMENT = "user_id"
        const val COLUMN_AMOUNT = "amount"
        const val COLUMN_PAYMENT_DATE = "payment_date"
        const val COLUMN_SERVICE_TYPE = "service_type"
        const val COLUMN_STATUS = "status"
        const val COLUMN_PERIOD = "period"
    }

    override fun onCreate(db: SQLiteDatabase) {
        // Таблица пользователей
        val createUserTable = """
            CREATE TABLE $TABLE_USERS (
                $COLUMN_USER_ID INTEGER PRIMARY KEY AUTOINCREMENT,  
                $COLUMN_USERNAME TEXT UNIQUE NOT NULL,             
                $COLUMN_PASSWORD TEXT NOT NULL                      
            )
        """.trimIndent()

        // Таблица счетчиков
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

        // Таблица заявок
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

        // Таблица штрих-кодов
        val createBarcodesTable = """
            CREATE TABLE $TABLE_BARCODES (
                $COLUMN_BARCODE_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_BARCODE_VALUE TEXT UNIQUE NOT NULL,        
                $COLUMN_PRODUCT_NAME TEXT NOT NULL,                 
                $COLUMN_DESCRIPTION TEXT                            
            )
        """.trimIndent()

        // НОВАЯ: Таблица платежей
        val createPaymentsTable = """
            CREATE TABLE $TABLE_PAYMENTS (
                $COLUMN_PAYMENT_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_USER_ID_PAYMENT INTEGER NOT NULL,
                $COLUMN_AMOUNT REAL NOT NULL,
                $COLUMN_SERVICE_TYPE TEXT NOT NULL,
                $COLUMN_STATUS TEXT NOT NULL,
                $COLUMN_PERIOD TEXT NOT NULL,
                $COLUMN_PAYMENT_DATE TEXT NOT NULL,
                FOREIGN KEY ($COLUMN_USER_ID_PAYMENT) REFERENCES $TABLE_USERS($COLUMN_USER_ID)
            )
        """.trimIndent()

        // Выполнение всех запросов
        db.execSQL(createUserTable)
        db.execSQL(createMetersTable)
        db.execSQL(createApplicationTable)
        db.execSQL(createBarcodesTable)
        db.execSQL(createPaymentsTable)

        // Добавление тестовых данных
        insertSampleBarcodes(db)
        insertSamplePayments(db)
        insertSampleUser(db)
    }

    private fun insertSampleBarcodes(db: SQLiteDatabase) {
        val barcodes = listOf(
            ContentValues().apply {
                put(COLUMN_BARCODE_VALUE, "123456789012")
                put(COLUMN_PRODUCT_NAME, "Минеральная вода")
                put(COLUMN_DESCRIPTION, "Газированная минеральная вода, 1л")
            },
            ContentValues().apply {
                put(COLUMN_BARCODE_VALUE, "234567890123")
                put(COLUMN_PRODUCT_NAME, "Хлеб")
                put(COLUMN_DESCRIPTION, "Белый хлеб, 500г")
            },
            ContentValues().apply {
                put(COLUMN_BARCODE_VALUE, "345678901234")
                put(COLUMN_PRODUCT_NAME, "Молоко")
                put(COLUMN_DESCRIPTION, "Пастеризованное молоко, 2.5% жирности, 1л")
            }
        )
        barcodes.forEach { db.insert(TABLE_BARCODES, null, it) }
    }

    // НОВЫЙ: Добавление тестовых платежей
    private fun insertSamplePayments(db: SQLiteDatabase) {
        val payments = listOf(
            ContentValues().apply {
                put(COLUMN_USER_ID_PAYMENT, 1)
                put(COLUMN_AMOUNT, 1500.0)
                put(COLUMN_SERVICE_TYPE, "Коммунальные услуги")
                put(COLUMN_STATUS, "Оплачено")
                put(COLUMN_PERIOD, "Январь 2024")
                put(COLUMN_PAYMENT_DATE, "2024-01-15")
            },
            ContentValues().apply {
                put(COLUMN_USER_ID_PAYMENT, 1)
                put(COLUMN_AMOUNT, 1200.0)
                put(COLUMN_SERVICE_TYPE, "Электричество")
                put(COLUMN_STATUS, "Оплачено")
                put(COLUMN_PERIOD, "Февраль 2024")
                put(COLUMN_PAYMENT_DATE, "2024-02-10")
            },
            ContentValues().apply {
                put(COLUMN_USER_ID_PAYMENT, 1)
                put(COLUMN_AMOUNT, 800.0)
                put(COLUMN_SERVICE_TYPE, "Вода")
                put(COLUMN_STATUS, "Ожидает оплаты")
                put(COLUMN_PERIOD, "Март 2024")
                put(COLUMN_PAYMENT_DATE, "")
            }
        )
        payments.forEach { db.insert(TABLE_PAYMENTS, null, it) }
    }

    // НОВЫЙ: Добавление тестового пользователя
    private fun insertSampleUser(db: SQLiteDatabase) {
        val user = ContentValues().apply {
            put(COLUMN_USERNAME, "testuser")
            put(COLUMN_PASSWORD, "password123")
        }
        db.insert(TABLE_USERS, null, user)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_USERS")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_METERS")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_BARCODES")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_APPLICATION")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_PAYMENTS")
        onCreate(db)
    }

    // === СУЩЕСТВУЮЩИЕ МЕТОДЫ ===
    fun addUser(username: String, password: String): Long {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_USERNAME, username)
            put(COLUMN_PASSWORD, password)
        }
        return db.insert(TABLE_USERS, null, values)
    }

    fun checkUser(username: String, password: String): Boolean {
        val db = this.readableDatabase
        val selection = "$COLUMN_USERNAME = ? AND $COLUMN_PASSWORD = ?"
        val selectionArgs = arrayOf(username, password)
        val cursor = db.query(TABLE_USERS, null, selection, selectionArgs, null, null, null)
        val count = cursor.count
        cursor.close()
        return count > 0
    }

    fun addMeterReading(userId: Int, water: Double, gas: Double, electricity: Double, date: String): Long {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_USER_ID_FK, userId)
            put(COLUMN_WATER, water)
            put(COLUMN_GAS, gas)
            put(COLUMN_ELECTRICITY, electricity)
            put(COLUMN_DATE, date)
        }
        return db.insert(TABLE_METERS, null, values)
    }

    fun getBarcodeInfo(barcodeValue: String): BarcodeInfo? {
        val db = this.readableDatabase
        val selection = "$COLUMN_BARCODE_VALUE = ?"
        val selectionArgs = arrayOf(barcodeValue)
        val cursor = db.query(TABLE_BARCODES, null, selection, selectionArgs, null, null, null)

        return if (cursor.moveToFirst()) {
            val productName = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PRODUCT_NAME))
            val description = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRIPTION))
            BarcodeInfo(barcodeValue, productName, description)
        } else {
            null
        }.also { cursor.close() }
    }

    fun addApplication(userId: Int, title: String, content: String, priority: String): Long {
        val db = this.writableDatabase
        var result: Long = -1

        try {
            val values = ContentValues().apply {
                put(COLUMN_USER_ID_APP, userId)
                put(COLUMN_TITLE, title)
                put(COLUMN_CONTENT, content)
                put(COLUMN_PRIORITY, priority)
                put(COLUMN_DATE_APP, SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date()))
            }
            result = db.insert(TABLE_APPLICATION, null, values)
        } catch (e: Exception) {
            android.util.Log.e("DatabaseHelper", "Exception in addApplication: ${e.message}", e)
        } finally {
            db.close()
        }
        return result
    }

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

    // === НОВЫЕ МЕТОДЫ ДЛЯ ИСТОРИИ И ПЛАТЕЖЕЙ ===

    // Получение истории показаний
    fun getMeterReadings(userId: Int): List<MeterReading> {
        val readings = mutableListOf<MeterReading>()
        val db = this.readableDatabase
        val cursor = db.rawQuery(
            "SELECT * FROM $TABLE_METERS WHERE $COLUMN_USER_ID_FK = ? ORDER BY $COLUMN_DATE DESC",
            arrayOf(userId.toString())
        )

        cursor?.use {
            while (it.moveToNext()) {
                val reading = MeterReading(
                    id = it.getInt(it.getColumnIndexOrThrow(COLUMN_METER_ID)),
                    userId = it.getInt(it.getColumnIndexOrThrow(COLUMN_USER_ID_FK)),
                    water = it.getDouble(it.getColumnIndexOrThrow(COLUMN_WATER)),
                    gas = it.getDouble(it.getColumnIndexOrThrow(COLUMN_GAS)),
                    electricity = it.getDouble(it.getColumnIndexOrThrow(COLUMN_ELECTRICITY)),
                    date = it.getString(it.getColumnIndexOrThrow(COLUMN_DATE))
                )
                readings.add(reading)
            }
        }
        db.close()
        return readings
    }

    // Получение платежей пользователя
    fun getPayments(userId: Int): List<Payment> {
        val payments = mutableListOf<Payment>()
        val db = this.readableDatabase
        val cursor = db.rawQuery(
            "SELECT * FROM $TABLE_PAYMENTS WHERE $COLUMN_USER_ID_PAYMENT = ? ORDER BY $COLUMN_PAYMENT_DATE DESC",
            arrayOf(userId.toString())
        )

        cursor?.use {
            while (it.moveToNext()) {
                val payment = Payment(
                    id = it.getInt(it.getColumnIndexOrThrow(COLUMN_PAYMENT_ID)),
                    userId = it.getInt(it.getColumnIndexOrThrow(COLUMN_USER_ID_PAYMENT)),
                    amount = it.getDouble(it.getColumnIndexOrThrow(COLUMN_AMOUNT)),
                    serviceType = it.getString(it.getColumnIndexOrThrow(COLUMN_SERVICE_TYPE)),
                    status = it.getString(it.getColumnIndexOrThrow(COLUMN_STATUS)),
                    period = it.getString(it.getColumnIndexOrThrow(COLUMN_PERIOD)),
                    date = it.getString(it.getColumnIndexOrThrow(COLUMN_PAYMENT_DATE))
                )
                payments.add(payment)
            }
        }
        db.close()
        return payments
    }

    // Добавление нового платежа
    fun addPayment(userId: Int, amount: Double, serviceType: String, period: String): Long {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_USER_ID_PAYMENT, userId)
            put(COLUMN_AMOUNT, amount)
            put(COLUMN_SERVICE_TYPE, serviceType)
            put(COLUMN_STATUS, "Ожидает оплаты")
            put(COLUMN_PERIOD, period)
            put(COLUMN_PAYMENT_DATE, "")
        }
        return db.insert(TABLE_PAYMENTS, null, values)
    }

    // Получение статистики по платежам
    fun getPaymentStatistics(userId: Int): PaymentStatistics {
        val db = this.readableDatabase
        val cursor = db.rawQuery(
            "SELECT $COLUMN_SERVICE_TYPE, SUM($COLUMN_AMOUNT) as total FROM $TABLE_PAYMENTS WHERE $COLUMN_USER_ID_PAYMENT = ? GROUP BY $COLUMN_SERVICE_TYPE",
            arrayOf(userId.toString())
        )

        var totalPaid = 0.0
        var totalPending = 0.0
        val serviceBreakdown = mutableMapOf<String, Double>()

        cursor?.use {
            while (it.moveToNext()) {
                val serviceType = it.getString(it.getColumnIndexOrThrow(COLUMN_SERVICE_TYPE))
                val total = it.getDouble(it.getColumnIndexOrThrow("total"))
                serviceBreakdown[serviceType] = total
            }
        }

        // Подсчет оплаченных и ожидающих платежей
        val statusCursor = db.rawQuery(
            "SELECT $COLUMN_STATUS, SUM($COLUMN_AMOUNT) as total FROM $TABLE_PAYMENTS WHERE $COLUMN_USER_ID_PAYMENT = ? GROUP BY $COLUMN_STATUS",
            arrayOf(userId.toString())
        )

        statusCursor?.use {
            while (it.moveToNext()) {
                val status = it.getString(it.getColumnIndexOrThrow(COLUMN_STATUS))
                val total = it.getDouble(it.getColumnIndexOrThrow("total"))
                if (status == "Оплачено") {
                    totalPaid = total
                } else {
                    totalPending = total
                }
            }
        }

        // В классе DatabaseHelper добавьте эти методы:

        // Метод проверки существования таблицы заявок
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

        // Метод проверки структуры таблицы заявок
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

        // Метод для добавления тестового пользователя (для отладки)
        fun addTestUser(): Long {
            return addUser("testuser", "testpassword")
        }

        db.close()
        return PaymentStatistics(totalPaid, totalPending, serviceBreakdown)
    }

    // === DATA CLASSES ===
    data class BarcodeInfo(
        val barcodeValue: String,
        val productName: String,
        val description: String
    )

    data class Application(
        val id: Int,
        val userId: Int,
        val title: String,
        val content: String,
        val priority: String,
        val date: String
    )

    data class MeterReading(
        val id: Int,
        val userId: Int,
        val water: Double,
        val gas: Double,
        val electricity: Double,
        val date: String
    )

    data class Payment(
        val id: Int,
        val userId: Int,
        val amount: Double,
        val serviceType: String,
        val status: String,
        val period: String,
        val date: String
    )

    data class PaymentStatistics(
        val totalPaid: Double,
        val totalPending: Double,
        val serviceBreakdown: Map<String, Double>
    )
}