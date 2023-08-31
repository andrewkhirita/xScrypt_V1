package ro.ase.chirita.xscrypt.domain.repository

import com.elrond.erdkotlin.domain.wallet.models.Wallet

interface WalletRepository {

     fun saveWallet(mnemonic: String)
     fun createWallet():List<String>
     fun loadWallet(): Wallet?
     fun deleteWallet()
}