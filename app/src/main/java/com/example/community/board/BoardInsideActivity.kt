package com.example.community.board

import android.content.Context
import android.content.Intent
import android.graphics.Rect
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.PopupMenu
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import com.example.community.R
import com.example.community.comment.CommentDataModel
import com.example.community.comment.CommentLVAdapter
import com.example.community.databinding.ActivityBoardInsideBinding
import com.example.community.util.FBAuth
import com.example.community.util.FBRef
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class BoardInsideActivity : AppCompatActivity() {

    private lateinit var binding : ActivityBoardInsideBinding

    private lateinit var key : String

    private val commentDataList = mutableListOf<CommentDataModel>()

    private lateinit var commentLVAdapter: CommentLVAdapter

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_board_inside)

        key = intent.getStringExtra("key").toString()
        getBoardData(key)

        binding.commentSaveBtn.setOnClickListener {
            insertComment(key)
            getCommentData(key)
        }

        commentLVAdapter = CommentLVAdapter(commentDataList)
        binding.commentLV.adapter = commentLVAdapter


        // 댓글 입력창을 제외한 나머지 레이아웃 클릭시 키패드 내리기
        binding.tochEvent.setOnClickListener {
            CloseKeyboard()
        }

        binding.boardEditBtn.setOnClickListener {
            val popupMenu = PopupMenu(applicationContext,it)

            menuInflater?.inflate(R.menu.boardoption,popupMenu.menu)
            popupMenu.show()
            popupMenu.setOnMenuItemClickListener {
                when(it.itemId){
                    R.id.deleteBtn -> {
                        FBRef.boardRef.child(key).removeValue()
                        FBRef.commentRef.child(key).removeValue()
                        return@setOnMenuItemClickListener true
                    }
                    R.id.editBtn -> {
                        val intent = Intent(this, BoardEditActivity::class.java)
                        intent.putExtra("key",key)
                        startActivity(intent)
                        return@setOnMenuItemClickListener true
                    } else ->{
                        return@setOnMenuItemClickListener false
                    }
                }
            }
        }

        getCommentData(key)

    }

//    /// 다른화면 클릭 시 키보드 내리기
//    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
//        val imm: InputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
//        imm.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
//
//        if(currentFocus is EditText) {
//            currentFocus!!.clearFocus()
//        }
//
//        return super.dispatchTouchEvent(ev)
//    }

    private fun getBoardData(key : String){
        val postListener = object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val dataModel = snapshot.getValue(BoardDataModel::class.java)
                binding.titleArea.text = dataModel!!.title
                binding.contentArea.text = dataModel!!.content
                binding.timeArea.text = dataModel!!.time

                val myUid = FBAuth.getUid()
                val boardUid = dataModel.uid



                if(myUid.equals(boardUid)){
                    binding.boardEditBtn.isVisible = true
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }

        }
        FBRef.boardRef.child(key).addValueEventListener(postListener)
    }

    private fun insertComment(key : String){
        val comment = binding.commentArea.text.toString()
        if(comment.isNotEmpty()){
            FBRef.commentRef
                .child(key)
                .push()
                .setValue(
                    CommentDataModel(binding.commentArea.text.toString(),FBAuth.getTime(),FBAuth.getUid())
                )
            binding.commentArea.text.clear()
            CloseKeyboard()
        }


    }

    private fun getCommentData(key : String){
        val postListener = object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                commentDataList.clear()
                for(dataModel in snapshot.children){
                    val item = dataModel.getValue(CommentDataModel::class.java)
                    commentDataList.add(item!!)
                }

                commentLVAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {

            }

        }
        FBRef.commentRef.child(key).addValueEventListener(postListener)
    }

    fun CloseKeyboard(){
        val view = this.currentFocus
        if(view != null){
            val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(view.windowToken,0)
        }
    }

}