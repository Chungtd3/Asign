package com.example.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.model.Chats
import com.example.model.Users
import com.example.test_code.R
import com.example.test_code.MessageActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import de.hdodenhof.circleimageview.CircleImageView

class UserAdapter(context: Context,userlist : List<Users>,ischat : Boolean) : RecyclerView.Adapter<UserAdapter.MyHolder>() {

     var context: Context
     var userlist: List<Users>
    var isChat : Boolean

    var friendid: String? = null
    var thelastmessage: String? = null
    var firebaseUser: FirebaseUser? = null

//    fun UserAdapter(context: Context, userlist: List<Users>, isChat: Boolean) {
//        this.context = context
//        this.userlist = userlist
//        this.isChat = isChat
//    }
    init {
        this.context = context
        this.userlist = userlist
        this.isChat = ischat
    }


    inner class MyHolder(itemView: View) : RecyclerView.ViewHolder(itemView),View.OnClickListener{
        var username: TextView
        var last_msg: TextView
        var imageView: CircleImageView
        var image_on: CircleImageView
        var image_off: CircleImageView
        init {
            username = itemView.findViewById(R.id.username_userfrag)
            imageView = itemView.findViewById(R.id.image_user_userfrag)
            image_on = itemView.findViewById(R.id.image_online)
            image_off = itemView.findViewById(R.id.image_offline)
            last_msg = itemView.findViewById(R.id.lastMessage)
            itemView.setOnClickListener(this)
        }
        override fun onClick(p0: View?) {
            val user = userlist[adapterPosition]
            friendid = user.id

            val intent = Intent(context,MessageActivity::class.java)
            intent.putExtra("friendid",friendid)
            context.startActivity(intent)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.layout_of_user,parent,false)
        return MyHolder(view)
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {

        val user = userlist.get(position)
        friendid = user.id
        holder.username.text = user.username
        if(user.imageURL == "default"){
            holder.imageView.setImageResource(R.drawable.user)

        }else{
            Glide.with(context).load(user.imageURL).into(holder.imageView)
        }
        if(isChat){
            if(user.status == "online"){
                holder.image_on.visibility= View.VISIBLE
                holder.image_off.visibility = View.GONE
            }else{
                holder.image_on.visibility = View.GONE
                holder.image_off.visibility = View.VISIBLE
            }
        }else{
            holder.image_on.visibility = View.GONE
            holder.image_off.visibility = View.GONE
        }
        if(isChat){
            LastMessage(user.id,holder.last_msg)
        }else{
            holder.last_msg.visibility = View.GONE
        }
    }

    private fun LastMessage(friendid: String, last_msg: TextView) {
        thelastmessage = "default"
        firebaseUser = FirebaseAuth.getInstance().currentUser
        var reference = FirebaseDatabase.getInstance().getReference("Chats")
        reference.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
               for (ds : DataSnapshot  in snapshot.children) {

                    val chats : Chats? = ds.getValue(Chats::class.java)

                   if(firebaseUser!=null && chats!=null){
                       if(chats.sender == friendid && chats.reciever == firebaseUser!!.uid ||
                           chats.sender == firebaseUser!!.uid && chats.reciever == friendid
                       ){
                           thelastmessage = chats.message
                       }
                   }
                }
                when (thelastmessage) {
                    "default" -> last_msg.text = "No message"
                    else -> last_msg.text = thelastmessage
                }
                thelastmessage = "default"

            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })


    }

    override fun getItemCount(): Int {
        return userlist.size
    }

}