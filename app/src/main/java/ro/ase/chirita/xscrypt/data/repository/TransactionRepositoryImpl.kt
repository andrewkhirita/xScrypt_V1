package ro.ase.chirita.xscrypt.data.repository

import com.elrond.erdkotlin.domain.transaction.GetTransactionInfoUsecase
import com.elrond.erdkotlin.domain.transaction.GetTransactionStatusUsecase
import ro.ase.chirita.xscrypt.domain.repository.TransactionRepository
import ro.ase.chirita.xscrypt.core.retry
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TransactionRepositoryImpl @Inject constructor(
    private val getTransactionInfoUsecase: GetTransactionInfoUsecase,
    private val getTransactionStatusUsecase: GetTransactionStatusUsecase
): TransactionRepository {

    override suspend fun executeTransactionInfo(txHash: String) = retry(initialDelay = 300){
        getTransactionInfoUsecase.execute(txHash)
    }

    override suspend fun executeTransactionStatus(txHash: String){
        getTransactionStatusUsecase.execute(txHash)
    }
}