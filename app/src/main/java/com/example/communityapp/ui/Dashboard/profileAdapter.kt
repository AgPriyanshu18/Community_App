package com.example.communityapp.ui.Dashboard

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.communityapp.R
import com.example.communityapp.data.models.Member
import com.example.communityapp.databinding.ProfileItemLayoutBinding

class profileAdapter(private val context : Context, private val members : List<Member>) : RecyclerView.Adapter<profileAdapter.ViewHolder>() {

    class ViewHolder(binding: ProfileItemLayoutBinding) : RecyclerView.ViewHolder(binding.root){
        var name = binding.profileName
        val address = binding.profileAddressFixed
        val age_gender = binding.profileAgeGender
        val button = binding.callNow
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ProfileItemLayoutBinding.inflate(
                LayoutInflater.from(parent.context),parent,false
            )
        )
    }

    override fun getItemCount(): Int {
        return members.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val model = members[position]

        holder.name.text = model.name
        val name = context.getString(R.string.address)
        holder.address.text = "${name} ${model.address}"
        holder.age_gender.text = "${model.age}/${model.gender}"

        holder.button.setOnClickListener {
            val intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse("tel:${model.contact}")
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

            ContextCompat.startActivity(context, intent, null)
        }

    }

}