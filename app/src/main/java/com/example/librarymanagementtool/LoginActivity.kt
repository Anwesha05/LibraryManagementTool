package com.example.librarymanagementtool

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class LoginActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Initialize FirebaseAuth
        auth = FirebaseAuth.getInstance()

        // Views
        val etEmail: EditText = findViewById(R.id.etEmail)
        val etPassword: EditText = findViewById(R.id.etPassword)
        val btnLogin: Button = findViewById(R.id.btnLogin)
        val cbRememberMe: CheckBox = findViewById(R.id.cbRememberMe)
        val etSignIn: TextView = findViewById(R.id.etSignIn)

        etSignIn.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }

        // Get email and password from Intent extras (sent from MainActivity or elsewhere)
        val emailFromMain = intent.getStringExtra("email")
        val passwordFromMain = intent.getStringExtra("password")

        // Prefill the email and password fields if they exist
        if (!emailFromMain.isNullOrEmpty()) {
            etEmail.setText(emailFromMain)
        }
        if (!passwordFromMain.isNullOrEmpty()) {
            etPassword.setText(passwordFromMain)
        }

        // Get SharedPreferences instance
        val sharedPreferences = getSharedPreferences("LibraryAppPrefs", MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        // Check if Remember Me was enabled previously
        val isRemembered = sharedPreferences.getBoolean("isRemembered", false)
        if (isRemembered) {
            val savedEmail = sharedPreferences.getString("savedEmail", "")
            val savedPassword = sharedPreferences.getString("savedPassword", "")
            etEmail.setText(savedEmail)
            etPassword.setText(savedPassword)
            cbRememberMe.isChecked = true
        }

        // Login logic
        btnLogin.setOnClickListener {
            val enteredEmail = etEmail.text.toString().trim()
            val enteredPassword = etPassword.text.toString().trim()

            if (enteredEmail.isNotEmpty() && enteredPassword.isNotEmpty()) {
                Toast.makeText(this, "Logging in...", Toast.LENGTH_SHORT).show()

                // Perform Firebase login
                auth.signInWithEmailAndPassword(enteredEmail, enteredPassword)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val userId = auth.currentUser?.uid

                            // Fetch the user's role from Firebase Realtime Database
                            val database = FirebaseDatabase.getInstance().reference
                            database.child("users").child(userId!!)
                                .get()
                                .addOnSuccessListener { snapshot ->
                                    if (snapshot.exists()) {
                                        val isAdmin = snapshot.child("isAdmin").getValue(Boolean::class.java) ?: false

                                        // Save user login state and data
                                        editor.putBoolean("isLoggedIn", true)
                                        editor.putString("email", enteredEmail)
                                        editor.putBoolean("isAdmin", isAdmin)

                                        // If Remember Me is checked, save credentials
                                        if (cbRememberMe.isChecked) {
                                            editor.putBoolean("isRemembered", true)
                                            editor.putString("savedEmail", enteredEmail)
                                            editor.putString("savedPassword", enteredPassword)
                                        } else {
                                            // Clear saved credentials if Remember Me is unchecked
                                            editor.remove("isRemembered")
                                            editor.remove("savedEmail")
                                            editor.remove("savedPassword")
                                        }
                                        editor.apply()

                                        // Redirect based on user role
                                        if (isAdmin) {
                                            startActivity(Intent(this, AdminDashboardActivity::class.java))
                                        } else {
                                            startActivity(Intent(this, UserDashboardActivity::class.java))
                                        }
                                        finish()
                                    } else {
                                        Toast.makeText(this, "User data not found", Toast.LENGTH_SHORT).show()
                                    }
                                }
                                .addOnFailureListener {
                                    Toast.makeText(this, "Failed to retrieve user data", Toast.LENGTH_SHORT).show()
                                }
                        } else {
                            Toast.makeText(this, "Login Failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
            } else {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
