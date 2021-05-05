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
import kotlinx.android.synthetic.main.activity_add_new_contact.*
import kotlinx.android.synthetic.main.activity_contacts_details.*

class ContactsDetailsActivity : AppCompatActivity() {

    private lateinit var contacts: NewContactModel

    private val ANSWER_CALLS = 101
    private val READ_CONTACTS = 102
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contacts_details)

        editContact()

        val intent = intent
        contacts = intent.getSerializableExtra("CONTACTS") as NewContactModel
        val name = contacts.newContactName
        val phoneNumber = contacts.newContactPhoneNumber
        val email = contacts.newContactEmail

        Log.d("check Details", "Checking $contacts")
        idTVName.text = name
        idTVPhone.text = phoneNumber

        makeCall()
        deleteContact()
        shareContact()

        //
    }

    private fun deleteContact() {
        delete_icon.setOnClickListener {
            val key = contacts.id
            val postReference = FirebaseDatabase.getInstance().getReference().child("contacts").child(key!!)
            postReference.removeValue()

            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

    private fun shareContact() {
        val name = contacts.newContactName
        val phoneNumber = contacts.newContactPhoneNumber
        share_contact.setOnClickListener {
            val shareIntent = Intent()
            shareIntent.action = Intent.ACTION_SEND
            shareIntent.type = "text/plain"
            shareIntent.putExtra(Intent.EXTRA_TEXT, "$name $phoneNumber")
            startActivity(Intent.createChooser(shareIntent, getString(R.string.send_to)))
        }
    }

    private fun makeCall() {
//        val phoneNumber = contacts.newContactPhoneNumber
        phone_callIv.setOnClickListener {

            checkForPermissions(android.Manifest.permission.CALL_PHONE, "make calls", ANSWER_CALLS)
        }
    }

    private fun callIntent() {
        val phoneNumber = contacts.newContactPhoneNumber
        val callIntent = Intent(Intent.ACTION_CALL)
        callIntent.data = Uri.parse("tel: $phoneNumber")
        startActivity(callIntent)
    }

    private fun checkForPermissions(permission: String, name: String, requestCode: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            when {
                ContextCompat.checkSelfPermission(
                    this, permission
                ) == PackageManager.PERMISSION_GRANTED -> {
                    callIntent()
//                    Toast.makeText(applicationContext, "$name permission granted", Toast.LENGTH_SHORT).show()
                }
                shouldShowRequestPermissionRationale(permission) -> showDialog(
                    permission, name, requestCode
                )

                else -> ActivityCompat.requestPermissions(this, arrayOf(permission), requestCode)
            }
        }
    }

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

    private fun editContact() {

        edit_icon.setOnClickListener {
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
        }
    }
}
