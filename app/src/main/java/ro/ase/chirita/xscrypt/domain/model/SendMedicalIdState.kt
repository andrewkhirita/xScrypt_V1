package ro.ase.chirita.xscrypt.domain.model

sealed class SendMedicalIdState {
    object Loading : SendMedicalIdState()
    data class Content(
        val account: WalletAccountUi,
    ) : SendMedicalIdState()

}