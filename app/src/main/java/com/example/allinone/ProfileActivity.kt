package com.example.allinone

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.allinone.Model.ProfileInfo
import com.example.netclanremake.R
import com.example.netclanremake.databinding.ActivityProfileBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class ProfileActivity : AppCompatActivity() {
    private val binding: ActivityProfileBinding by lazy {
        ActivityProfileBinding.inflate(layoutInflater)
    }
    private lateinit var firstname: String
    private lateinit var lastname: String
    private lateinit var profession: String
    private lateinit var cityname: String
    private lateinit var dob: String
    private var gender: String = "female"
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        auth = Firebase.auth
        database = Firebase.database.reference

        saveUserData()

        val textView: TextView = findViewById(R.id.dob)
        textView.text = SimpleDateFormat("dd-MM-yyyy").format(System.currentTimeMillis())
        val c = Calendar.getInstance()
        val dateSetListener =
            DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                c.set(Calendar.YEAR, year)
                c.set(Calendar.MONTH, monthOfYear)
                c.set(Calendar.DAY_OF_MONTH, dayOfMonth)

                val myFormat = "dd-MM-yyyy" // mention the format you need
                val sdf = SimpleDateFormat(myFormat, Locale.US)
                textView.text = sdf.format(c.time)

            }
        textView.setOnClickListener {
            DatePickerDialog(
                this, dateSetListener,
                c.get(Calendar.YEAR),
                c.get(Calendar.MONTH),
                c.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        binding.male.setOnClickListener {
            gender = "male"
            binding.female.isChecked = false
            binding.others.isChecked = false
        }
        binding.female.setOnClickListener {
            gender = "female"
            binding.male.isChecked = false
            binding.others.isChecked = false
        }
        binding.others.setOnClickListener {
            gender = "others"
            binding.male.isChecked = false
            binding.female.isChecked = false
        }

        binding.apply {
            firstName.isEnabled = false
            lastName.isEnabled = false
            gender.isEnabled = false
            dob.isEnabled = false
            profession.isEnabled = false
            cityName.isEnabled = false

            binding.edit.setOnClickListener {
                firstName.isEnabled = !firstName.isEnabled
                lastName.isEnabled = !lastName.isEnabled
                gender.isEnabled = !gender.isEnabled
                dob.isEnabled = !dob.isEnabled
                profession.isEnabled = !profession.isEnabled
                cityName.isEnabled = !cityName.isEnabled
            }
        }

        binding.button.setOnClickListener {
            firstname = binding.firstName.text.toString()
            lastname = binding.lastName.text.toString().trim()
            profession = binding.profession.text.toString().trim()
            cityname = binding.cityName.text.toString().trim()
            dob = binding.dob.text.toString().trim()

            updateUserData(firstname, lastname, gender, dob, profession, cityname)
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
        binding.backButton.setOnClickListener {
            val intent= Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

    }

    private fun updateUserData(
        firstname: String,
        lastname: String,
        gender: String,
        dob: String,
        profession: String,
        cityname: String
    ) {
        val userId = auth.currentUser?.uid
        if (userId != null) {
            val userReference = database.child("profile").child(userId)
            val userData = hashMapOf(
                "firstName" to firstname,
                "lastName" to lastname,
                "gender" to gender,
                "dob" to dob,
                "profession" to profession,
                "cityName" to cityname
            )
            userReference.setValue(userData).addOnSuccessListener {
                Toast.makeText(this, "Profile Updated Successfully", Toast.LENGTH_SHORT)
                    .show()
                finish()
            }
                .addOnFailureListener {
                    Toast.makeText(this, "Profile Update Failed", Toast.LENGTH_SHORT)
                        .show()
                }
        }

    }

    private fun saveUserData() {
        val userId = auth.currentUser?.uid
        if (userId != null) {
            val userReference = database.child("profile").child(userId)

            userReference.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        val userProfile = snapshot.getValue(ProfileInfo::class.java)
                        if (userProfile != null) {
                            binding.firstName.setText(userProfile.firstName)
                            binding.lastName.setText(userProfile.lastName)
                            binding.dob.setText(userProfile.dob)
                            binding.profession.setText(userProfile.profession)
                            binding.cityName.setText(userProfile.cityName)
                        }
                    }
                }
                override fun onCancelled(error: DatabaseError) {

                }
            })
        }
    }


}