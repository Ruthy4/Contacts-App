package com.decagon.android.sq007

import java.io.Serializable

data class NewContactModel(val newContactName: String? = " ", val newContactPhoneNumber: String? = " ", val newContactEmail: String? = " "): Serializable

// private fun saveContact (){
//    var  name = contactName.text.toString().trim()
//    if(name.isEmpty()){contactName.error = "Please enter a name"
//
//        return
//    }
// }
