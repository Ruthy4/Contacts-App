package com.decagon.android.sq007.secondimplementation

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.provider.ContactsContract
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.decagon.android.sq007.ContactsDetailsActivity
import com.decagon.android.sq007.NewContactModel
import com.decagon.android.sq007.R
import kotlinx.android.synthetic.main.activity_second_implementation.*

class SecondImplementationActivity : AppCompatActivity(), SecondRecyclerAdapter.OnItemClickListener {
    private val READ_CONTACTS = 102
    private lateinit var contactRecyclerView2: RecyclerView
    lateinit var contactAdapter: SecondRecyclerAdapter
    private var contactArrayList = ArrayList<NewContactModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second_implementation)

        contactRecyclerView2 = findViewById(R.id.recyclerView)
        checkForPermissions(android.Manifest.permission.READ_CONTACTS, "read_contacts", READ_CONTACTS)
//        contactArrayList = ArrayList()

//        contactRecyclerView2.adapter = SecondRecyclerAdapter(contactArrayList, this@SecondImplementationActivity)

//        readContacts()

//        load_contacts.setOnClickListener{
//            readContacts()
//            Log.d("Checking Contact button", "${readContacts()}")
//
//
//        }
    }

    private fun checkForPermissions(permission: String, name: String, requestCode: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            when {
                ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED -> {
                    readContacts()
                }
                shouldShowRequestPermissionRationale(permission) -> showDialog(permission, name, requestCode)

                else -> ActivityCompat.requestPermissions(this, arrayOf(permission), requestCode)
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        fun innerCheck(name: String) {
            if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {

                Toast.makeText(applicationContext, "$name permission refused", Toast.LENGTH_SHORT).show()
            } else {
                readContacts()

                Toast.makeText(applicationContext, "$name permission granted SHOULD CALL", Toast.LENGTH_SHORT).show()
            }
        }

        when (requestCode) {
            READ_CONTACTS -> innerCheck("read_contacts")
        }
    }

    private fun showDialog(permission: String, name: String, requestCode: Int) {
        val builder = AlertDialog.Builder(this)

        builder.apply {
            setMessage("Permission to access your $name is required to use this app")
            setTitle("Permission required")
            setPositiveButton("OK") { dialog, which ->
                ActivityCompat.requestPermissions(this@SecondImplementationActivity, arrayOf(permission), requestCode)
            }
        }

        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    override fun onItemClick(position: Int, recyclerViewModelList: ArrayList<NewContactModel>) {
        val intent = Intent(this, ContactsDetailsActivity::class.java).apply {
            putExtra("CONTACTS", recyclerViewModelList[position])
        }
        startActivity(intent)
    }

    private fun readContacts() {
//        checkForPermissions(android.Manifest.permission.READ_CONTACTS, "make calls", READ_CONTACTS)
//            val contactList:MutableList<NewContactModel> = ArrayList()
        val contacts = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null)

        var count = 0
        while (contacts!!.moveToNext()) {
            val name = contacts.getString(contacts.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))
            val PhoneNumber = contacts.getString(contacts.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))

            val obj = NewContactModel()
            obj.newContactName = name
            obj.newContactPhoneNumber = PhoneNumber

            contactArrayList.add(obj)
            count++
        }
        contactAdapter = SecondRecyclerAdapter(contactArrayList, this@SecondImplementationActivity)
        contactRecyclerView2.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        contactRecyclerView2.adapter = contactAdapter

//        Log.d("ContactArray", "readContacts: ${contactArrayList[0]}")
        contacts.close()
    }
}
