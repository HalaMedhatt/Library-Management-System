package com.hala.librarymanagementsystem.model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LibraryViewModel(application: Application): AndroidViewModel(application) {
    private val database: LibraryDB = LibraryDB.getDatabase(application.applicationContext)

    fun addBook(book: BookData) {
        viewModelScope.launch(Dispatchers.IO) {
            database.libraryDao().insertBook(book)
        }
    }

    fun addMember(member: MemberData) {
        viewModelScope.launch(Dispatchers.IO) {
            database.libraryDao().insertMember(member)
        }
    }

    fun updateBook(book: BookData) {
        viewModelScope.launch(Dispatchers.IO) {
            database.libraryDao().updateBook(book)
        }
    }

    fun updateMember(member: MemberData) {
        viewModelScope.launch(Dispatchers.IO) {
            database.libraryDao().updateMember(member)
        }
    }

    fun deleteBook(book: BookData) {
        viewModelScope.launch(Dispatchers.IO) {
            database.libraryDao().deleteBook(book)
        }
    }

    fun deleteMember(member: MemberData) {
        viewModelScope.launch(Dispatchers.IO) {
            database.libraryDao().deleteMember(member)
        }
    }

    fun listAllBooks(): LiveData<List<BookData>> {
        return liveData(Dispatchers.IO) {
            val books = database.libraryDao().getAllBooks()
            emit(books)
        }
    }

    fun findBookById(bookId: Int): LiveData<BookData> {
        return liveData(Dispatchers.IO) {
            val book = database.libraryDao().getBookById(bookId)
            emit(book)
        }
    }

    fun findBookByTitle(title: String): LiveData<List<BookData>> {
        return liveData(Dispatchers.IO) {
            val books = database.libraryDao().getBookByTitle(title)
            emit(books)
        }
    }

    fun findBookByAuthor(author: String): LiveData<List<BookData>>  {
        return liveData(Dispatchers.IO) {
            val books = database.libraryDao().getBookByAuthor(author)
            emit(books)
        }
    }

    fun findBookByGenre(genre: String): LiveData<List<BookData>>  {
        return liveData(Dispatchers.IO) {
            val books = database.libraryDao().getBookByGenre(genre)
            emit(books)
        }
    }

    fun listAllMembers(): LiveData<List<MemberData>> {
        return liveData(Dispatchers.IO) {
            val members = database.libraryDao().getAllMembers()
            emit(members)
        }
    }

    fun listPremiumMembers(): LiveData<List<MemberData>> {
        return liveData(Dispatchers.IO) {
            val members = database.libraryDao().getPremiumMembers()
            emit(members)
        }
    }

    fun findMemberById(memberId: Int): LiveData<MemberData> {
        return liveData(Dispatchers.IO) {
            val member = database.libraryDao().getMemberById(memberId)
            emit(member)
        }
    }

    fun findMemberByEmail(email: String): LiveData<MemberData> {
        return liveData(Dispatchers.IO) {
            val member = database.libraryDao().getMemberByEmail(email)
            emit(member)
        }
    }

    fun findMemberByName(name: String): LiveData<List<MemberData>> {
        return liveData(Dispatchers.IO) {
            val members = database.libraryDao().getMemberByName(name)
            emit(members)
        }
    }

    fun findPremiumMemberByName(name: String): LiveData<List<MemberData>> {
        return liveData(Dispatchers.IO) {
            val members = database.libraryDao().getPremiumMemberByName(name)
            emit(members)
        }
    }
}