package com.example.community.board

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.example.community.R
import com.example.community.databinding.ActivityBoardEditBinding
import com.example.community.util.FBAuth
import com.example.community.util.FBRef
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class BoardEditActivity : AppCompatActivity() {

    private lateinit var key : String

    private lateinit var binding : ActivityBoardEditBinding

    private lateinit var editUid : String

    private lateinit var boardTime : String

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_board_edit)
        key = intent.getStringExtra("key").toString()

        getBoardData()

        binding.editBtn.setOnClickListener {
            editBoardData(key)
        }


    }

    private fun editBoardData(key : String){
        val title = binding.titleArea.text.toString()
        val content = binding.contentArea.text.toString()


        FBRef.boardRef
            .child(key)
            .setValue(BoardDataModel(title,content,editUid,boardTime))
        finish()
    }

    private fun getBoardData(){
        val postListener = object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val dataModel = snapshot.getValue(BoardDataModel::class.java)
                binding.titleArea.setText(dataModel?.title)
                binding.contentArea.setText(dataModel?.content)
                boardTime = dataModel!!.time
                editUid = dataModel!!.uid
            }

            override fun onCancelled(error: DatabaseError) {

            }

        }
        FBRef.boardRef.child(key).addValueEventListener(postListener)
    }
}