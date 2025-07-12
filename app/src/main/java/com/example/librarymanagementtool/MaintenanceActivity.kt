package com.example.librarymanagementtool

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.widget.Button

class MaintenanceActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maintenance)
        title="Maintenance"
        // Enable the home icon in the action bar
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.outline_home_24)

        // Initialize buttons
        val btnAddMembership: Button = findViewById(R.id.btnAddMembership)
        val btnUpdateMembership: Button = findViewById(R.id.btnUpdateMembership)
        val btnAddBooksMovies: Button = findViewById(R.id.btnAddBooksMovies)
        val btnUpdateBooksMovies: Button = findViewById(R.id.btnUpdateBooksMovies)
        val btnAddUserManagement: Button = findViewById(R.id.btnAddUserManagement)
        val btnUpdateUserManagement: Button = findViewById(R.id.btnUpdateUserManagement)


        // Set click listeners for each button

        // Add Membership
        btnAddMembership.setOnClickListener {
            // Redirect to Add Membership Page
            val intent = Intent(this, AddMembershipActivity::class.java)
            startActivity(intent)
        }

        // Update Membership
        btnUpdateMembership.setOnClickListener {
            // Redirect to Update Membership Page
            val intent = Intent(this, UpdateMembershipActivity::class.java)
            startActivity(intent)
        }

        // Add Books/Movies
        btnAddBooksMovies.setOnClickListener {
            // Redirect to Add Books/Movies Page
            val intent = Intent(this, AddBookActivity::class.java)
            startActivity(intent)
        }

        // Update Books/Movies
        btnUpdateBooksMovies.setOnClickListener {
            // Redirect to Update Books/Movies Page
            val intent = Intent(this, UpdateBookActivity::class.java)
            startActivity(intent)
        }

        // Add User Management
        btnAddUserManagement.setOnClickListener {
            // Redirect to Add User Management Page
            Toast.makeText(this,"Coming soon",Toast.LENGTH_SHORT).show()
        }

        // Update User Management
        btnUpdateUserManagement.setOnClickListener {
            // Redirect to Update User Management Page
            Toast.makeText(this,"Coming soon",Toast.LENGTH_SHORT).show()
        }
    }
    //Home Button
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                // Navigate to AdminDashboardActivity
                val intent = Intent(this, AdminDashboardActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP // Clear the back stack
                startActivity(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}