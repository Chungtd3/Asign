package com.example.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.adapter.UserAdapter
import com.example.model.Chatslist
import com.example.model.Users
import com.example.test_code.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ChatFragment : Fragment() {

    lateinit var userlist: List<Chatslist>
    lateinit var mUsers: List<Users>
    lateinit var recyclerView: RecyclerView
    lateinit var mAdapter: UserAdapter
    lateinit var firebaseUser: FirebaseUser

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view =  inflater.inflate(R.layout.fragment_chat, container, false)

        userlist = arrayListOf()
        recyclerView = view.findViewById(R.id.chat_recyclerview_chatfrag)
        recyclerView.layoutManager = LinearLayoutManager(context)
        firebaseUser = FirebaseAuth.getInstance().currentUser!!

        val reference = FirebaseDatabase.getInstance().getReference("Chatlist").child(firebaseUser.uid)
        reference.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                (userlist as ArrayList<Chatslist>).clear()
                for(ds in snapshot.children){
                    val chatslist = ds.getValue(Chatslist::class.java)
                    (userlist as ArrayList<Chatslist>).add(chatslist!!)
                }
                chatListing()
            }

            override fun onCancelled(error: DatabaseError) {
            }

        })

        return view
    }

    private fun chatListing() {
        mUsers = arrayListOf()
        val databaseReference = FirebaseDatabase.getInstance().getReference("Users")
        databaseReference.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                (mUsers as ArrayList<Users>).clear()
                for(ds in snapshot.children){
                    val users = ds.getValue(Users::class.java)
                    for(chatslist in userlist){
                        if (users!!.id == chatslist.id){
                            (mUsers as ArrayList<Users>).add(users)
                        }
                    }
                }
                mAdapter = UserAdapter(context!!,mUsers,true)
                recyclerView.adapter = mAdapter
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

}