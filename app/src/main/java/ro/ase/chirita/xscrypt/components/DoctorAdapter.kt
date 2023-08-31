package ro.ase.chirita.xscrypt.components

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ro.ase.chirita.xscrypt.R
import ro.ase.chirita.xscrypt.databinding.DoctorItemBinding
import ro.ase.chirita.xscrypt.domain.model.Doctor

class DoctorAdapter(private var doctorList: List<Doctor>, private val onItemClick: (Doctor) -> Unit): RecyclerView.Adapter<DoctorAdapter.UserViewHolder>() {

    inner class UserViewHolder(
        private val binding: DoctorItemBinding,
        private val onItemClick: (Doctor) -> Unit
    ): RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(doctor: Doctor) {
            binding.tvDoctorName.text = "Dr. " + doctor.name
            Glide.with(binding.ivDoctorProfile.context)
                .load(doctor.photoUrl)
                .placeholder(R.drawable.ic_profile_user)
                .error(R.drawable.ic_profile_user)
                .into(binding.ivDoctorProfile)

            binding.clTopAdd.setOnClickListener {
                onItemClick(doctor)
            }
        }
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val binding = DoctorItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UserViewHolder(binding,onItemClick)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val currentUser = doctorList[position]
        holder.bind(currentUser)
    }

    override fun getItemCount() = doctorList.size

    @SuppressLint("NotifyDataSetChanged")
    fun updateList(newList: List<Doctor>) {
        doctorList = newList
        notifyDataSetChanged()
    }
}
