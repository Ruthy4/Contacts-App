package com.decagon.android.sq007

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.decagon.android.sq007.secondimplementation.SecondImplementationActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.*
import com.google.firebase.database.ktx.getValue
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), ContactListRecyclerAdapter.OnItemClickListener {
    companion object {
        private const val CALL_PERMISSION_CODE = 100
    }

    private lateinit var dbref: DatabaseReference
    private lateinit var contactRecyclerView: RecyclerView
    private lateinit var contactArrayList: ArrayList<NewContactModel>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        secondImplementation()
//        contact_message_iv.setOnClickListener{
//            val contactList:MutableList<NewContactModel> = ArrayList()
//            val contacts =contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null)
//
//        while (contacts!!.moveToNext()){
//            val name = contacts.getString(contacts.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))
//            val PhoneNumber = contacts.getString(contacts.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
//
//            val obj = NewContactModel()
//            obj.newContactName = name
//            obj.newContactPhoneNumber = PhoneNumber
//
//            contactList.add(obj)
//        }
//            contactRecyclerView.adapter = ContactListRecyclerAdapter(contactArrayList, this)
//            contacts.close()
//        }

        var addContacts =
            findViewById<FloatingActionButton>(R.id.add_contacts_floating_action_button)
        addContacts.setOnClickListener {
            val intent = Intent(this, AddNewContactActivity::class.java)
            startActivity(intent)
        }

        contactRecyclerView = findViewById(R.id.contact_recycler_view)
        contactRecyclerView.layoutManager = LinearLayoutManager(this)
        contactRecyclerView.setHasFixedSize(true)

        contactArrayList = arrayListOf()
        getUserData()
    }

    private fun getUserData() {
        dbref = FirebaseDatabase.getInstance().getReference("contacts")
        dbref.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
            }
            override fun onDataChange(snapshot: DataSnapshot) {
                contactArrayList.clear()

                Log.d("MainActivity", "this is calling")
                if (snapshot.exists()) {
                    for (userSnapshot in snapshot.children) {
                        val contacts = userSnapshot.getValue<NewContactModel>()!!

//                        Log.d("CHECKERSS", "LLOO $contacts")

                        contactArrayList.add(contacts!!)
//                        Log.d("MainActivity", "this $contacts")
                    }
                    contactRecyclerView.adapter =
                        ContactListRecyclerAdapter(contactArrayList, this@MainActivity)
                }
            }
        })
    }

    override fun onItemClick(position: Int, recyclerViewModelList: ArrayList<NewContactModel>) {

        val intent = Intent(this, ContactsDetailsActivity::class.java).apply {
            putExtra("CONTACTS", recyclerViewModelList[position])
        }
        startActivity(intent)
    }

    fun secondImplementation() {
        load_second_implementation.setOnClickListener() {
            val intent = Intent(this, SecondImplementationActivity::class.java)
            startActivity(intent)
        }
    }
}
