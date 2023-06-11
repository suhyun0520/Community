package com.example.community

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.community.auth.IntroActivity
import com.example.community.board.BoardDataModel
import com.example.community.board.BoardInsideActivity
import com.example.community.board.BoardRVAdapter
import com.example.community.board.BoardWriteActivity
import com.example.community.util.FBRef
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {



    private lateinit var auth : FirebaseAuth

    private val items = ArrayList<BoardDataModel>()
    private val boardKeyList = ArrayList<String>()


    lateinit var rvAdapter : BoardRVAdapter

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        auth = Firebase.auth
        getBoardData()
        rvAdapter = BoardRVAdapter(items,boardKeyList)
        val rv_board  = findViewById<RecyclerView>(R.id.rv_board)
        rv_board.adapter = rvAdapter
        rv_board.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false)


        var opption = findViewById<ImageView>(R.id.settingBtn)
        var createBtn = findViewById<ImageView>(R.id.createBtn)

        opption.setOnClickListener {
            val popupMenu = PopupMenu(applicationContext,it)

            menuInflater?.inflate(R.menu.popup,popupMenu.menu)
            popupMenu.show()
            popupMenu.setOnMenuItemClickListener {
                when(it.itemId){
                    R.id.logout ->{
                        Toast.makeText(applicationContext, "로그아웃", Toast.LENGTH_LONG).show()
                        auth.signOut()
                        val intent = Intent(this, IntroActivity::class.java)
                        startActivity(intent)
                        return@setOnMenuItemClickListener true
                    }else->{
                        return@setOnMenuItemClickListener false
                    }
                }
            }
        }

        createBtn.setOnClickListener {
            val intent = Intent(this, BoardWriteActivity::class.java)
            startActivity(intent)
            getBoardData()
        }



    }

    private fun getBoardData(){
        val postListener = object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                items.clear()
                for(dataModel in snapshot.children){
                    Log.d("dataModeloo",dataModel.toString())
                    val item = dataModel.getValue(BoardDataModel::class.java)
                    items.add(item!!)
                    boardKeyList.add(dataModel.key.toString())
                }
                Log.d("modelok",items.toString())
                items.reverse()
                boardKeyList.reverse()
                rvAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w("BoardError", "loadPost:onCancelled", error.toException())

            }

        }
        FBRef.boardRef.addValueEventListener(postListener)
    }
}