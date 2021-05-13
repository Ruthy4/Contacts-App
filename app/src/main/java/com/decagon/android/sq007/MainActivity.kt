package com.decagon.android.sq007

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.decagon.android.sq007.secondimplementation.SecondImplementationActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.*
import com.google.firebase.database.ktx.getValue
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), ContactListRecyclerAdapter.OnItemClickListener {

    private lateinit var dbref: DatabaseReference
    private lateinit var contactRecyclerView: RecyclerView
    private lateinit var contactArrayList: ArrayList<NewContactModel>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        secondImplementation()

        // add an onclick listener to the floating action button to open the AddNewContact activity
        val addContacts = findViewById<FloatingActionButton>(R.id.add_contacts_floating_action_button)
        addContacts.setOnClickListener {
            val intent = Intent(this, AddNewContactActivity::class.java)
            startActivity(intent)
        }

        contactRecyclerView = findViewById(R.id.contact_recycler_view)
        contactRecyclerView.layoutManager = LinearLayoutManager(this)
        contactRecyclerView.setHasFixedSize(true)
        // gets the contact array list
        contactArrayList = arrayListOf()
        getUserData()
    }

    // method to get the user data stored in firebase
    private fun getUserData() {
        dbref = FirebaseDatabase.getInstance().getReference("contacts")
        dbref.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
            }
            override fun onDataChange(snapshot: DataSnapshot) {
                contactArrayList.clear()
                // checks if data exists and adds it to the contactArrayList
                if (snapshot.exists()) {
                    for (userSnapshot in snapshot.children) {
                        val contacts = userSnapshot.getValue<NewContactModel>()!!
                        contactArrayList.add(contacts)
                        contactArrayList.sortWith(compareBy { it.newContactName })
                    }
                    // pass the list to the recycler view adapter
                    contactRecyclerView.adapter =
                        ContactListRecyclerAdapter(contactArrayList, this@MainActivity)
                }
            }
        })
    }

    // method to open the ContactDetails activity on click of the recycler view item
    override fun onItemClick(position: Int, recyclerViewModelList: ArrayList<NewContactModel>) {

        val intent = Intent(this, ContactsDetailsActivity::class.java).apply {
            putExtra("CONTACTS", recyclerViewModelList[position])
        }
        startActivity(intent)
    }

    // opens the SecondImplementation activity
    private fun secondImplementation() {
        load_second_implementation.setOnClickListener() {
            val intent = Intent(this, SecondImplementationActivity::class.java)
            startActivity(intent)
        }
    }
}
