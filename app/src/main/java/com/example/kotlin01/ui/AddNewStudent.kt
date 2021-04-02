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
import kotlinx.android.synthetic.main.activity_add_new_student.*

class AddNewStudent : AppCompatActivity() {
    private val gson = Gson()
    private lateinit var spinner: Spinner
    private val student = Student()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_new_student)
        initSpinner()
        btn_add_student.setOnClickListener {
//            Log.d("datnt", checkDuplicatePhone().toString())
            if (edt_name.text.isNullOrBlank() || edt_speciality.text.isNullOrBlank() || edt_phone.text.isNullOrBlank() || edt_speciality.text.isNullOrBlank()) {
                Toast.makeText(this@AddNewStudent, "Please fill all the field", Toast.LENGTH_SHORT)
                    .show()

            } else if (checkDuplicatePhone()) {
                Toast.makeText(
                    this@AddNewStudent,
                    "Phone Number already exists",
                    Toast.LENGTH_SHORT
                )
                    .show()
            } else {
                val resultIntent = Intent()
                student.name = edt_name.text.toString()
                student.DOB = edt_dob.text.toString()
                student.phoneNumber = edt_phone.text.toString()
                student.speciality = edt_speciality.text.toString()
                val studentJson = gson.toJson(student)
                resultIntent.putExtra("result", studentJson)
                setResult(Activity.RESULT_OK, resultIntent)
                finish()
            }

        }

    }

    private fun initSpinner() {
        spinner = findViewById(R.id.spinner)
        val options = arrayListOf("University","College")
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
                student.level = options[position]
            }

        }
    }

    private fun checkDuplicatePhone(): Boolean {
        var checkResult = true
        val listStudent: ArrayList<Student> =
            intent.getSerializableExtra("list_student") as ArrayList<Student>
        for (i in 0 until listStudent.size) {
            checkResult = listStudent[i].phoneNumber == edt_phone.text.toString()
            break
        }
        return checkResult
    }
}