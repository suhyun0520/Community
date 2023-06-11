package com.example.community

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.example.community.auth.IntroActivity
import com.example.community.auth.JoinActivity
import com.example.community.auth.LoginActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class SplashActivity : AppCompatActivity() {

    private lateinit var auth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        auth = Firebase.auth

        if(auth.currentUser?.uid == null){
            Handler().postDelayed({
                startActivity(Intent(this, IntroActivity::class.java))
                finish()
            },3000)
        }else{
            Handler().postDelayed({
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }, 3000)
        }

    }
}