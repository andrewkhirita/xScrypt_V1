package ro.ase.chirita.xscrypt.components

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ro.ase.chirita.xscrypt.core.getRelativeTimeSpanString
import ro.ase.chirita.xscrypt.databinding.NewsItemBinding
import ro.ase.chirita.xscrypt.domain.model.MedicalArticle

class NewsAdapter(private val listOfNews: List<MedicalArticle>, private val onItemClick: (String) -> Unit): RecyclerView.Adapter<NewsAdapter.NewsViewHolder>() {
    private var filteredListOfNews = listOfNews.filter { it.urlToImage != null }

    inner class NewsViewHolder(
        private val binding: NewsItemBinding,
        private val onItemClick: (String) -> Unit
    ): RecyclerView.ViewHolder(binding.root) {
        fun bind(medicalArticle: MedicalArticle) {
            binding.tvNewsTitle.text = if (medicalArticle.title.length > 55) {
                medicalArticle.title.substring(0, 55) + "..."
            } else {
                medicalArticle.title
            }
            Glide.with(binding.ivNewsImage.context)
                .load(medicalArticle.urlToImage)
                .into(binding.ivNewsImage)
            val inputDate = medicalArticle.publishedAt
            val outputDate = getRelativeTimeSpanString(inputDate)
            binding.tvDate.text = outputDate
            binding.root.setOnClickListener {
                onItemClick(medicalArticle.url)
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val binding = NewsItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NewsViewHolder(binding,onItemClick)
    }

    override fun getItemCount() = filteredListOfNews.size

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        val currentArticle = filteredListOfNews[position]
        holder.bind(currentArticle)
    }
}