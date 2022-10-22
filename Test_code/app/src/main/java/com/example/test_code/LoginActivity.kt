package com.example.test_code

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.rengwuxian.materialedittext.MaterialEditText

class LoginActivity : AppCompatActivity() {

    var et_email: MaterialEditText? = null
    var et_password: MaterialEditText? = null
    var loginBtn: Button? = null
    var toolbar: Toolbar? = null
    var email: String? = null
    var password: String? = null
    var mAuth: FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login)

        val toolbar = findViewById<Toolbar>(R.id.toolbarlogin)
        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            // Set toolbar title/app title
            title = "Login"

        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)


        val et_mai = findViewById<EditText>(R.id.log_email)
        val et_password = findViewById<EditText>(R.id.log_password)
        val login = findViewById<Button>(R.id.loggin_account)

        mAuth = FirebaseAuth.getInstance()

        login.setOnClickListener {
            val emai = et_mai.text.toString()
            val password = et_password.text.toString()

            if (TextUtils.isEmpty(emai)) {
                et_mai.error = "Required"
            } else if (TextUtils.isEmpty(password)) {
                et_password.error = "Required"
            } else {
                loginMeIn(emai, password)
//                val intent = Intent(this,MainActivity::class.java)
//                startActivity(intent)

            }
        }

    }


    private fun loginMeIn(email: String, password: String) {
        mAuth?.signInWithEmailAndPassword(email,password)?.addOnCompleteListener(OnCompleteListener {
            if(it.isSuccessful){
                val intent = Intent(this,MainActivity::class.java)
                intent.flags= (Intent.FLAG_ACTIVITY_CLEAR_TOP or  Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
                finish()
                Toast.makeText(this, "Logged In Successfully", Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(this, "Logged In Fail", Toast.LENGTH_SHORT).show()
            }
        })

    }
}
