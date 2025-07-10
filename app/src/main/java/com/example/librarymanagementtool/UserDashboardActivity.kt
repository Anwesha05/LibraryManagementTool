package com.example.librarymanagementtool

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class UserDashboardActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_dashboard)
//
//        // Initialize Firebase Auth
//        auth = FirebaseAuth.getInstance()
//
//        // Buttons
//        val btnViewProfile: Button = findViewById(R.id.btnViewProfile)
//        val btnLogout: Button = findViewById(R.id.btnLogout)
//
//        // Handle button clicks
//        btnViewProfile.setOnClickListener {
//            Toast.makeText(this, "Viewing Profile", Toast.LENGTH_SHORT).show()
//            // Add logic to navigate to profile screen
//        }
//
//        btnLogout.setOnClickListener {
//            auth.signOut()
//            Toast.makeText(this, "Logged out", Toast.LENGTH_SHORT).show()
//            val intent = Intent(this, LoginActivity::class.java)
//            startActivity(intent)
//            finish()
//        }
    }
}
