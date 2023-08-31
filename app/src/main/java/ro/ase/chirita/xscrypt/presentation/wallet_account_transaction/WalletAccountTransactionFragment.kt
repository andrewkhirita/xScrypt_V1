package ro.ase.chirita.xscrypt.presentation.wallet_account_transaction

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import ro.ase.chirita.xscrypt.components.TransactionAdapter
import ro.ase.chirita.xscrypt.databinding.FragmentWalletAccountTransactionBinding

@AndroidEntryPoint
class WalletAccountTransactionFragment : Fragment() {

    private lateinit var binding: FragmentWalletAccountTransactionBinding
    private val viewModel: WalletAccountTransactionViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentWalletAccountTransactionBinding.inflate(inflater,container,false)
        return binding.root
    }

    private fun setupRecyclerView() {
        binding.rvTransactions.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        viewModel.getTransactionsResponse.observe(viewLifecycleOwner) { transactionList ->
            if(transactionList.isEmpty()){
                binding.tvNoTransactions.visibility = View.VISIBLE
            }else{
                binding.tvNoTransactions.visibility = View.GONE
                binding.rvTransactions.visibility = View.VISIBLE
                binding.rvTransactions.adapter = TransactionAdapter(transactionList)
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.refreshTransactionsAccount()
        viewModel.viewState.observe(requireActivity()) {
            setupRecyclerView()
        }
        refresh()
    }

    private fun refresh(){
        binding.swipeRefreshTransactions.setOnRefreshListener {
            viewModel.viewState.observe(requireActivity()) {
                viewModel.refreshTransactionsAccount()
                setupRecyclerView()
                binding.swipeRefreshTransactions.isRefreshing = false
            }
        }
    }

}
