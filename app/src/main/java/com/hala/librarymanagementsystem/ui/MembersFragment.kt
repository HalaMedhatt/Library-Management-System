package com.hala.librarymanagementsystem.ui

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
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
import androidx.fragment.app.viewModels
import com.hala.librarymanagementsystem.R
import com.hala.librarymanagementsystem.databinding.DialogAddMemberBinding
import com.hala.librarymanagementsystem.databinding.FragmentMembersBinding
import com.hala.librarymanagementsystem.model.LibraryViewModel
import com.hala.librarymanagementsystem.model.MemberData
import com.hala.librarymanagementsystem.model.MemberFilters
import com.hala.librarymanagementsystem.ui.adapter.MemberRecyclerView

class MembersFragment : Fragment() {

    private lateinit var membersBinding: FragmentMembersBinding
    private lateinit var dialogBinding: DialogAddMemberBinding
    private val memberFilters: List<String> = enumValues<MemberFilters>().map { it.displayName }
    private var selectedSearchFilter = MemberFilters.ALL
    private var isSearchBarVisible = false
    private val memberRecyclerView:MemberRecyclerView by lazy {
        MemberRecyclerView()
    }
    private val memberViewModel: LibraryViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        membersBinding = FragmentMembersBinding.inflate(inflater, container, false)
        return membersBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpRecyclerView()
        membersBinding.expandSearchIb.setOnClickListener {
            toggleSearchBar()
        }
        membersBinding.addMemberBtn.setOnClickListener{
            showAddMemberDialog()
        }
        membersBinding.filterBtn.setOnClickListener {
            showFilterMenu()
        }
        membersBinding.searchEt.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE ||
                (event != null && event.keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_DOWN)
            ) {
                hideKeyboard()
                val searchText = v.text.toString()
                searchByFilter(searchText)
                true
            } else false
        }
        membersBinding.searchEt.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.isNullOrEmpty()) {
                    searchByFilter("")
                }
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })
    }
    private fun setUpRecyclerView() {
        membersBinding.membersRv.adapter=memberRecyclerView
        memberViewModel.listAllMembers().observe(viewLifecycleOwner) { members ->
            memberRecyclerView.addMember(members)
        }
    }
    @SuppressLint("SuspiciousIndentation")
    private fun showAddMemberDialog() {
        // Inflate the custom dialog layout using ViewBinding
        dialogBinding = DialogAddMemberBinding.inflate(layoutInflater)

        // Set an OnCheckedChangeListener to show/hide borrow books input
        dialogBinding.radioGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.normal_rb -> {
                    dialogBinding.borrowBooksEt.visibility = View.GONE
                    dialogBinding.borrowBooksEt.setText("3") // Default value for normal members
                }
                R.id.premium_rb -> {
                    dialogBinding.borrowBooksEt.visibility = View.VISIBLE
                    dialogBinding.borrowBooksEt.setText("") // Clear previous input
                }
            }
        }

        // Build and show the AlertDialog
        val dialog = AlertDialog.Builder(requireContext())
            .setView(dialogBinding.root)
            .create()

        // Handle the Add button click
        dialogBinding.btnAdd.setOnClickListener {
            // Get data from input fields
            val name = dialogBinding.nameEt.text.toString().trim()
            val email = dialogBinding.emailEt.text.toString().trim()
            val isPremium = dialogBinding.premiumRb.isChecked
            val borrowBooks = if (isPremium) dialogBinding.borrowBooksEt.text.toString().toIntOrNull() ?: 0 else 3

            // Add the member to the list
            if(addMemberToList(email, name, isPremium, borrowBooks))
            dialog.dismiss()

        }

        // Handle the Cancel button click
        dialogBinding.cancelBtn.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }

    private fun addMemberToList(email:String,name: String, isPremium: Boolean, borrowBooks: Int):Boolean {
        // Check if a radio button is selected

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(context, "Please enter a valid email.", Toast.LENGTH_SHORT).show()
            return false
        }
        if (name.isEmpty() ) {
            Toast.makeText(context, "Please enter a valid name.", Toast.LENGTH_SHORT).show()
            return false
        }
        val selectedMemberTypeId = dialogBinding.radioGroup.checkedRadioButtonId
        if (selectedMemberTypeId == -1) {
            // Show error if no radio button is selected
            dialogBinding.radioGroup.requestFocus()
            dialogBinding.radioGroup.clearCheck()
            Toast.makeText(requireContext(), "Please select a member type.", Toast.LENGTH_SHORT).show()
            return false
        }
        if(dialogBinding.premiumRb.isChecked&&dialogBinding.borrowBooksEt.text.isEmpty()){
            Toast.makeText(requireContext(), "Please enter the number of borrow books.", Toast.LENGTH_SHORT).show()
            return false
        }

            // Add the member to the list
            val newMember = MemberData(
                email = email,
                name = name,
                isPremium =  isPremium,
                maxNumberOfBooks =  borrowBooks)
            memberViewModel.addMember(newMember)
            memberViewModel.listAllMembers().observe(viewLifecycleOwner) { members ->
                memberRecyclerView.addMember(members)
            }
            Toast.makeText(context, "Added Member", Toast.LENGTH_SHORT).show()
             // Store Member in the database
        return true

    }
    private fun toggleSearchBar() {
        if (isSearchBarVisible) {
            val slideOut = AnimationUtils.loadAnimation(requireContext(), android.R.anim.fade_out)
            membersBinding.searchEt.startAnimation(slideOut)
            membersBinding.searchEt.visibility = View.GONE
            hideKeyboard()
        } else {
            membersBinding.searchEt.visibility = View.VISIBLE
            val slideIn = AnimationUtils.loadAnimation(requireContext(), android.R.anim.fade_in)
            membersBinding.searchEt.startAnimation(slideIn)
            membersBinding.searchEt.requestFocus()
            showKeyboard(membersBinding.searchEt)
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
        imm.hideSoftInputFromWindow(membersBinding.root.windowToken, 0)
    }

    private fun showFilterMenu() {
        val popupMenu = PopupMenu(context, membersBinding.filterBtn)

        memberFilters.forEachIndexed { index, filter ->
            popupMenu.menu.add(0, index, 0, filter)
        }

        popupMenu.setOnMenuItemClickListener { menuItem: MenuItem ->
            val selectedFilter = memberFilters[menuItem.itemId]
            selectedSearchFilter = MemberFilters.entries.find { it.displayName == selectedFilter }!!

            val searchText = membersBinding.searchEt.text.toString()
            searchByFilter(searchText)

            true
        }

        popupMenu.show()
    }

    private fun searchByFilter(searchText: String) {
        when(selectedSearchFilter) {
            MemberFilters.ALL -> {
                if(searchText.isEmpty()) {
                    memberViewModel.listAllMembers().observe(viewLifecycleOwner) { members ->
                        showSearchResult(members)
                    }
                }
                else {
                    memberViewModel.findMemberByName(searchText)
                        .observe(viewLifecycleOwner) { members ->
                            showSearchResult(members)
                        }
                }
            }
            MemberFilters.PREMIUM -> {
                if(searchText.isEmpty()) {
                    memberViewModel.listPremiumMembers()
                        .observe(viewLifecycleOwner) { members ->
                            showSearchResult(members)
                        }
                }
                else {
                    memberViewModel.findPremiumMemberByName(searchText)
                        .observe(viewLifecycleOwner) { members ->
                            showSearchResult(members)
                        }
                }
            }
        }
    }

    private fun showSearchResult(members: List<MemberData>?) {
        if(members.isNullOrEmpty()) {
            membersBinding.membersRv.visibility = View.GONE
            membersBinding.noResultTv.visibility = View.VISIBLE
        }
        else {
            membersBinding.noResultTv.visibility = View.GONE
            memberRecyclerView.addMember(members)
            membersBinding.membersRv.visibility = View.VISIBLE
        }
    }

}