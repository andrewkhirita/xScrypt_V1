package ro.ase.chirita.xscrypt.components

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ro.ase.chirita.xscrypt.R
import ro.ase.chirita.xscrypt.databinding.AllergyListItemBinding
import ro.ase.chirita.xscrypt.databinding.ImmunizationItemBinding
import ro.ase.chirita.xscrypt.databinding.ImmunizationListItemBinding
import ro.ase.chirita.xscrypt.domain.model.ImmunizationList

class ImmunizationListAdapter(private val immunizationListCollection: MutableList<ImmunizationList>,
                              private val deleteItem: (parentPosition: Int, childPosition: Int) -> Unit = { _, _ -> }) :
    RecyclerView.Adapter<ImmunizationListAdapter.ViewHolder>() {

    private var immunzationsItemAdapter: MutableList<ImmunizationItemAdapter> = mutableListOf()


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding = ImmunizationListItemBinding.bind(itemView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.immunization_list_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = immunizationListCollection.size

    @SuppressLint("NotifyDataSetChanged")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.apply {
            val immunizationList = immunizationListCollection[position]

            val subItemAdapter = ImmunizationItemAdapter(immunizationList.immunizationList) { childPosition ->
                deleteItem(holder.adapterPosition, childPosition)
            }
            rvImmunizationListItem.adapter = subItemAdapter

            tvImmunizationListItem.setOnClickListener {
                rvImmunizationListItem.visibility = if (rvImmunizationListItem.isShown) View.GONE else View.VISIBLE
            }
            subItemAdapter.notifyDataSetChanged()
            calculateTotalRecords(position, subItemAdapter)
        }
    }

    private fun ImmunizationListItemBinding.calculateTotalRecords(
        position: Int,
        subItemAdapter: ImmunizationItemAdapter
    ) {
        if (immunzationsItemAdapter.size > position) {
            immunzationsItemAdapter[position] = subItemAdapter
        } else {
            immunzationsItemAdapter.add(subItemAdapter)
        }

        var totalMedications = 0
        immunzationsItemAdapter.forEach { adapter ->
            totalMedications += adapter.itemCount
        }
        tvNumber.text = totalMedications.toString()
    }
}