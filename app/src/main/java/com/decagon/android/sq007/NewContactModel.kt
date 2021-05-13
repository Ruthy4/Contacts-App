package com.decagon.android.sq007

import com.google.firebase.database.Exclude
import java.io.Serializable

data class NewContactModel(
    @Exclude var id: String? = null,
    var newContactName: String? = "",
    var newContactPhoneNumber: String? = "",
    val newContactEmail: String? = "",
    val newContactImage: Int = 0
) : Serializable
