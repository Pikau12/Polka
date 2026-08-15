package com.polka.android.utils

import java.util.EnumSet

inline fun <reified T : Enum<T>> Long.toEnumSet(): EnumSet<T> {
    val set = EnumSet.noneOf(T::class.java)
    val values = enumValues<T>()
    for (value in values) {
        if ((this and (1L shl value.ordinal)) != 0L) {
            set.add(value)
        }
    }
    return set
}

inline fun <reified T : Enum<T>> Collection<T>.toBitmask(): Long {
    var mask = 0L
    for (value in this) {
        mask = mask or (1L shl value.ordinal)
    }
    return mask
}
