package com.hala.librarymanagementsystem

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.hala.librarymanagementsystem.databinding.ActivityHomeBinding

class HomeActivity : AppCompatActivity() {
    private lateinit var activityHomeBinding: ActivityHomeBinding
    private lateinit var home: HomeFragment
    private lateinit var books: BooksFragment
    private lateinit var members: MembersFragment
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityHomeBinding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(activityHomeBinding.root)

        home = HomeFragment()
        books = BooksFragment()
        members = MembersFragment()

        activityHomeBinding.bottomNavView.selectedItemId = R.id.home_mi
        setCurrentFragment(home)

        activityHomeBinding.bottomNavView.setOnItemSelectedListener{
            when(it.itemId) {
                R.id.home_mi -> setCurrentFragment(home)
                R.id.book_mi -> setCurrentFragment(books)
                R.id.member_mi -> setCurrentFragment(members)
            }
            true
        }
    }

    private fun setCurrentFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragment_container, fragment)
            commit()
        }
    }
}