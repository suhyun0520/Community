package com.example.community.comment

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.example.community.R
import com.example.community.util.FBRef
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue

class CommentLVAdapter(val commentList : MutableList<CommentDataModel>) : BaseAdapter() {
    override fun getCount(): Int {
        return commentList.size
    }

    override fun getItem(position: Int): Any {
        return commentList[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var view = convertView
        var r_name : String

        if(view == null){
            view = LayoutInflater.from(parent?.context).inflate(R.layout.comment_list_item,parent,false)
        }

        val commentUid = view?.findViewById<TextView>(R.id.commentId)
        val comment = view?.findViewById<TextView>(R.id.comment)
        val commentTime = view?.findViewById<TextView>(R.id.commentTime)

        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                r_name  = dataSnapshot.child("name").getValue().toString()

                if (r_name == "null"){
                    commentUid!!.text = "익명"
                } else {
                    commentUid!!.text = dataSnapshot.child("name").getValue().toString()
                }
            }
            override fun onCancelled(databaseError: DatabaseError) {
                Log.w("loadPost:onCancelled", databaseError.toException())
            }
        }
        FBRef.usersRef.child(commentList[position].commentUid).addValueEventListener(postListener)

//        commentUid!!.text = commentList[position].commentUid
        comment!!.text = commentList[position].comment
        commentTime!!.text = commentList[position].commentTime

        return view!!
    }
}