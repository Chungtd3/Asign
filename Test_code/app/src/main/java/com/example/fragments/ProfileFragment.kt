package com.example.fragments

import android.Manifest
import android.app.Activity.RESULT_OK
import android.app.AlertDialog
import android.content.ContentValues
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.model.Users
import com.example.test_code.R
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import de.hdodenhof.circleimageview.CircleImageView
import java.util.HashMap

class ProfileFragment : Fragment() {

    lateinit var reference: DatabaseReference
    var user: FirebaseUser? = null
    lateinit var imageView: CircleImageView
    lateinit var username: TextView
    val CAMERA_CODE = 200
    val GALLERY_CODE = 100
    lateinit var imageUri: Uri

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_profile, container, false)
        permission()

        imageView = view.findViewById(R.id.profile_frag_image)
        username = view.findViewById(R.id.username_profile_frag)

        user = FirebaseAuth.getInstance().currentUser

        reference = FirebaseDatabase.getInstance().getReference("Users").child(user!!.uid)

        reference.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val users = snapshot.getValue(Users::class.java)
                username.text = users!!.username
                if(users.imageURL == "default"){
                    imageView.setImageResource(R.drawable.user)
                }else{
                    Glide.with(activity!!.applicationContext).load(users.imageURL).into(imageView)

                }
            }

            override fun onCancelled(error: DatabaseError) {
            }

        })
        imageView.setOnClickListener {
            changeImage()
        }

        return view
    }

    private fun changeImage() {
        val options = arrayOf("Camera","Gallery")
        val builder : AlertDialog.Builder = AlertDialog.Builder(context)

        with(builder)
        {
            builder.setTitle("Choose an Option")

            builder.setItems(options)
            { _, which,->
            if (options[which]=="Camera"){
                openCamera()
            }
            if (options[which]=="Gallery"){
                openGallery()
            }
        }
            show()
        }
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent,GALLERY_CODE)
    }

    private fun openCamera() {
        val values = ContentValues()
        values.put("Tep Pick",MediaStore.Images.Media.TITLE)
        values.put("Tep Desc",MediaStore.Images.Media.TITLE)
        imageUri = context?.contentResolver?.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,values)!!

        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.putExtra(MediaStore.EXTRA_OUTPUT,imageUri)
        startActivityForResult(intent,CAMERA_CODE)
    }

    private fun permission() {
        Dexter.withContext(context).withPermissions(
            Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        ).withListener(object : MultiplePermissionsListener {

            override fun onPermissionsChecked(p0: MultiplePermissionsReport?) {
            }
            override fun onPermissionRationaleShouldBeShown(
                p0: MutableList<PermissionRequest>?,
                p1: PermissionToken?
            ) {
            }

        }).check()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == GALLERY_CODE && resultCode == RESULT_OK) {
            imageUri = data!!.data!!
            val filepath = "Photos/" + "userprofile" + user!!.uid

            val reference: StorageReference = FirebaseStorage.getInstance().getReference(filepath)
            reference.putFile(imageUri).addOnSuccessListener(OnSuccessListener { it ->
                val task = it.metadata!!.reference!!.downloadUrl
                task.addOnSuccessListener {
                    val imageURL = it.toString()

                    val reference1 : DatabaseReference = FirebaseDatabase.getInstance().getReference("Users").child(user!!.uid)

                    val hashMap :HashMap<String,String> = hashMapOf()
                    hashMap["imageURL"] = imageURL
                    reference1.updateChildren(hashMap as Map<String, Any>)
                }
            })
        }
    }

}