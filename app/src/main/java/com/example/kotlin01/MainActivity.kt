package com.example.kotlin01

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.SearchView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kotlin01.adapter.StudentAdapter
import com.example.kotlin01.model.Student
import com.example.kotlin01.ui.AddNewStudent
import com.example.kotlin01.ui.EditStudentInfo
import com.example.kotlin01.ultis.ClickListener
import com.example.kotlin01.ultis.SwipeToDeleteCallback
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.view.*
import java.util.*

class MainActivity : AppCompatActivity(),
    ClickListener {
    private var studentAdapter: StudentAdapter = StudentAdapter(this)
    private var listStudent: ArrayList<Student> = arrayListOf()
    private val gson = Gson()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        addDumpData()
        initRecyclerView()
    }

    private fun addNewStudent() {
        val intentAdd = Intent(this@MainActivity, AddNewStudent::class.java)
        startActivityForResult(intentAdd, 1)
    }

    private fun addDumpData() {
        for (i in 1..5) {
            val student = Student()
            student.name = "Dat $i"
            student.speciality = "CNTT"
            student.DOB = "1998"
            student.phoneNumber = "12345678$i"
            student.level = "University"
            listStudent.add(student)
        }
        for (i in 1..5) {
            val student = Student()
            student.name = "Huy $i"
            student.speciality = "ATTT"
            student.DOB = "2000"
            student.phoneNumber = "09123456$i"
            student.level = "College"
            listStudent.add(student)
        }
    }

    private fun initRecyclerView() {
        recycler_view.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = studentAdapter
            studentAdapter.setData(listStudent)
            val swipeDelete = object : SwipeToDeleteCallback() {
                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    studentAdapter.delItem(viewHolder.adapterPosition)
                }
            }
            val touchHelper = ItemTouchHelper(swipeDelete)
            touchHelper.attachToRecyclerView(recycler_view)
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK && data != null) {
                val studentResult: Student =
                    gson.fromJson(data.getStringExtra("result"), Student::class.java)
                listStudent.add(0, studentResult)
                studentAdapter.setData(listStudent)
            }
        }
        if (requestCode == 2) {
            if (resultCode == Activity.RESULT_OK && data != null) {
                val currentPosition = data.getIntExtra("position", 0)
                val studentEdited: Student =
                    gson.fromJson(data.getStringExtra("result"), Student::class.java)
                listStudent[currentPosition] = studentEdited
                studentAdapter.setData(listStudent)
            }
        }
    }

    override fun onClickListener(student: Student, position: Int) {
        val intentEdit = Intent(this@MainActivity, EditStudentInfo::class.java)
        val studentJson = gson.toJson(student)
        intentEdit.putExtra("student", studentJson)
        intentEdit.putExtra("position", position)
        startActivityForResult(intentEdit, 2)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.nav_menu, menu)
        val search = menu?.findItem(R.id.search)
        val searchView = search?.actionView as SearchView
        searchView.queryHint = "Search"
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                studentAdapter.filter.filter(newText)
                return true
            }

        })
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.add_student -> addNewStudent()
            R.id.sort -> showSortDialog()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showSortDialog() {
        val builder = AlertDialog.Builder(this@MainActivity)
        builder.setTitle("Sort List")

        val level = arrayOf("University", "College", "Name")
        builder.setSingleChoiceItems(level, -1) { dialog, which ->
            when (which) {
                0 -> sortBy(0)
                1 -> sortBy(1)
                2 -> sortBy(2)
            }
        }
        builder.setPositiveButton("OK") { dialog, which ->
            // user clicked OK
        }
        val dialog = builder.create()
        dialog.show()
    }

    private fun sortBy(i: Int) {
        when (i) {
            0 -> {
                listStudent.sortByDescending { it.level }
                studentAdapter.notifyDataSetChanged()
            }
            1 -> {
                listStudent.sortBy { it.level }
                studentAdapter.notifyDataSetChanged()
            }
            2 -> {
                listStudent.sortBy { it.name }
                studentAdapter.notifyDataSetChanged()
            }

        }
    }
}
