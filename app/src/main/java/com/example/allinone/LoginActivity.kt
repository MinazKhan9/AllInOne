package com.example.allinone

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.netclanremake.databinding.ActivityLoginBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.database

class LoginActivity : AppCompatActivity() {
    private val binding: ActivityLoginBinding by lazy{
        ActivityLoginBinding.inflate(layoutInflater)
    }
    private var phoneNo: String? = null
    private lateinit var auth:FirebaseAuth
    private lateinit var database:DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        database = Firebase.database.reference

        if (auth.currentUser!=null){
            startActivity(Intent(this, MainActivity::class.java))
        }

        binding.continueButton.setOnClickListener {
            phoneNo = binding.phoneNumber.text.toString().trim()

            if (phoneNo!!.isEmpty()){
                Toast.makeText(this,"Please Enter Your Number!!",Toast.LENGTH_SHORT).show()
            }else{
                val intent = Intent(this, OtpActivity::class.java)
                intent.putExtra("number",binding.phoneNumber.text.toString())
                startActivity(intent)
            }
        }
    }
}