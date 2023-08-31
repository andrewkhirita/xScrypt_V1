package ro.ase.chirita.xscrypt.presentation.create_wallet

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.elrond.erdkotlin.domain.wallet.models.Address
import com.elrond.erdkotlin.domain.wallet.models.Wallet
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import ro.ase.chirita.xscrypt.domain.model.CreateWalletAction
import ro.ase.chirita.xscrypt.domain.model.CreateWalletViewState
import ro.ase.chirita.xscrypt.domain.repository.UserRepository
import ro.ase.chirita.xscrypt.domain.repository.WalletRepository
import ro.ase.chirita.xscrypt.core.launch
import javax.inject.Inject

@HiltViewModel
class CreateWalletViewModel @Inject constructor(
    private val walletRepository: WalletRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    private val _viewAction = MutableLiveData<CreateWalletAction>()
    val viewAction: LiveData<CreateWalletAction> = _viewAction

    private val _viewState = MutableLiveData<CreateWalletViewState>()
    val viewState: LiveData<CreateWalletViewState> = _viewState

    private var wallet: Wallet? = null

    fun generateMnemonic() {
        launch(Dispatchers.IO) {
            val mnemonic = walletRepository.createWallet()
            _viewState.postValue(
                CreateWalletViewState.GeneratedMnemonic(mnemonic.joinToString())
            )
        }
    }

    fun saveWallet(mnemonic: String, email: String) {
        launch(Dispatchers.IO) {
            val trimmedMnemonic = mnemonic.trim()
            walletRepository.saveWallet(trimmedMnemonic)
            pushWalletInDatabase(email)
            _viewAction.postValue(CreateWalletAction.CloseScreen)
        }
    }

    private suspend fun pushWalletInDatabase(email: String) {
        val wallet = wallet ?: walletRepository.loadWallet()?.also { wallet = it }
        val address = Address.fromHex(wallet!!.publicKeyHex)
        userRepository.updateUserWallet(email,address.bech32)
    }

    fun refactorSeedPhraseInView(mnemonicString: String): String {
        val mnemonic = mnemonicString.split(" ")
        val pageSize = 3
        val pages: MutableList<List<String>> = mutableListOf()
        for (i in mnemonic.indices step pageSize) {
            val page = mnemonic.subList(i, minOf(i + pageSize, mnemonic.size))
            pages.add(page)
        }
        val sb = StringBuilder()
        for (page in pages) {
            val numberedPage = StringBuilder()
            for (i in page.indices) {
                val numberedString = "${i + 1 + pageSize * pages.indexOf(page)} ${page[i]}"
                numberedPage.append(numberedString)
                if (i < page.size - 1) {
                    numberedPage.append(" • ")
                }
            }
            sb.append(numberedPage)
            sb.append("\n")
        }
        return sb.toString().replace(",", "")
    }

    fun copySeedPhrase(text: String, context: Context) {
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("label", text)
        clipboard.setPrimaryClip(clip)
    }

}