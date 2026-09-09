package com.polka.android.utils

import java.time.LocalDate
import java.time.format.DateTimeFormatter

fun LocalDate.toUiString(): String {
    val formatter = DateTimeFormatter.ofPattern("dd.MM.yy")
    return this.format(formatter)
}