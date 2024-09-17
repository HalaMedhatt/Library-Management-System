package com.hala.librarymanagementsystem.ui.adapter

import com.hala.librarymanagementsystem.model.BookData

interface OnItemDeleted<T> {
    fun onItemDeleted(item: T)
}