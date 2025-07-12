package com.example.librarymanagementtool

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import android.text.TextWatcher
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class RegistrationActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

        // Firebase
        auth = FirebaseAuth.getInstance()

        // Views
        val etEmail: EditText  = findViewById(R.id.etEmail)
        val etPassword: EditText = findViewById(R.id.etPassword)
        val btnRegister: Button  = findViewById(R.id.btnRegister)

        // Prefill from MainActivity, if any
        intent.getStringExtra("email")?.let { etEmail.setText(it) }
        intent.getStringExtra("password")?.let { etPassword.setText(it) }

        // Inside onCreate()
        etEmail.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val emailInput = s.toString().lowercase()

                if ("admin" in emailInput) {
                    btnRegister.isEnabled = false
                    etEmail.error = "Emails containing 'admin' are not allowed for registration"
                } else {
                    btnRegister.isEnabled = true
                    etEmail.error = null
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })


        btnRegister.setOnClickListener {
            val email    = etEmail.text.toString().trim()
            val password = etPassword.text.toString().trim()

            // Block emails that contain 'admin' as a substring
            if ("admin" in email.lowercase()) {
                Toast.makeText(this, "Emails containing 'admin' are not allowed", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            // Basic validation
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(this, "Enter a valid email", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (password.length < 6) {
                Toast.makeText(this, "Password must be ≥ 6 characters", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val db        = FirebaseDatabase.getInstance().reference
            val safeEmail = email.replace(".", ",")

            // 1️⃣  Block admin emails from self‑registration
            db.child("adminEmails").child(safeEmail).get()
                .addOnSuccessListener { snap ->
                    if (snap.exists()) {
                        Toast.makeText(
                            this,
                            "Admins cannot register from the app.",
                            Toast.LENGTH_LONG
                        ).show()
                        return@addOnSuccessListener
                    }

                    // 2️⃣  Proceed with user registration
                    auth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener { regTask ->
                            if (!regTask.isSuccessful) {
                                Toast.makeText(
                                    this,
                                    "Registration failed: ${regTask.exception?.message}",
                                    Toast.LENGTH_LONG
                                ).show()
                                return@addOnCompleteListener
                            }

                            val userId   = auth.currentUser!!.uid
                            val userData = mapOf(
                                "email"   to email,
                                "isAdmin" to false
                            )

                            // 3️⃣  Save user profile under /users
                            db.child("users").child(userId).setValue(userData)
                                .addOnCompleteListener { saveTask ->
                                    if (saveTask.isSuccessful) {
                                        Toast.makeText(
                                            this,
                                            "Registration successful!",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        startActivity(Intent(this, LoginActivity::class.java))
                                        finish()
                                    } else {
                                        Toast.makeText(
                                            this,
                                            "Could not save user data: ${saveTask.exception?.message}",
                                            Toast.LENGTH_LONG
                                        ).show()
                                    }
                                }
                        }
                }
                .addOnFailureListener {
                    Toast.makeText(
                        this,
                        "Could not check admin list: ${it.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }
        }
    }
}
