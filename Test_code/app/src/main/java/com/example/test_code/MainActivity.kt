package com.example.test_code

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.Glide
import com.example.fragments.ChatFragment
import com.example.fragments.ProfileFragment
import com.example.fragments.UserFragment
import com.example.model.Users
import com.google.android.material.tabs.TabLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.sql.DatabaseMetaData


class MainActivity : AppCompatActivity() {

    lateinit var mAuth: FirebaseAuth
    var toolbar: Toolbar? = null

    lateinit var imageView: ImageView
    lateinit var username: TextView

    lateinit var reference: DatabaseReference
    lateinit var firebaseUser: FirebaseUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mAuth = FirebaseAuth.getInstance()

        imageView = findViewById(R.id.profile_image)
        username = findViewById(R.id.usernameonmainactivity)
        toolbar = findViewById(R.id.toolbarmain)

        setSupportActionBar(toolbar)
        supportActionBar?.title = ""
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val tabLayout = findViewById<TabLayout>(R.id.tablayout)
        val viewPager = findViewById<ViewPager>(R.id.viewPager)

        val viewPagerAdapter = ViewPagerAdapter(supportFragmentManager)

        viewPagerAdapter.addFragment(ChatFragment(),"Chats")
        viewPagerAdapter.addFragment(UserFragment(),"Users")
        viewPagerAdapter.addFragment(ProfileFragment(),"Profile")

        viewPager.adapter= viewPagerAdapter
        tabLayout.setupWithViewPager(viewPager)

        firebaseUser = FirebaseAuth.getInstance().currentUser!!
        reference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.uid)
        reference.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val users = snapshot.getValue(Users::class.java)
                username.text = users!!.username
                if (users.imageURL == "default"){
                    imageView.setImageResource(R.drawable.user)
                }else{
                    Glide.with(applicationContext).load(users.imageURL).into(imageView)
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })

    }


   inner class ViewPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

         var fragments: ArrayList<Fragment> = arrayListOf()
       var titles: ArrayList<String> = arrayListOf()

//               fun ViewPagerAdapter(fm: FragmentManager?) {
//            this.fragments = arrayListOf()
//            this.titles = arrayListOf()
//        }

        override fun getCount(): Int {
            return fragments.size
        }

        override fun getItem(position: Int): Fragment {
            return fragments[position]
        }

        fun addFragment(fragment :Fragment,title : String){
            fragments.add(fragment)
            titles.add(title)
        }

    }





    override fun onCreateOptionsMenu(menu: Menu): Boolean {
            menuInflater.inflate(R.menu.menu, menu)
            return super.onCreateOptionsMenu(menu)
        }
        override fun onOptionsItemSelected(item: MenuItem): Boolean {
            if (item.itemId == R.id.logout) {
                mAuth.signOut()
                finish()
//              val intent = Intent(this,StartActivity::class.java)
//                startActivity(intent)
                return true
            }
            return super.onOptionsItemSelected(item)
        }

    override fun onBackPressed() {
        super.onBackPressed()
        finishAffinity()
    }
    private fun status(status :String){
        val reference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.uid)

        val hashMap :HashMap<String,String> = hashMapOf()
        hashMap["status"] = status
        reference.updateChildren(hashMap as Map<String, Any>)

    }

    override fun onResume() {
        super.onResume()
        status("online")
    }

    override fun onPause() {
        super.onPause()
        status("offline")
    }
}


