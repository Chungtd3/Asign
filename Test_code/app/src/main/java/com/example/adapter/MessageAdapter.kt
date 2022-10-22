package com.example.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.model.Chats
import com.example.test_code.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import de.hdodenhof.circleimageview.CircleImageView

class MessageAdapter(context: Context,chatslist :List<Chats>,imageURL :String) : RecyclerView.Adapter<MessageAdapter.MyViewHolder>() {
     var context: Context
     var chatslist: List<Chats>
    var imageURL:String? = null

    val MESSAGE_RIGHT : Int = 0 // FOR ME (

    val MESSAGE_LEFT : Int = 1 // FOR FRIEND

//    fun MessageAdapter(context: Context, chatslist: List<Chats>, imageURL: String) {
//        this.context = context
//        this.chatslist = chatslist
//        this.imageURL = imageURL
//    }
    init {
        this.context = context
        this.chatslist = chatslist
        this.imageURL = imageURL

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
         if (viewType == MESSAGE_RIGHT) {
            val view: View = LayoutInflater.from(context).inflate(R.layout.chat_item_right, parent, false)
           return MyViewHolder(view)
        } else {
            val view: View = LayoutInflater.from(context).inflate(R.layout.chat_item_left, parent, false)
             return MyViewHolder(view)
        }
    }



    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var messagetext : TextView
        var imageView : CircleImageView
        var seen : TextView
        init {
            messagetext = itemView.findViewById(R.id.show_message)
            imageView = itemView.findViewById(R.id.chat_image)
            seen = itemView.findViewById(R.id.text_Seen)
        }
    }


    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val chats : Chats = chatslist.get(position)

        holder.messagetext.text = chats.message
        if(imageURL.equals("default")){
            holder.imageView.setImageResource(R.drawable.user)
        }else{
            Glide.with(context).load(imageURL).into(holder.imageView)
        }

        if(position == chatslist.size -1){
            if(chats.isseen){
                holder.seen.text = "seen"
            }else{
                holder.seen.text = "Delivered"
            }
        }else{
            holder.seen.visibility = View.GONE
        }
    }

    override fun getItemCount(): Int {
        return chatslist.size
    }

    override fun getItemViewType(position: Int): Int {
        val user : FirebaseUser = FirebaseAuth.getInstance().currentUser!!
        if(chatslist[position].sender == user.uid){
            return MESSAGE_RIGHT
        }else{
            return MESSAGE_LEFT
        }

    }

}
