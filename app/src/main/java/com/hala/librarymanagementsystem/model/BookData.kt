package com.hala.librarymanagementsystem.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "books")
class BookData(
    @PrimaryKey(autoGenerate = true) val id:Int = 0,
    val title: String,
    val author: String,
    val genre: String,
    val imageUri: String,
    var isBorrowed: Boolean = false
)