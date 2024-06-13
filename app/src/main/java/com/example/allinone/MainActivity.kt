package com.example.allinone

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.bumptech.glide.Glide
import com.example.allinone.Fragment.ChatFragment
import com.example.allinone.Fragment.ConnectionFragment
import com.example.allinone.Fragment.ContactFragment
import com.example.allinone.Fragment.ExploreFragment
import com.example.allinone.Fragment.GroupFragment
import com.example.netclanremake.R
import com.example.netclanremake.databinding.ActivityMainBinding
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private lateinit var fragmentManager: FragmentManager
    private lateinit var binding: ActivityMainBinding
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navView: NavigationView
    private lateinit var img_avatar: ImageView
    private lateinit var waitingDialog: AlertDialog
    private lateinit var storageReference: StorageReference
    private var imageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        navView = binding.navigationDrawer
        init()

        setSupportActionBar(binding.toolbar)

        val toogle = ActionBarDrawerToggle(
            this,
            binding.drawerLayout,
            binding.toolbar,
            R.string.app_name,
            R.string.app_name
        )
        binding.drawerLayout.addDrawerListener(toogle)
        toogle.syncState()

        binding.navigationDrawer.setNavigationItemSelectedListener(this)

        binding.bottomNavigation.background = null
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.exploreFragment -> openFragment(ExploreFragment())
                R.id.connectionFragment -> openFragment(ConnectionFragment())
                R.id.chatFragment -> openFragment(ChatFragment())
                R.id.contactFragment -> openFragment(ContactFragment())
                R.id.groupFragment -> openFragment(GroupFragment())
            }
            true
        }
        fragmentManager = supportFragmentManager
        openFragment(ExploreFragment())
    }

    private fun init() {
        storageReference = FirebaseStorage.getInstance().getReference()

        waitingDialog = AlertDialog.Builder(this)
            .setMessage("Waiting...")
            .setCancelable(false).create()

        val headerView = navView.getHeaderView(0)
        val txt_name = headerView.findViewById<View>(R.id.txt_name) as TextView
        val txt_city_name = headerView.findViewById<View>(R.id.txt_city_name) as TextView
        img_avatar = headerView.findViewById<View>(R.id.img_avatar) as ImageView

        // txt_name.setText(Common.buildWelcomeMessage())
        //txt_city_name.setText(Common.currentUser!!.cityName)

        if (Common.currentUser != null && Common.currentUser!!.avatar != null && !TextUtils.isEmpty(
                Common.currentUser!!.avatar
            )
        ) {
            Glide.with(this)
                .load(Common.currentUser!!.avatar)
                .into(img_avatar)
        }

        img_avatar.setOnClickListener {
            val intent = Intent()
            intent.setType("image/*")
            intent.setAction(Intent.ACTION_GET_CONTENT)
            startActivityForResult(
                Intent.createChooser(intent, "Select Picture"),
                PICK_IMAGE_REQUEST
            )
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK) {
            if (data != null && data.data != null) {
                imageUri = data.data
                img_avatar.setImageURI(imageUri)

                showDialogUpload()
            }
        }

    }

    private fun showDialogUpload() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Change Avatar")
            .setMessage("Do you really want to change Avatar?")
            .setNegativeButton("CANCEL") { dialogInterface, _ -> dialogInterface.dismiss() }
            .setPositiveButton("UPLOAD") { dialogInterface, _ ->
                if (imageUri != null) {
                    waitingDialog.show()
                    val avatarFolder =
                        storageReference.child("avatars/" + FirebaseAuth.getInstance().currentUser!!.uid)
                    avatarFolder.putFile(imageUri!!)
                        .addOnFailureListener { e ->
                            Snackbar.make(drawerLayout, e.message!!, Snackbar.LENGTH_LONG)
                                .show()
                            waitingDialog.dismiss()
                        }
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                avatarFolder.downloadUrl.addOnSuccessListener { uri ->
                                    val update_data = HashMap<String, Any>()
                                    update_data.put("avatar", uri.toString())

                                    Common.updateUser(drawerLayout, update_data)
                                }
                            }
                            waitingDialog.dismiss()
                        }
                        .addOnProgressListener { taskSnapshot ->
                            val progress =
                                (100.0 * taskSnapshot.bytesTransferred / taskSnapshot.totalByteCount)
                            waitingDialog.setMessage(
                                StringBuilder("Uploading").append(progress).append("%")
                            )

                        }

                }
            }.setCancelable(false)
        val dialog = builder.create()
        dialog.setOnShowListener {
            dialog.getButton(AlertDialog.BUTTON_POSITIVE)
                .setTextColor(resources.getColor(android.R.color.holo_red_dark))
            dialog.getButton(AlertDialog.BUTTON_NEGATIVE)
                .setTextColor(resources.getColor(android.R.color.black))
        }
        dialog.show()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_profile -> {
                val intent = Intent(this, ProfileActivity::class.java)
                startActivity(intent)
                finish()
            }

            R.id.nav_network -> {
                openFragment(ExploreFragment())
            }

            R.id.nav_services -> {
                val builder = AlertDialog.Builder(this)
                builder.setMessage("Dear User,\nYou are not signed up as a Services on Allinone.Would you like to sign-up as a Services user?")
                    .setNegativeButton(
                        "CANCEL"
                    ) { dialogInterface, _ -> dialogInterface.dismiss() }
                    .setPositiveButton("Switch") { dialogInterface, _ ->
                        FirebaseAuth.getInstance().signOut()
                        val intent =
                            Intent(this, LoginActivity::class.java)
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                        startActivity(intent)
                        finish()
                    }.setCancelable(false)

                val dialog = builder.create()
                dialog.setOnShowListener {
                    dialog.getButton(AlertDialog.BUTTON_POSITIVE)
                        .setTextColor(resources.getColor(android.R.color.holo_green_dark))
                    dialog.getButton(AlertDialog.BUTTON_NEGATIVE)
                        .setTextColor(resources.getColor(android.R.color.black))
                }
                dialog.show()
            }

            R.id.nav_business -> {
                val builder = AlertDialog.Builder(this)
                builder.setMessage("Dear User,\nYou are not signed up as a Businesses on Allinone.Would you like to sign-up as a Businesses user?")
                    .setNegativeButton(
                        "CANCEL"
                    ) { dialogInterface, _ -> dialogInterface.dismiss() }
                    .setPositiveButton("Switch") { dialogInterface, _ ->
                        FirebaseAuth.getInstance().signOut()
                        val intent =
                            Intent(this, LoginActivity::class.java)
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                        startActivity(intent)
                        finish()
                    }.setCancelable(false)

                val dialog = builder.create()
                dialog.setOnShowListener {
                    dialog.getButton(AlertDialog.BUTTON_POSITIVE)
                        .setTextColor(resources.getColor(android.R.color.holo_green_dark))
                    dialog.getButton(AlertDialog.BUTTON_NEGATIVE)
                        .setTextColor(resources.getColor(android.R.color.black))
                }
                dialog.show()
            }

            R.id.nav_notes -> {
                val intent = Intent(this, NotesActivity::class.java)
                startActivity(intent)
                finish()
            }

            R.id.nav_sign_out -> {
                val builder = AlertDialog.Builder(this)
                builder.setTitle("Sign out")
                    .setMessage("Do you really want to sign out?")
                    .setNegativeButton(
                        "CANCEL"
                    ) { dialogInterface, _ -> dialogInterface.dismiss() }
                    .setPositiveButton("SIGN OUT") { dialogInterface, _ ->
                        FirebaseAuth.getInstance().signOut()
                        val intent =
                            Intent(this, LoginActivity::class.java)
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                        startActivity(intent)
                        finish()
                    }.setCancelable(false)

                val dialog = builder.create()
                dialog.setOnShowListener {
                    dialog.getButton(AlertDialog.BUTTON_POSITIVE)
                        .setTextColor(resources.getColor(android.R.color.holo_red_dark))
                    dialog.getButton(AlertDialog.BUTTON_NEGATIVE)
                        .setTextColor(resources.getColor(android.R.color.black))
                }
                dialog.show()
            }
        }
        binding.drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }


    override fun onBackPressed() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    private fun openFragment(fragment: Fragment) {
        val fragmentTransaction: FragmentTransaction =
            fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragment_container, fragment)
        fragmentTransaction.commit()
    }

    companion object {
        val PICK_IMAGE_REQUEST = 7272
    }
}
