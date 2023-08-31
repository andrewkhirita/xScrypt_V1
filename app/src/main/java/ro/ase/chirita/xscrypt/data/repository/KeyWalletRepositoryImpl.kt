package ro.ase.chirita.xscrypt.data.repository

import ro.ase.chirita.xscrypt.domain.repository.KeyWalletRepository

class KeyWalletRepositoryImpl : KeyWalletRepository {
    private var privateKey: String? = null

    override fun getPrivateKey(): String? {
        return privateKey
    }

    override fun setPrivateKey(privateKey: String?) {
        this.privateKey = privateKey
    }
}
