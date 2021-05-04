package com.decagon.android.sq007

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_add_new_contact.*

class AddNewContactActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_new_contact)

        val database = FirebaseDatabase.getInstance().reference

        save_contact_button.setOnClickListener {
            val contactName = idEdtName.text.toString()
            val contactPhoneNumber = idEdtPhoneNumber.text.toString()
            val contactEmail = idEdtEmail.text.toString()

            database.child("contacts").child(contactName).setValue(NewContactModel(contactName, contactPhoneNumber, contactEmail))
        }
    }
}
