package com.hala.librarymanagementsystem.model

class memberData(val id:Int , val email:String,val name:String, val isPremium:Boolean ,val MaxNumberOfBooks:Int) {

private var borrowedBooks : ArrayList <String> =ArrayList()

    fun getCurrentlyBorrowedBooksCount(): Int = borrowedBooks.size
    // Function to return the list of borrowed books as a formatted string
    fun getBorrowedBooksList(): String {
        return borrowedBooks.joinToString(", ")
    }

}