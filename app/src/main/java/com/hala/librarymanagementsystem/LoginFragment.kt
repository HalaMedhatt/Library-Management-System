package com.hala.librarymanagementsystem

import android.os.Bundle
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.hala.librarymanagementsystem.databinding.FragmentLoginBinding

class LoginFragment : Fragment() {

    private var email=""
    private var password=""
    private lateinit var loginBinding: FragmentLoginBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        loginBinding = FragmentLoginBinding.inflate(inflater, container, false)
        return loginBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loginBinding.loginButton.setOnClickListener{
            validateData()
            findNavController().navigate(R.id.action_loginFragment_to_homeActivity)
        }
    }

    private fun validateData() {
        email=loginBinding.emailEditText.text.toString().trim()
        password=loginBinding.passwordEditText.text.toString().trim()
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches())
            Toast.makeText(context,"Invalid Email...",Toast.LENGTH_SHORT).show()
        if(password.isEmpty())
            Toast.makeText(context,"Enter the Password...",Toast.LENGTH_SHORT).show()
    }
}