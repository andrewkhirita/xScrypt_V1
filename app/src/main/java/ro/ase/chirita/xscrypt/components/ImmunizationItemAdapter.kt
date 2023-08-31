package ro.ase.chirita.xscrypt.components

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ro.ase.chirita.xscrypt.R
import ro.ase.chirita.xscrypt.databinding.ImmunizationItemBinding

class ImmunizationItemAdapter(private val immunizationItemList: MutableList<String>,
                              private val deleteItem: (position: Int) -> Unit) :
    RecyclerView.Adapter<ImmunizationItemAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding = ImmunizationItemBinding.bind(itemView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.immunization_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = immunizationItemList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.apply {
            tvImmunizationItem.text = immunizationItemList[position]
            btnDeleteImmunizationItem.setOnClickListener {
                deleteItem(position)
                notifyItemRangeChanged(position, immunizationItemList.size)
                immunizationItemList.removeAt(position)
            }
        }
    }
}
