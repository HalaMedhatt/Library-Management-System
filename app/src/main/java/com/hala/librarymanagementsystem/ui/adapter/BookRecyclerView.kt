package com.hala.librarymanagementsystem.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.hala.librarymanagementsystem.R
import com.hala.librarymanagementsystem.model.BookData

class BookRecyclerView(private val itemDeletedListener: OnItemDeleted<BookData>) : RecyclerView.Adapter<BookRecyclerView.BookViewHolder>() {

    private var bookList: MutableList<BookData> = mutableListOf()

    fun setBooks(book: List<BookData>) {
        this.bookList = book.toMutableList() // Convert List to MutableList
        notifyDataSetChanged()
    }

    inner class BookViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val bookImage: ImageView = itemView.findViewById(R.id.bookImage_iv)
        private val bookId: TextView = itemView.findViewById(R.id.bookId_tv)
        private val bookTitle: TextView = itemView.findViewById(R.id.bookTitle_tv)
        private val bookGenre: TextView = itemView.findViewById(R.id.bookGenre_tv)

        fun bind(book: BookData, position: Int) {
            bookId.text = "ID: ${book.id}"
            bookTitle.text = book.title
            bookGenre.text = "Genre: ${book.genre}"
            // Load the image using Glide
            Glide.with(itemView.context)
                .load(book.imageUri)
                .placeholder(R.drawable.temp_book) // Placeholder image
                .into(bookImage)

            // Set Long Click Listener for deleting the book
            itemView.setOnLongClickListener {
                showDeleteConfirmationDialog(position)
                true
            }
        }

        private fun showDeleteConfirmationDialog(position: Int) {
            val context = itemView.context
            val builder = AlertDialog.Builder(context, R.style.CustomAlertDialogTheme) // Apply custom style
            builder.setTitle("Delete Book")
            builder.setMessage("Are you sure that you want to delete this book?")

            builder.setPositiveButton("Yes") { dialog, _ ->
                deleteBook(position) // Delete the book
                dialog.dismiss()
            }

            builder.setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }

            builder.create().show()
        }

        private fun deleteBook(position: Int) {
           // Log.d("deleteBook","${position}")
            var book =bookList[position]
            if (position >= 0 && position < bookList.size) {
                if(book.isBorrowed) {
                    Toast.makeText(itemView.context, "This book is currently borrowed. Please, return it first.", Toast.LENGTH_SHORT).show()
                }
                else {
                    bookList.removeAt(position)
                    notifyItemRemoved(position)
                    itemDeletedListener.onItemDeleted(book)
                }
            }

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
        holder.bind(book, position)
    }
}