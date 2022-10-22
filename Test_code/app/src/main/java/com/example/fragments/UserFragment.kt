package com.example.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.adapter.UserAdapter
import com.example.model.Users
import com.example.test_code.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class UserFragment : Fragment() {

    lateinit var recyclerView: RecyclerView
    lateinit var usersList: List<Users>
    lateinit var mAdapter: UserAdapter
    lateinit var firebaseUser: FirebaseUser

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {


        val view = inflater.inflate(R.layout.fragment_user,container,false)

        recyclerView = view.findViewById(R.id.recyclerview_users)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(context)
        displayusers()
        return view
    }

    private fun displayusers() {
        usersList = arrayListOf()
        val reference = FirebaseDatabase.getInstance().getReference("Users")
        reference.addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                (usersList as ArrayList<Users>).clear()
                for (ds in snapshot.children){
                    val users = ds.getValue(Users::class.java)
                    firebaseUser = FirebaseAuth.getInstance().currentUser!!
                    if (!users?.id.equals(firebaseUser.uid)){
                        (usersList as ArrayList<Users>).add(users!!)
                    }
                    mAdapter = UserAdapter(context!!,usersList,false)
                    recyclerView.adapter = mAdapter
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }
}