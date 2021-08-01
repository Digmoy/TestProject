package com.digmoy.testapllicationdigmoy.Adapter

import android.app.AlertDialog
import android.content.Context
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.digmoy.testapllicationdigmoy.R
import com.digmoy.testapllicationdigmoy.RoomDB.Entity.UserTableModel
import com.digmoy.testapllicationdigmoy.Utils.UserInterFace
import com.digmoy.testapllicationdigmoy.databinding.UserLayoutBinding


class UserListAdapter(private val context: Context, private var modelList: List<UserTableModel>,private var userInterFace: UserInterFace) :
    RecyclerView.Adapter<UserListAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: UserLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun setDataToViews(position: Int) {
            binding.name.text = modelList[position].name
            binding.email.text = modelList[position].email
            binding.phone.text = modelList[position].phone
            binding.address.text = modelList[position].address

            setImageViewWithByteArray(binding.imageView, modelList[position].byteArray!!)

            binding.llCard.setOnClickListener {
                userInterFace.getUserId(position, modelList[position].id!!)
            }

            binding.llCard.setOnLongClickListener {
                val builder = AlertDialog.Builder(context)
                builder.setTitle(R.string.dialogTitle)
                builder.setMessage(R.string.dialogMessage)
                builder.setIcon(android.R.drawable.ic_dialog_alert)

                builder.setPositiveButton("Yes") { dialogInterface, which ->

                    Toast.makeText(context, "clicked yes", Toast.LENGTH_LONG).show()
                }
                builder.setNeutralButton("Cancel") { dialogInterface, which ->
                    Toast.makeText(context, "clicked cancel\n operation cancel", Toast.LENGTH_LONG)
                        .show()
                }
                builder.setNegativeButton("No") { dialogInterface, which ->
                    Toast.makeText(context, "clicked No", Toast.LENGTH_LONG).show()
                }
                val alertDialog: AlertDialog = builder.create()
                alertDialog.setCancelable(false)
                alertDialog.show()

                true
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserListAdapter.ViewHolder {
        return ViewHolder(UserLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: UserListAdapter.ViewHolder, position: Int) {
        holder.setDataToViews(position)
    }

    override fun getItemCount(): Int {
        return modelList.size
    }

    fun setImageViewWithByteArray(view: ImageView, data: ByteArray) {
        val bitmap = BitmapFactory.decodeByteArray(data, 0, data.size)
        view.setImageBitmap(bitmap)
    }

    fun onLongClick(view: View): Boolean {
        Toast.makeText(view.context, "long click", Toast.LENGTH_SHORT).show()
        // Return true to indicate the click was handled
        return true
    }
}