package com.hala.librarymanagementsystem

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.hala.librarymanagementsystem.databinding.FragmentBooksBinding

class BooksFragment : Fragment() {

    private lateinit var booksBinding: FragmentBooksBinding
    private lateinit var bookFilters: List<String>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        booksBinding = FragmentBooksBinding.inflate(inflater, container, false)
        return booksBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}