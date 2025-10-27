package com.example.comfortandmodernityoftheuniversityenvironment
app
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ApplicationsListActivity : AppCompatActivity() {

    private lateinit var dbHelper: DatabaseHelper
    private lateinit var recyclerView: RecyclerView
    private lateinit var applicationsAdapter: ApplicationsAdapter
    private var applicationsList = mutableListOf<DatabaseHelper.Application>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_applications_list)

        initViews()
        setupRecyclerView()
        loadApplications()
    }

    private fun initViews() {
        dbHelper = DatabaseHelper(this)
        recyclerView = findViewById(R.id.recyclerViewApplications)
    }

    private fun setupRecyclerView() {
        applicationsAdapter = ApplicationsAdapter(applicationsList)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = applicationsAdapter

        // Добавляем разделитель между элементами
        recyclerView.addItemDecoration(
            androidx.recyclerview.widget.DividerItemDecoration(
                this,
                LinearLayoutManager.VERTICAL
            )
        )
    }

    private fun loadApplications() {
        try {
            // Получаем все заявки из базы данных
            val applications = dbHelper.getAllApplications()
            applicationsList.clear()
            applicationsList.addAll(applications)
            applicationsAdapter.notifyDataSetChanged()

            if (applications.isEmpty()) {
                Toast.makeText(this, "Заявок пока нет", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Загружено заявок: ${applications.size}", Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            Toast.makeText(this, "Ошибка загрузки заявок", Toast.LENGTH_SHORT).show()
            android.util.Log.e("ApplicationsListActivity", "Error loading applications", e)
        }
    }

    override fun onResume() {
        super.onResume()
        // Обновляем список при возвращении на экран
        loadApplications()
    }

    override fun onDestroy() {
        super.onDestroy()
        dbHelper.close()
    }
}