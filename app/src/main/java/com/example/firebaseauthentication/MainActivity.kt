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

class MainActivity : AppCompatActivity() {
    private lateinit var email:TextInputEditText
    private lateinit var password:TextInputEditText
    private lateinit var confirmPassword:TextInputEditText
    private lateinit var registerBtn:Button
    private lateinit var loginText:TextView
    private lateinit var emailContainer:TextInputLayout
    private lateinit var passwordContainer:TextInputLayout
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        email = findViewById(R.id.emailEt)
        password = findViewById(R.id.passEt)
        confirmPassword = findViewById(R.id.conPassEt)
        registerBtn = findViewById(R.id.registerBtn)
        loginText = findViewById(R.id.loginText)
        emailContainer = findViewById(R.id.emailCon)
        passwordContainer = findViewById(R.id.passCon)

        auth = FirebaseAuth.getInstance()
        registerBtn.setOnClickListener {
            authentication()
        }
        loginText.setOnClickListener {
            val intent = Intent(
                this@MainActivity,LoginActivity::class.java
            )
            startActivity(intent)
            finishAffinity()
        }
        //e-mail validation
        emailFocusedListener()
        //password validation
        passwordFocusedListener()

    }
    private fun authentication(){
        val email = email.text.toString()
        val password = password.text.toString()
        val confirmPassword = confirmPassword.text.toString()

        if (email.isNotEmpty() && password.isNotEmpty() && confirmPassword.isNotEmpty()){
            //Registration auth
            auth.createUserWithEmailAndPassword(email,password).addOnSuccessListener {
                Toast.makeText(this@MainActivity, "Registration successful.", Toast.LENGTH_SHORT).show()
                val intent = Intent(
                    this@MainActivity,LoginActivity::class.java
                )
                startActivity(intent)
                finishAffinity()
            }
                .addOnFailureListener {
                    Toast.makeText(this@MainActivity, "Registration failed!", Toast.LENGTH_SHORT).show()
                }
        }
        else if(password!=confirmPassword){
            Toast.makeText(this@MainActivity, "Wrong password!", Toast.LENGTH_SHORT).show()
        }

        else{
            //Alert dialog create
            val dialog = AlertDialog.Builder(this@MainActivity)
            dialog.setIcon(android.R.drawable.ic_dialog_alert)
            dialog.setTitle("Registration Alert !")
            dialog.setMessage("Please fill out all the information.")
            dialog.setPositiveButton("Okay"){
              dialogInterface,dialog->
                dialogInterface.dismiss()
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
                this@MainActivity,HomeActivity::class.java
            )
            startActivity(intent)
        }
    }
    private fun emailFocusedListener(){
        email.setOnFocusChangeListener { view, focused ->
            if (!focused){
                emailContainer.helperText = validEmail()
            }
        }
    }
    private fun validEmail() : String?{
        val emailText = email.text.toString()
        if (!Patterns.EMAIL_ADDRESS.matcher(emailText).matches()){
            return "Invalid E-mail address"
        }
        return null
    }
    private fun passwordFocusedListener(){
        password.setOnFocusChangeListener { view, focused ->
            if (!focused){
                passwordContainer.helperText = validPassword()
            }
        }
    }
    private fun validPassword():String?{
        val passwordText = password.text.toString()
        if (passwordText.length<8){
            return "Minimum 8 character password."
        }
        else if(!passwordText.matches(".*[A-Z].*".toRegex())){
            return "Must contain 1 upper-case character."
        }
        else if(!passwordText.matches(".*[a-z].*".toRegex())){
            return "Must contain 1 lower-case character."
        }
        else if(!passwordText.matches(".*[!@#$%^&*()~`/?,<>;:|=+_{}].*".toRegex())){
            return "Must contain 1 special character."
        }
        else if(!passwordText.matches(".*[0123456789].*".toRegex())){
            return "Must contain 1 number."
        }
        return null
    }
}