package com.hala.librarymanagementsystem.ui

import android.os.Bundle
import android.text.InputType
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.hala.librarymanagementsystem.R
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
        loginBinding.showPasswordCheckBox.setOnCheckedChangeListener{_, isChecked ->
            if (isChecked) {
                loginBinding.passwordEditText.inputType = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
            } else {
                loginBinding.passwordEditText.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            }
            loginBinding.passwordEditText.setSelection(loginBinding.passwordEditText.text.length)
        }
        loginBinding.loginButton.setOnClickListener{
            if(validateData()) {
                findNavController().navigate(R.id.action_loginFragment_to_homeActivity)
            }
        }
    }
    private fun validateData():Boolean {
        email=loginBinding.emailEditText.text.toString().trim()
        password=loginBinding.passwordEditText.text.toString().trim()
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(context, "Invalid Email...", Toast.LENGTH_SHORT).show()
            return false;
        }
        if(password.isEmpty()) {
            Toast.makeText(context, "Enter the Password...", Toast.LENGTH_SHORT).show()
            return false;
        }
        if(password!="123"){
            Toast.makeText(context, "Enter the Correct Password...", Toast.LENGTH_SHORT).show()
            return false;
        }

        return true;
    }
}