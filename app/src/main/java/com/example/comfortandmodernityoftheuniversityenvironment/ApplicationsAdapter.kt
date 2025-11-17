package com.example.comfortandmodernityoftheuniversityenvironment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

class ApplicationsAdapter(private val dbHelper: DatabaseHelper) :
    ListAdapter<DatabaseHelper.Application, ApplicationsAdapter.ApplicationViewHolder>(ApplicationDiffCallback()) {

    class ApplicationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvTitle: TextView = itemView.findViewById(R.id.tvTitle)
        val tvDescription: TextView = itemView.findViewById(R.id.tvDescription)
        val tvPriority: TextView = itemView.findViewById(R.id.tvPriority)
        val tvDate: TextView = itemView.findViewById(R.id.tvDate)
        val tvStatus: TextView = itemView.findViewById(R.id.tvStatus)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ApplicationViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_application, parent, false)
        return ApplicationViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ApplicationViewHolder, position: Int) {
        val application = getItem(position)
        val status = dbHelper.getApplicationStatus(application.id)

        holder.tvTitle.text = application.title
        holder.tvDescription.text = application.content
        holder.tvPriority.text = "Приоритет: ${application.priority}"
        holder.tvDate.text = application.date
        holder.tvStatus.text = "Статус: $status"

        // Цвет статуса в зависимости от состояния
        when (status) {
            "Выполнено" -> holder.tvStatus.setTextColor(android.graphics.Color.GREEN)
            "В работе" -> holder.tvStatus.setTextColor(android.graphics.Color.BLUE)
            "Отложено" -> holder.tvStatus.setTextColor(android.graphics.Color.YELLOW)
            else -> holder.tvStatus.setTextColor(android.graphics.Color.GRAY)
        }
    }
}

class ApplicationDiffCallback : DiffUtil.ItemCallback<DatabaseHelper.Application>() {
    override fun areItemsTheSame(oldItem: DatabaseHelper.Application, newItem: DatabaseHelper.Application): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: DatabaseHelper.Application, newItem: DatabaseHelper.Application): Boolean {
        return oldItem == newItem
    }
}