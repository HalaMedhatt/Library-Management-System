package com.hala.librarymanagementsystem

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.navigation.fragment.findNavController
import com.hala.librarymanagementsystem.databinding.FragmentSplashScreenBinding

class SplashScreenFragment : Fragment() {

    private lateinit var splashScreenBinding: FragmentSplashScreenBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        splashScreenBinding = FragmentSplashScreenBinding.inflate(inflater, container, false)
        return splashScreenBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val animation= AnimationUtils.loadAnimation(context,R.anim.slide)
        splashScreenBinding.ivLogo.startAnimation(animation)

        Handler().postDelayed({
            findNavController().navigate(R.id.action_splashScreenFragment_to_loginFragment)
        },3000)
    }
}
