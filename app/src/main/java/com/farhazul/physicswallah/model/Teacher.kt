package com.farhazul.physicswallah.model

import java.lang.StringBuilder

data class Teacher(
    val id: Int,
    val name: String,
    val subjects: List<String>,
    val qualifications: List<String>,
    val profileImg: String,
){
    /*Returns qualification String of qualifications-array*/
    fun qulificationsToString(): String{
        val sb = StringBuilder()

        for (i in qualifications.indices){
            val word = qualifications[i]
            if (i < qualifications.size-1) sb.append(word).append(",").append(" ")
            else sb.append(word)
        }

        return sb.toString()
    }

    /*Returns subject-string of subjects-array*/
    fun subjectsToString(): String{
        val sb = StringBuilder()
        for (i in subjects.indices){
            val word = subjects[i]
            if (i < subjects.size-1) sb.append(word).append(",").append(" ")
            else sb.append(word)
        }
        return sb.toString()
    }
}
