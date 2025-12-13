package com.example.tokobuku

import androidx.room.TypeConverter

class Converters {
    @TypeConverter
    fun fromString(value: String): Array<String> {
        return value.split(",").toTypedArray()
    }

    @TypeConverter
    fun fromArray(array: Array<String>): String {
        return array.joinToString(",")
    }
}
