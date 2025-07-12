package com.example.librarymanagementtool

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class LoginActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Firebase init
        auth = FirebaseAuth.getInstance()

        // Views
        val etEmail: EditText = findViewById(R.id.etEmail)
        val etPassword: EditText = findViewById(R.id.etPassword)
        val btnLogin: Button = findViewById(R.id.btnLogin)
        val cbRememberMe: CheckBox = findViewById(R.id.cbRememberMe)
        val etSignIn: TextView = findViewById(R.id.etSignIn)

        // Intent prefills
        val emailFromMain = intent.getStringExtra("email")
        val passwordFromMain = intent.getStringExtra("password")
        if (!emailFromMain.isNullOrEmpty()) etEmail.setText(emailFromMain)
        if (!passwordFromMain.isNullOrEmpty()) etPassword.setText(passwordFromMain)

        // SharedPrefs
        val sharedPreferences = getSharedPreferences("LibraryAppPrefs", MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        // Pre-fill remembered login
        if (sharedPreferences.getBoolean("isRemembered", false)) {
            etEmail.setText(sharedPreferences.getString("savedEmail", ""))
            etPassword.setText(sharedPreferences.getString("savedPassword", ""))
            cbRememberMe.isChecked = true
        }

        // SignIn redirect
        etSignIn.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }

        // Login button logic
        btnLogin.setOnClickListener {
            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString().trim()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                Toast.makeText(this, "Logging in...", Toast.LENGTH_SHORT).show()

                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val db = FirebaseDatabase.getInstance().reference
                            val safeEmail = email.replace(".", ",")

                            // Check admin status
                            db.child("adminEmails").child(safeEmail).get()
                                .addOnSuccessListener { snapshot ->
                                    val isAdmin = snapshot.exists() && snapshot.getValue(Boolean::class.java) == true

                                    // Save login state
                                    editor.putBoolean("isLoggedIn", true)
                                    editor.putString("email", email)
                                    editor.putBoolean("isAdmin", isAdmin)

                                    if (cbRememberMe.isChecked) {
                                        editor.putBoolean("isRemembered", true)
                                        editor.putString("savedEmail", email)
                                        editor.putString("savedPassword", password)
                                    } else {
                                        editor.remove("isRemembered")
                                        editor.remove("savedEmail")
                                        editor.remove("savedPassword")
                                    }
                                    editor.apply()

                                    // Redirect
                                    if (isAdmin) {
                                        startActivity(Intent(this, AdminDashboardActivity::class.java))
                                    } else {
                                        startActivity(Intent(this, UserDashboardActivity::class.java))
                                    }
                                    finish()
                                }
                                .addOnFailureListener {
                                    Toast.makeText(this, "Error checking admin status: ${it.message}", Toast.LENGTH_SHORT).show()
                                }
                        } else {
                            Toast.makeText(this, "Login failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
            } else {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
