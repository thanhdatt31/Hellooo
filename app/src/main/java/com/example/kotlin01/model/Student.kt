package com.example.kotlin01.model

import java.io.Serializable

data class Student(
    var name: String = "",
    var DOB: String = "",
    var phoneNumber: String = "",
    var speciality: String = "",
    var level: String = ""
) : Serializable
