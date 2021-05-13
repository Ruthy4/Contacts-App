package com.decagon.android.sq007

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_contacts_details.*

class ContactsDetailsActivity : AppCompatActivity() {

    private lateinit var contacts: NewContactModel

    private val ANSWER_CALLS = 101

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contacts_details)

        val intent = intent
        contacts = intent.getSerializableExtra("CONTACTS") as NewContactModel
        val name = contacts.newContactName
        val phoneNumber = contacts.newContactPhoneNumber

        contact_NameTV.text = name
        contact_Phone_numberTV.text = phoneNumber

        delete_icon.setOnClickListener {
            deleteAlertDialog()
        }

        share_contact.setOnClickListener {
            shareContact()
        }

        phone_callIv.setOnClickListener {
            makeCall()
        }

        edit_icon.setOnClickListener {
            editContact()
        }
    }

    // alert dialog to confirm delete
    private fun deleteAlertDialog() {

        AlertDialog.Builder(this).also {
            it.setTitle("Contact will be deleted")
            it.setPositiveButton("DELETE") { dialog, which ->
                deleteContact()
                Toast.makeText(
                    this,
                    "Contact Deleted",
                    Toast.LENGTH_SHORT
                ).show()
            }
            it.setNegativeButton("CANCEL") { dialog, which ->
                dialog.cancel()
            }
        }.create().show()
    }

    // function to delete contact
    private fun deleteContact() {
        val key = contacts.id
        val postReference = FirebaseDatabase.getInstance().reference.child("contacts").child(key!!)
        postReference.removeValue()

        Toast.makeText(this, "deleting contact", Toast.LENGTH_SHORT).show()

        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    // method to share contacts
    private fun shareContact() {
        val name = contacts.newContactName
        val phoneNumber = contacts.newContactPhoneNumber

        val shareIntent = Intent()
        shareIntent.action = Intent.ACTION_SEND
        shareIntent.type = "text/plain"
        shareIntent.putExtra(Intent.EXTRA_TEXT, "$name $phoneNumber")
        startActivity(Intent.createChooser(shareIntent, getString(R.string.send_to)))
        finish()
    }

    // method to make calls after permission has been accepted
    private fun makeCall() {
        checkForPermissions(android.Manifest.permission.CALL_PHONE, "make calls", ANSWER_CALLS)
    }

    // call intent function
    private fun callIntent() {
        val phoneNumber = contacts.newContactPhoneNumber
        val callIntent = Intent(Intent.ACTION_CALL)
        callIntent.data = Uri.parse("tel: $phoneNumber")
        startActivity(callIntent)
    }

    // method to check for user permission
    private fun checkForPermissions(permission: String, name: String, requestCode: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            when {
                ContextCompat.checkSelfPermission(
                    this, permission
                ) == PackageManager.PERMISSION_GRANTED -> {
                    callIntent()
                }
                shouldShowRequestPermissionRationale(permission) -> showDialog(
                    permission, name, requestCode
                )

                else -> ActivityCompat.requestPermissions(this, arrayOf(permission), requestCode)
            }
        }
    }

    // function to check the result from permission
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        fun innerCheck(name: String) {
            if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(applicationContext, "$name permission refused", Toast.LENGTH_SHORT).show()
            } else {
                callIntent()
                Toast.makeText(applicationContext, "$name permission granted SHOULD CALL", Toast.LENGTH_SHORT).show()
            }
        }
        when (requestCode) {
            ANSWER_CALLS -> innerCheck("calls")
        }
    }

    // build the permission and show the permission dialog
    private fun showDialog(permission: String, name: String, requestCode: Int) {
        val builder = AlertDialog.Builder(this)

        builder.apply {
            setMessage("Permission to access your $name is required to use this app")
            setTitle("Permission required")
            setPositiveButton("OK") { dialog, which ->
                ActivityCompat.requestPermissions(this@ContactsDetailsActivity, arrayOf(permission), requestCode)
            }
        }
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    // function to edit contact
    private fun editContact() {
        val intent = Intent(this, AddNewContactActivity::class.java)
            .apply {
                putExtra("ContactName", contacts.newContactName)
                putExtra("ContactPhoneNumber", contacts.newContactPhoneNumber)
                putExtra("ContactsEmail", contacts.newContactEmail)
                putExtra("KEY", "message")

                putExtra("ID", contacts.id)
            }
        Log.d("Check intent", "$intent")
        startActivity(intent)
        finish()
    }
}
