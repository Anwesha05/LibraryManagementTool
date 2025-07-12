package com.example.librarymanagementtool

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class RegistrationActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

        // Initialize FirebaseAuth
        auth = FirebaseAuth.getInstance()

        // Views
        val etEmail: EditText = findViewById(R.id.etEmail)
        val etPassword: EditText = findViewById(R.id.etPassword)
        val btnRegister: Button = findViewById(R.id.btnRegister)

        // Get email and password from Intent extras (sent from MainActivity)
        val emailFromMain = intent.getStringExtra("email")
        val passwordFromMain = intent.getStringExtra("password")

        // Prefill the email and password fields if data is available
        if (!emailFromMain.isNullOrEmpty()) {
            etEmail.setText(emailFromMain)
        }
        if (!passwordFromMain.isNullOrEmpty()) {
            etPassword.setText(passwordFromMain)
        }

        // Registration logic
        btnRegister.setOnClickListener {
            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString().trim()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val userId = auth.currentUser?.uid
                            val database = FirebaseDatabase.getInstance().reference

                            // Save user data to Firebase Realtime Database
                            val isAdmin = email == "admin@library.com" // Check if the user is admin
                            val userData = mapOf(
                                "email" to email,
                                "isAdmin" to isAdmin
                            )

                            // Write to the 'users' node in the database
                            database.child("users").child(userId!!)
                                .setValue(userData)
                                .addOnCompleteListener { dbTask ->
                                    if (dbTask.isSuccessful) {
                                        Toast.makeText(this, "Registration Successful", Toast.LENGTH_SHORT).show()
                                        // Redirect to LoginActivity
                                        startActivity(Intent(this, LoginActivity::class.java))
                                        finish()
                                    } else {
                                        Toast.makeText(this, "Failed to save user data: ${dbTask.exception?.message}", Toast.LENGTH_SHORT).show()
                                    }
                                }
                        } else {
                            Toast.makeText(this, "Registration Failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
            } else {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
