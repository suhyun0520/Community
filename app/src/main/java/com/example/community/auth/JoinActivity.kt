package com.example.community.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.community.MainActivity
import com.example.community.R
import com.example.community.databinding.ActivityJoinBinding
import com.example.community.util.FBAuth
import com.example.community.util.FBRef
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class JoinActivity : AppCompatActivity() {

    private lateinit var auth : FirebaseAuth

    private lateinit var binding : ActivityJoinBinding

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_join)

        auth = Firebase.auth
        binding = DataBindingUtil.setContentView(this, R.layout.activity_join)

        binding.joinBtn.setOnClickListener {
            var successJoin = true
            val email = binding.emailArea.text.toString()
            val password1 = binding.passwordArea1.text.toString()
            val password2 = binding.passwordArea2.text.toString()
            val name = binding.nameArea.text.toString()
            val date = binding.dateArea.text.toString()

            if(email.isEmpty()){
                Toast.makeText(this, "이메일을 입력해주세요",Toast.LENGTH_LONG).show()
                successJoin = false
            }

            if(password1.isEmpty()){
                Toast.makeText(this, "Password1을 입력해주세요",Toast.LENGTH_LONG).show()
                successJoin = false
            }

            if(password2.isEmpty()){
                Toast.makeText(this, "Password2을 입력해주세요",Toast.LENGTH_LONG).show()
                successJoin = false
            }

            if(name.isEmpty()){
                Toast.makeText(this, "이릅을 입력해주세요",Toast.LENGTH_LONG).show()
                successJoin = false
            }

            if(date.isEmpty()){
                Toast.makeText(this, "생년월일을 입력해주세요",Toast.LENGTH_LONG).show()
                successJoin = false
            }
            Log.d("LoginTest",successJoin.toString())
            Log.d("LoginTest",email)
            Log.d("LoginTest",password1)


            if(successJoin){
                auth.createUserWithEmailAndPassword(email, password1)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(this,"회원가입 되었습니다", Toast.LENGTH_LONG).show()
                            FBRef.usersRef
                                .child(FBAuth.getUid())
                                .setValue(userDataModel(name,date))
                            val intent = Intent(this, MainActivity::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            startActivity(intent)
                        } else {
                            Toast.makeText(this, "회원가입이 실패되었습니다.",Toast.LENGTH_LONG).show()
                        }
                    }
            }

        }
    }
}