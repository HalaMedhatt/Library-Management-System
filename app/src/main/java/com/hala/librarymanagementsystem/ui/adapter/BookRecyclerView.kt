package com.hala.librarymanagementsystem.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.hala.librarymanagementsystem.R
import com.hala.librarymanagementsystem.model.BookData

class BookRecyclerView : RecyclerView.Adapter<BookRecyclerView.BookViewHolder>() {

    private var bookList: List<BookData> = ArrayList()

    fun setBooks(bookList: List<BookData>) {
        this.bookList = bookList
        notifyDataSetChanged()
    }

    inner class BookViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val bookImage: ImageView = itemView.findViewById(R.id.bookImage_iv)
        private val bookId: TextView = itemView.findViewById(R.id.bookId_tv)
        private val bookTitle: TextView = itemView.findViewById(R.id.bookTitle_tv)
        private val bookGenre: TextView = itemView.findViewById(R.id.bookGenre_tv)

        fun bind(book: BookData) {
            bookId.text = "ID: ${book.id}"
            bookTitle.text = book.title
            bookGenre.text = "Genre: ${book.genre}"
            // Load the image using Glide
            Glide.with(itemView.context)
                .load(book.imageUri)
                .placeholder(R.drawable.temp_book) // Placeholder image
                .into(bookImage)

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.book_item, parent, false)
        return BookViewHolder(view)
    }

    override fun getItemCount(): Int {
        return bookList.size
    }

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        val book = bookList[position]
        holder.bind(book)
    }
}
