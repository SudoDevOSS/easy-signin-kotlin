package com.sudodevoss.easysignin.networking.domain.account.models

import com.squareup.moshi.JsonClass
import java.lang.IllegalArgumentException

@JsonClass(generateAdapter = true)
class UserEntity(id: String, firstName: String, lastName: String, email: String, phoneNumber: String) {
    val id: String = id
    var firstName = firstName
        private set
    var lastName = lastName
        private set
    var email = email
        private set
    var phoneNumber = phoneNumber
        private set

    /**
     * Update user email address
     *
     * @throws IllegalArgumentException if email is not valid
     */
    fun changeEmail(email: String) {
        if (email.isEmpty()) {
            throw IllegalArgumentException("Email cannot be empty")
        }
        this.email = email
    }

    /**
     * Change user first name & last name if inputs are valid
     */
    fun changeName(firstName: String? = null, lastName: String? = null) {
        if (firstName != null && firstName.isNotEmpty()) {
            this.firstName = firstName
        }
        if (lastName != null && lastName.isNotEmpty()) {
            this.lastName = lastName
        }
    }

    /**
     * Change user phone number if valid
     *
     * @throws IllegalArgumentException if new number is not valid
     */
    fun changePhoneNumber(number: String) {
        if (number.isEmpty()) {
            throw IllegalArgumentException("Phone number is not valid")
        }
        this.phoneNumber = number
    }
}