package com.example.community.board

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.community.R
import com.example.community.util.FBRef
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener



@Suppress("DEPRECATION")
class BoardRVAdapter(val items : ArrayList<BoardDataModel>, val keys: ArrayList<String>) : RecyclerView.Adapter<BoardRVAdapter.ViewHolder>() {



    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view){

        fun bindItems(item : BoardDataModel){
            val title = itemView.findViewById<TextView>(R.id.titleArea)
            val content = itemView.findViewById<TextView>(R.id.contentArea)
            val name = itemView.findViewById<TextView>(R.id.nameArea)
            val time = itemView.findViewById<TextView>(R.id.timeArea)
            var r_name : String

            itemView.setOnClickListener {
                val intent = Intent(itemView.context,BoardInsideActivity::class.java)
                intent.putExtra("key",keys[position])
                ContextCompat.startActivity(itemView.context,intent,null)
                Log.d("intentmessage",keys[position])

            }


            val postListener = object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    r_name  = dataSnapshot.child("name").getValue().toString()

                    if (r_name == "null"){
                        name.text = "익명"

                    } else {
                        name.text = dataSnapshot.child("name").getValue().toString()

                    }
                }
                override fun onCancelled(databaseError: DatabaseError) {
                    Log.w("loadPost:onCancelled", databaseError.toException())
                    name.text = "익명"
                }
            }
            FBRef.usersRef.child(item.uid).addValueEventListener(postListener)
            title.text = item.title
            content.text = item.content
//            name.text = "테스트"
            time.text = item.time


        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.board_list_item,parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
       holder.bindItems(items[position])
    }

    override fun getItemCount(): Int {
        return items.size
    }
}