package com.example.kotlin01.ultis

import com.example.kotlin01.model.Student

interface ClickListener {
    fun onClickListener(student: Student, position: Int)
}