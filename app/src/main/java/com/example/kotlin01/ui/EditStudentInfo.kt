package com.example.kotlin01.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.kotlin01.R
import com.example.kotlin01.model.Student
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_edit_student_info.*

class EditStudentInfo : AppCompatActivity() {
    private val gson = Gson()
    private lateinit var spinner: Spinner
    private lateinit var student: Student
    private var studentEdited: Student = Student()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_student_info)
        initSpinner()
        val resultIntent = intent
        val studentJson = resultIntent.getStringExtra("student")
        val currentPosition = resultIntent.getIntExtra("position", 0)
        if (studentJson != null) {
            student = gson.fromJson(studentJson, Student::class.java)
            edt_name.setText(student.name)
            edt_dob.setText(student.DOB)
            edt_phone.setText(student.phoneNumber)
            edt_speciality.setText(student.speciality)
        }
        btn_save.setOnClickListener {
            if (edt_name.text.isNullOrBlank() || edt_speciality.text.isNullOrBlank() || edt_phone.text.isNullOrBlank() || edt_speciality.text.isNullOrBlank()) {
                Toast.makeText(
                    this@EditStudentInfo,
                    "Please fill all the field",
                    Toast.LENGTH_SHORT
                )
                    .show()
            } else {
                val resultEditIntent = Intent()
                studentEdited.name = edt_name.text.toString()
                studentEdited.DOB = edt_dob.text.toString()
                studentEdited.phoneNumber = edt_phone.text.toString()
                studentEdited.speciality = edt_speciality.text.toString()
                val studentEditJson = gson.toJson(studentEdited)
                resultEditIntent.putExtra("result", studentEditJson)
                resultEditIntent.putExtra("position", currentPosition)
                setResult(Activity.RESULT_OK, resultEditIntent)
                finish()
            }

        }
    }

    private fun initSpinner() {
        spinner = findViewById(R.id.spinner)
        val options = arrayListOf("University", "College")
        spinner.adapter =
            ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, options)
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                studentEdited.level = options[position]
            }
        }
    }
}