package com.example.communityapp.ui.family

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.communityapp.R
import com.example.communityapp.data.models.Member
import com.example.communityapp.data.models.allMembers
import com.example.communityapp.databinding.ProfileItemLayoutBinding
import com.example.communityapp.utils.Constants

class FamilyAdapter(private val context : Context, private val members : List<List<Member>>) : RecyclerView.Adapter<FamilyAdapter.ViewHolder>() {

    class ViewHolder(binding: ProfileItemLayoutBinding) : RecyclerView.ViewHolder(binding.root){
        var name = binding.profileName
        val address = binding.profileAddressFixed
        val age_gender = binding.profileAgeGender
        val button = binding.callNow
        var relation =binding.profileRelation
        var image = binding.profileImage
        var ItemView = binding.itemView
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
        val model = members[position][0]

        holder.name.text = model.name
        val name = context.getString(R.string.address)
        holder.address.text = "${name} ${model.address}"
        holder.age_gender.text = "${model.age}/${model.gender}"
        holder.relation.text = model.relation

        if(model.contact == "NA") {
            holder.button.visibility = View.GONE
        }

        if (model.relation.uppercase() == "HEAD"){
            holder.ItemView.background = ContextCompat.getDrawable(context, R.drawable.head_drawable_border)
        }

        holder.button.setOnClickListener {
            val intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse("tel:${model.contact}")
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

            ContextCompat.startActivity(context, intent, null)
        }

        holder.ItemView.setOnClickListener {
            val intent = Intent(context, FamilyDetailsActivity::class.java)
            intent.putExtra(Constants.FAMILYDATA, allMembers(members[position]))
            context.startActivity(intent)
        }

        Glide.with(context)
            .load(model.profilePic)
            .centerCrop()
            .placeholder(R.drawable.baseline_person_24)
            .into(holder.image)

    }

}