package com.hala.librarymanagementsystem

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.hala.librarymanagementsystem.databinding.FragmentMembersBinding

class MembersFragment : Fragment() {

    private lateinit var membersBinding: FragmentMembersBinding
    private lateinit var memberFilters: List<String>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        membersBinding = FragmentMembersBinding.inflate(inflater, container, false)
        return membersBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}