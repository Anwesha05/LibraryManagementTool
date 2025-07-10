package com.example.librarymanagementtool

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class AdminDashboardActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_dashboard)
        title = "Home"

        auth = FirebaseAuth.getInstance()

        val btnMaintenance: Button = findViewById(R.id.btnMaintenance)
        val btnReports: Button = findViewById(R.id.btnReports)
        val btnTransactions: Button = findViewById(R.id.btnTransactions)
        val btnLogout: Button = findViewById(R.id.btnLogout)

        // Navigate to different activities
        btnMaintenance.setOnClickListener {
            startActivity(Intent(this, MaintenanceActivity::class.java))
        }

        btnReports.setOnClickListener {
            startActivity(Intent(this, ReportsActivity::class.java))
        }

        btnTransactions.setOnClickListener {
            startActivity(Intent(this, TransactionsActivity::class.java))
        }

        btnLogout.setOnClickListener {
            FirebaseAuth.getInstance().signOut()

            val sharedPreferences = getSharedPreferences("LibraryAppPrefs", MODE_PRIVATE)
            val editor = sharedPreferences.edit()

            val isRemembered = sharedPreferences.getBoolean("isRemembered", false)
            val savedEmail = sharedPreferences.getString("savedEmail", "")
            val savedPassword = sharedPreferences.getString("savedPassword", "")

            editor.clear()
            if (isRemembered) {
                editor.putBoolean("isRemembered", true)
                editor.putString("savedEmail", savedEmail)
                editor.putString("savedPassword", savedPassword)
            }
            editor.apply()

            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }

        // ✅ Modern back press handling
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // Exit the app
                finishAffinity()
            }
        })
    }
}
