package com.example.test_code

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.adapter.MessageAdapter
import com.example.model.Chats
import com.example.model.Users
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import de.hdodenhof.circleimageview.CircleImageView

class MessageActivity  : AppCompatActivity() {

    lateinit var friendid: String
    lateinit var message: String
    lateinit var myid: String
    lateinit var imageViewOnToolbar: CircleImageView
    lateinit var usernameonToolbar: TextView
    lateinit var toolbar: Toolbar
    lateinit var firebaseUser: FirebaseUser

    lateinit var et_message: EditText
    lateinit var send: Button

    lateinit var reference: DatabaseReference

    lateinit var chatsList: List<Chats>
    lateinit var messageAdapter: MessageAdapter
    lateinit var recyclerView: RecyclerView
    lateinit var seenlistener: ValueEventListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.message)

        toolbar = findViewById(R.id.toolbar_message)
        setSupportActionBar(toolbar)
        supportActionBar?.title = ""
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        imageViewOnToolbar = findViewById(R.id.profile_image_toolbar_message)
        usernameonToolbar = findViewById(R.id.username_ontoolbar_message)

        recyclerView = findViewById(R.id.recyclerview_messages)

        val layoutManager = LinearLayoutManager(this)
        layoutManager.stackFromEnd = true
        recyclerView.layoutManager = layoutManager

        send = findViewById(R.id.send_messsage_btn)
        et_message = findViewById(R.id.edit_message_text)

        firebaseUser = FirebaseAuth.getInstance().currentUser!!
        myid = firebaseUser.uid

        friendid = intent.getStringExtra("friendid")!!
        firebaseUser = FirebaseAuth.getInstance().currentUser!!

        val reference = FirebaseDatabase.getInstance().getReference("Users").child(friendid)

        reference.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val users = snapshot.getValue(Users::class.java)
                usernameonToolbar.text = users!!.username
                if (users.imageURL=="default"){
                    imageViewOnToolbar.setImageResource(R.drawable.user)
                }else{
                    Glide.with(applicationContext).load(users.imageURL).into(imageViewOnToolbar)
                }

                readMessages(myid,friendid,users.imageURL)
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
        seenMessage(friendid)

        et_message.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                send.isEnabled = s.toString().isNotEmpty()
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val text = et_message.text.toString()
                if(!text.startsWith(" ")){
                    et_message.text.insert(0," ")
                }
            }

            override fun afterTextChanged(s: Editable?) {
            }

        })

        send.setOnClickListener {
            message = et_message.text.toString()
            sendMessage(myid, friendid, message)
            et_message.text = null
        }

    }

    private fun readMessages(myid: String, friendid: String, imageURL: String) {
        chatsList = arrayListOf()
        val reference = FirebaseDatabase.getInstance().getReference("Chats")
        reference.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                (chatsList as ArrayList<Chats>).clear()
                for (ds in snapshot.children){
                    val chats = ds.getValue(Chats::class.java)

                    if (chats!!.sender == myid && chats.reciever == friendid||
                        chats.sender == friendid && chats.reciever ==myid){
                        (chatsList as ArrayList<Chats>).add(chats)
                    }

                    messageAdapter = MessageAdapter(this@MessageActivity,chatsList,imageURL)
                    recyclerView.adapter = messageAdapter
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }

        })
    }

    private fun seenMessage(friendid: String) {
        val reference = FirebaseDatabase.getInstance().getReference("Chats")
        seenlistener = reference.addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                for(ds in snapshot.children){
                    val chats = ds.getValue(Chats::class.java)
                    if(chats!!.reciever==myid && chats.sender == friendid){
                        val hashMap : HashMap<String,Boolean> = hashMapOf()
                        hashMap["isseen"] = true
                        ds.ref.updateChildren(hashMap as Map<String, Any>)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })

    }

    private fun sendMessage(myid: String, friendid: String, message: String) {
        val reference = FirebaseDatabase.getInstance().reference

        val hashMap : HashMap<String,String> = hashMapOf()
        hashMap["sender"] = myid
        hashMap["receiver"] = friendid
        hashMap["message"] = message
        hashMap["isseen"] = false.toString()

        reference.child("Chats").push().setValue(hashMap)

        val reference1 = FirebaseDatabase.getInstance().getReference("Chatslist").child(myid).child(friendid)
        reference1.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (!snapshot.exists()){
                    reference1.child("id").setValue(friendid)
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })

    }
    private fun status(status : String){
        val reference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.uid)
        val hashMap : HashMap<String,String> = hashMapOf()
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
        reference.removeEventListener(seenlistener)
    }



}