package com.example.comfortandmodernityoftheuniversityenvironment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

class ApplicationsAdapter :
    ListAdapter<DatabaseHelper.Application, ApplicationsAdapter.ApplicationViewHolder>(ApplicationDiffCallback()) {

    class ApplicationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvTitle: TextView = itemView.findViewById(R.id.tvTitle)
        private val tvDescription: TextView = itemView.findViewById(R.id.tvDescription)
        private val tvPriority: TextView = itemView.findViewById(R.id.tvPriority)
        private val tvDate: TextView = itemView.findViewById(R.id.tvDate)
        private val tvStatus: TextView = itemView.findViewById(R.id.tvStatus)

        fun bind(application: DatabaseHelper.Application) {
            tvTitle.text = application.title
            tvDescription.text = application.content
            tvPriority.text = itemView.context.getString(R.string.priority_format, application.priority)
            tvDate.text = application.date
            tvStatus.text = itemView.context.getString(R.string.status_format, application.status)

            // Цвет статуса в зависимости от состояния
            setStatusColor(application.status)
            // Цвет приоритета в зависимости от уровня
            setPriorityColor(application.priority)
        }

        private fun setStatusColor(status: String) {
            val color = when (status) {
                "Выполнено" -> android.graphics.Color.parseColor("#00bd1c")
                "В работе" -> android.graphics.Color.parseColor("#1526e8")
                "Отложено" -> android.graphics.Color.parseColor("#8b3e13")
                else -> android.graphics.Color.GRAY
            }
            tvStatus.setTextColor(color)
        }

        private fun setPriorityColor(priority: String) {
            val color = when (priority) {
                "Низкий" -> android.graphics.Color.parseColor("#00a619")
                "Средний" -> android.graphics.Color.parseColor("#e6c702")
                "Высокий" -> android.graphics.Color.parseColor("#c94e0c")
                "Критический" -> android.graphics.Color.parseColor("#e30000")
                else -> android.graphics.Color.GRAY
            }
            tvPriority.setTextColor(color)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ApplicationViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_application, parent, false)
        return ApplicationViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ApplicationViewHolder, position: Int) {
        val application = getItem(position)
        holder.bind(application)
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