package ro.ase.chirita.xscrypt.presentation.import_wallet

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import ro.ase.chirita.xscrypt.domain.model.CreateWalletAction
import ro.ase.chirita.xscrypt.domain.repository.WalletRepository
import ro.ase.chirita.xscrypt.core.launch
import javax.inject.Inject

@HiltViewModel
class ImportWalletViewModel @Inject constructor(
    private val walletRepository: WalletRepository
): ViewModel() {

    private val _viewAction = MutableLiveData<CreateWalletAction>()
    val viewAction: LiveData<CreateWalletAction> = _viewAction

    fun importWallet(mnemonic: String) {
        launch(Dispatchers.IO) {
            val trimmedMnemonic = mnemonic.trim()
            if (trimmedMnemonic.split(" ").size != 24){
                _viewAction.postValue(CreateWalletAction.InvalidMnemonic)
            }
            else {
                walletRepository.saveWallet(trimmedMnemonic)
                _viewAction.postValue(CreateWalletAction.CloseScreen)
            }
        }
    }
}