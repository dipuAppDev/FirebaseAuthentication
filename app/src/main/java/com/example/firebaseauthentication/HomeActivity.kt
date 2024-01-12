package com.example.firebaseauthentication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

class HomeActivity : AppCompatActivity() {
    private lateinit var logoutBtn: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)


        logoutBtn = findViewById(R.id.logoutBtn)


        logoutBtn.setOnClickListener {
            Firebase.auth.signOut()
            val intent = Intent(
                this,LoginActivity::class.java
            )
            startActivity(intent)
            finishAffinity()
        }
    }
}