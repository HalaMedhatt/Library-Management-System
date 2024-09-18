package com.hala.librarymanagementsystem.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import com.hala.librarymanagementsystem.databinding.FragmentHomeBinding
import com.hala.librarymanagementsystem.model.LibraryViewModel

class HomeFragment : Fragment() {

    private lateinit var homeBinding: FragmentHomeBinding
    private val viewModel: LibraryViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        homeBinding = FragmentHomeBinding.inflate(inflater, container, false)
        return homeBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        homeBinding.borrowButton.isEnabled = false
        homeBinding.returnButton.isEnabled = false
        homeBinding.memberIdInput.addTextChangedListener {
            val ok =  homeBinding.memberIdInput.text.toString().isNotEmpty() && homeBinding.bookIdInput.text.toString().isNotEmpty()
            homeBinding.borrowButton.isEnabled = ok
            homeBinding.returnButton.isEnabled = ok

        }
        homeBinding.bookIdInput.addTextChangedListener {
            val ok =  homeBinding.memberIdInput.text.toString().isNotEmpty() && homeBinding.bookIdInput.text.toString().isNotEmpty()
            homeBinding.borrowButton.isEnabled = ok
            homeBinding.returnButton.isEnabled = ok
        }
        homeBinding.borrowButton.setOnClickListener {
            homeBinding.waitHome.visibility = View.VISIBLE
            val memberId = homeBinding.memberIdInput.text.toString().toInt()
            val bookId = homeBinding.bookIdInput.text.toString().toInt()
            borrowBook(memberId, bookId)
            homeBinding.bookIdInput.text.clear()
            homeBinding.memberIdInput.text.clear()
        }
        homeBinding.returnButton.setOnClickListener {
            val memberId = homeBinding.memberIdInput.text.toString().toInt()
            val bookId = homeBinding.bookIdInput.text.toString().toInt()
            returnBook(memberId, bookId)
            homeBinding.bookIdInput.text.clear()
            homeBinding.memberIdInput.text.clear()
        }

    }
    private fun borrowBook(memberId: Int, bookId: Int) {
        viewModel.findMemberById(memberId).observe(viewLifecycleOwner) { member ->
            if(member != null) {
                if(member.borrowedBooks.size == member.maxNumberOfBooks) {
                    Toast.makeText(context, "This member has exceeded allowed number of books to borrow.", Toast.LENGTH_SHORT).show()
                }
                else {
                    viewModel.findBookById(bookId).observe(viewLifecycleOwner) { book ->
                        if (book != null) {
                            if(book.isBorrowed) {
                                Toast.makeText(context, "This book is not available.", Toast.LENGTH_SHORT).show()
                            }
                            else {
                                book.isBorrowed = true
                                member.borrowedBooks.add(bookId)
                                viewModel.updateBook(book) { isBorrowed ->
                                    if(isBorrowed) {
                                        viewModel.updateMember(member) { hasBorrowed ->
                                            if(hasBorrowed) {
                                                Toast.makeText(context, "Book is borrowed successfully.", Toast.LENGTH_SHORT).show()
                                            }
                                            else Toast.makeText(context, "Something went wrong!", Toast.LENGTH_SHORT).show()
                                        }

                                    }
                                    else  Toast.makeText(context, "Something went wrong!", Toast.LENGTH_SHORT).show()
                                }
                            }

                        } else {
                            Toast.makeText(
                                context,
                                "No such book with ID: $bookId",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }
            else {
                Toast.makeText(context, "No such member with ID: $memberId", Toast.LENGTH_SHORT).show()
            }
            homeBinding.waitHome.visibility = View.GONE
        }
    }

    private fun returnBook(memberId: Int, bookId: Int) {
        viewModel.findMemberById(memberId).observe(viewLifecycleOwner) { member ->
            if(member != null) {
                if(!(member.borrowedBooks.contains(bookId))) {
                    Toast.makeText(context, "This book isn't borrowed by this member.", Toast.LENGTH_SHORT).show()
                }
                else {
                    viewModel.findBookById(bookId).observe(viewLifecycleOwner) { book ->
                        book.isBorrowed = false
                        member.borrowedBooks.remove(bookId)
                        viewModel.updateBook(book) { isReturned ->
                            if(isReturned) {
                                viewModel.updateMember(member) { hasReturned ->
                                    if(hasReturned) {
                                        Toast.makeText(context, "Book is returned successfully.", Toast.LENGTH_SHORT).show()
                                    }
                                    else Toast.makeText(context, "Something went wrong!", Toast.LENGTH_SHORT).show()
                                }
                            }
                            else {
                                Toast.makeText(context, "Something went wrong!", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }
            }
            else {
                Toast.makeText(context, "No such member with ID: $memberId", Toast.LENGTH_SHORT).show()
            }
            homeBinding.waitHome.visibility = View.GONE
        }
    }
}