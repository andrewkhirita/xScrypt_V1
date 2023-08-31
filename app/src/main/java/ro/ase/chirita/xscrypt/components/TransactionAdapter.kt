package ro.ase.chirita.xscrypt.components

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.elrond.erdkotlin.domain.transaction.models.TransactionOnNetwork
import ro.ase.chirita.xscrypt.databinding.TransactionItemBinding
import java.math.BigDecimal
import java.math.BigInteger
import java.text.SimpleDateFormat
import java.util.*

class TransactionAdapter(private var transactionList: List<TransactionOnNetwork>): RecyclerView.Adapter<TransactionAdapter.TransactionViewHolder>() {
    inner class TransactionViewHolder(
        private val binding: TransactionItemBinding,
    ): RecyclerView.ViewHolder(binding.root) {
        fun bind(transaction: TransactionOnNetwork) {
            val hashTxFormat = transaction.hash.substring(0, 30) + "..."
            binding.tvHashTxValue.text = hashTxFormat

            val receiverFormat = transaction.receiver.bech32.substring(0,30) + "..."
            binding.tvValueValue.text = receiverFormat

            val fee = BigInteger(transaction.fee)
            val feeInEgld = fee.toBigDecimal().divide(BigDecimal.TEN.pow(18))
            val feeMultiplied = feeInEgld.multiply(BigDecimal.valueOf(4.50))
            val feeMultipliedFormatted = "%.4f".format(feeMultiplied)
            binding.tvFeeValue.text = feeMultipliedFormatted

            val timestamp = transaction.timestamp
            val date = Date(timestamp * 1000L)
            val dateFormat = SimpleDateFormat("MMM dd, yyyy hh:mm:ss", Locale.ENGLISH)
            val formattedDate = dateFormat.format(date)
            binding.tvTimestampValue.text = formattedDate
            binding.tvStatusValue.text = transaction.status
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionViewHolder {
        val binding = TransactionItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TransactionViewHolder(binding)
    }

    override fun getItemCount() = transactionList.size

    override fun onBindViewHolder(holder: TransactionViewHolder, position: Int) {
        val currentTransaction = transactionList[position]
        holder.bind(currentTransaction)
    }
}