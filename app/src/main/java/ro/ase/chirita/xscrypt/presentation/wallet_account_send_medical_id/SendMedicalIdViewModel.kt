package ro.ase.chirita.xscrypt.presentation.wallet_account_send_medical_id

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.elrond.erdkotlin.Exceptions
import com.elrond.erdkotlin.domain.account.GetAccountUsecase
import com.elrond.erdkotlin.domain.networkconfig.GetNetworkConfigUsecase
import com.elrond.erdkotlin.domain.transaction.EstimateCostOfTransactionUsecase
import com.elrond.erdkotlin.domain.transaction.SendTransactionUsecase
import com.elrond.erdkotlin.domain.transaction.models.Transaction
import com.elrond.erdkotlin.domain.wallet.models.Address
import com.elrond.erdkotlin.domain.wallet.models.Wallet
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import ro.ase.chirita.xscrypt.core.launch
import ro.ase.chirita.xscrypt.data.mapper.toUi
import ro.ase.chirita.xscrypt.domain.model.SendMedicalIdState
import ro.ase.chirita.xscrypt.domain.repository.WalletRepository
import java.math.BigInteger
import javax.inject.Inject

@HiltViewModel
class SendMedicalIdViewModel @Inject constructor(
    private val walletRepository: WalletRepository,
    private val getAccountUsecase: GetAccountUsecase,
    private val sendTransactionUsecase: SendTransactionUsecase,
    private val estimateCostOfTransactionUsecase: EstimateCostOfTransactionUsecase,
    private val getNetworkConfigUsecase: GetNetworkConfigUsecase,
): ViewModel()  {

    private val _viewState = MutableLiveData<SendMedicalIdState>(SendMedicalIdState.Loading)
    val viewState: LiveData<SendMedicalIdState> = _viewState

    private var wallet: Wallet? = null

    fun getWalletAccount() = launch(Dispatchers.IO) {
        _viewState.postValue(SendMedicalIdState.Loading)
        val wallet = wallet ?: walletRepository.loadWallet()?.also { wallet = it }

        val address = Address.fromHex(wallet!!.publicKeyHex)
        val account = getAccountUsecase.execute(address)
        val accountUi = account.toUi()
        val state = when (val state = _viewState.value) {
            is SendMedicalIdState.Content -> state.copy(
                account = accountUi
            )
            else -> SendMedicalIdState.Content(
                account = accountUi
            )
        }
        _viewState.postValue(state)
    }

    fun sendTransaction(toAddress: String, message: String?) {
        val receiverAddress = extractAddress(toAddress)

        val wallet = requireNotNull(wallet)
        launch(Dispatchers.IO) {
            val account = getAccountUsecase.execute(Address.fromHex(wallet.publicKeyHex))
            val networkConfig = getNetworkConfigUsecase.execute()
            val transaction = run {
                val transaction = Transaction(
                    sender = Address.fromHex(wallet.publicKeyHex),
                    receiver = receiverAddress!!,
                    value = BigInteger.ZERO,
                    data = message,
                    chainID = networkConfig.chainID,
                    gasPrice = networkConfig.minGasPrice,
                    nonce = account.nonce
                )
                val gasLimit = estimateCostOfTransactionUsecase.execute(transaction)
                transaction.copy(gasLimit = gasLimit.toLong())
            }
            sendTransactionUsecase.execute(transaction, wallet)
        }
    }


    private fun extractAddress(toAddress: String) = try {
        when {
            Address.isValidBech32(toAddress) -> Address.fromBech32(toAddress)
            else -> Address.fromHex(toAddress)
        }
    } catch (e: Exceptions.AddressException) {
        e.printStackTrace()
        null
    } catch (e: Exceptions.BadAddressHrpException) {
        e.printStackTrace()
        null
    }

}