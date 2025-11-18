package com.example.comfortandmodernityoftheuniversityenvironment

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import java.text.SimpleDateFormat
import java.util.*

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "AppDatabase.db"
        private const val DATABASE_VERSION = 3

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

        // Таблица платежей
        const val TABLE_PAYMENTS = "payments"
        const val COLUMN_PAYMENT_ID = "payment_id"
        const val COLUMN_USER_ID_PAYMENT = "user_id"
        const val COLUMN_AMOUNT = "amount"
        const val COLUMN_PAYMENT_DATE = "payment_date"
        const val COLUMN_SERVICE_TYPE = "service_type"
        const val COLUMN_STATUS = "status"
        const val COLUMN_PERIOD = "period"

        // Таблицы для инвентаризации
        const val TABLE_INVENTORY = "inventory"
        const val COLUMN_INVENTORY_ID = "inventory_id"
        const val COLUMN_INVENTORY_NAME = "inventory_name"
        const val COLUMN_INVENTORY_NUMBER = "inventory_number"
        const val COLUMN_LOCATION = "location"
        const val COLUMN_CONDITION = "condition"
        const val COLUMN_PHOTO_PATH = "photo_path"
        const val COLUMN_BARCODE_INV = "barcode_inv"
        const val COLUMN_DESCRIPTION_INV = "description_inv"
        const val COLUMN_ADDED_DATE = "added_date"

        const val TABLE_APPLICATION_STATUS = "application_status"
        const val COLUMN_STATUS_ID = "status_id"
        const val COLUMN_APPLICATION_ID_FK = "application_id"
        const val COLUMN_STATUS_APP = "status_app"
        const val COLUMN_STATUS_DATE = "status_date"
        const val COLUMN_ASSIGNED_TO = "assigned_to"

        const val TABLE_USER_ROLES = "user_roles"
        const val COLUMN_ROLE_ID = "role_id"
        const val COLUMN_USER_ID_ROLE = "user_id_role"
        const val COLUMN_ROLE = "role"
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

        // Таблица платежей
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

        // Таблица инвентаризации
        val createInventoryTable = """
            CREATE TABLE $TABLE_INVENTORY (
                $COLUMN_INVENTORY_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_INVENTORY_NAME TEXT NOT NULL,
                $COLUMN_INVENTORY_NUMBER TEXT UNIQUE NOT NULL,
                $COLUMN_LOCATION TEXT NOT NULL,
                $COLUMN_CONDITION TEXT NOT NULL,
                $COLUMN_DESCRIPTION_INV TEXT,
                $COLUMN_BARCODE_INV TEXT UNIQUE,
                $COLUMN_PHOTO_PATH TEXT,
                $COLUMN_ADDED_DATE TEXT NOT NULL
            )
        """.trimIndent()

        // Таблица статусов заявок
        val createApplicationStatusTable = """
            CREATE TABLE $TABLE_APPLICATION_STATUS (
                $COLUMN_STATUS_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_APPLICATION_ID_FK INTEGER NOT NULL,
                $COLUMN_STATUS_APP TEXT NOT NULL,
                $COLUMN_STATUS_DATE TEXT NOT NULL,
                $COLUMN_ASSIGNED_TO INTEGER,
                FOREIGN KEY ($COLUMN_APPLICATION_ID_FK) REFERENCES $TABLE_APPLICATION($COLUMN_APPLICATION_ID),
                FOREIGN KEY ($COLUMN_ASSIGNED_TO) REFERENCES $TABLE_USERS($COLUMN_USER_ID)
            )
        """.trimIndent()

        // Таблица ролей пользователей
        val createUserRolesTable = """
            CREATE TABLE $TABLE_USER_ROLES (
                $COLUMN_ROLE_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_USER_ID_ROLE INTEGER NOT NULL,
                $COLUMN_ROLE TEXT NOT NULL,
                FOREIGN KEY ($COLUMN_USER_ID_ROLE) REFERENCES $TABLE_USERS($COLUMN_USER_ID)
            )
        """.trimIndent()

        // Выполнение всех запросов
        db.execSQL(createUserTable)
        db.execSQL(createMetersTable)
        db.execSQL(createApplicationTable)
        db.execSQL(createBarcodesTable)
        db.execSQL(createPaymentsTable)
        db.execSQL(createInventoryTable)
        db.execSQL(createApplicationStatusTable)
        db.execSQL(createUserRolesTable)

        // Добавление тестовых данных
        insertSampleBarcodes(db)
        insertSamplePayments(db)
        insertSampleUser(db)
        insertSampleInventory(db)
        insertSampleRoles(db)
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

    private fun insertSampleUser(db: SQLiteDatabase) {
        val user = ContentValues().apply {
            put(COLUMN_USERNAME, "testuser")
            put(COLUMN_PASSWORD, "password123")
        }
        db.insert(TABLE_USERS, null, user)
    }

    private fun insertSampleInventory(db: SQLiteDatabase) {
        val inventoryItems = listOf(
            ContentValues().apply {
                put(COLUMN_INVENTORY_NAME, "Компьютерный стол")
                put(COLUMN_INVENTORY_NUMBER, "INV-001")
                put(COLUMN_LOCATION, "Кабинет 101")
                put(COLUMN_CONDITION, "Отличное")
                put(COLUMN_DESCRIPTION_INV, "Деревянный компьютерный стол")
                put(COLUMN_BARCODE_INV, "123456789012")
                put(COLUMN_ADDED_DATE, "2024-01-15")
            },
            ContentValues().apply {
                put(COLUMN_INVENTORY_NAME, "Проектор")
                put(COLUMN_INVENTORY_NUMBER, "INV-002")
                put(COLUMN_LOCATION, "Аудитория 201")
                put(COLUMN_CONDITION, "Хорошее")
                put(COLUMN_DESCRIPTION_INV, "Мультимедийный проектор")
                put(COLUMN_BARCODE_INV, "234567890123")
                put(COLUMN_ADDED_DATE, "2024-01-16")
            },
            ContentValues().apply {
                put(COLUMN_INVENTORY_NAME, "Стул офисный")
                put(COLUMN_INVENTORY_NUMBER, "INV-003")
                put(COLUMN_LOCATION, "Кабинет 102")
                put(COLUMN_CONDITION, "Удовлетворительное")
                put(COLUMN_DESCRIPTION_INV, "Офисный стул с регулировкой высоты")
                put(COLUMN_BARCODE_INV, "345678901234")
                put(COLUMN_ADDED_DATE, "2024-01-17")
            }
        )
        inventoryItems.forEach { db.insert(TABLE_INVENTORY, null, it) }
    }

    private fun insertSampleRoles(db: SQLiteDatabase) {
        val roles = listOf(
            ContentValues().apply {
                put(COLUMN_USER_ID_ROLE, 1)
                put(COLUMN_ROLE, "user")
            },
            ContentValues().apply {
                put(COLUMN_USER_ID_ROLE, 1)
                put(COLUMN_ROLE, "executor")
            }
        )
        roles.forEach { db.insert(TABLE_USER_ROLES, null, it) }
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_USERS")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_METERS")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_BARCODES")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_APPLICATION")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_PAYMENTS")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_INVENTORY")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_APPLICATION_STATUS")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_USER_ROLES")
        onCreate(db)
    }

    // === МЕТОДЫ РАБОТЫ С ПОЛЬЗОВАТЕЛЯМИ ===
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

    // === МЕТОДЫ РАБОТЫ С СЧЕТЧИКАМИ ===
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

    fun getMeterReadings(userId: Int): List<MeterReading> {
        val readings = mutableListOf<MeterReading>()
        val db = this.readableDatabase
        val cursor = db.rawQuery(
            "SELECT * FROM $TABLE_METERS WHERE $COLUMN_USER_ID_FK = ? ORDER BY $COLUMN_DATE DESC",
            arrayOf(userId.toString())
        )

        cursor.use {
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

    // === МЕТОДЫ РАБОТЫ СО ШТРИХ-КОДАМИ ===
    fun getBarcodeInfo(barcodeValue: String): BarcodeInfo? {
        val db = this.readableDatabase
        val selection = "$COLUMN_BARCODE_VALUE = ?"
        val selectionArgs = arrayOf(barcodeValue)
        val cursor = db.query(TABLE_BARCODES, null, selection, selectionArgs, null, null, null)

        return if (cursor.moveToFirst()) {
            val productName = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PRODUCT_NAME))
            val description = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRIPTION))
            cursor.close()
            BarcodeInfo(barcodeValue, productName, description)
        } else {
            cursor.close()
            null
        }
    }

    // === МЕТОДЫ РАБОТЫ С ЗАЯВКАМИ ===
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
            Log.e("DatabaseHelper", "Exception in addApplication: ${e.message}", e)
        } finally {
            db.close()
        }
        return result
    }

    fun getAllApplications(): List<Application> {
        val applications = mutableListOf<Application>()
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $TABLE_APPLICATION ORDER BY $COLUMN_APPLICATION_ID DESC", null)

        cursor.use {
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

        cursor.use {
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
        return applications
    }

    // Добавьте этот метод в класс DatabaseHelper
    fun checkUserExists(userId: Int): Boolean {
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT $COLUMN_USER_ID FROM $TABLE_USERS WHERE $COLUMN_USER_ID = ?", arrayOf(userId.toString()))
        val exists = cursor.count > 0
        cursor.close()
        return exists
    }

    // === МЕТОДЫ РАБОТЫ С ПЛАТЕЖАМИ ===
    fun getPayments(userId: Int): List<Payment> {
        val payments = mutableListOf<Payment>()
        val db = this.readableDatabase
        val cursor = db.rawQuery(
            "SELECT * FROM $TABLE_PAYMENTS WHERE $COLUMN_USER_ID_PAYMENT = ? ORDER BY $COLUMN_PAYMENT_DATE DESC",
            arrayOf(userId.toString())
        )

        cursor.use {
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

    fun getPaymentStatistics(userId: Int): PaymentStatistics {
        val db = this.readableDatabase
        var totalPaid = 0.0
        var totalPending = 0.0
        val serviceBreakdown = mutableMapOf<String, Double>()

        // Подсчет по типам услуг
        val cursor = db.rawQuery(
            "SELECT $COLUMN_SERVICE_TYPE, SUM($COLUMN_AMOUNT) as total FROM $TABLE_PAYMENTS WHERE $COLUMN_USER_ID_PAYMENT = ? GROUP BY $COLUMN_SERVICE_TYPE",
            arrayOf(userId.toString())
        )

        cursor.use {
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

        statusCursor.use {
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

        db.close()
        return PaymentStatistics(totalPaid, totalPending, serviceBreakdown)
    }

    // === МЕТОДЫ РАБОТЫ С ИНВЕНТАРИЗАЦИЕЙ ===
    fun addInventoryItem(name: String, inventoryNumber: String, location: String,
                         condition: String, description: String, barcode: String? = null): Long {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_INVENTORY_NAME, name)
            put(COLUMN_INVENTORY_NUMBER, inventoryNumber)
            put(COLUMN_LOCATION, location)
            put(COLUMN_CONDITION, condition)
            put(COLUMN_DESCRIPTION_INV, description)
            put(COLUMN_BARCODE_INV, barcode)
            put(COLUMN_ADDED_DATE, SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date()))
        }
        return db.insert(TABLE_INVENTORY, null, values)
    }

    fun getAllInventoryItems(): List<InventoryItem> {
        val items = mutableListOf<InventoryItem>()
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $TABLE_INVENTORY ORDER BY $COLUMN_INVENTORY_NAME", null)

        cursor.use {
            while (it.moveToNext()) {
                val item = InventoryItem(
                    id = it.getInt(it.getColumnIndexOrThrow(COLUMN_INVENTORY_ID)),
                    name = it.getString(it.getColumnIndexOrThrow(COLUMN_INVENTORY_NAME)),
                    inventoryNumber = it.getString(it.getColumnIndexOrThrow(COLUMN_INVENTORY_NUMBER)),
                    location = it.getString(it.getColumnIndexOrThrow(COLUMN_LOCATION)),
                    condition = it.getString(it.getColumnIndexOrThrow(COLUMN_CONDITION)),
                    description = it.getString(it.getColumnIndexOrThrow(COLUMN_DESCRIPTION_INV)),
                    barcode = it.getString(it.getColumnIndexOrThrow(COLUMN_BARCODE_INV)),
                    photoPath = it.getString(it.getColumnIndexOrThrow(COLUMN_PHOTO_PATH)),
                    addedDate = it.getString(it.getColumnIndexOrThrow(COLUMN_ADDED_DATE))
                )
                items.add(item)
            }
        }
        db.close()
        return items
    }

    fun searchInventory(query: String): List<InventoryItem> {
        val items = mutableListOf<InventoryItem>()
        val db = this.readableDatabase
        val selection = "$COLUMN_INVENTORY_NAME LIKE ? OR $COLUMN_INVENTORY_NUMBER LIKE ? OR $COLUMN_LOCATION LIKE ?"
        val selectionArgs = arrayOf("%$query%", "%$query%", "%$query%")

        val cursor = db.query(TABLE_INVENTORY, null, selection, selectionArgs, null, null, null)

        cursor.use {
            while (it.moveToNext()) {
                val item = InventoryItem(
                    id = it.getInt(it.getColumnIndexOrThrow(COLUMN_INVENTORY_ID)),
                    name = it.getString(it.getColumnIndexOrThrow(COLUMN_INVENTORY_NAME)),
                    inventoryNumber = it.getString(it.getColumnIndexOrThrow(COLUMN_INVENTORY_NUMBER)),
                    location = it.getString(it.getColumnIndexOrThrow(COLUMN_LOCATION)),
                    condition = it.getString(it.getColumnIndexOrThrow(COLUMN_CONDITION)),
                    description = it.getString(it.getColumnIndexOrThrow(COLUMN_DESCRIPTION_INV)),
                    barcode = it.getString(it.getColumnIndexOrThrow(COLUMN_BARCODE_INV)),
                    photoPath = it.getString(it.getColumnIndexOrThrow(COLUMN_PHOTO_PATH)),
                    addedDate = it.getString(it.getColumnIndexOrThrow(COLUMN_ADDED_DATE))
                )
                items.add(item)
            }
        }
        db.close()
        return items
    }

    fun getInventoryByBarcode(barcode: String): InventoryItem? {
        val db = this.readableDatabase
        val selection = "$COLUMN_BARCODE_INV = ?"
        val selectionArgs = arrayOf(barcode)
        val cursor = db.query(TABLE_INVENTORY, null, selection, selectionArgs, null, null, null)

        return if (cursor.moveToFirst()) {
            val item = InventoryItem(
                id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_INVENTORY_ID)),
                name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_INVENTORY_NAME)),
                inventoryNumber = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_INVENTORY_NUMBER)),
                location = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_LOCATION)),
                condition = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CONDITION)),
                description = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRIPTION_INV)),
                barcode = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_BARCODE_INV)),
                photoPath = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PHOTO_PATH)),
                addedDate = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ADDED_DATE))
            )
            cursor.close()
            item
        } else {
            cursor.close()
            null
        }
    }

    // === МЕТОДЫ РАБОТЫ СО СТАТУСАМИ ЗАЯВОК ===
    fun updateApplicationStatus(applicationId: Int, status: String, assignedTo: Int? = null): Boolean {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_APPLICATION_ID_FK, applicationId)
            put(COLUMN_STATUS_APP, status)
            put(COLUMN_STATUS_DATE, SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date()))
            if (assignedTo != null) {
                put(COLUMN_ASSIGNED_TO, assignedTo)
            }
        }
        val result = db.insert(TABLE_APPLICATION_STATUS, null, values)
        return result != -1L
    }

    fun getApplicationStatus(applicationId: Int): String {
        val db = this.readableDatabase
        val cursor = db.rawQuery(
            "SELECT $COLUMN_STATUS_APP FROM $TABLE_APPLICATION_STATUS WHERE $COLUMN_APPLICATION_ID_FK = ? ORDER BY $COLUMN_STATUS_DATE DESC LIMIT 1",
            arrayOf(applicationId.toString())
        )

        return if (cursor.moveToFirst()) {
            val status = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_STATUS_APP))
            cursor.close()
            status
        } else {
            cursor.close()
            "Новая"
        }
    }

    // === ВСПОМОГАТЕЛЬНЫЕ МЕТОДЫ ===
    fun checkDatabase(): Boolean {
        val db = this.readableDatabase
        return try {
            db.rawQuery("SELECT name FROM sqlite_master WHERE type='table' AND name='$TABLE_APPLICATION'", null).use { cursor ->
                cursor.count > 0
            }
        } catch (e: Exception) {
            Log.e("DatabaseHelper", "Error checking database", e)
            false
        } finally {
            db.close()
        }
    }

    fun checkTableStructure(): Boolean {
        val db = this.readableDatabase
        return try {
            db.rawQuery("PRAGMA table_info($TABLE_APPLICATION)", null).use { cursor ->
                Log.d("DatabaseHelper", "Table structure of $TABLE_APPLICATION:")
                while (cursor.moveToNext()) {
                    val name = cursor.getString(cursor.getColumnIndexOrThrow("name"))
                    val type = cursor.getString(cursor.getColumnIndexOrThrow("type"))
                    Log.d("DatabaseHelper", "Column: $name, Type: $type")
                }
                true
            }
        } catch (e: Exception) {
            Log.e("DatabaseHelper", "Error checking table structure", e)
            false
        } finally {
            db.close()
        }
    }

    fun addTestUser(): Long {
        return addUser("testuser", "testpassword")
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
        val date: String,
        val status: String = ""
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

    data class InventoryItem(
        val id: Int,
        val name: String,
        val inventoryNumber: String,
        val location: String,
        val condition: String,
        val description: String,
        val barcode: String?,
        val photoPath: String?,
        val addedDate: String
    )
}