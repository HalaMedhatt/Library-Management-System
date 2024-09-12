package com.hala.librarymanagementsystem.model

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {

    @TypeConverter
    fun fromArrayList(value: ArrayList<Int>?): String? {
        // Convert ArrayList<Integer> to JSON String
        return Gson().toJson(value)
    }

    @TypeConverter
    fun toArrayList(value: String?): ArrayList<Int>? {
        // Convert JSON String back to ArrayList<Integer>
        val listType = object : TypeToken<ArrayList<Int>>() {}.type
        return Gson().fromJson(value, listType)
    }
}
