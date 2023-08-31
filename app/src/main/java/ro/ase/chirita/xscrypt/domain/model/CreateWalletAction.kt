package ro.ase.chirita.xscrypt.domain.model

sealed class CreateWalletAction {
    object CloseScreen : CreateWalletAction()
    object InvalidMnemonic: CreateWalletAction()
}