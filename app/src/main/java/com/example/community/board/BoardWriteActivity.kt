package com.example.community.board

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.community.R
import com.example.community.databinding.ActivityBoardWriteBinding
import com.example.community.util.FBAuth
import com.example.community.util.FBRef

class BoardWriteActivity : AppCompatActivity() {

    private lateinit var binding : ActivityBoardWriteBinding

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_board_write)

        binding.saveBtn.setOnClickListener {

            val title = binding.titleArea.text.toString()
            val content = binding.contentArea.text.toString()
            val uid = FBAuth.getUid()
            val time = FBAuth.getTime()
            val key = FBRef.boardRef.push().key.toString()

            FBRef.boardRef
                .child(key)
                .setValue(BoardDataModel(title,content,uid,time))

            Toast.makeText(this, "게시글 입력 완료",Toast.LENGTH_LONG).show()
            finish()
        }

    }
}