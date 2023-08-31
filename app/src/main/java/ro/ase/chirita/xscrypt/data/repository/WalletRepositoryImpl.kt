package ro.ase.chirita.xscrypt.data.repository

import com.elrond.erdkotlin.domain.wallet.models.Wallet
import ro.ase.chirita.xscrypt.domain.repository.KeyWalletRepository
import ro.ase.chirita.xscrypt.domain.repository.WalletRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WalletRepositoryImpl @Inject constructor(
    private var keyWalletRepository: KeyWalletRepository
): WalletRepository {

    override fun saveWallet(mnemonic: String) {
        val wallet = Wallet.createFromMnemonic(mnemonic, 0)
        keyWalletRepository.setPrivateKey(wallet.privateKeyHex)
    }

    override fun createWallet(): List<String> {
        return Wallet.generateMnemonic()
    }

    override fun loadWallet(): Wallet? {
        val privateKey = keyWalletRepository.getPrivateKey() ?: return null
        return Wallet.createFromPrivateKey(privateKey)
    }

    override fun deleteWallet() {
        keyWalletRepository.setPrivateKey(null)
    }

}
