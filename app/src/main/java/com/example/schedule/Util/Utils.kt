package com.example.schedule.Util

object Utils {

    private const val sem = "Пр"
    private const val lab = "Лаб"
    private const val lect = "Лекц"

    fun deleteTypeOfSubjectPart(subjectName: String): String {
        val s = subjectName.split(".")
        return if (s.isNotEmpty()) {
            when (s[0]) {
                sem, lab, lect -> s[1].trim()
                else -> subjectName
            }
        } else subjectName
    }

    fun toUpperCaseFirstLetter(input: String) = input[0].toUpperCase() + input.substring(1)
}
