package com.hala.librarymanagementsystem.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.hala.librarymanagementsystem.R
import com.hala.librarymanagementsystem.model.MemberData

class MemberRecyclerView : RecyclerView.Adapter<MemberRecyclerView.memberViewHolder>() {

    private var memberList: List<MemberData> = ArrayList()

    fun addMember(memberList: List<MemberData>) {
        this.memberList = memberList
        notifyDataSetChanged()
    }

    inner class memberViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var memberName: TextView = itemView.findViewById(R.id.showMemberName_tv)
        var memberId: TextView = itemView.findViewById(R.id.showMemberId_tv)
        var premiumIv: ImageView = itemView.findViewById(R.id.premium_iv)
        var memberDetailsLayout: LinearLayout = itemView.findViewById(R.id.memberDetailsLayout)
        var emailTextView: TextView = itemView.findViewById(R.id.memberEmail_tv)
        var borrowBooksTextView: TextView = itemView.findViewById(R.id.memberBorrowBooks_tv)
        var currentBorrowedBooksTextView: TextView = itemView.findViewById(R.id.memberCurrentBorrowedBooks_tv)
        var borrowedBooksListTextView: TextView = itemView.findViewById(R.id.borrowedBooksList_tv)

        fun bind(member: MemberData) {
            memberName.text = "Name: ${member.name}"
            memberId.text = "ID: ${member.id}"

            // Set visibility and image for premium member
            if (member.isPremium) {
                premiumIv.visibility = View.VISIBLE
            } else {
                premiumIv.visibility = View.GONE
            }

            // Set member details in the expandable section
            emailTextView.text = "Email: ${member.email}"
            borrowBooksTextView.text = "Maximum Books: ${member.maxNumberOfBooks}"
            if(member.getCurrentlyBorrowedBooksCount()!=0) {
                currentBorrowedBooksTextView.text = "Currently Borrowed: ${member.getCurrentlyBorrowedBooksCount()}"
                borrowedBooksListTextView.text = "Borrowed Books: ${member.getBorrowedBooksList()}"
            }
            else
            {
                currentBorrowedBooksTextView.text = "No Borrowed Books "
                borrowedBooksListTextView.text=""
            }

            // Handle expand/collapse
            itemView.findViewById<ImageButton>(R.id.moor_data_ib).setOnClickListener {
                if (memberDetailsLayout.visibility == View.VISIBLE) {
                    memberDetailsLayout.visibility = View.GONE
                } else {
                    memberDetailsLayout.visibility = View.VISIBLE
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): memberViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.member_item, parent, false)
        return memberViewHolder(view)
    }

    override fun getItemCount(): Int {
        return memberList.size
    }

    override fun onBindViewHolder(holder: memberViewHolder, position: Int) {
        val member: MemberData = memberList[position]
        holder.bind(member)
    }
}
