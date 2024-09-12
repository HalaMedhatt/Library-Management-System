package com.hala.librarymanagementsystem.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "members")
class MemberData(
    @PrimaryKey(autoGenerate = true) val id:Int = 0,
    val email:String,
    val name:String,
    val isPremium:Boolean,
    val maxNumberOfBooks:Int) {

    var borrowedBooks : ArrayList<Int> = ArrayList()

    fun getCurrentlyBorrowedBooksCount(): Int = borrowedBooks.size
    // Function to return the list of borrowed books as a formatted string
    fun getBorrowedBooksList(): String {
        return borrowedBooks.joinToString(", ")
    }

}