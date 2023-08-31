package ro.ase.chirita.xscrypt.domain.repository

import com.elrond.erdkotlin.domain.transaction.models.TransactionInfo

interface TransactionRepository {
    suspend fun executeTransactionInfo(txHash: String): TransactionInfo
    suspend fun executeTransactionStatus(txHash: String)
}