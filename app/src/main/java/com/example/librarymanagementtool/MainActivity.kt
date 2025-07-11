package com.example.librarymanagementtool

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    // Firebase Authentication instance
    private lateinit var auth: FirebaseAuth

    // Declare views
    lateinit var etEmail: EditText
    lateinit var etPassword: EditText
    lateinit var btnRegister: Button
    lateinit var btnLogin: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        // Initialize Firebase
//        FirebaseApp.initializeApp(this)
//        auth = FirebaseAuth.getInstance()
//
//        // Initialize views
//        etEmail = findViewById(R.id.etEmail)
//        etPassword = findViewById(R.id.etPassword)
//        btnRegister = findViewById(R.id.btnRegister)
//        btnLogin = findViewById(R.id.btnLogin)
//
//        // Handle register button click
//        btnRegister.setOnClickListener {
//            val email = etEmail.text.toString().trim()
//            val password = etPassword.text.toString().trim()
//
//            if (email.isNotEmpty() && password.isNotEmpty()) {
//                // Navigate to RegistrationActivity
//                val intent = Intent(this, RegistrationActivity::class.java)
//                intent.putExtra("email", email)
//                intent.putExtra("password", password)
//                startActivity(intent)
//            } else {
//                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
//            }
//        }
//
//        // Handle login button click
//        btnLogin.setOnClickListener {
//            val email = etEmail.text.toString().trim()
//            val password = etPassword.text.toString().trim()
//
//            if (email.isNotEmpty() && password.isNotEmpty()) {
//                // Navigate to LoginActivity
//                val intent = Intent(this, LoginActivity::class.java)
//                intent.putExtra("email", email)
//                intent.putExtra("password", password)
//                startActivity(intent)
//            } else {
//                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
//            }
//        }
    }
}
