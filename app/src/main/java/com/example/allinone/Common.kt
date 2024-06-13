package com.example.allinone

import android.view.View
import com.example.allinone.Model.ProfileInfo
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

object Common {
    fun buildWelcomeMessage(): String {
        return StringBuilder("Welcome,")
            .append(currentUser!!.firstName)
            .append(" ")
            .append(currentUser!!.lastName)
            .toString()
    }
    var currentUser: ProfileInfo? = null

    fun updateUser(view: View?, updateData: HashMap<String, Any>) {
        FirebaseDatabase.getInstance()
            .getReference("profile")
            .child(FirebaseAuth.getInstance().currentUser!!.uid)
            .updateChildren(updateData)
            .addOnFailureListener { e ->
                Snackbar.make(view!!,e.message!!, Snackbar.LENGTH_LONG).show()
            }
            .addOnSuccessListener {
                Snackbar.make(view!!,"Updated information successfully", Snackbar.LENGTH_LONG).show()
            }

    }
}