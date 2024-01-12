package com.example.firebaseauthentication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {
    private lateinit var logEmailEt:TextInputEditText
    private lateinit var logPasswordEt:TextInputEditText
    private lateinit var logRegText:TextView
    private lateinit var loginBtn: Button
    private lateinit var logEmailCon:TextInputLayout
    private lateinit var logPassCon:TextInputLayout
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        logEmailEt = findViewById(R.id.logEmailEt)
        logPasswordEt = findViewById(R.id.logPassEt)
        logRegText = findViewById(R.id.logRegText)
        loginBtn = findViewById(R.id.loginBtn)
        logEmailCon = findViewById(R.id.logEmailCon)
        logPassCon = findViewById(R.id.logPassCon)


        logRegText.setOnClickListener {
            val intent = Intent(
                this@LoginActivity,MainActivity::class.java
            )
            startActivity(intent)
            finishAffinity()

        }
        auth = FirebaseAuth.getInstance()
        loginBtn.setOnClickListener {
            userLogin()
        }
        //E-mail validation
        emailFocusedListener()
        //Password validation
        passwordFocusedListener()

    }
    private fun userLogin(){
        val email = logEmailEt.text.toString()
        val pass = logPasswordEt.text.toString()
        if (email.isNotEmpty() && pass.isNotEmpty()){
            //Login auth
            auth.signInWithEmailAndPassword(email,pass).addOnCompleteListener {
                if (it.isSuccessful){
                    Toast.makeText(this@LoginActivity, "Login successful.", Toast.LENGTH_SHORT).show()
                    val intent = Intent(
                        this@LoginActivity,HomeActivity::class.java
                    )
                    startActivity(intent)
                    finishAffinity()


                }else{
                    Toast.makeText(this@LoginActivity, "Login failed!", Toast.LENGTH_SHORT).show()
                }
            }
        }
        else{
            val dialog = AlertDialog.Builder(this@LoginActivity)
            dialog.setIcon(android.R.drawable.ic_dialog_alert)
            dialog.setTitle("Login Alert !")
            dialog.setMessage("Please fill out all the information.")
            dialog.setPositiveButton("Okay"){dialog,dialogInterface->
                dialog.dismiss()
            }
            val alertDialog = dialog.create()
            alertDialog.show()
            alertDialog.setCancelable(false)
        }



    }

    override fun onStart() {
        super.onStart()
        if (FirebaseAuth.getInstance().currentUser!=null){
            val intent = Intent(
                this@LoginActivity,HomeActivity::class.java
            )
            startActivity(intent)
            finishAffinity()
        }
    }
    private fun emailFocusedListener(){
        logEmailEt.setOnFocusChangeListener { view, focused ->
            if(!focused){
                logEmailCon.helperText = validEmail()
            }
        }

    }
    private fun validEmail() : String?{
        val email = logEmailEt.text.toString()
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            return "Invalid E-mail address."
        }
        return null
    }
    private fun passwordFocusedListener(){
        logPasswordEt.setOnFocusChangeListener { view, focused ->
            if (!focused){
                logPassCon.helperText = validPassword()
            }
        }
    }
    private fun validPassword() : String?{
        val password = logPasswordEt.text.toString()
        if (password.length<8){
            return "Minimum 8 character password."
        }
        if (!password.matches(".*[A-Z].*".toRegex())){
            return "Must contain 1 upper-case character."
        }
        if (!password.matches(".*[a-z].*".toRegex())){
            return "Must contain 1 lower-case character."
        }
        if (!password.matches(".*[~`!@#$%^&*()_=+{}:;'|/?><].*".toRegex())){
            return "Must contain 1 special character."
        }
        if (!password.matches(".*[0123456789].*".toRegex())){
            return "Must contain 1 number."
        }
        return null
    }
}