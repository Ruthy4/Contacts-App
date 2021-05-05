package com.decagon.android.sq007

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.decagon.android.sq007.Validator.emailValidate
import com.decagon.android.sq007.Validator.mobileValidate
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_add_new_contact.*
import kotlinx.android.synthetic.main.activity_contacts_details.*

class AddNewContactActivity : AppCompatActivity() {
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_new_contact)

        validateEmail()
        validatePhoneNumber()

        Log.d("Check intent", "E enter o!")
        database = FirebaseDatabase.getInstance().reference

        val key = intent.getStringExtra("KEY")

        Log.d("Check", "The key is $key")
        if (key == null) { saveContact() } else { saveEditedContact() }
    }

    private fun saveContact() {
        save_contact_button.setOnClickListener {
            val contactName = idEdtName.text.toString()
            val contactPhoneNumber = idEdtPhoneNumber.text.toString()
            val contactEmail = idEdtEmail.text.toString()
            val contact = NewContactModel(newContactName = contactName, newContactEmail = contactEmail, newContactPhoneNumber = contactPhoneNumber)

            contact.id = database.child("contacts").push().key
            database.child("contacts").child(contact.id!!).setValue(contact)

            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            onBackPressed()
        }
    }

    private fun saveEditedContact() {

        Log.d("Check", "Save Edited Contact is calling")
        val contactName = intent.getStringExtra("ContactName")
        val contactPhoneNumber = intent.getStringExtra("ContactPhoneNumber")
        val contactEmail = intent.getStringExtra("ContactsEmail")
        val id = intent.getStringExtra("ID")

        idEdtName.setText(contactName)
        idEdtPhoneNumber.setText(contactPhoneNumber)
        idEdtEmail.setText(contactEmail)

        save_contact_button.setOnClickListener {
            val editedName = idEdtName.text.toString().trim()
            val editedPhoneNumber = idEdtPhoneNumber.text.toString().trim()
            val editedEmail = idEdtEmail.text.toString().trim()

            if (editedName.isEmpty() && editedPhoneNumber.isEmpty() && editedEmail.isEmpty()) {
                Toast.makeText(applicationContext, "please fill all fields correctly", Toast.LENGTH_SHORT).show()
            }

            if (editedName.isNotEmpty() && editedPhoneNumber.isNotEmpty() && editedEmail.isNotEmpty()) {

                database.child("contacts").child(id!!).setValue(
                    NewContactModel(
                        id = id,
                        newContactName = editedName,
                        newContactPhoneNumber = editedPhoneNumber,
                        newContactEmail = editedEmail
                    )
                )

                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
        }
    }

    private fun validateEmail() {

        // adds a listener to the email and validates on text changed
        idEdtEmail.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (emailValidate(idEdtEmail.text.toString())) {
                    save_contact_button.isEnabled = true
                } else {
                    idEdtEmail.error = "Enter a valid Email"
                    save_contact_button.isEnabled = false
                }
            }
        })
    }

    private fun validatePhoneNumber() {
        // add a listener to the phoneNumber edit text to perform validation on text changed
        idEdtPhoneNumber.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (mobileValidate(idEdtPhoneNumber.text.toString())) {
                    save_contact_button.isEnabled = true
                } else {
                    idEdtPhoneNumber.error = "Enter a valid phone number"
                    save_contact_button.isEnabled = false
                }
            }
        })
    }
}
