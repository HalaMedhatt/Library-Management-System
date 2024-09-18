package com.hala.librarymanagementsystem.model

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface LibraryDao {

    @Insert
    suspend fun insertBook(book: BookData)

    @Insert
    suspend fun insertMember(member: MemberData)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateBook(book: BookData)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateMember(member: MemberData)

    @Delete
    suspend fun deleteBook(book: BookData): Int

    @Delete
    suspend fun deleteMember(member: MemberData)

    @Query("SELECT * FROM books")
    suspend fun getAllBooks(): List<BookData>

    @Query("SELECT * FROM books WHERE id = :bookId")
    suspend fun getBookById(bookId: Int): BookData

    @Query("SELECT * FROM books WHERE title = :title")
    suspend fun getBookByTitle(title: String): List<BookData>

    @Query("SELECT * FROM books WHERE author = :author")
    suspend fun getBookByAuthor(author: String): List<BookData>

    @Query("SELECT * FROM books WHERE genre = :genre")
    suspend fun getBookByGenre(genre: String): List<BookData>

    @Query("SELECT * FROM members")
    suspend fun getAllMembers(): List<MemberData>

    @Query("SELECT * FROM members WHERE isPremium = 1")
    suspend fun getPremiumMembers(): List<MemberData>

    @Query("SELECT * FROM members WHERE id = :memberId")
    suspend fun getMemberById(memberId: Int): MemberData

    @Query("SELECT * FROM members WHERE email = :email")
    suspend fun getMemberByEmail(email: String): MemberData

    @Query("SELECT * FROM members WHERE name = :name")
    suspend fun getMemberByName(name: String): List<MemberData>

    @Query("SELECT * FROM members WHERE isPremium = 1 AND name = :name")
    suspend fun getPremiumMemberByName(name: String): List<MemberData>

}