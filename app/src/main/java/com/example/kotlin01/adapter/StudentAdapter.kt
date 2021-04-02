package com.example.kotlin01.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.kotlin01.R
import com.example.kotlin01.model.Student
import com.example.kotlin01.ultis.ClickListener
import java.util.*


class StudentAdapter(private val click: ClickListener) :
    RecyclerView.Adapter<StudentAdapter.ViewHolder>(), Filterable {
    var listStudent: ArrayList<Student> = arrayListOf()
    var listFiltered: ArrayList<Student> = arrayListOf()

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var studentName: TextView = itemView.findViewById(R.id.tv_name)
        val studentDOB: TextView = itemView.findViewById(R.id.tv_dob)
        val studentPhoneNumber: TextView = itemView.findViewById(R.id.tv_phone_number)
        val studentSpeciality: TextView = itemView.findViewById(R.id.tv_speciality)
        val studentLevel: TextView = itemView.findViewById(R.id.tv_level)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_student, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listFiltered.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val student: Student = listFiltered[position]
        holder.studentName.text = student.name
        holder.studentDOB.text = student.DOB
        holder.studentPhoneNumber.text = student.phoneNumber
        holder.studentSpeciality.text = student.speciality
        holder.studentLevel.text = student.level
        holder.itemView.setOnClickListener {
            click.onClickListener(student, position)
        }

    }

    fun setData(data: ArrayList<Student>) {
        listStudent = data
        listFiltered = data
        notifyDataSetChanged()
    }

    fun delItem(position: Int) {
        listFiltered.removeAt(position)
        notifyDataSetChanged()
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val student: String = constraint.toString()
                listFiltered = if (student.isEmpty()) {
                    listStudent
                } else {
                    val filteredList: ArrayList<Student> = arrayListOf()
                    for (i in listStudent) {
                        if (i.name.toLowerCase(Locale.ROOT)
                                .contains(student.toLowerCase(Locale.ROOT)) || i.phoneNumber.contains(
                                student
                            ) || i.DOB.contains(student)
                        ) {
                            filteredList.add(i)
                        }
                    }
                    filteredList
                }
                val filterResult = FilterResults()
                filterResult.values = listFiltered
                filterResult.count = listFiltered.size
                return filterResult
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                if (results != null) listFiltered = results.values as ArrayList<Student>
                notifyDataSetChanged()
            }

        }
    }


}