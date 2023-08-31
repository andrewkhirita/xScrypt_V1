package ro.ase.chirita.xscrypt.components

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ro.ase.chirita.xscrypt.R
import ro.ase.chirita.xscrypt.databinding.MedicationItemBinding

class MedicationItemAdapter(private val medicationItemList: MutableList<String>,
                            private val deleteItem: (position: Int) -> Unit):
    RecyclerView.Adapter<MedicationItemAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding = MedicationItemBinding.bind(itemView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.medication_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = medicationItemList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.apply {
            tvMedicationItem.text = medicationItemList[position]
            btnDeleteMedicationItem.setOnClickListener {
                deleteItem(position)
                notifyItemRangeChanged(position, medicationItemList.size)
                medicationItemList.removeAt(position)
            }
        }
    }


}