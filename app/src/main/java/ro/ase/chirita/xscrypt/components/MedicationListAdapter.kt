package ro.ase.chirita.xscrypt.components

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ro.ase.chirita.xscrypt.R
import ro.ase.chirita.xscrypt.databinding.MedicationListItemBinding
import ro.ase.chirita.xscrypt.domain.model.MedicationList

class MedicationListAdapter(private val medicationListCollection: MutableList<MedicationList>,
                            private val deleteItem: (parentPosition: Int, childPosition: Int) -> Unit = { _, _ -> }) :
    RecyclerView.Adapter<MedicationListAdapter.ViewHolder>() {

    private var medicationsItemAdapter: MutableList<MedicationItemAdapter> = mutableListOf()


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding = MedicationListItemBinding.bind(itemView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.medication_list_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = medicationListCollection.size


    @SuppressLint("NotifyDataSetChanged")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.apply {
            val medicationList = medicationListCollection[position]

            val subItemAdapter = MedicationItemAdapter(medicationList.medicationList) { childPosition ->
                deleteItem(holder.adapterPosition, childPosition)
            }
            rvMedicationListItem.adapter = subItemAdapter

            tvMedicationListItem.setOnClickListener {
                rvMedicationListItem.visibility = if (rvMedicationListItem.isShown) View.GONE else View.VISIBLE
            }
            subItemAdapter.notifyDataSetChanged()
            calculateTotalRecords(position, subItemAdapter)

        }
    }

    private fun MedicationListItemBinding.calculateTotalRecords(
        position: Int,
        subItemAdapter: MedicationItemAdapter
    ) {
        if (medicationsItemAdapter.size > position) {
            medicationsItemAdapter[position] = subItemAdapter
        } else {
            medicationsItemAdapter.add(subItemAdapter)
        }

        var totalMedications = 0
        medicationsItemAdapter.forEach { adapter ->
            totalMedications += adapter.itemCount
        }
        tvNumber.text = totalMedications.toString()
    }
}