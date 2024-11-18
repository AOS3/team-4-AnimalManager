package com.lion.a066ex_animalmanager.ui.extensions

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun Date.formatToString(pattern: String = "yyyy년 MM월 dd일", locale: Locale = Locale.getDefault()): String {
    val dateFormat = SimpleDateFormat(pattern, locale)
    return dateFormat.format(this)
}