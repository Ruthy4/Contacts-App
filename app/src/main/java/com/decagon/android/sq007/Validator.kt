package com.decagon.android.sq007

import java.util.regex.Matcher
import java.util.regex.Pattern

object Validator {

    // validates phoneNumber and attach to a matcher to check if it matches
    // returns true or false if it does or not
    fun mobileValidate(text: String): Boolean {
        val pattern: Pattern = Pattern.compile("^(0|234)((70)|([89][01]))[0-9]{8}\$")
        val matcher: Matcher = pattern.matcher(text)

        return matcher.matches()
    }

    // method to validate email address input to check if it matches the pattern defined in the regrex
    // returns true or false if it does or not
    fun emailValidate(text: String): Boolean {
        val pattern = Pattern.compile("[a-zA-Z0-9.]+@[A-Za-z0-9.]+\\.[a-zA-Z]{3,64}")
        val matcher: Matcher = pattern.matcher(text)

        return matcher.matches()
    }
}
