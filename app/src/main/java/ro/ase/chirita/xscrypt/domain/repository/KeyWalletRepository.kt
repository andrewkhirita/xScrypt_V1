package ro.ase.chirita.xscrypt.domain.repository

interface KeyWalletRepository {

    fun getPrivateKey(): String?
    fun setPrivateKey(privateKey: String?)
}