package com.hala.librarymanagementsystem.ui

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.hala.librarymanagementsystem.R
import com.hala.librarymanagementsystem.databinding.DialogAddBookBinding
import com.hala.librarymanagementsystem.databinding.DialogAddMemberBinding
import com.hala.librarymanagementsystem.databinding.FragmentBooksBinding
import com.hala.librarymanagementsystem.model.BookData
import com.hala.librarymanagementsystem.ui.adapter.BookRecyclerView
import com.hala.librarymanagementsystem.ui.adapter.MemberRecyclerView

class BooksFragment : Fragment() {

    private lateinit var booksBinding: FragmentBooksBinding
    private lateinit var dialogAddBookBinding: DialogAddBookBinding
    private lateinit var bookFilters: List<String>
    private val bookList = ArrayList<BookData>()
    private var selectedImageUri: Uri? = null
    private var isSearchBarVisible = false
    private val bookRecyclerView: BookRecyclerView by lazy {
        BookRecyclerView()
    }
    companion object {
        private const val REQUEST_IMAGE_PICK = 1
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        booksBinding = FragmentBooksBinding.inflate(inflater, container, false)
        return booksBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        booksBinding.expandSearchBookIb.setOnClickListener {
            toggleSearchBar()
        }
        booksBinding.addNewBookBtn.setOnClickListener{
            showAddBookDialog()
        }
    }

    private fun setupRecyclerView() {
        booksBinding.booksRv.layoutManager = GridLayoutManager(context, 2) // 2 columns
        booksBinding.booksRv.adapter = bookRecyclerView
        bookRecyclerView.setBooks(bookList) // Initialize with existing list or empty list
    }

    private fun showAddBookDialog() {
        dialogAddBookBinding = DialogAddBookBinding.inflate(layoutInflater)

        val dialog = AlertDialog.Builder(requireContext())
            .setView(dialogAddBookBinding.root)
            .create()

        dialogAddBookBinding.selectImageBtn.setOnClickListener {
            // Launch intent to pick an image from the gallery
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, REQUEST_IMAGE_PICK)
        }

        dialogAddBookBinding.addBookBtn.setOnClickListener {
            val title = dialogAddBookBinding.titleEt.text.toString().trim()
            val author = dialogAddBookBinding.authorEt.text.toString().trim()
            val genre = dialogAddBookBinding.genreEt.text.toString().trim()

            if ( title.isNotEmpty() && author.isNotEmpty() && genre.isNotEmpty()) {
                val newBook = BookData(
                    id = bookList.size + 1,
                    title = title,
                    author = author,
                    genre = genre,
                    imageUri = selectedImageUri?.toString() ?: "android.resource://${requireContext().packageName}/${R.drawable.temp_book}"  // Use default image if none is selected
                )
                bookList.add(newBook)
                bookRecyclerView.setBooks(bookList) // Update the adapter with the new list
                dialog.dismiss()
            } else {
                // Show error message
                Toast.makeText(requireContext(), "Please fill all fields.", Toast.LENGTH_SHORT).show()
            }
        }

        dialogAddBookBinding.cancelBookBtn.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Check if the result is from the image picker activity and if it was successful
        if (requestCode == REQUEST_IMAGE_PICK && resultCode == AppCompatActivity.RESULT_OK) {
            // Retrieve the URI of the selected image from the intent's data
            selectedImageUri = data?.data

            // Set the selected image URI to the ImageView in the dialog to display the image
            dialogAddBookBinding.bookImageIv.setImageURI(selectedImageUri)
        }
    }
    private fun toggleSearchBar() {
        if (isSearchBarVisible) {
            val slideOut = AnimationUtils.loadAnimation(requireContext(), android.R.anim.fade_out)
            booksBinding.searchBookEt.startAnimation(slideOut)
            booksBinding.searchBookEt.visibility = View.GONE
            hideKeyboard()
        } else {
            booksBinding.searchBookEt.visibility = View.VISIBLE
            val slideIn = AnimationUtils.loadAnimation(requireContext(), android.R.anim.fade_in)
            booksBinding.searchBookEt.startAnimation(slideIn)
            booksBinding.searchBookEt.requestFocus()
            showKeyboard(booksBinding.searchBookEt)
        }
        isSearchBarVisible = !isSearchBarVisible
    }
    private fun showKeyboard(view: View) {
        view.requestFocus()
        val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
    }
    private fun hideKeyboard() {
        val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(booksBinding.root.windowToken, 0)
    }

}