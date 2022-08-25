package com.udacity.asteroidradar.main

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.databinding.AsteroidItemBinding
import com.udacity.asteroidradar.db.AsteroidModel

class AsteroidAdapter(var listener: adapterDetails) :
    RecyclerView.Adapter<AsteroidAdapter.MainAsteroidViewHolder>() {
    private lateinit var binding: AsteroidItemBinding
    private var mlist = ArrayList<AsteroidModel>()
    lateinit var context: Context

    class MainAsteroidViewHolder(itemView: AsteroidItemBinding) :
        RecyclerView.ViewHolder(itemView.root) {
        var name: TextView = itemView.asteroidNum
        var date: TextView = itemView.asteroidDate
        var status: ImageView = itemView.asteroidImg

        fun bind(asteroidItem: AsteroidModel) {
            name.text = asteroidItem.codename
            date.text = asteroidItem.closeApproachDate
            if (asteroidItem.isPotentiallyHazardous) {
                status.setImageResource(R.drawable.ic_status_potentially_hazardous)
            } else {
                status.setImageResource(R.drawable.ic_status_normal)
            }
        }

        companion object {
            fun from(parent: ViewGroup): MainAsteroidViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = AsteroidItemBinding.inflate(layoutInflater, parent, false)
                return MainAsteroidViewHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainAsteroidViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        binding = AsteroidItemBinding.inflate(layoutInflater, parent, false)
        return MainAsteroidViewHolder.from(parent)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MainAsteroidViewHolder, position: Int) {
        holder.bind(mlist[position])
        binding.asteroid = mlist[position]
        holder.itemView.setOnClickListener {
            listener.onClick(
                AsteroidModel(
                    id = mlist[position].id,
                    relativeVelocity = mlist[position].relativeVelocity,
                    isPotentiallyHazardous = mlist[position].isPotentiallyHazardous,
                    estimatedDiameter = mlist[position].estimatedDiameter,
                    distanceFromEarth = mlist[position].distanceFromEarth,
                    codename = mlist[position].codename,
                    closeApproachDate = mlist[position].closeApproachDate,
                    absoluteMagnitude = mlist[position].absoluteMagnitude

                )
            )
        }
    }

    override fun getItemCount(): Int {
        return mlist.size
    }

    fun setList(context: Context, mlist: ArrayList<AsteroidModel>) {
        this.mlist = mlist
        this.context = context
    }
}


interface adapterDetails {
    fun onClick(asteroidItem: AsteroidModel)
}