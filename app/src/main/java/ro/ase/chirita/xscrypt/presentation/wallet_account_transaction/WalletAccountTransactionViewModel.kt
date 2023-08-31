package ro.ase.chirita.xscrypt.presentation.wallet_account_transaction

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elrond.erdkotlin.domain.transaction.GetAddressTransactionsUsecase
import com.elrond.erdkotlin.domain.transaction.models.TransactionOnNetwork
import com.elrond.erdkotlin.domain.wallet.models.Address
import com.elrond.erdkotlin.domain.wallet.models.Wallet
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ro.ase.chirita.xscrypt.domain.model.*
import ro.ase.chirita.xscrypt.domain.repository.WalletRepository
import javax.inject.Inject

@HiltViewModel
class WalletAccountTransactionViewModel @Inject constructor(
    private val walletRepository: WalletRepository,
    private val getAddressTransactionsUsecase: GetAddressTransactionsUsecase,
): ViewModel() {

    private val _viewState = MutableLiveData<SendMedicalIdState>(SendMedicalIdState.Loading)
    val viewState: LiveData<SendMedicalIdState> = _viewState

    private val _getTransactionsResponse = MutableLiveData<List<TransactionOnNetwork>>()
    val getTransactionsResponse: LiveData<List<TransactionOnNetwork>> = _getTransactionsResponse

    private var wallet: Wallet? = null

    fun refreshTransactionsAccount() = viewModelScope.launch(Dispatchers.IO) {
        val wallet = wallet ?: walletRepository.loadWallet()?.also { wallet = it }
        val address = Address.fromHex(wallet!!.publicKeyHex)
        val transactionList = getAddressTransactionsUsecase.execute(address)
        _getTransactionsResponse.postValue(transactionList)
    }
}

