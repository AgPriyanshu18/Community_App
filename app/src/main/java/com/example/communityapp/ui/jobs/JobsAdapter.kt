package com.example.communityapp.ui.jobs

import android.content.Context
import android.util.Log.e
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.communityapp.R
import com.example.communityapp.data.models.Job
import com.example.communityapp.data.models.NewsFeed

class JobsAdapter(private val jobList: List<Pair<Job,String>>, private val activity: JobsActivity) :
    RecyclerView.Adapter<JobsAdapter.JobsViewHolder>() {
    lateinit var bottomSheetFragment: BottomSheetFragment

    inner class JobsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val jobPosition: TextView = itemView.findViewById(R.id.job_position)
        val companyName:TextView=itemView.findViewById(R.id.company_name)
        val jobDescription: TextView = itemView.findViewById(R.id.job_description)
        val jobSalary: TextView = itemView.findViewById(R.id.salary)
        val jobLocation: TextView = itemView.findViewById(R.id.job_location)
        val requirements: TextView = itemView.findViewById(R.id.job_requirements)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JobsViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.job_item, parent, false)
        return JobsViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: JobsViewHolder, position: Int) {
        val currentItem = jobList[position]

        holder.jobPosition.text = currentItem.first.jobTitle
        holder.jobDescription.text = currentItem.first.jobDescription
        holder.jobSalary.text = currentItem.first.salary.toString()
        holder.jobLocation.text = currentItem.first.location
        holder.requirements.text = currentItem.first.requirements.toString()
        holder.companyName.text = currentItem.first.businessName


        holder.itemView.setOnClickListener {
            try {
                bottomSheetFragment = BottomSheetFragment(currentItem.second) // send the id to fragment
                bottomSheetFragment.show(activity.supportFragmentManager, "BSDialogFragment")
            }
            catch (e:Exception) {
                e("error", "$e")
            }

            e("JobsAdapter", "id: ${currentItem.second}")

        }
    }

    override fun getItemCount(): Int {
        return jobList.size
    }
}
