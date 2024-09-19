package com.hala.librarymanagementsystem.ui

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.PopupMenu
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.hala.librarymanagementsystem.R
import com.hala.librarymanagementsystem.databinding.DialogAddBookBinding
import com.hala.librarymanagementsystem.databinding.FragmentBooksBinding
import com.hala.librarymanagementsystem.model.BookData
import com.hala.librarymanagementsystem.model.BookFilters
import com.hala.librarymanagementsystem.model.LibraryViewModel
import com.hala.librarymanagementsystem.ui.adapter.BookRecyclerView
import com.hala.librarymanagementsystem.ui.adapter.OnItemDeleted

class BooksFragment : Fragment() ,OnItemDeleted<BookData>{

    private lateinit var booksBinding: FragmentBooksBinding
    private lateinit var dialogAddBookBinding: DialogAddBookBinding
    private val bookFilters: List<String> = enumValues<BookFilters>().map { it.displayName }
    private var selectedSearchFilter = BookFilters.TITLE
    private var selectedImageUri: Uri? = null
    private var isSearchBarVisible = false
    private val bookRecyclerView: BookRecyclerView by lazy {
        BookRecyclerView(this)
    }
    private val bookViewModel: LibraryViewModel by viewModels()

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
        booksBinding.addNewBookBtn.setOnClickListener {
            showAddBookDialog()
        }
        booksBinding.filterBtn.setOnClickListener {
            showFilterMenu()
        }
        booksBinding.searchBookEt.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE ||
                (event != null && event.keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_DOWN)
            ) {
                hideKeyboard()
                val searchText = v.text.toString()
                searchByFilter(searchText)
                true
            } else false
        }
        booksBinding.searchBookEt.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.isNullOrEmpty()) {
                    bookViewModel.listAllBooks().observe(viewLifecycleOwner) { books ->
                        booksBinding.noResultTv.visibility = View.GONE
                        bookRecyclerView.setBooks(books)
                        booksBinding.booksRv.visibility = View.VISIBLE
                    }
                }
            }
            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun setupRecyclerView() {
        booksBinding.booksRv.layoutManager = GridLayoutManager(context, 2) // 2 columns
        booksBinding.noResultTv.visibility = View.GONE
        booksBinding.booksRv.adapter = bookRecyclerView
        // Initialize with existing list or empty list
        bookViewModel.listAllBooks().observe(viewLifecycleOwner) { books ->
            bookRecyclerView.setBooks(books)
        }
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

            if (title.isNotEmpty() && author.isNotEmpty() && genre.isNotEmpty()) {
                val newBook = BookData(
                    title = title,
                    author = author,
                    genre = genre,
                    imageUri = selectedImageUri?.toString()
                        ?: "android.resource://${requireContext().packageName}/${R.drawable.temp_book}"  // Use default image if none is selected
                )
                bookViewModel.addBook(newBook) { isAdded ->
                    if (isAdded) {
                        bookViewModel.listAllBooks().observe(viewLifecycleOwner) { books ->
                            bookRecyclerView.setBooks(books)
                        }
                        Toast.makeText(context, "Book added successfully", Toast.LENGTH_SHORT).show()
                        selectedImageUri=null
                    } else {
                        Toast.makeText(context, "Failed to add book", Toast.LENGTH_SHORT).show()
                    }
                }
                dialog.dismiss()

            } else {
                // Show error message
                Toast.makeText(requireContext(), "Please fill all fields.", Toast.LENGTH_SHORT)
                    .show()
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
        val imm =
            requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
    }

    private fun hideKeyboard() {
        val imm =
            requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(booksBinding.root.windowToken, 0)
    }

    private fun showFilterMenu() {
        val popupMenu = PopupMenu(context, booksBinding.filterBtn)

        bookFilters.forEachIndexed { index, filter ->
            popupMenu.menu.add(0, index, 0, filter)
        }

        popupMenu.setOnMenuItemClickListener { menuItem: MenuItem ->
            val selectedFilter = bookFilters[menuItem.itemId]
            selectedSearchFilter = BookFilters.entries.find { it.displayName == selectedFilter }!!

            val searchText = booksBinding.searchBookEt.text.toString()
            if(searchText.isNotEmpty()) {
                searchByFilter(searchText)
            }

            true
        }
        popupMenu.show()
    }

    private fun searchByFilter(searchText: String) {
        when (selectedSearchFilter) {
            BookFilters.TITLE -> {
                bookViewModel.findBookByTitle(searchText)
                    .observe(viewLifecycleOwner) { books ->
                        showSearchResult(books)
                    }
            }

            BookFilters.AUTHOR -> {
                bookViewModel.findBookByAuthor(searchText)
                    .observe(viewLifecycleOwner) { books ->
                        showSearchResult(books)
                    }
            }

            BookFilters.GENRE -> {
                bookViewModel.findBookByGenre(searchText)
                    .observe(viewLifecycleOwner) { books ->
                        showSearchResult(books)
                    }
            }
        }
    }

    private fun showSearchResult(books: List<BookData>?) {
        if(books.isNullOrEmpty()) {
            booksBinding.booksRv.visibility = View.GONE
            booksBinding.noResultTv.visibility = View.VISIBLE
        }
        else {
            booksBinding.noResultTv.visibility = View.GONE
            bookRecyclerView.setBooks(books)
            booksBinding.booksRv.visibility = View.VISIBLE
        }
    }

    override fun onItemDeleted(book: BookData) {
        booksBinding.waitBook.visibility = View.VISIBLE
        bookViewModel.deleteBook(book) { isDeleted ->
            if (isDeleted) {
                Toast.makeText(context, "Book deleted successfully", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "Failed to delete book", Toast.LENGTH_SHORT).show()
            }
            booksBinding.waitBook.visibility = View.GONE
        }
    }
}