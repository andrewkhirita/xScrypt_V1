package ro.ase.chirita.xscrypt.components

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ro.ase.chirita.xscrypt.R
import ro.ase.chirita.xscrypt.databinding.AllergyListItemBinding
import ro.ase.chirita.xscrypt.databinding.MedicationListItemBinding
import ro.ase.chirita.xscrypt.domain.model.AllergyList

class AllergyListAdapter(
    private val allergyList: MutableList<AllergyList>,
    private val deleteItem: (parentPosition: Int, childPosition: Int) -> Unit = { _, _ -> }
) : RecyclerView.Adapter<AllergyListAdapter.ViewHolder>() {

    private var allergiesItemAdapter: MutableList<AllergyItemAdapter> = mutableListOf()

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding = AllergyListItemBinding.bind(itemView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.allergy_list_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = allergyList.size

    @SuppressLint("NotifyDataSetChanged")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.apply {
            val allergyListItem = allergyList[holder.adapterPosition]

            val subItemAdapter = AllergyItemAdapter(allergyListItem.allergyList) { childPosition ->
                deleteItem(holder.adapterPosition, childPosition)
            }
            rvAllergyListItem.adapter = subItemAdapter

            tvAllergyListItem.setOnClickListener {
                rvAllergyListItem.visibility =
                    if (rvAllergyListItem.isShown) View.GONE else View.VISIBLE
            }
            subItemAdapter.notifyDataSetChanged()
            calculateTotalRecords(position, subItemAdapter)
        }
    }

    private fun AllergyListItemBinding.calculateTotalRecords(position: Int, subItemAdapter: AllergyItemAdapter) {
        if (allergiesItemAdapter.size > position) {
            allergiesItemAdapter[position] = subItemAdapter
        } else {
            allergiesItemAdapter.add(subItemAdapter)
        }

        var totalMedications = 0
        allergiesItemAdapter.forEach { adapter ->
            totalMedications += adapter.itemCount
        }
        tvNumber.text = totalMedications.toString()
    }

}