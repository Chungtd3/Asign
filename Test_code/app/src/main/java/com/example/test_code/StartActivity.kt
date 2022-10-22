package com.example.test_code
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class StartActivity : AppCompatActivity() {
    lateinit var login: Button
    lateinit var register:Button


     override fun onCreate(savedInstanceState: Bundle?) {
         super.onCreate(savedInstanceState)
         setContentView(R.layout.start)

         login = findViewById(R.id.loginBtn)


         login.setOnClickListener {
             startActivity(Intent(this, LoginActivity::class.java))
         }

         register = findViewById(R.id.registerbtn)

         register.setOnClickListener {
             startActivity(Intent(this,RegisterActivity::class.java))
         }

     }

    override fun onStart() {
        super.onStart()

        val user = FirebaseAuth.getInstance().currentUser
        if (user != null){
            startActivity(Intent(this,MainActivity::class.java))
        }
    }

}