package ro.ase.chirita.xscrypt.components

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ro.ase.chirita.xscrypt.R
import ro.ase.chirita.xscrypt.databinding.AllergyItemBinding


class AllergyItemAdapter(
    private val allergyItemList: MutableList<String>,
    private val deleteItem: (position: Int) -> Unit
) : RecyclerView.Adapter<AllergyItemAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding = AllergyItemBinding.bind(itemView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.allergy_item, parent, false)
        return ViewHolder(view)
    }
    override fun getItemCount() = allergyItemList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.apply {
            tvAllergyItem.text = allergyItemList[position]
            btnDeleteAllergyItem.setOnClickListener {
                deleteItem(position)
                notifyItemRangeChanged(position, allergyItemList.size)
                allergyItemList.removeAt(position)
            }
        }
    }

}
