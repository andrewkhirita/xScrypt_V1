package ro.ase.chirita.xscrypt.domain.model

sealed class CreateWalletViewState {
    data class GeneratedMnemonic(val mnemonic: String) : CreateWalletViewState()
}