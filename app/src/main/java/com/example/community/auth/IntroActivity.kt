package com.example.community.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.community.MainActivity
import com.example.community.R
import com.example.community.databinding.ActivityIntroBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class IntroActivity : AppCompatActivity() {

    private lateinit var binding : ActivityIntroBinding

    private lateinit var auth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_intro)

        auth = Firebase.auth
        binding = DataBindingUtil.setContentView(this, R.layout.activity_intro)

        binding.joinBtn.setOnClickListener {
            val intent = Intent(this, JoinActivity::class.java)
            startActivity(intent)
        }

        binding.loginBtn.setOnClickListener {
            val intent = Intent(this,LoginActivity::class.java)
            startActivity(intent)
        }

        binding.unknownBtn.setOnClickListener {
            auth.signInAnonymously()
                .addOnCompleteListener(this){task ->
                    if(task.isSuccessful){
                        val intent = Intent(this, MainActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(intent)
                        Toast.makeText(this, "로그인 성공",Toast.LENGTH_LONG).show()
                    }else{
                        Toast.makeText(this, "로그인 실패",Toast.LENGTH_LONG).show()
                    }
                }
        }

    }
}