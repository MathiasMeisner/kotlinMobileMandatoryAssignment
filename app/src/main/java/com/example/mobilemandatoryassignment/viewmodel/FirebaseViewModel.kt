package com.example.mobilemandatoryassignment.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class FirebaseViewModel {
    private lateinit var mAuth: FirebaseAuth
    val message: MutableLiveData<String> = MutableLiveData()
    val user: MutableLiveData<FirebaseUser?> = MutableLiveData()

    fun signIn(email: String, password: String) {
        mAuth = Firebase.auth

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                user.value = mAuth.currentUser
            } else {
                message.value = task.exception?.message
                Log.d("Apple", "Login failed.")
            }
        }
    }

    fun createUser(email: String, password: String) {
        mAuth = Firebase.auth

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                user.value = mAuth.currentUser
            } else {
                message.value = task.exception?.message
                Log.d("Apple", "Failed creating user.")
            }
        }
    }

    fun signOut() {
        Firebase.auth.signOut()
        user.value = null
    }
}