package com.example.test_code

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase


class RegisterActivity : AppCompatActivity() {

    lateinit var username_et: EditText
    lateinit var et_password: EditText
    lateinit var et_email: EditText
    lateinit var registerbtn: Button
    lateinit var toolbar: Toolbar

    lateinit var username: String
    lateinit var email: String
    lateinit var password :String

    var mAuth: FirebaseAuth? = null

    lateinit var reference: DatabaseReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.register)

        toolbar = findViewById(R.id.toolbarregister)
        setSupportActionBar(toolbar)
        supportActionBar!!.title = "Register"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        username_et = findViewById(R.id.reg_username)
        et_password = findViewById(R.id.reg_password)
        et_email = findViewById(R.id.reg_email)
        registerbtn = findViewById(R.id.register_Account_btn)

        mAuth = FirebaseAuth.getInstance()
//        mAuth = Firebase.auth

        registerbtn.setOnClickListener {
            username = username_et.text.toString()
            password = et_password.text.toString()
            email = et_email.text.toString()


//            if(TextUtils.isEmpty(username)) {
//                username_et.error = "Required"
//            }
//            else if(TextUtils.isEmpty(email)){
//                    et_email.error = "Required"
//                }
//            else if(TextUtils.isEmpty((password))){
//                et_password.error = "Required"
//            }
//            else if(password.length < 6){
//                et_password.error = "Length Must Be 6 or more"
//            } else {
                registerUser(username, password, email)
//            startActivity(Intent(this,MainActivity::class.java))
//        }

    }
    }

    private fun registerUser(username: String, password: String, email: String) {

        Log.d("CCC", "$mAuth")
        mAuth!!.createUserWithEmailAndPassword(email,password)
        .addOnCompleteListener(this) {
            Log.d("DDD isSuccessful","${it.isSuccessful}")
            Log.d("DDD isComplete","${it.isComplete}")
            Log.d("DDD exception","${it.exception}")
            Log.d("DDD result","${it.result}")
            if (it.isSuccessful) {
                val user = mAuth!!.currentUser
                reference = FirebaseDatabase.getInstance().getReference("Users").child(user!!.uid)

                val hashMap: HashMap<String, String> = hashMapOf()
                hashMap["username"] = username
                hashMap["email"] = email
                hashMap["id"] = user.uid
                hashMap["imageURL"] = "default"
                hashMap["status"] = "offline"

                reference.setValue(hashMap).addOnCompleteListener(this) {
                    if (it.isSuccessful) {
                        Toast.makeText(this, "Registered Successfully", Toast.LENGTH_SHORT).show()
                        startActivity(
                            Intent(this, StartActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                        )
                    } else {
                        Toast.makeText(this, "Registered Fail", Toast.LENGTH_SHORT).show()
                    }
                }

            }
        }

//        Log.d("TAG", "email $email")
//        Log.d("TAG", "password $password")
//        mAuth.createUserWithEmailAndPassword(email, password)
//            .addOnCompleteListener(this) { task ->
//                if (task.isSuccessful) {
//                    // Sign in success, update UI with the signed-in user's information
//                    Log.d("TAG", "createUserWithEmail:success")
//                    val user = mAuth.currentUser
//                    Log.d("TAG", "user $user")
////                    updateUI(user)
//                } else {
//                    // If sign in fails, display a message to the user.
//                    Log.w("TAG", "createUserWithEmail:failure", task.exception)
//                    Toast.makeText(this, "Authentication failed.",
//                        Toast.LENGTH_SHORT).show()
////                    updateUI(null)
//                }
//
////                hideProgressBar()
//            }
    }

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = mAuth!!.currentUser
        if(currentUser != null){
            reload();
        }
    }

    private fun reload() {
        mAuth!!.currentUser!!.reload().addOnCompleteListener { task ->
            if (task.isSuccessful) {
//                updateUI(auth.currentUser)
                Toast.makeText(this, "Reload successful!", Toast.LENGTH_SHORT).show()
            } else {
                Log.e("TAG", "reload", task.exception)
                Toast.makeText(this, "Failed to reload user.", Toast.LENGTH_SHORT).show()
            }
        }
    }

}