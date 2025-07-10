package com.example.librarymanagementtool

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.Animation
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import android.view.animation.AnimationUtils

class SplashActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        // Initialize Firebase Auth and Database
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().reference


        val logoAnimation=AnimationUtils.loadAnimation(this, R.anim.fade_in)
        // Reference your logo or any view
        val ivLogo: ImageView = findViewById(R.id.SplashLogo)
        ivLogo.startAnimation(logoAnimation)

        logoAnimation.setAnimationListener(object : Animation.AnimationListener{
            override fun onAnimationStart(animation: Animation?) {}
            override fun onAnimationEnd(animation: Animation?) {
                Handler(Looper.getMainLooper()).postDelayed({
                    checkLoginStatus()
                }, 1500)
            }
            override fun onAnimationRepeat(animation: Animation?) {}
        })

    }

    private fun checkLoginStatus() {
        val currentUser = auth.currentUser

        if (currentUser != null) {
            // User is logged in, fetch role and redirect accordingly
            val userId = currentUser.uid
            database.child("users").child(userId).get()
                .addOnSuccessListener { snapshot ->
                    if (snapshot.exists()) {
                        val isAdmin = snapshot.child("isAdmin").getValue(Boolean::class.java) ?: false
                        if (isAdmin) {
                            // Redirect to Admin Dashboard
                            startActivity(Intent(this, AdminDashboardActivity::class.java))
                        } else {
                            // Redirect to User Dashboard
                            startActivity(Intent(this, UserDashboardActivity::class.java))
                        }
                    } else {
                        // If user data is missing, redirect to login
                        Toast.makeText(this, "User data not found", Toast.LENGTH_SHORT).show()
                        redirectToLogin()
                    }
                    finish() // Close SplashActivity
                }
                .addOnFailureListener {
                    // Database fetch failed, redirect to login
                    Toast.makeText(this, "Failed to fetch user data: ${it.message}", Toast.LENGTH_SHORT).show()
                    redirectToLogin()
                }
        } else {
            // No user is logged in, redirect to login
            redirectToLogin()
        }
    }

    private fun redirectToLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }
}
