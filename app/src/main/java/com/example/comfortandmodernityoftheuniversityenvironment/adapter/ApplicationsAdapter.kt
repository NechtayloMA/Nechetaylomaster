package com.example.comfortandmodernityoftheuniversityenvironment

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ApplicationsAdapter(
    private var applications: List<DatabaseHelper.Application>
) : RecyclerView.Adapter<ApplicationsAdapter.ApplicationViewHolder>() {

    inner class ApplicationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvTitle: TextView = itemView.findViewById(R.id.tvTitle)
        val tvContent: TextView = itemView.findViewById(R.id.tvContent)
        val tvPriority: TextView = itemView.findViewById(R.id.tvPriority)
        val tvDate: TextView = itemView.findViewById(R.id.tvDate)
        val tvId: TextView = itemView.findViewById(R.id.tvId)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ApplicationViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_application, parent, false)
        return ApplicationViewHolder(view)
    }

    override fun onBindViewHolder(holder: ApplicationViewHolder, position: Int) {
        val application = applications[position]

        holder.tvTitle.text = application.title
        holder.tvContent.text = application.content
        holder.tvPriority.text = application.priority
        holder.tvDate.text = formatDate(application.date)
        holder.tvId.text = "№${application.id}"

        // Устанавливаем цвет приоритета
        setPriorityColor(holder.tvPriority, application.priority)
    }

    override fun getItemCount(): Int = applications.size

    private fun formatDate(dateString: String): String {
        return try {
            // Если дата в формате timestamp
            if (dateString.toLongOrNull() != null) {
                val date = java.util.Date(dateString.toLong())
                android.text.format.DateFormat.format("dd.MM.yyyy HH:mm", date).toString()
            } else {
                // Если дата уже в строковом формате
                dateString
            }
        } catch (e: Exception) {
            dateString
        }
    }

    private fun setPriorityColor(textView: TextView, priority: String) {
        val color = when (priority.toLowerCase()) {
            "низкий" -> Color.parseColor("#4CAF50") // Зеленый
            "средний" -> Color.parseColor("#FF9800") // Оранжевый
            "высокий" -> Color.parseColor("#f74e42ff") // Красный
            "критический" -> Color.parseColor("#b50202") // Фиолетовый
            else -> Color.parseColor("#757575") // Серый
        }
        textView.setTextColor(color)
    }

    // Метод для обновления данных
    fun updateApplications(newApplications: List<DatabaseHelper.Application>) {
        applications = newApplications
        notifyDataSetChanged()
    }
}